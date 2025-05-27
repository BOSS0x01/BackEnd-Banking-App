package bankapp;

import bankapp.dtos.BankAccountDTO;
import bankapp.dtos.CurrentAccountDTO;
import bankapp.dtos.CustomerDTO;
import bankapp.dtos.SavingAccountDTO;
import bankapp.entities.*;
import bankapp.enums.AccountStatus;
import bankapp.enums.OperationType;
import bankapp.exceptions.BalanceNotSufficientException;
import bankapp.exceptions.BankAccountNotFoundException;
import bankapp.exceptions.CustomerNotFoundException;
import bankapp.mappers.BankAccountMapperImp;
import bankapp.mappers.CustomerMapperImp;
import bankapp.repositories.AccountOperationRepository;
import bankapp.repositories.BankAccountRepository;
import bankapp.repositories.CustomerRepository;
import bankapp.services.BankAccountService;
import bankapp.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner1(CustomerService customerService, BankAccountMapperImp bankAccountMapperImp, BankAccountService bankAccountService, BankAccountRepository bankAccountRepository, CustomerRepository customerRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan","Karima","Jamal").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerService.saveCustomer(customer);
//                customerService.getAllCustomers().getBody().getData().forEach(c ->{
//                    CurrentAccountDTO currentAccount = new CurrentAccountDTO();
//                    currentAccount.setCustomer(c);
//                    currentAccount.setId(UUID.randomUUID().toString());
//                    currentAccount.setBalance(Math.random()*10000);
//                    currentAccount.setOverDraft(500);
//                    currentAccount.setCurrency("$");
//                    currentAccount.setStatus(AccountStatus.CREATED);
//                    currentAccount.setCreatedAt(new Date());
//
//                    bankAccountRepository.save(bankAccountMapperImp.toCurrentAccount(currentAccount));
//
//                    SavingAccountDTO savingAccount = new SavingAccountDTO();
//                    savingAccount.setCustomer(c);
//                    savingAccount.setId(UUID.randomUUID().toString());
//                    savingAccount.setBalance(Math.random()*10000);
//                    savingAccount.setInterestRate(5.5);
//                    savingAccount.setCurrency("$");
//                    savingAccount.setStatus(AccountStatus.CREATED);
//                    savingAccount.setCreatedAt(new Date());
//
//                    bankAccountRepository.save(bankAccountMapperImp.toSavingAccount(savingAccount));
//                } );


//                Objects.requireNonNull(bankAccountService.getAllBankAccounts().getBody()).getData().forEach(acc->{
//
//                    for (int i = 0; i < 10; i++) {
//                        AccountOperation accountOperation = new AccountOperation();
//                        accountOperation.setDate(new Timestamp(new Date().getTime()));
//                        accountOperation.setBankAccount(bankAccountMapperImp.toBankAccount(acc));
//                        accountOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
//                        accountOperation.setAmount(Math.random()*9000);
//                        accountOperationRepository.save(accountOperation);
//                    }
//                });


            });

            customerService.getAllCustomers().getBody().forEach(c->{
                try {
                    bankAccountService.saveCurrentAccount(Math.random()*90000,9000,c.getId());
                    bankAccountService.saveSavingAccount(Math.random()*120000,5.5,c.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            List<BankAccountDTO> bankAccounts = bankAccountService.getAllBankAccounts().getBody();
            for (BankAccountDTO bankAccount:bankAccounts){
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingAccountDTO){
                        accountId=((SavingAccountDTO) bankAccount).getId();
                    } else {
                        accountId=((CurrentAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                }
            }
//            List<CustomerDTO> customerDTOList = customerService.getAllCustomers().getBody().getData();
//            customerDTOList.forEach(c->{
//
//                try {
////                    bankAccountService.saveCurrentAccount(Math.round(Math.random()*10000),500,c.getId());
////                    bankAccountService.saveSavingAccount(Math.round(Math.random()*10000),5.5,c.getId());
//
//                    for (BankAccountDTO bankAccountDTO: bankAccountService.getAllBankAccounts().getBody().getData()){
//                        for (int i = 0; i < 10 ; i++) {
//                            bankAccountService.credit(bankAccountDTO.getId(),10000+Math.random()*10000,"Credit");
//                            bankAccountService.debit(bankAccountDTO.getId(),900+Math.random()*900,"Debit");
//                        }
//                    }
//                } catch (BalanceNotSufficientException| BankAccountNotFoundException e) {
//                    e.printStackTrace();
//                }
//            });


        };
    }

}
