import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankMethodsTest {
    final static int accNum = 100;
    final static int maxMoney = 100000;
    final static int multipl = 1000;
    final static Random random = new Random(1);
    private static final Bank testBank = new Bank();
    final Waiter waiter = new Waiter();
    int moneyTestValue = 50000;
    int bigMoneyAmount = (int) (moneyTestValue * 1.1);
    int smallMoneyAmount = 1000;

    @BeforeAll
    static void initAll() {
//Создаем тестовый банк
        for (int i = 1; i <= accNum; i++) {
            testBank.addAccount(String.valueOf(i), i * multipl);
        }
    }

    @BeforeEach
    void init() {
        //Наполним счета деньгами
        //Т.к. иметь публичные методы для установки денег и получения всех счетов банка не здорово - воспользуемся
        //Reflection API
        try {
            Method method = testBank.getClass().getDeclaredMethod("getAccounts");
            method.setAccessible(true);
            final HashMap<String, Account> accounts = (HashMap<String, Account>) method.invoke(testBank);
            accounts.forEach((s, account) -> {
                try {
                    Method accountMethod = account.getClass().getDeclaredMethod("setMoney", long.class);
                    accountMethod.setAccessible(true);
                    accountMethod.invoke(account, Integer.parseInt(s) * multipl);
                    account.unblock();
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    @Test
    @Order(1)
    void testMissedAccount() {
        assertThrows(NullPointerException.class, () ->
                testBank.getBalance("101"));
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({
            "1,    1000",
            "5,    5000",
            "49,   49000",
            "100,  100000"
    })
    void testGetMoney(String accountNumber, long expectedMoney) {
        long actualMoney = testBank.getBalance(String.valueOf(accountNumber));
        assertEquals(expectedMoney, actualMoney);
    }

    //Проверка маленьких переводов (без проверки банком)
    @Test
    @Order(3)
    void testSmallTransfer() {
        String firstAccount = "1";
        String secondAccount = "2";

        long firstAccountExpectedMoney = testBank.getBalance(firstAccount) - smallMoneyAmount;
        long secondAccountExpectedMoney = testBank.getBalance(secondAccount) + smallMoneyAmount;
        testBank.transfer(firstAccount, secondAccount, smallMoneyAmount);
        testGetMoney(firstAccount, firstAccountExpectedMoney);
        testGetMoney(secondAccount, secondAccountExpectedMoney);
        notEnoughMoneyTest(firstAccount, secondAccount);
    }

    //Проверка больших переводов - с проверкой банком
    @Test
    @Order(4)
    void testBigTransfer() {
        String firstAccount = "100";
        String secondAccount = "3";
        String thirdAccount = "50";
        //Проверка работы защиты
        fraudOrNotTest(firstAccount, secondAccount);
        //Проверка работы с заблокированными счетами
        oneBlockedTest(firstAccount, thirdAccount);
        bothBlockedTest(firstAccount, secondAccount);

    }

    private void notEnoughMoneyTest(String firstAccount, String secondAccount) {
        assertEquals(TransferResult.NOT_ENOUGH_MONEY,
                testBank.transfer(firstAccount, secondAccount, testBank.getBalance(firstAccount) + 1));
    }

    void fraudOrNotTest(String firstAccount, String secondAccount) {
        //Поскольку защита с рандомом, проверка может не поймать ошибку
        //С вероятностью 0.0009765625 мы можем получить 10 раз подряд false в методе Bank.isFraud()
        //Что можно считать практически невероятным

        int tryCount = 0;
        for (; tryCount < 10; tryCount++) {
            long expectedMoneyOnFirst = testBank.getAccount(firstAccount).getMoney();
            long expectedMoneyOnSecond = testBank.getAccount(secondAccount).getMoney();
            if (testBank.transfer(firstAccount, secondAccount, bigMoneyAmount) == TransferResult.FRAUD) {
                break;
            } else {
                //Одновременно проверяем, что переводы больших сумм так же работают
                assertEquals(expectedMoneyOnFirst - bigMoneyAmount, testBank.getAccount(firstAccount).getMoney());
                assertEquals(expectedMoneyOnSecond + bigMoneyAmount, testBank.getAccount(secondAccount).getMoney());
            }
            //Восстанавливаем деньги, чтобы продолжить переводы
            testBank.getAccount(firstAccount).addMoney(bigMoneyAmount);
        }
        //Проверяем, что метод Bank.isFraud() сработал
        assertNotEquals(tryCount, 10);
    }

    //Проверяем логику переводов с одного заблокированного счета
    void oneBlockedTest(String blockedAccount, String notBlockedAccount) {
        long expectedMoneyOnBlockedAccount = testBank.getAccount(blockedAccount).getMoney();
        long expectedMoneyOnNotBlockedAccount = testBank.getAccount(notBlockedAccount).getMoney();

        TransferResult tr = testBank.transfer(blockedAccount, notBlockedAccount, smallMoneyAmount);
        assertEquals(tr, TransferResult.FIRST_BLOCKED);
        assertEquals(expectedMoneyOnBlockedAccount, testBank.getAccount(blockedAccount).getMoney());
        assertEquals(expectedMoneyOnNotBlockedAccount, testBank.getAccount(notBlockedAccount).getMoney());

        tr = testBank.transfer(notBlockedAccount, blockedAccount, smallMoneyAmount);
        assertEquals(tr, TransferResult.SECOND_BLOCKED);
        assertEquals(expectedMoneyOnBlockedAccount, testBank.getAccount(blockedAccount).getMoney());
        assertEquals(expectedMoneyOnNotBlockedAccount, testBank.getAccount(notBlockedAccount).getMoney());
    }

    //Проверяем логику переводов с двух заблокированных счетов
    void bothBlockedTest(String firstAccount, String secondAccount) {
        long expectedMoneyOnFirst = testBank.getAccount(firstAccount).getMoney();
        long expectedMoneyOnSecond = testBank.getAccount(secondAccount).getMoney();
        assertEquals(testBank.transfer(firstAccount, secondAccount, smallMoneyAmount), TransferResult.BOTH_BLOCKED);
        assertEquals(expectedMoneyOnFirst, testBank.getAccount(firstAccount).getMoney());
        assertEquals(expectedMoneyOnSecond, testBank.getAccount(secondAccount).getMoney());
    }

    //Проверка параллельных переводов со всех счетов небольших сумм
    @Test
    @Order(5)
    void parallelTransferSmallMoneyTest() throws TimeoutException, InterruptedException {
        ArrayList<Thread> transfers = new ArrayList<>();
        long expectedBankMoney = testBank.getTotalBalance();
        for (int i = 0; i < accNum; i++) {
            String firstAccount = String.valueOf(i + 1);
            String secondAccount = String.valueOf(random.nextInt(accNum) + 1);
            transfers.add(new Thread(() -> {
                testBank.transfer(firstAccount, secondAccount, smallMoneyAmount);
                waiter.resume();
            }));
        }
        ;

        transfers.forEach(Thread::start);
        waiter.await(30, TimeUnit.SECONDS, accNum);
        assertEquals(expectedBankMoney, testBank.getTotalBalance());
    }

    //Проверка параллельных переводов со всех счетов больших сумм
    //С учетом количества счетов и времени проверки тест занимает не менее 100 секунд
    @Test
    @Order(6)
    void parallelTransferBigMoneyTest() throws TimeoutException, InterruptedException {
        ArrayList<Thread> transfers = new ArrayList<>();
        long expectedBankMoney = testBank.getTotalBalance();
        for (int i = 0; i < accNum; i++) {
            String firstAccount = String.valueOf(i + 1);
            String secondAccount = String.valueOf(random.nextInt(accNum) + 1);
            transfers.add(new Thread(() -> {
                testBank.transfer(firstAccount, secondAccount, bigMoneyAmount);
                waiter.resume();
            }));
        }
        ;

        transfers.forEach(Thread::start);
        waiter.await(accNum * 2, TimeUnit.SECONDS, accNum);
        assertEquals(expectedBankMoney, testBank.getTotalBalance());
    }

}
