package paf.day24.workshop.d24workshop.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import paf.day24.workshop.d24workshop.models.Detail;
import paf.day24.workshop.d24workshop.models.Order;
import paf.day24.workshop.d24workshop.repo.DetailRepo;
import paf.day24.workshop.d24workshop.repo.OrderException;
import paf.day24.workshop.d24workshop.repo.OrderRepo;

@Service
public class OrderService implements MessageListener {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    DetailRepo detailRepo;

    //workshop 24 normal transaction 
    // @Transactional (rollbackFor = OrderException.class)
    // public boolean updateTables(Order order, List<Detail> detailList) throws OrderException{
    //     boolean orderUpdated = orderRepo.updateOrder(order);
    //     String orderId = "";

    //     if (orderUpdated){
    //         orderId = orderRepo.getOrder();
    //         for (Detail d : detailList){
    //             d.setOrderId(orderId);
    //         }
    //     }

    //     int listSize = detailList.size();
    //     int inserts = detailRepo.updateDetail(detailList);

    //     boolean updateDetail = listSize == inserts; 

    //     if (!(orderUpdated && updateDetail)){
    //         throw new OrderException ("test error catch");
    //     }
    //     return orderUpdated && updateDetail;
    // }

    // IF PUBSUB
    @Override 
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("MESSAGE RECEIVED!!!!!! SUBSCRIBER HERE");


        // byte[] data = message.getBody();

        // System.out.println(">>>> " + message.toString());
        // JsonReader reader = Json.createReader(new ByteArrayInputStream(data));
        // JsonObject jsonData = reader.readObject();
        // Order order = Order.createJson(jsonData);
        // System.out.println("ORDERRRRR TO PUT IN SQL" + order);

        // boolean orderUpdated = orderRepo.updateOrder(order);
        // String orderId = "";
        // List<Detail> detailList = order.getAllDetails();

        // if (orderUpdated){
        //     orderId = orderRepo.getOrder();
        //     for (Detail d : detailList){
        //         d.setOrderId(orderId);
        //     }
        // }

        // int listSize = detailList.size();
        // int inserts = detailRepo.updateDetail(detailList);

        // boolean updateDetail = listSize == inserts; 

        // //Transactional
        // if (!(orderUpdated && updateDetail)){
        //     try {
        //         throw new OrderException ("test error catch");
        //     } catch (OrderException e) {
        //         e.printStackTrace();
        //     }
        // }

    }

    
}
