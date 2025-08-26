package be.vdab.toys;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AndereGebruikerHeeftOrderGewijzigdException extends RuntimeException {
    public AndereGebruikerHeeftOrderGewijzigdException() {
        super("Een andere gebruiker heeft het order gewijzigd.");
    }
}
