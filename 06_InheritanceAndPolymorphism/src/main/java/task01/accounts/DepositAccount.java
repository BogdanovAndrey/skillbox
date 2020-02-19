package task01.accounts;

import java.time.LocalDateTime;

public class DepositAccount extends GeneralAccount {

    public DepositAccount(double moneyAmount, String currencyCode) {
        super(moneyAmount, currencyCode);
    }

    @Override
    public void withdrawMoney(double moneyAmount) {
        if (LocalDateTime.now().isBefore(getDateOfCreation().plusMonths(1))) {
            System.out.println("Too early to withdraw money");
        } else {
            super.withdrawMoney(moneyAmount);
        }
    }
}
