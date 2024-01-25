package paf.day24.workshop.d24workshop.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import paf.day24.workshop.d24workshop.models.Detail;

@Repository
public class DetailRepo {

    @Autowired
    JdbcTemplate template;

    public int updateDetail(List<Detail> detailList){
        System.out.println("DETAIL LIST >>>>>" + detailList);
        int count =0;
        for (Detail d : detailList){
            template.update(Queries.INSERT_DETAIL, d.getProduct(), d.getUnitPrice(), d.getDiscount(), d.getQuantity(), d.getOrderId());
            count ++;
        }
        System.out.println("TOTAL INSERTS" +count);
        return count;
    }
    
}
