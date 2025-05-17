package bankapp.controllers;

import bankapp.dtos.*;
import bankapp.exceptions.BalanceNotSufficientException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.services.BankAccountService;
import bankapp.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class BankAccountRestController {
    private final BankAccountService bankAccountService;

    @GetMapping("/{bankAccountId}")
    ResponseEntity<ApiResponse<BankAccountDTO>>  getBankAccount(@PathVariable String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(bankAccountId);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<BankAccountDTO>>>  getAllBankAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/{bankAccountId}/operations")
    ResponseEntity<ApiResponse<List<AccountOperationDTO>>> getBankAccountOperations(@PathVariable String bankAccountId)  {
        return bankAccountService.getAccountOperations(bankAccountId);
    }

    @GetMapping("/{bankAccountId}/history")
    public ResponseEntity<ApiResponse<AccountHistoryDTO>> getAccountHistory(@PathVariable String bankAccountId,
                                                                            @RequestParam(name = "page",defaultValue = "0") int page ,
                                                                            @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(bankAccountId, page, size);
    }


//    ResponseEntity<ApiResponse<CurrentAccountDTO>> saveCurrentAccount(double initialBalance, double overDraft, Long  customerId) throws CustomerNotFoundException {
//
//    }
//
//    ResponseEntity<ApiResponse<SavingAccountDTO>>  saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
//
//    }
//
//    ResponseEntity<ApiResponse<Void>> debit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException, BalanceNotSufficientException {
//
//    }
//    ResponseEntity<ApiResponse<Void>> credit(String bankAccountId,double amount,String description) throws  BankAccountNotFoundException {
//
//    }
//    ResponseEntity<ApiResponse<Void>> transfer(String accountSourceId,String accountDestinationId,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
//
//    }

}
