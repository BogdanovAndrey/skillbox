package task01.accounts;


public class CardAccount extends GeneralAccount {
    private final int withdrawCommission = 1; //percent 

    public CardAccount(double moneyAmount, String currencyCode) {
        super(moneyAmount, currencyCode);
    }

    @Override
    public void withdrawMoney(double moneyAmount) {
        double moneyAmountWithCommission = moneyAmount * (100.0 + withdrawCommission) / 100;
        super.withdrawMoney(moneyAmountWithCommission);
    }

    @Override
    protected void printWithdrawInfo(double moneyAmount) {
        double commission = moneyAmount / ( 100.0 + withdrawCommission);
        System.out.println("\tWithdrawn: " + (moneyAmount - commission) + " " + getCurrency());
        System.out.println("\tCommission: " + commission + " " + getCurrency());
        System.out.println("\tMoney available: " + getMoneyAmount() + " " + getCurrency());

    }

    @Override
    protected void printAddInfo(double moneyAmount) {
        super.printAddInfo(moneyAmount);
    }
}
