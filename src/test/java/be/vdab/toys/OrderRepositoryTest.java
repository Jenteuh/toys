package be.vdab.toys;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/orders.sql")
class OrderRepositoryTest {

    private static final String ORDERS_TABLE = "orders";
    private final OrderRepository orderRepository;
    private final JdbcClient jdbcClient;

    OrderRepositoryTest(OrderRepository orderRepository, JdbcClient jdbcClient) {
        this.orderRepository = orderRepository;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestOrder() {
        return jdbcClient.sql("select id from orders where comments = 'testorder'")
                .query(Long.class)
                .single();
    }

    @Test
    void findUnshippedOrdersVindtUnshippedOrders() {
        assertThat(orderRepository.findUnshippedOrders().size())
                .isEqualTo(JdbcTestUtils.countRowsInTableWhere(jdbcClient, ORDERS_TABLE, "status NOT IN ('CANCELLED', 'SHIPPED')"));
    }

    @Test void findByIdMetBestaandeIdVindtOrder() {
        assertThat(orderRepository.findById(idVanTestOrder()).get().getComments())
                .isEqualTo("testorder");
    }
    @Test void findByIdMetOnbestaandeIdVindtGeenArtikel() {
        assertThat(orderRepository.findById(Long.MAX_VALUE)).isEmpty();
    }
}
