import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.out;

public class RedisStorage {

    private final static String KEY = "TRAVEL_ROUTES";
    // Объект для работы с Redis
    private RedissonClient redisson;
    // Объект для работы с ключами
    private RKeys rKeys;
    // Объект для работы с Sorted Set'ом
    private final RScoredSortedSet<String> citiesList;

    public RedisStorage() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        citiesList = redisson.getScoredSortedSet(KEY);
        rKeys.deleteByPattern("*");
    }

    // Вывод выбранного диапазона
    public Map<String, Double> listKeys(int start, int end) {
        assert start >= 0;
        assert end < citiesList.size();
        LinkedHashMap<String, Double> result = new LinkedHashMap<>(end - start);
        citiesList.entryRange(start, end).
                forEach(stringScoredEntry ->
                        result.put(stringScoredEntry.getValue(),
                                stringScoredEntry.getScore()));
        return result;
    }

    public int size() {
        return citiesList.size();
    }

    void shutdown() {
        redisson.shutdown();
    }

    // Фиксирует стоимость билета до выбранного города
    void storeRoute(String city, double price) {
        //ZADD TRAVEL_ROUTES
        citiesList.add(price, city);
    }


}
