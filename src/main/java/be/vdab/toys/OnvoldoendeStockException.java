package be.vdab.toys;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OnvoldoendeStockException extends RuntimeException {
    public OnvoldoendeStockException( long id) {
        super("Onvoldoende stock van product: " + id);
    }
}
