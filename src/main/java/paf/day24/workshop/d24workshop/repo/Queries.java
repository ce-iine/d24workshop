package paf.day24.workshop.d24workshop.repo;

public class Queries {

    public final static String INSERT_ORDER = """
        insert into orders(order_date, customer_name, ship_address, notes, tax)
                values 
                    (?, ?, ?, ?, ?);
            """;

    public final static String INSERT_DETAIL = """
        insert into order_details(product, unit_price, discount, quantity, order_id)
                values 
                    (?, ?, ?, ?, ?);
            """;
    public final static String GET_ORDER_ID = """
            select order_id 
            from orders 
            order by order_id desc 
            limit 1;
            """;

}
