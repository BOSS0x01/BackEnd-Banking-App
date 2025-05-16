package bankapp.repositories;

import bankapp.dtos.AccountOperationDTO;
import bankapp.entities.AccountOperation;
import bankapp.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
