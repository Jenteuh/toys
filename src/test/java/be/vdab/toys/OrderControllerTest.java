package be.vdab.toys;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class OrderControllerTest {

    private final static String ORDERS_TABLE = "orders";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    public OrderControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
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
}
