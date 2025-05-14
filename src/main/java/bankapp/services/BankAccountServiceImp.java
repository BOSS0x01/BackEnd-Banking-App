package bankapp.services;

import bankapp.dtos.BankAccountDTO;
import bankapp.dtos.CurrentAccountDTO;
import bankapp.dtos.CustomerDTO;
import bankapp.dtos.SavingAccountDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class BankAccountServiceImp implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountMapperImp bankAccountMapperImp;

    @Override
    public ResponseEntity<ApiResponse<CustomerDTO>> saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerRepository.save(bankAccountMapperImp.toCustomer(customerDTO));
        return  ResponseEntity.ok(new ApiResponse<>(true,"Customer found",bankAccountMapperImp.toCustomerDTO(customer))) ;
    }

    @Override
    public  ResponseEntity<ApiResponse<List<CustomerDTO>>>  getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS =customers.stream().map(bankAccountMapperImp::toCustomerDTO).toList();
        return customers.isEmpty()? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false,"List of customers is empty")):ResponseEntity.ok(new ApiResponse<>(true,"List of customers",customerDTOS)) ;
    }


    @Override
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomerById(customerId);
        return  ResponseEntity.ok(new ApiResponse<>(true,"Customer found",bankAccountMapperImp.toCustomerDTO(customer))) ;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomerById(customerId);
        customerRepository.delete(customer);
        return ResponseEntity.ok(new ApiResponse<>(true,"Customer deleted successfully"));
    }

    @Override
    public ResponseEntity<ApiResponse<CurrentAccountDTO>>  saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        CurrentAccount savedCurrentAccount =  bankAccountRepository.save(currentAccount);
        return ResponseEntity.ok(new ApiResponse<>(true,"Current account saved successfully",bankAccountMapperImp.toCurrentAccountDTO(savedCurrentAccount)));
    }

    @Override
    public ResponseEntity<ApiResponse<SavingAccountDTO>>  saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        SavingAccount savedSavingAccount =  bankAccountRepository.save(savingAccount);

        return ResponseEntity.ok(new ApiResponse<>(true,"Saving account saved successfully",bankAccountMapperImp.toSavingAccountDTO(savedSavingAccount)));
    }

    @Override
    public ResponseEntity<ApiResponse<BankAccountDTO>> getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(bankAccountId);
        BankAccountDTO savedBankAccountDTO =  bankAccountMapperImp.toBankAccountDTO(bankAccount);
        return ResponseEntity.ok(new ApiResponse<>(true,"Bank account found",savedBankAccountDTO));
    }



    @Override
    public ResponseEntity<ApiResponse<Void>> debit(String bankAccountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = getBankAccountById(bankAccountId);

        if (bankAccount instanceof CurrentAccount) {
            double overDraft = ((CurrentAccount) bankAccount).getOverDraft();
            if (bankAccount.getBalance() + overDraft < amount) {
                throw new BalanceNotSufficientException("Insufficient funds: Tried to debit " + amount +
                        ", but only " + (bankAccount.getBalance() + overDraft) + " available.");
            }
        } else {
            if (bankAccount.getBalance() < amount) {
                throw new BalanceNotSufficientException("Balance not sufficient");
            }
        }

        AccountOperation accountOperation = AccountOperation
                .builder()
                .bankAccount(bankAccount)
                .amount(amount)
                .type(OperationType.DEBIT)
                .date(new Date())
                .description(description)
                .build();
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

        return ResponseEntity.ok(new ApiResponse<>(true, "Debit operation has been performed successfully"));
    }


    @Override
    public ResponseEntity<ApiResponse<Void>> credit(String bankAccountId, double amount, String description) throws  BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(bankAccountId);

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
        return ResponseEntity.ok(new ApiResponse<>(true,"credit operation has been performed successfully"));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> transfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountSourceId,amount,"Transfer to "+accountDestinationId);
        credit(accountDestinationId,amount,"Transfer from "+accountSourceId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Money has been transferred to " + accountDestinationId +" successfully"));
    }

    @Override
    public ResponseEntity<ApiResponse<List<BankAccountDTO>>>  getAllBankAccounts(){
        List<BankAccount> bankAccounts=  bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccountMapperImp::toBankAccountDTO).toList();
        return bankAccountDTOS.isEmpty()? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false,"List of customers is empty"))
                :ResponseEntity.ok(new ApiResponse<>(true,"List of accounts",bankAccountDTOS)) ;

    }

    private BankAccount getBankAccountById(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
    }

    private Customer getCustomerById(Long customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }


}
