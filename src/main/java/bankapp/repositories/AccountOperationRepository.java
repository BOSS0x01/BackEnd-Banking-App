package bankapp.repositories;

import bankapp.dtos.AccountOperationDTO;
import bankapp.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccountId(String bankAccountId);
}
