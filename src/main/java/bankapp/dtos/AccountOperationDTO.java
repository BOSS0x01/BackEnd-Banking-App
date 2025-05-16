package bankapp.dtos;

import bankapp.entities.BankAccount;
import bankapp.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private OperationType type;
    private String description;
}
