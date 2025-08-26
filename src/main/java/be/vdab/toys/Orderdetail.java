package be.vdab.toys;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "orderdetails")
public class Orderdetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;
    @OneToOne
    @JoinColumn(name = "productId")
    private Product product;
    private int ordered;
    private BigDecimal priceEach;

    public long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getOrdered() {
        return ordered;
    }

    public BigDecimal getPriceEach() {
        return priceEach;
    }

    public BigDecimal getValue() {
        return priceEach.multiply(BigDecimal.valueOf(ordered));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Orderdetail that)) return false;
        return Objects.equals(order, that.order) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
