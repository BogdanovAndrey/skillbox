package task03.clients;

public class EntrepreneurClient extends Client {
    final private double COMMISSION = 0.01;
    final private int COMMISSION_BORDER = 1000;

    public EntrepreneurClient(double moneyAmount) {
        super(moneyAmount);
    }

    @Override
    public void addMoney(double moneyAmount) {
        if (moneyAmount < COMMISSION_BORDER) {
            super.addMoney(moneyAmount * (1 - COMMISSION));
        } else {
            super.addMoney(moneyAmount * (1 - COMMISSION / 2));
        }
    }
}
