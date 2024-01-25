package paf.day24.workshop.d24workshop.models;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    Integer orderId;
    Date date;
    String customerName;
    String shipAddress;
    String notes;
    Double tax = 0.05;
    List<Detail> allDetails;


    public static Order createJson(JsonObject o){
        Order order = new Order();
        order.setCustomerName(o.getString("customer_name"));
        order.setShipAddress(o.getString("ship_address"));
        order.setNotes(o.getString("notes"));
        order.setTax(o.getJsonNumber("tax").doubleValue());

      List<Detail> result = new LinkedList<Detail>();
      
      JsonArray detailArr = o.getJsonArray("line_items");
      for(int i=0; i < detailArr.size(); i++){
         JsonObject x = detailArr.getJsonObject(i);
         Detail t = Detail.createJson(x);
         result.add(t);
      }
      order.setAllDetails(result);
      return order;
   }
}
