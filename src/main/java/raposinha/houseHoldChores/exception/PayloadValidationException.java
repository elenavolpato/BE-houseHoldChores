package raposinha.houseHoldChores.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class PayloadValidationException extends RuntimeException {
    private List<String> errors;

    public PayloadValidationException(List<String> errors) {
        super("Alcuni errori sono avvenuti nel processo di validazione");
        this.errors = errors;
    }
}
