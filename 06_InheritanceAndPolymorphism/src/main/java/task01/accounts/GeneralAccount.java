package task01.accounts;

import java.util.Currency;

public class GeneralAccount {
    private static final String NOT_ENOUGH_MONEY = "Not enough money on your account";
    private static int totalAccountNumber;
    private int accountNumber;

    private double moneyAmount;
    private Currency currency;

    GeneralAccount(double moneyAmount, String currencyCode) {
        totalAccountNumber++;
        accountNumber = totalAccountNumber;
        this.moneyAmount = moneyAmount ;
        this.currency = Currency.getInstance(currencyCode);
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public static int getTotalAccountNumber() {
        return totalAccountNumber;
    }

    public void addMoney(double moneyAmount) {
        this.moneyAmount += moneyAmount;
        printAddInfo(moneyAmount);
    }

    public void withdrawMoney (double moneyAmount){
        if (getMoneyAmount() >= moneyAmount) {
            this.moneyAmount -= moneyAmount;
            printWithdrawInfo(moneyAmount);
        } else {
            System.out.println(NOT_ENOUGH_MONEY);
        }
    }
    protected void printWithdrawInfo (double moneyAmount){
        System.out.println("\tWithdrawn: " + moneyAmount + " " + getCurrency());
        System.out.println("\tMoney available: " + this.moneyAmount + " " + getCurrency());
    }
    protected void printAddInfo (double moneyAmount){
        System.out.println("Added: " + moneyAmount + " " + getCurrency());
        System.out.println("Money available: " + this.moneyAmount + " " + getCurrency());
    }
}
