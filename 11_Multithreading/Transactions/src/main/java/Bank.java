
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

public class Bank {
    private HashMap<String, Account> accounts = new HashMap<>();
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
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
            accounts.get(fromAccountNum).block();
            accounts.get(toAccountNum).block();
        } else {
            accounts.get(fromAccountNum).decMoney(amount);
            accounts.get(toAccountNum).addMoney(amount);
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
}
