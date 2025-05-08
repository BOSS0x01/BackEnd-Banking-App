package bankapp.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@DiscriminatorValue(value = "CA")
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
