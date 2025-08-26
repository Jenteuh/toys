package be.vdab.toys;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT o FROM Order o JOIN FETCH o.customer
            WHERE o.status NOT IN ('CANCELLED', 'SHIPPED')
            ORDER BY o.id
            """)
    List<Order> findUnshippedOrders();

    @Override
    @EntityGraph(attributePaths = {"customer", "customer.country"})
    Optional<Order> findById(Long id);

}
