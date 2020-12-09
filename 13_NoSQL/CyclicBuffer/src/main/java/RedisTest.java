import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

import static java.lang.System.out;

public class RedisTest {

    // Запуск докер-контейнера:
    // docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis

    // Для теста будем считать неактивными пользователей, которые не заходили 2 секунды
    private static final int DELETE_SECONDS_AGO = 2;

    // Кол-во показов пользователей
    private static final int CYCLES = 50000;

    // И всего на сайт заходило 1000 различных пользователей
    private static final int NUM_USER = 20;

    // Также мы добавим задержку между посещениями
    private static final int SLEEP = 1; // 1 миллисекунда

    private static final SimpleDateFormat DF = new SimpleDateFormat("HH:mm:ss");

    private static int USERS_SHOWN = 0;

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis");
        RedisStorage redis = new RedisStorage();
        redis.init();

        //Исходные данные
        fillStorage(redis, NUM_USER);
        redis.printAll();

        // Эмулируем работу сайта
        for (int i = 1; i <= CYCLES; i++) {

            int oldScore = 0;
            //Каждый 10 показ кто-то платит донат
            if (i % 10 == 0) {
                //Получаем номер "счастливчика"
                int premiumUserNum = premiumUserNum();
                //Запоминаем его счет
                oldScore = redis.changeScore(premiumUserNum, 0);

                out.printf("Пользователь >%d< заплатил.\n", premiumUserNum);
            }
            //Показываем пользователя и запоминаем его ID
            int shownID = showUsers(redis);
            //Запоминаем его старый счет (если платил, то счет уже запомнен)
            oldScore = (oldScore == 0) ? redis.getUser(shownID) : oldScore;
            //Закидываем его в новый цикл показа
            //По условию ДЗ не ясно, что делать если пользователь заплатил еще раз до окончания цикла показа
            //Например: Показали 1, 2, 3. 5 заплатил. Показали 5, 4, 6. Опять заплатил 5.
            //В текущей реализации он "штатно" появится теперь через 2 круга показов
            // (его стартовый счет был 5, потом 25 и в итоге стал 45)
            int newScore = oldScore + NUM_USER;

            //Возвращаем пользователя назад в очередь с новым счетом
            redis.addUser(shownID, newScore);
        }

        redis.shutdown();
    }

    //Возвращает номер пользователя в диапазоне от 1 до NUM_USER
    private static int premiumUserNum() {
        return (new Random().nextInt(NUM_USER - 1) + 1);
    }


    private static void fillStorage(RedisStorage redis, int numUsers) {
        for (int i = 1; i <= numUsers; i++) {
            redis.addUser(i, i);
        }
    }

    private static int showUsers(RedisStorage redis) {
        int shownID = redis.getFirstUser();
        out.printf("Показываем пользователя номер: %d\n", shownID);
        USERS_SHOWN++;
        return shownID;

    }


}
