package core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MoneyMovement {
    private final MovementType type;
    private final LocalDate date;
    private String reference;
    private String description;
    private double amount;

}
