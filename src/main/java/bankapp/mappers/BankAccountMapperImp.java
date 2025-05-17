package bankapp.mappers;

import bankapp.dtos.*;
import bankapp.entities.*;
import lombok.AllArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountMapperImp {

    private final CustomerMapperImp customerMapperImp;

    public BankAccountDTO toBankAccountDTO(BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        if (bankAccount instanceof SavingAccount) {
            bankAccountDTO = toSavingAccountDTO((SavingAccount) bankAccount);
        } else if (bankAccount instanceof CurrentAccount) {
            bankAccountDTO = toCurrentAccountDTO((CurrentAccount) bankAccount);
        }
        return bankAccountDTO;
    }

    public BankAccount toBankAccount(BankAccountDTO bankAccountDTO) {
        if (bankAccountDTO instanceof CurrentAccountDTO) {
            return toCurrentAccount((CurrentAccountDTO) bankAccountDTO);
        } else if (bankAccountDTO instanceof SavingAccountDTO) {
            return toSavingAccount((SavingAccountDTO) bankAccountDTO);
        } else {
            throw new IllegalArgumentException("Unsupported account DTO type: " + bankAccountDTO.getClass());
        }
    }

    public SavingAccountDTO toSavingAccountDTO (SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        savingAccountDTO.setCustomer(customerMapperImp.toCustomerDTO(savingAccount.getCustomer()));
        savingAccountDTO.setType(SavingAccount.class.getSimpleName());
        return savingAccountDTO;
    }

    public  SavingAccount toSavingAccount(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(customerMapperImp.toCustomer(savingAccountDTO.getCustomer()));
        return savingAccount;
    }

    public CurrentAccountDTO toCurrentAccountDTO (CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomer(customerMapperImp.toCustomerDTO(currentAccount.getCustomer()));
        currentAccountDTO.setType(CurrentAccount.class.getSimpleName());
        return currentAccountDTO;
    }

    public  CurrentAccount toCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(customerMapperImp.toCustomer(currentAccountDTO.getCustomer()));
        return currentAccount;
    }

    public AccountOperationDTO toAccountOperationDTO (AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

//    public AccountHistoryDTO toAccountHistoryDTO (Page accountOperationDTOS, BankAccount bankAccount) {
//        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
//        accountHistoryDTO.setAccountId(bankAccount.getId());
//        accountHistoryDTO.setAccountOperations(accountOperationDTOS);
//        accountHistoryDTO.setBalance(bankAccount.getBalance());
//        accountHistoryDTO.setCurrentPage(accountOperationDTO.getNumber());
//        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
//        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
//        return ;
//    }
}
