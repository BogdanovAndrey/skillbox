import java.util.ArrayList;
import java.util.Random;

public class Main {
    final static Bank myBank = new Bank();
    final static Random random = new Random(1);
    final static int accNum = 100;
    final static int maxMoney = 100000;

    public static void main(String[] args) {
        ArrayList<Thread> transfers = new ArrayList<>(accNum);
        for (int i = 1; i <= accNum; i++) {
            myBank.addAccount(String.valueOf(i), random.nextInt(maxMoney));
        }

        for (int i = 0; i < accNum; i++) {
            transfers.add(new Thread(new Transfer(
                    myBank,
                    random,
                    String.valueOf(i + 1),
                    String.valueOf(random.nextInt(accNum)+1))));
        }
        transfers.forEach(Thread::start);
        System.out.println("complete");
    }
}
