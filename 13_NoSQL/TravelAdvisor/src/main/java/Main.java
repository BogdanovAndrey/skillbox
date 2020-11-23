import Model.Adviser;
import Model.util.CityCodeDB;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class Main {
    final static int CITY_COUNT = 3;
    final static int OUTPUT_COUNT = 3;
    private static Adviser adviser;

    public static void main(String[] args) throws IOException, InterruptedException {
        int inpCount = 0;
        String origin = null;

        Process p = Runtime.getRuntime().exec("docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis");
        Thread.sleep(1000);
        final RedisStorage storage = new RedisStorage();

        while (origin == null) {
            try {
                origin = CityCodeDB.getCityCode(getCityFromUser("Введите название города отправления"));
                adviser = new Adviser(origin);
            } catch (IllegalArgumentException ex) {
                System.out.println("Город не найден. Проверьте ввод или попробуйте ввести название на английском.");
            }
        }

        while (inpCount < CITY_COUNT) {
            try {
                String city = getCityFromUser("Введите название города");
                log.debug(city);
                storage.storeRoute(city, adviser.getPrice(city));
                inpCount++;
            } catch (IllegalArgumentException ex) {
                System.out.println("Данные по данному маршруту не найдены. Проверьте ввод или попробуйте ввести название города на английском.");
            } catch (IOException ex) {
                System.out.println("Не удалось получить данные с сервера.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Введено городов " + inpCount);
        }
        System.out.println("\n*************************************\n");
        printResult(storage);
        storage.shutdown();
    }


    private static String getCityFromUser(String msg) {
        Scanner inp = new Scanner(System.in);
        System.out.println(msg);
        System.out.print("\t> ");
        return inp.nextLine();
    }

    private static void printResult(RedisStorage storage) {
        int rightBorder = storage.size() - 1;
        System.out.println("Самые дешевые поездки будут в эти города:");
        System.out.println(resultFormat(storage.listKeys(0, OUTPUT_COUNT - 1)));
        System.out.println("Самые дорогие поездки будут в эти города:");
        System.out.println(resultFormat(storage.listKeys(rightBorder - OUTPUT_COUNT, rightBorder)));

    }

    private static String resultFormat(Map<String, Double> result) {
        StringBuilder outStr = new StringBuilder();
        int num = 0;
        for (Map.Entry<String, Double> entry : result.entrySet()) {
            num++;
            outStr.append("\t")
                    .append(num).append(". ")
                    .append(entry.getKey())
                    .append(" - ")
                    .append(entry.getValue())
                    .append(" руб.\n");
        }
        return outStr.toString();
    }


}
