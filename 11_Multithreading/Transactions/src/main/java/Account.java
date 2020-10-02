import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Account {
    private long money;
    private final String accNumber;
    private AccountStatus status;

    public synchronized void addMoney(long modMoney) {
        money += modMoney;

    }

    public synchronized void decMoney(long modMoney) throws IllegalArgumentException {
        if (money >= modMoney) {
            money -= modMoney;
        } else {
            throw new IllegalArgumentException("Недостаточно средств");
        }
    }

    public boolean isBlocked() {
        return status == AccountStatus.BLOCKED;
    }

    public synchronized void block() {
        status = AccountStatus.BLOCKED;
    }

    public synchronized void unblock() {
        status = AccountStatus.UNBLOCKED;
    }

    public long getMoney() {
        return money;
    }

    private void setMoney(long money) {
        this.money = money;
    }

    public String getName() {
        return accNumber;
    }
}
