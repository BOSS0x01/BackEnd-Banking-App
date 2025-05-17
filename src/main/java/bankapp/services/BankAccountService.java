package bankapp.services;

import bankapp.dtos.*;
import bankapp.exceptions.BalanceNotSufficientException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankAccountService {
    ResponseEntity<ApiResponse<CurrentAccountDTO>> saveCurrentAccount(double initialBalance, double overDraft, Long  customerId) throws CustomerNotFoundException ;
    ResponseEntity<ApiResponse<SavingAccountDTO>> saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException ;
    ResponseEntity<ApiResponse<BankAccountDTO>> getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    ResponseEntity<ApiResponse<List<BankAccountDTO>>> getAllBankAccounts();

    ResponseEntity<ApiResponse<Void>> debit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException, BalanceNotSufficientException;
    ResponseEntity<ApiResponse<Void>> credit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException;
    ResponseEntity<ApiResponse<Void>> transfer(String accountSourceId,String accountDestinationId,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;



    ResponseEntity<ApiResponse<List<AccountOperationDTO>>>  getAccountOperations(String accountId);

    ResponseEntity<ApiResponse<AccountHistoryDTO>> getAccountHistory(String accountId, int page, int size)throws BankAccountNotFoundException;
}
