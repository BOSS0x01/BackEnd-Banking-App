package bankapp.services;

import bankapp.entities.*;
import bankapp.enums.OperationType;
import bankapp.exceptions.BalanceNotSufficienttException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.repositories.AccountOperationRepository;
import bankapp.repositories.BankAccountRepository;
import bankapp.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BankAccountServiceImp implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingAccount(Double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElse(null);
        if (bankAccount== null) {
            throw new BankAccountNotFoundException("Bank account not found");
        }
        return bankAccount;
    }

    @Override
    public void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficienttException {
        BankAccount bankAccount = this.getBankAccount(bankAccountId);
        if(bankAccount instanceof CurrentAccount) {
            if (((CurrentAccount)bankAccount).getOverDraft() < amount) {
                throw new BalanceNotSufficienttException("Balance not sufficient to debit " + amount);
            }
        }else{
            if(bankAccount.getBalance() < amount) {
                throw new BalanceNotSufficienttException("Balance not sufficient");
            }
        }

        AccountOperation accountOperation = AccountOperation
                .builder()
                .bankAccount(bankAccount)
                .amount(amount)
                .type(OperationType.DEBIT)
                .date(new Date())
                .build();
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String bankAccountId, double amount, String description) throws  BankAccountNotFoundException {
        BankAccount bankAccount = this.getBankAccount(bankAccountId);

        AccountOperation accountOperation = AccountOperation
                .builder()
                .bankAccount(bankAccount)
                .amount(amount)
                .type(OperationType.CREDIT)
                .date(new Date())
                .build();
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficienttException {
        debit(accountSourceId,amount,"Transfer to "+accountDestinationId);
        credit(accountDestinationId,amount,"Transfer from "+accountSourceId);
    }
}
