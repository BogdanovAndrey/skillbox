import Model.Advisor;
import Model.util.CityCodeDB;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    final static int CITY_COUNT = 3;
    final static int OUTPUT_COUNT = 3;

    public static void main(String[] args) throws IOException, IllegalAccessException {


        int inpCount = 0;
        String origin = null;

        while (origin == null) {
            try {
                origin = CityCodeDB.getCityCode(getCityFromUser("Введите название города отправления"));
                Advisor advisor = new Advisor(origin);
            } catch (IllegalArgumentException ex) {
                System.out.println("Город не найден. Проверьте ввод или попробуйте ввести название на английском.");
            }
        }

        while (inpCount <= CITY_COUNT) {
            try {
                String city = getCityFromUser("Введите название города");

                inpCount++;
            } catch (IllegalArgumentException ex) {
                System.out.println("Город не найден. Проверьте ввод или попробуйте ввести название на английском.");
            } catch (IOException ex) {
                System.out.println("Не удалось получить данные с сервера.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Введено городов " + inpCount);
        }


    }


    private static String getCityFromUser(String msg) throws IOException, IllegalAccessException {
        Scanner inp = new Scanner(System.in);
        System.out.println(msg);
        System.out.print("\t> ");
        return inp.nextLine();
    }

    private static void putPriceInDB(String cityName) {

    }

}
