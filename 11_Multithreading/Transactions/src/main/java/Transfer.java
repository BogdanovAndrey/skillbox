import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class Transfer implements Runnable {
    final Bank myBank;
    final long money;
    final String fromAccount;
    final String toAccount;


    @Override
    public void run() {
        try {
            TransferResult result = myBank.transfer(fromAccount,
                    toAccount,
                    money);
            printResult(result);
        } catch (Exception ex) {
            System.out.println(Thread.currentThread().getName() + ": ");
            ex.printStackTrace();
        }
    }

    public void printResult(TransferResult result) {
        switch (result) {
            case BOTH_BLOCKED: {
                System.out.println("Оба аккаунта заблокированы.");
            }
            case FIRST_BLOCKED: {
                System.out.println("Аккаунт " + fromAccount + " заблокирован");
                break;
            }
            case SECOND_BLOCKED: {
                System.out.println("Аккаунт " + toAccount + " заблокирован");
                break;
            }
            case OK: {
                System.out.println(Thread.currentThread().getName() + ": \n\t" +
                        "Переведено: " + money +
                        "\n\tсо счета " + fromAccount +
                        "\n\tна счет " + toAccount);
                break;
            }
            case FRAUD: {
                System.out.println("Обнаружено мошенничество! Оба аккаунта заблокированы!");
            }
        }


    }
}
