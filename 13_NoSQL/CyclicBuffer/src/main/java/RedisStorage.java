import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import static java.lang.System.out;

public class RedisStorage {

    private final static String KEY = "USERS";
    // Объект для работы с Redis
    private RedissonClient redisson;
    // Объект для работы с ключами
    private RKeys rKeys;
    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<Integer> users;

    void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        users = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    void shutdown() {
        redisson.shutdown();
    }

    // Добавляем пользователя в список
    void addUser(int userID, int userPoz) {
        //ZADD USERS
        users.add(userPoz, userID);
    }

    // Получаем пользователя с наименьшим "счетом"
    public int getFirstUser() {
        return users.first();
    }

    public int getLastUserScore() {
        return users.takeLast();
    }

    //Меняем счет пользователя
    public int changeScore(int userID, int score) {
        int oldScore = getUser(userID);
        users.add(score, userID);
        return oldScore;
    }


    public void printAll() {
        out.println("Users:");
        users.stream().forEach(integer -> out.print(integer + " "));

        out.println("\nScores:");
        users.stream().forEach(integer -> out.print(users.getScore(integer) + " "));
        out.println();

    }

    public int getUser(Integer ID) {
        return users.getScore(ID).intValue();
    }
}
