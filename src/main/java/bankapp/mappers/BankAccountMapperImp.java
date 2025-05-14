package bankapp.mappers;

import bankapp.dtos.BankAccountDTO;
import bankapp.dtos.CurrentAccountDTO;
import bankapp.dtos.CustomerDTO;
import bankapp.dtos.SavingAccountDTO;
import bankapp.entities.BankAccount;
import bankapp.entities.CurrentAccount;
import bankapp.entities.Customer;
import bankapp.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImp {

    public CustomerDTO toCustomerDTO (Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public  Customer toCustomer(CustomerDTO customerDTO ) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

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
        savingAccountDTO.setCustomer(toCustomerDTO(savingAccount.getCustomer()));
        savingAccountDTO.setType(SavingAccount.class.getSimpleName());
        return savingAccountDTO;
    }

    public  SavingAccount toSavingAccount(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(toCustomer(savingAccountDTO.getCustomer()));
        return savingAccount;
    }

    public CurrentAccountDTO toCurrentAccountDTO (CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomer(toCustomerDTO(currentAccount.getCustomer()));
        currentAccountDTO.setType(CurrentAccount.class.getSimpleName());
        return currentAccountDTO;
    }

    public  CurrentAccount toCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(toCustomer(currentAccountDTO.getCustomer()));
        return currentAccount;
    }

}
