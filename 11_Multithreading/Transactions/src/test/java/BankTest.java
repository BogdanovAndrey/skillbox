import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


public class BankTest {
    private static final Bank myBank = new Bank();
    final static int accNum = 100;
    final static int maxMoney = 100000;
final static int multipl = 1000;
    @BeforeAll
    static void initAll() {

        for (int i = 1; i <= accNum; i++) {
            myBank.addAccount(String.valueOf(i), i * multipl);
        }
    }

    @BeforeEach
    void init() {
        myBank.getAccounts().forEach((s, account) -> account.setMoney(Integer.parseInt(s)*multipl));
    }

    @Test
    void testMissedAccount() {
        assertThrows(NullPointerException.class, () ->
                myBank.getBalance("101"));
    }

    @ParameterizedTest(name = "Money from account {0} equals {1}")
    @CsvSource({
            "1,    1000",
            "5,    5000",
            "49,   49000",
            "100,  100000"
    })
    void testGetMoney(String accountNumber, long expectedMoney) {
        long actualMoney = myBank.getBalance(String.valueOf(accountNumber));
        assertEquals(expectedMoney, actualMoney);
    }

    @Test
    void testSmallTransfer() {
        String firstAccount = "1";
        String secondAccount = "2";
        int moneyAmount = 1000;
        long firstAccountExpectedMoney = myBank.getBalance(firstAccount) - moneyAmount;
        long secondAccountExpectedMoney = myBank.getBalance(secondAccount) + moneyAmount;
        try {
            myBank.transfer(firstAccount, secondAccount, moneyAmount);
            testGetMoney(firstAccount, firstAccountExpectedMoney);
            testGetMoney(secondAccount, secondAccountExpectedMoney);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testBigTransfer() {
        String firstAccount = "100";
        String secondAccount = "3";
        int moneyAmount = 60000;

        try {
            int i = 0;
            for (; i < 10; i++) {
                if (myBank.transfer(firstAccount, secondAccount, moneyAmount) == TransferResult.FRAUD) {
                    break;
                }
                myBank.getAccount(firstAccount).addMoney(moneyAmount);
            }
            assertNotEquals(i, 10);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
