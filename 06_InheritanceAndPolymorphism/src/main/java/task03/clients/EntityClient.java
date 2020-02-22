package task03.clients;

public class EntityClient extends Client {
    private final double commission = 0.01;

    public EntityClient(double moneyAmount) {
        super(moneyAmount);
    }


    public double getCommission() {
        return commission;
    }

    @Override
    public void withdrawMoney(double moneyAmount) {
        super.withdrawMoney(moneyAmount * (1 + commission));
    }
}
