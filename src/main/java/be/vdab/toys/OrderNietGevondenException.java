package be.vdab.toys;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNietGevondenException extends RuntimeException {
    public OrderNietGevondenException() {
        super("Order niet gevonden");
    }
}
