package raposinha.houseHoldChores.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import raposinha.houseHoldChores.DTO.ExceptionDTO;
import raposinha.houseHoldChores.DTO.ExceptionListDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    // --- CUSTOM CLIENT EXCEPTIONS ---

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFoundException(NotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDTO> handleBadRequestException(BadRequestException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionDTO> handleUnauthorizedException(UnauthorizedException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleEmailConflict(EmailAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    // --- VALIDATION ERROR HANDLERS ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionListDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ExceptionListDTO errorList = new ExceptionListDTO(
                "Validation failed for incoming payload",
                LocalDateTime.now(),
                validationErrors
        );
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PayloadValidationException.class)
    public ResponseEntity<ExceptionListDTO> handlePayloadValidationException(PayloadValidationException ex) {
        ExceptionListDTO errorList = new ExceptionListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrors());
        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    // --- SPRING INFRASTRUCTURE HANDLERS ---

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildResponse("Malformed JSON request body or missing body fields.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return buildResponse("Access denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildResponse("Invalid parameter format provided in URL query or path variable.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ExceptionDTO> handlePropertyReferenceException(PropertyReferenceException ex) {
        return buildResponse("Invalid search filter or sorting parameter applied.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildResponse("Database integrity constraint violated. Action rejected.", HttpStatus.BAD_REQUEST);
    }

    // --- FALLBACK GLOBAL EXCEPTION ---

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex) {
        // Essential to print logs locally during development so you can trace systemic issues!
        ex.printStackTrace();
        return buildResponse("An unexpected server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- DRY HELPER METHOD ---

    private ResponseEntity<ExceptionDTO> buildResponse(String message, HttpStatus status) {
        ExceptionDTO dto = new ExceptionDTO(message, status.value(), LocalDateTime.now());
        return new ResponseEntity<>(dto, status);
    }
}
