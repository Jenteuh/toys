package be.vdab.toys;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    private static final String ORDERS_TABLE = "orders";
    private final OrderRepository orderRepository;
    private final JdbcClient jdbcClient;

    OrderRepositoryTest(OrderRepository orderRepository, JdbcClient jdbcClient) {
        this.orderRepository = orderRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void findUnshippedOrdersVindtUnshippedOrders() {
        assertThat(orderRepository.findUnshippedOrders().size())
                .isEqualTo(JdbcTestUtils.countRowsInTableWhere(jdbcClient, ORDERS_TABLE, "status NOT IN ('CANCELLED', 'SHIPPED')"));
    }
}
