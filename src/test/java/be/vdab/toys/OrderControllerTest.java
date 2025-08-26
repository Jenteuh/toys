package be.vdab.toys;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/orders.sql")
class OrderControllerTest {

    private final static String ORDERS_TABLE = "orders";
    private final OrderService orderService;
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    public OrderControllerTest(OrderService orderService, MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.orderService = orderService;
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestOrder() {
        return jdbcClient.sql("select id from orders where comments = 'testorder'")
                .query(Long.class)
                .single();
    }

    @Test
    void findUnshippedOrdersVindtUnshippedOrders(){
        assertThat(mockMvcTester.get().uri("/orders/unshipped"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(JdbcTestUtils.countRowsInTableWhere(jdbcClient, ORDERS_TABLE,
                        "status NOT IN ('CANCELLED', 'SHIPPED')"));
    }

    @Test
    void findByIdMetBestaandeIdVindtOrder() {
        assertThat(mockMvcTester.get().uri("/orders/{id}", idVanTestOrder()))
                .hasStatusOk()
                .bodyJson().extractingPath("value").isEqualTo(0);
    }

    @Test
    void findByIdMetOnbestaandeIdVindtGeenOrder() {
        assertThat(mockMvcTester.get().uri("/orders/{id}", Long.MAX_VALUE))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void shipOrderWijzigtStatusEnShipped() {

        var id = idVanTestOrder();
        var order = orderService.findById(id);

        var response = mockMvcTester.put()
                .uri("/orders/{id}/shippings", id).contentType(MediaType.APPLICATION_FORM_URLENCODED);
        assertThat(response).hasStatusOk();
        assertThat(order.get().getShipped()).isEqualTo(LocalDate.now());
        assertThat(order.get().getStatus()).isEqualTo(Status.SHIPPED);
    }

    @Test
    void shipOrderMetOnbestaandeIdMislukt() {
        assertThatExceptionOfType(OrderNietGevondenException.class).isThrownBy(() -> orderService.shipOrder(Long.MAX_VALUE));
    }

    @Test
    void ShipOrderOpShippedOrderMislukt() {
        var id = idVanTestOrder();
        jdbcClient.sql("update orders set status = 'SHIPPED', shipped = now() where id = " + id).update();
        assertThatExceptionOfType(OrderAlShippedException.class).isThrownBy(() -> orderService.shipOrder(id));
    }
}
