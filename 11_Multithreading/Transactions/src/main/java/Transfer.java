import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class Transfer implements Runnable {
    final Bank myBank;
    final Random random;
    final String fromAccount;
    final String toAccount;


    @Override
    public void run() {
        try {
            int money = random.nextInt((int) myBank.getBalance(fromAccount));
            myBank.transfer(fromAccount,
                    toAccount,
                    money);
            System.out.println(Thread.currentThread().getName() + ": \n\t" +
                    "Переведено: " + money +
                    "\n\tсо счета " + fromAccount +
                    "\n\tна счет " + toAccount);
            if (myBank.getAccount(fromAccount).isBlocked()) {
                System.out.println("Аккаунт " + fromAccount + " заблокирован");
            }
            if (myBank.getAccount(toAccount).isBlocked()) {
                System.out.println("Аккаунт " + toAccount + " заблокирован");
            }
        } catch (Exception ex) {
            System.out.println(Thread.currentThread().getName() + ": ");
            ex.printStackTrace();
        }
    }
    public void printResult (TransferResult result){

    }
}
