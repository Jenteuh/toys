package be.vdab.toys;

import jakarta.persistence.Embeddable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("orders")
class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private record OrderBeknoptMetCustomerName(long id, LocalDate ordered, LocalDate required, String customerName, Status status) {
        OrderBeknoptMetCustomerName(Order order) {
            this(order.getId(), order.getOrdered(), order.getRequired(),
                    order.getCustomer().getName(), order.getStatus());
        }
    }

    private record OrderGedetailleerd(long id, LocalDate ordered, LocalDate required, String customerName, String countryName,BigDecimal value, Set<Details> details) {
        OrderGedetailleerd(Order order) {
            this(order.getId(), order.getOrdered(), order.getRequired(), order.getCustomer().getName(),
                    order.getCustomer().getCountry().getName(),order.getValue(),
                    order.getOrderdetails().stream().map(Details::new).collect(Collectors.toSet()));
        }
    }

    @Embeddable
    private record Details(int ordered, BigDecimal priceEach, BigDecimal value, String productName) {
        Details(Orderdetail orderdetail) {
            this(orderdetail.getOrdered(), orderdetail.getPriceEach(), orderdetail.getValue(), orderdetail.getProduct().getName());
        }
    }

    @GetMapping("unshipped")
    Stream<OrderBeknoptMetCustomerName> findUnshippedOrders() {
        return orderService.findUnshippedOrders()
                .stream()
                .map(OrderBeknoptMetCustomerName::new);
    }

    @GetMapping("{id}")
    OrderGedetailleerd findById(@PathVariable long id) {
        return orderService.findById(id)
                .map(OrderGedetailleerd::new)
                .orElseThrow(OrderNietGevondenException::new);

    }

    @PutMapping("{id}/shippings")
    void shipOrder(@PathVariable long id) {
        try {
            orderService.shipOrder(id);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new AndereGebruikerHeeftOrderGewijzigdException();
        }
    }

}
