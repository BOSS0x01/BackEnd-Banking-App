package bankapp.exceptions;

public class BalanceNotSufficienttException extends Exception {
    public BalanceNotSufficienttException(String message) {
        super(message);
    }
}
