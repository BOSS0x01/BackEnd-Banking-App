package bankapp.services;

import bankapp.dtos.CustomerDTO;
import bankapp.entities.*;
import bankapp.enums.OperationType;
import bankapp.exceptions.BalanceNotSufficientException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.mappers.BankAccountMapperImp;
import bankapp.repositories.AccountOperationRepository;
import bankapp.repositories.BankAccountRepository;
import bankapp.repositories.CustomerRepository;
import bankapp.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class BankAccountServiceImp implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountMapperImp bankAccountMapperImp;

    @Override
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerRepository.save(bankAccountMapperImp.fromCustomerDTO(customerDTO));
        return bankAccountMapperImp.fromCustomer(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(bankAccountMapperImp::fromCustomer).collect(Collectors.toList());
    }


    @Override
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->
                new CustomerNotFoundException("Customer not found")
        );
        return  ResponseEntity.ok(new ApiResponse<>(true,"Customer found",bankAccountMapperImp.fromCustomer(customer))) ;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
        return ResponseEntity.ok(new ApiResponse<>(true,"Customer deleted successfully"));
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId).orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
    }

    @Override
    public void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = this.getBankAccount(bankAccountId);
        if(bankAccount instanceof CurrentAccount) {
            double overDraft = ((CurrentAccount)bankAccount).getOverDraft();
            if ( overDraft < amount) {
                throw new BalanceNotSufficientException("Balance not sufficient to debit " + amount);
            }else
                ((CurrentAccount)bankAccount).setOverDraft(overDraft - amount);
        }else{
            if(bankAccount.getBalance() < amount) {
                throw new BalanceNotSufficientException("Balance not sufficient");
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
    public void transfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountSourceId,amount,"Transfer to "+accountDestinationId);
        credit(accountDestinationId,amount,"Transfer from "+accountSourceId);
    }

    @Override
    public List<BankAccount> getAllBankAccounts(){
        return bankAccountRepository.findAll();
    }


}
