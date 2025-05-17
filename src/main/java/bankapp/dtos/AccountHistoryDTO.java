package bankapp.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int pageSize;
    private int currentPage;
    private int totalPages;
    private List<AccountOperationDTO> accountOperations;
}
