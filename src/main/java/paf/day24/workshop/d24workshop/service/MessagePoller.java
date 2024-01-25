package paf.day24.workshop.d24workshop.service;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import paf.day24.workshop.d24workshop.models.Detail;
import paf.day24.workshop.d24workshop.models.Order;
import paf.day24.workshop.d24workshop.repo.DetailRepo;
import paf.day24.workshop.d24workshop.repo.OrderException;
import paf.day24.workshop.d24workshop.repo.OrderRepo;

@Component
public class MessagePoller {

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    DetailRepo detailRepo;

    @Async
    public void start(String name) {
        Runnable run = () -> {
            ListOperations<String, String> listOps = template.opsForList();

            // listOps.leftPush("sales", "STARTING 123");
            // check from commandline - lpush sales apple
            System.out.println("Start polling" + name + "queue....");
            while (true) {
                String value = listOps.rightPop(name, Duration.ofSeconds(10)); // LISTEN TO LIST of their own name
                if ((null == value) || ("" == value.trim())) {
                    System.out.println("No data repolling");
                    continue;
                }

                System.out.printf(">>>>>>>>>>>>>Data: %s", value.formatted());

                // Process data

                JsonReader reader = Json.createReader(new StringReader(value));
                JsonObject jsonData = reader.readObject();
                Order order = Order.createJson(jsonData);
                System.out.println("ORDERRRRR TO PUT IN SQL" + order);

                boolean orderUpdated = orderRepo.updateOrder(order);
                String orderId = "";
                List<Detail> detailList = order.getAllDetails();

                if (orderUpdated) {
                    orderId = orderRepo.getOrder();
                    for (Detail d : detailList) {
                        d.setOrderId(orderId);
                    }
                }

                int listSize = detailList.size();
                int inserts = detailRepo.updateDetail(detailList);

                boolean updateDetail = listSize == inserts;

                // Transactional
                if (!(orderUpdated && updateDetail)) { 
                    try {
                        throw new OrderException("test error catch");
                    } catch (OrderException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        ExecutorService thrPool = Executors.newFixedThreadPool(1);
        thrPool.submit(run);

    }

}
