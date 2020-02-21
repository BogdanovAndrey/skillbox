package task01.accounts;

import java.time.LocalDateTime;

public class DepositAccount extends GeneralAccount {
    private LocalDateTime dateOfLastAdd;

    public DepositAccount(double moneyAmount, String currencyCode) {
        super(moneyAmount, currencyCode);
        dateOfLastAdd = LocalDateTime.now();
    }

    @Override
    public void withdrawMoney(double moneyAmount) {

        if (LocalDateTime.now().isBefore(dateOfLastAdd.plusMonths(1))) {
            System.out.println("Too early to withdraw money");
        } else {
            super.withdrawMoney(moneyAmount);
        }
    }

    @Override
    public void addMoney(double moneyAmount) {
        super.addMoney(moneyAmount);
        dateOfLastAdd = LocalDateTime.now();
    }
}
