package be.vdab.toys;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/products.sql")
public class ProductTest {

    private final static String PRODUCTS_TABLE = "products";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    public ProductTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void verlaagInStockEnInOrderVerlaagtInStockEnInOrder() {
        var product = jdbcClient.sql("select * from products where name = 'testproduct'").query(Product.class).single();
        product.verlaagInStockEnInOrder(9);

        assertThat(product.getInStock()).isEqualTo(1);
        assertThat(product.getInOrder()).isEqualTo(1);
    }

    @Test
    void verlaagInStockEnInOrderMisluktBijTeWeinigStock() {
        var product = jdbcClient.sql("select * from products where name = 'testproduct'").query(Product.class).single();

        assertThatExceptionOfType(OnvoldoendeStockException.class).isThrownBy(() -> product.verlaagInStockEnInOrder(11));
    }
}
