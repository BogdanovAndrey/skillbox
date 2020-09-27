import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class Account {
    private long money;
    private final String accNumber;
    private boolean status;

    public synchronized void addMoney(long modMoney) {
        money += modMoney;
    }

    public synchronized void decMoney(long modMoney) {
        money -= modMoney;
    }

    public boolean isBlocked() {
        return status;
    }

    public synchronized void block() {
        status = true;
    }

    public synchronized void unblock() {
        status = false;
    }

    public long getMoney() {
        return money;
    }
}
