package bankapp.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private BankAccountDTO bankAccount;
    private int pageSize;
    private int currentPage;
    private int totalPages;
    private List<AccountOperationDTO> accountOperations;
}
