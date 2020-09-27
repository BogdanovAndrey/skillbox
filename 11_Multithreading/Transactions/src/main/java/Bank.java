
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

public class Bank {
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();
    private final boolean defaultAccountStatus = false;

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
        Account from = accounts.get(fromAccountNum);
        Account to = accounts.get(toAccountNum);

        if (!from.isBlocked() && !to.isBlocked()) {
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                from.block();
                to.block();
                return TransferResult.FRAUD;
            } else {
                from.decMoney(amount);
                to.addMoney(amount);
                return TransferResult.OK;
            }
        } else {
            if (from.isBlocked()&&to.isBlocked()){
                return TransferResult.BOTH_BLOCKED;
            } else {
                if (from.isBlocked()){
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
        accounts.put(accountNum, new Account(money, accountNum, defaultAccountStatus));
    }

    public Account getAccount(String accountNum) {
        return accounts.get(accountNum);
    }


}
