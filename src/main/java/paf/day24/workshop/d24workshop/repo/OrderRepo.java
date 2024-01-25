package paf.day24.workshop.d24workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import paf.day24.workshop.d24workshop.models.Order;

@Repository
public class OrderRepo {

    @Autowired
    JdbcTemplate template;

    public boolean updateOrder(Order order){
        return template.update(Queries.INSERT_ORDER, 
            order.getDate(), order.getCustomerName(), order.getShipAddress(), order.getNotes(), order.getTax()) ==1;
    }

    public String getOrder(){
        SqlRowSet rs = template.queryForRowSet(Queries.GET_ORDER_ID);
        String orderId = "";
        while (rs.next()){
            orderId = rs.getString("order_id");
        }
        return orderId;
        

    }
    
}
