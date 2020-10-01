
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

public class Bank {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public TransferResult transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account source = accounts.get(fromAccountNum);
        Account receiver = accounts.get(toAccountNum);

        if (!source.isBlocked() && !receiver.isBlocked()) {
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                source.block();
                receiver.block();
                return TransferResult.FRAUD;
            } else {
                source.decMoney(amount);
                receiver.addMoney(amount);
                return TransferResult.OK;
            }
        } else {
            if (source.isBlocked() && receiver.isBlocked()) {
                return TransferResult.BOTH_BLOCKED;
            } else {
                if (source.isBlocked()) {
                    return TransferResult.FIRST_BLOCKED;
                } else {
                    return TransferResult.SECOND_BLOCKED;
                }
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getTotalBalance() throws NoSuchElementException {
        return accounts.values()
                .stream()
                .map(Account::getMoney)
                .reduce(Long::sum).orElseThrow();
    }

    public void addAccount(String accountNum, long money) {
        boolean defaultAccountStatus = false;
        accounts.put(accountNum, new Account(money, accountNum, defaultAccountStatus));
    }

    public Account getAccount(String accountNum) {
        return accounts.get(accountNum);
    }

    public Map<String,Account> getAccounts(){
        return accounts;
    }

}
