package paf.day24.workshop.d24workshop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import paf.day24.workshop.d24workshop.models.Order;
import paf.day24.workshop.d24workshop.models.Detail;

@Service
public class MessageSvc {

    @Autowired
    @Qualifier("registrationCache")
    private RedisTemplate<String, String> template;

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, String> userTemplate;

    public void addToRegistration(String name){
        ListOperations<String,String> LO = template.opsForList();
        LO.leftPush("registrations",name);
    }

    public String[] getAllRegisteredCust() {
        return template.opsForList().range("registrations", 0, -1).toArray(new String[0]);
    }

    public String makeJson(Order order) {
        List<Detail> lineItems = new ArrayList<>();
        lineItems = order.getAllDetails();

        JsonArrayBuilder itemArray = Json.createArrayBuilder();

        for (Detail each : lineItems) {
            JsonObject eachItem = Json.createObjectBuilder()
                    .add("product", each.getProduct())
                    .add("unitPrice", each.getUnitPrice())
                    .add("discount", each.getDiscount())
                    .add("quantity", each.getQuantity())
                    .build();
            itemArray.add(eachItem);
        }

        JsonObject constructReturn = Json.createObjectBuilder()
                .add("customer_name", order.getCustomerName())
                .add("ship_address", order.getShipAddress())
                .add("notes", order.getNotes())
                .add("tax", order.getTax())
                .add("line_items", itemArray.build())
                .build();
        return constructReturn.toString();
    }

    public boolean updateUserList(Order order, String orderJson){
        boolean result =  userTemplate.opsForList().leftPush(order.getCustomerName(), orderJson) >0;
        if(result){
            System.out.println("PUSHED TO USER LIST");
        }   
        //IF PUB SUB - set channel name 
        // template.convertAndSend(order.getCustomerName(), orderJson);

        return result;
    }

}
