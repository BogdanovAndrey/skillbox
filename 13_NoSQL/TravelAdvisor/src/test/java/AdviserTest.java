import Model.Adviser;
import Model.util.CityCodeDB;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
class AdviserTest {

    @Test
    void testGetPrice () throws IOException {
        Adviser adv = new Adviser(CityCodeDB.getCityCode("Moscow"));
        assertNotNull(adv.getPrice("Paris"));
    }
}
