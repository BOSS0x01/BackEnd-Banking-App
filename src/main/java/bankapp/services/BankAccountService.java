package bankapp.services;

import bankapp.entities.BankAccount;
import bankapp.entities.CurrentAccount;
import bankapp.entities.Customer;
import bankapp.entities.SavingAccount;
import bankapp.exceptions.BalanceNotSufficienttException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer) ;
    List<Customer> getAllCustomers();
    CurrentAccount saveCurrentAccount(double initialBalance,double overDraft,Long  customerId) throws CustomerNotFoundException ;
    SavingAccount saveSavingAccount(Double initialBalance,double interestRate,Long customerId) throws CustomerNotFoundException ;
    BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException;

    void debit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException, BalanceNotSufficienttException;
    void credit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException;

    void transfer(String accountSourceId,String accountDestinationId,double amount) throws BankAccountNotFoundException, BalanceNotSufficienttException;
}
