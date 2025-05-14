package bankapp.dtos;

import bankapp.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private String id;
    private String type;
    private Date createdAt;
    private double balance;
    private String currency;
    private AccountStatus status;
    private CustomerDTO customer;
}
