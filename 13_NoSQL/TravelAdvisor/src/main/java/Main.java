import Model.Adviser;
import Model.util.CityCodeDB;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class Main {
    final static int CITY_COUNT = 10;
    final static int OUTPUT_COUNT = 2;
    private static Adviser adviser;


    public static void main(String[] args) throws IOException, InterruptedException {
        int inpCount = 0;
        String origin = null;

        Runtime.getRuntime().exec("docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis");
        Thread.sleep(1000);
        final RedisStorage storage = new RedisStorage();

        System.out.println("Добро пожаловать в TripAdviser.\n\nВведите 10 городов, в которые Вы хотели бы отправиться,\n" +
                "и Я покажу цену трех самых дорогих и трех самых дешевых маршрутов.\nP.S. Название города можно вводить на " +
                "русском и английском языке.\n");
        while (origin == null) {
            try {
                origin = CityCodeDB.getCityCode(getCityFromUser("\tВведите название города отправления"));
                adviser = new Adviser(origin);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }

        while (inpCount < CITY_COUNT) {
            try {
                String city = getCityFromUser("\tВведите название города");
                storage.storeRoute(city, adviser.getPrice(city));
                inpCount++;
            } catch (IllegalArgumentException | IOException ex) {
                System.out.println(ex.getMessage());
                //System.out.println("Данные по данному маршруту не найдены. Проверьте ввод или попробуйте ввести название города на английском.");
            } //System.out.println("Не удалось получить данные с сервера. Проверьте правильность ввода города.");
            catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("\tОсталось ввести городов: " + (CITY_COUNT - inpCount));
        }
        System.out.println("\n*************************************\n");
        printResult(storage);
        storage.shutdown();
        Runtime.getRuntime().exec("docker kill skill-redis");
    }


    private static String getCityFromUser(String msg) {
        Scanner inp = new Scanner(System.in);
        System.out.println(msg);
        System.out.print("\t> ");
        String city = inp.nextLine();
        log.info("Введен город: " + city);
        return city;
    }

    private static void printResult(RedisStorage storage) {
        int rightBorder = storage.size() - 1;
        System.out.println("Самые дешевые поездки будут в эти города:");
        System.out.println(resultFormat(storage.listKeys(0, OUTPUT_COUNT)));
        System.out.println("Самые дорогие поездки будут в эти города:");
        System.out.println(resultFormat(storage.listKeys(rightBorder - OUTPUT_COUNT , rightBorder)));

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
