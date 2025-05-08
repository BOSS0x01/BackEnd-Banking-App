package bankapp.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private  String name;
    @Column(unique=true)
    private  String email;

    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "customer")
    private List<BankAccount> bankAccounts;
}
