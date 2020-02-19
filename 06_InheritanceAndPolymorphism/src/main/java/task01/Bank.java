package task01;

import task01.accounts.CardAccount;
import task01.accounts.DepositAccount;
import task01.accounts.GeneralAccount;

import java.time.format.DateTimeFormatter;

public class Bank {
    public static void main(String[] args) {

        DepositAccount firstDeposit = new DepositAccount(1000000, "RUB");
        DepositAccount secondDeposit = new DepositAccount(1000000, "USD");
        CardAccount firstCard = new CardAccount(200000, "EUR");
        CardAccount secondCard = new CardAccount(0, "RUB");

        System.out.println("First account number - " + firstDeposit.getAccountNumber());
        System.out.println("Second account number - " + secondDeposit.getAccountNumber());
        System.out.println("Third account number - " + firstCard.getAccountNumber());
        System.out.println("Forth account number - " + secondCard.getAccountNumber());
        System.out.println("\tCreated " + GeneralAccount.getTotalAccountNumber() + " accounts");
        System.out.println("\nAttempt to withdraw money from first deposit account:");
        firstDeposit.withdrawMoney(1000);
        System.out.println("\nWithdrawing money from first card account:");
        firstCard.withdrawMoney(10000);

        System.out.println("\nSecond deposit account was created at " + secondDeposit.getDateOfCreation().format(DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss")));

        secondCard.addMoney(5600.1);
        System.out.println("\nSecond card account has " + secondCard.getMoneyAmount() + " " + secondCard.getCurrency());
    }

}
