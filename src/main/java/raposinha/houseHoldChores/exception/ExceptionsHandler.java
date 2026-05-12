package raposinha.houseHoldChores.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import raposinha.houseHoldChores.DTO.ExceptionDTO;
import raposinha.houseHoldChores.DTO.ExceptionListDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

        @ExceptionHandler(BadRequestException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionDTO handleBadRequestException(BadRequestException ex) {
            return new ExceptionDTO(ex.getMessage(), LocalDateTime.now());
        }

        @ExceptionHandler(PayloadValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionListDTO handlePayloadValidationException(PayloadValidationException ex) {
            return new ExceptionListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrors());
        }

//        @ExceptionHandler(EmailAlreadyExistsException.class)
//        @ResponseStatus(HttpStatus.BAD_REQUEST)
//        public ExceptionDTO handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
//            return new ExceptionDTO(ex.getMessage(), LocalDateTime.now());
//        }

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ExceptionDTO handleNotFoundException(NotFoundException ex) {
            return new ExceptionDTO(ex.getMessage(), LocalDateTime.now());
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
            return new ExceptionDTO("Not valid input provided", LocalDateTime.now());
        }

        @ExceptionHandler(UnauthorizedException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public ExceptionDTO handleUnauthorizedException(UnauthorizedException ex) {
            return new ExceptionDTO(ex.getMessage(), LocalDateTime.now());
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ExceptionDTO handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
            return new ExceptionDTO("Access denied, you don't have the required permission", LocalDateTime.now());
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
            return new ExceptionDTO("Not valid input provided", LocalDateTime.now());
        }


        @ExceptionHandler(PropertyReferenceException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionDTO handlePropertyReferenceException(PropertyReferenceException ex) {
            return new ExceptionDTO("Not valid search param", LocalDateTime.now());
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ExceptionDTO handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
            return new ExceptionDTO("Deletion of the desired resource avoided", LocalDateTime.now());
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ExceptionDTO handleGenericException(Exception ex) {
            ex.printStackTrace();
            return new ExceptionDTO("Oops, a server error occurred!", LocalDateTime.now());
        }

}
