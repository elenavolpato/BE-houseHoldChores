package raposinha.houseHoldChores.exception;

import java.util.List;

public class ValidationExceptions extends RuntimeException {

    private List<String> errors;

    public ValidationExceptions(String message) {
        super(message);
    }

    public ValidationExceptions(List<String> errors) {
        super("Validation error");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
