package bankapp.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@DiscriminatorValue(value = "SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}
