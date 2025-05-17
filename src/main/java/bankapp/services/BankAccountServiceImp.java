package bankapp.services;

import bankapp.dtos.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return ResponseEntity.ok(
                new ApiResponse<>(true, bankAccountDTOS.isEmpty() ? "No accounts found" : "List of accounts", bankAccountDTOS)
        );

    }

    @Override
    public ResponseEntity<ApiResponse<List<AccountOperationDTO>>>  getAccountOperations(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        System.out.println(accountOperations);
        List<AccountOperationDTO> accountOperationDTOS =accountOperations.stream().map(bankAccountMapperImp::toAccountOperationDTO).toList();
        return ResponseEntity.ok(
                new ApiResponse<>(true, accountOperationDTOS.isEmpty() ? "No account operations found" : "List of account operations", accountOperationDTOS)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<AccountHistoryDTO>> getAccountHistory(String accountId, int page, int size)throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(accountId);
        Pageable pageable = PageRequest.of(page,size);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId,pageable);
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setAccountOperations(accountOperations.getContent().stream().map(bankAccountMapperImp::toAccountOperationDTO).toList());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(accountOperations.getNumber());
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setPageSize(size);
        return ResponseEntity.ok(new ApiResponse<>(true,"List of operations", accountHistoryDTO));
    }


    private BankAccount getBankAccountById(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
    }


}
