package task03.clients;

abstract public class Client {
    protected double moneyAmount;

    public Client(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void addMoney(double moneyAmount) {
        this.moneyAmount += moneyAmount;
    }

    public void withdrawMoney(double moneyAmount) {
        if (this.moneyAmount >= moneyAmount) {
            this.moneyAmount -= moneyAmount;
        } else {
            System.out.println("Not enough money");
        }
    }
}
