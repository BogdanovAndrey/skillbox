import Model.util.CityCodeDB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityCodeDBTest {


    @Test
    void responseTest() {
        try {
            assertEquals("MOW", CityCodeDB.getCityCode("Москва"));
            assertEquals("MOW", CityCodeDB.getCityCode("Moscow"));
            assertEquals("PAR", CityCodeDB.getCityCode("Paris"));
            assertEquals("PAR", CityCodeDB.getCityCode("Париж"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
