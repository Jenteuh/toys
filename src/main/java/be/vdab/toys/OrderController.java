package be.vdab.toys;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
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

    @GetMapping("unshipped")
    Stream<OrderBeknoptMetCustomerName> findUnshippedOrders() {
        return orderService.findUnshippedOrders()
                .stream()
                .map(OrderBeknoptMetCustomerName::new);
    }

}
