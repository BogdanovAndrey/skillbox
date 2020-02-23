package task03.clients;

public class EntityClient extends Client {
    private final double COMMISSION = 0.01;

    public EntityClient(double moneyAmount) {
        super(moneyAmount);
    }

    @Override
    public void withdrawMoney(double moneyAmount) {
        super.withdrawMoney(moneyAmount * (1 + COMMISSION));
    }
}
