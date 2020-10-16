import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    void testFields() {
        Account account = new Account(5000, "1", AccountStatus.UNBLOCKED);
        assertEquals(5000, account.getMoney());
        assertEquals("1", account.getName());
        assertFalse(account.isBlocked());
        account.block();
        assertTrue(account.isBlocked());
        account.unblock();
        assertFalse(account.isBlocked());
        account.addMoney(1000);
        assertEquals(6000, account.getMoney());
        account.decMoney(1000);
        assertEquals(5000, account.getMoney());
    }
}
