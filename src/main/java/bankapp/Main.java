package bankapp;

import bankapp.entities.*;
import bankapp.enums.AccountStatus;
import bankapp.enums.OperationType;
import bankapp.repositories.AccountOperationRepository;
import bankapp.repositories.BankAccountRepository;
import bankapp.repositories.CustomerRepository;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan","Karima","Jamal").forEach(name -> {
               Customer customer = new Customer();
               customer.setName(name);
               customer.setEmail(name+"@gmail.com");
               customerRepository.save(customer);

               customerRepository.findAll().forEach(c ->{
                   CurrentAccount currentAccount = new CurrentAccount();
                   currentAccount.setCustomer(c);
                   currentAccount.setId(UUID.randomUUID().toString());
                   currentAccount.setBalance(Math.random()*10000);
                   currentAccount.setOverDraft(Math.random()*10000);
                   currentAccount.setCurrency("$");
                   currentAccount.setStatus(AccountStatus.CREATED);
                   currentAccount.setCreatedAt(new Date());

                   bankAccountRepository.save(currentAccount);

                   SavingAccount savingAccount = new SavingAccount();
                   savingAccount.setCustomer(c);
                   savingAccount.setId(UUID.randomUUID().toString());
                   savingAccount.setBalance(Math.random()*10000);
                   savingAccount.setInterestRate(5.5);
                   savingAccount.setCurrency("$");
                   savingAccount.setStatus(AccountStatus.CREATED);
                   savingAccount.setCreatedAt(new Date());

                   bankAccountRepository.save(savingAccount);
               } );


                bankAccountRepository.findAll().forEach(acc->{
                    for (int i = 0; i < 10; i++) {
                        AccountOperation accountOperation = new AccountOperation();
                        accountOperation.setDate(new Timestamp(new Date().getTime()));
                        accountOperation.setBankAccount(acc);
                        accountOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                        accountOperation.setAmount(Math.random()*9000);
                        accountOperationRepository.save(accountOperation);
                    }
                });
            });
        };
    }
}
