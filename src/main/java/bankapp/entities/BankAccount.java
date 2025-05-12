package bankapp.entities;

import bankapp.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 4)
public abstract class BankAccount {
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private double balance;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "bankAccount",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountOperation> operations;
}
