package bankapp.services;

import bankapp.dtos.BankAccountDTO;
import bankapp.dtos.CurrentAccountDTO;
import bankapp.dtos.CustomerDTO;
import bankapp.dtos.SavingAccountDTO;
import bankapp.exceptions.BalanceNotSufficientException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankAccountService {
    ResponseEntity<ApiResponse<CustomerDTO>>  saveCustomer(CustomerDTO customerDTO) ;
    ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers();
    ResponseEntity<ApiResponse<Void>> deleteCustomer(Long customerId) throws CustomerNotFoundException;
    ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(Long customerId) throws CustomerNotFoundException;

    ResponseEntity<ApiResponse<CurrentAccountDTO>>  saveCurrentAccount(double initialBalance, double overDraft, Long  customerId) throws CustomerNotFoundException ;
    ResponseEntity<ApiResponse<SavingAccountDTO>>  saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException ;
    ResponseEntity<ApiResponse<BankAccountDTO>>  getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    ResponseEntity<ApiResponse<List<BankAccountDTO>>>  getAllBankAccounts();

    ResponseEntity<ApiResponse<Void>> debit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException, BalanceNotSufficientException;
    ResponseEntity<ApiResponse<Void>> credit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException;
    ResponseEntity<ApiResponse<Void>> transfer(String accountSourceId,String accountDestinationId,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;



}
