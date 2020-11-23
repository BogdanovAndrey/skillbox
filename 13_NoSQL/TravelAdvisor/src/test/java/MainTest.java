import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final static RedisStorage storage = new RedisStorage();


    @Test
    void test() {
        storage.storeRoute("Paris", 1);
        storage.storeRoute("Madrid", 2);
        storage.storeRoute("Berlin", 3);
        storage.storeRoute("London", 4);
        storage.storeRoute("Sapporo", 5);
        storage.storeRoute("Punta De Maisi", 6);
        storage.storeRoute("Jammu", 7);
        storage.storeRoute("Anta", 8);
        storage.storeRoute("Kirov", 9);
        storage.storeRoute("Lander", 10);

        assertEquals(storage.size(), 10);
        assertTrue(storage.listKeys(0, 1).containsKey("Paris"));
        assertTrue(storage.listKeys(storage.size() - 2, storage.size() - 1).containsKey("Lander"));
    }
}
