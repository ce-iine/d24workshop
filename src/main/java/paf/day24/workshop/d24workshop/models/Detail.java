package paf.day24.workshop.d24workshop.models;

import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Detail {
    Integer id;
    String product;
    Double unitPrice;
    Double discount = 1.0;
    Integer quantity;
    String orderId;

    public static Detail createJson(JsonObject o) {
        Detail d = new Detail();
        d.setProduct(o.getString("product"));
        d.setUnitPrice(o.getJsonNumber("unitPrice").doubleValue());
        d.setDiscount(o.getJsonNumber("discount").doubleValue());
        d.setQuantity(o.getJsonNumber("quantity").intValue());

        return d;
    }

}
