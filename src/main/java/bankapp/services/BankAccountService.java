package bankapp.services;

import bankapp.dtos.CustomerDTO;
import bankapp.entities.BankAccount;
import bankapp.entities.CurrentAccount;
import bankapp.entities.SavingAccount;
import bankapp.exceptions.BalanceNotSufficientException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO) ;
    List<CustomerDTO> getAllCustomers();

    ResponseEntity<ApiResponse<Void>> deleteCustomer(Long customerId) throws CustomerNotFoundException;

    CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long  customerId) throws CustomerNotFoundException ;
    SavingAccount saveSavingAccount(double initialBalance,double interestRate,Long customerId) throws CustomerNotFoundException ;
    BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException;

    void debit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException;

    void transfer(String accountSourceId,String accountDestinationId,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> getAllBankAccounts();

    ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(Long customerId) throws CustomerNotFoundException;
}
