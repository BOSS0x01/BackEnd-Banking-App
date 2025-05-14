package bankapp.dtos;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)

public class CurrentAccountDTO extends BankAccountDTO {
    private double overDraft;
}
