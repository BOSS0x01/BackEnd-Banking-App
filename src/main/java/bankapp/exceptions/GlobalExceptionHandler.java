package bankapp.exceptions;

import bankapp.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(CustomerNotFoundException.class)
        public ResponseEntity<?> handleCustomerNotFoundException() {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<Void>(false, "Customer Not Found"));
        }

        @ExceptionHandler(BankAccountNotFoundException.class)
        public ResponseEntity<?> handleBankAccountNotFoundException() {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<Void>(false, "Bank account not found"));
        }

        @ExceptionHandler(BalanceNotSufficientException.class)
        public ResponseEntity<?> handleBalanceNotSufficientException() {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<Void>(false, "Balance is not sufficient"));
        }


    }
