package bankapp.dtos;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)

public class SavingAccountDTO extends BankAccountDTO {
    private double interestRate;
}
