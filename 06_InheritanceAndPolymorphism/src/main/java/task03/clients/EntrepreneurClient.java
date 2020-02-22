package task03.clients;

public class EntrepreneurClient extends Client {
    final private double commission = 0.01;
    final private int commissionBorder = 1000;

    public EntrepreneurClient(double moneyAmount) {
        super(moneyAmount);
    }

    public double getCommission() {
        return commission;
    }

    @Override
    public void addMoney(double moneyAmount) {
        if (moneyAmount < commissionBorder) {
            super.addMoney(moneyAmount * (1 - commission));
        } else {
            super.addMoney(moneyAmount * (1 - commission / 2));
        }
    }
}
