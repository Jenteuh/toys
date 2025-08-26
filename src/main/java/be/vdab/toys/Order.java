package be.vdab.toys;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate ordered;
    private LocalDate required;
    private LocalDate shipped;
    private String comments;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Version
    private int version;
    @OneToMany
    @JoinTable(name = "orderdetails", joinColumns = @JoinColumn(name = "orderId"), inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Orderdetail> orderdetails;

    public long getId() {
        return id;
    }

    public LocalDate getOrdered() {
        return ordered;
    }

    public LocalDate getRequired() {
        return required;
    }

    public LocalDate getShipped() {
        return shipped;
    }

    public String getComments() {
        return comments;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Status getStatus() {
        return status;
    }

    public int getVersion() {
        return version;
    }

    public Set<Orderdetail> getOrderdetails() {
        return Collections.unmodifiableSet(orderdetails);
    }

    public BigDecimal getValue() {
        BigDecimal result = BigDecimal.ZERO;
        for (Orderdetail detail : orderdetails) {
            result = result.add(detail.getValue());
        }
        return result;
    }

    public void setShipped(LocalDate shipped) {
        this.shipped = shipped;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void ship() {

        var orderdetails = getOrderdetails();

        if(status != Status.SHIPPED) {
            setStatus(Status.SHIPPED);
        } else throw new OrderAlShippedException();

        setShipped(LocalDate.now());

        for (Orderdetail orderdetail : orderdetails) {
            orderdetail.getProduct().verlaagInStockEnInOrder(orderdetail.getOrdered());
        }
    }
}
