package be.vdab.toys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    List<Order> findUnshippedOrders() {
        return orderRepository.findUnshippedOrders();
    }

    Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    void shipOrder(long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(OrderNietGevondenException::new);
        order.ship();
    }
}
