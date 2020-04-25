import core.MoneyMovement;
import core.MovementType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

    private static final String DATA_FILE_PATH = "../files/movementList.csv";
    private static final int OPERATION_DATE_POZ = 3;
    private static final int REFERENCE_POZ = 4;
    private static final int DESCRIPTION_POZ = 5;
    private static final int INCOME_POZ = 6;
    private static final int OUTCOME_POZ = 7;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.uu");
    private static final char DEFAULT_SEPARATOR = ',';

    public static void main(String[] args) {
        try {
            /*
            Создаем историю всех операция, состояющию из:
            1) Тип операции - приход или расход
            2) Дата операции
            3) Референс операции
            4) Описание операции
            5) Количество движемых средств
            */
            ArrayList<MoneyMovement> operationHistory = parseDataFile(DATA_FILE_PATH);

            /*
            Для удобства дальнейшей работы разделим историю на две части: приход и расход
            И просуммируем одинаковые типы платежей внутри каждой части
             */
            Map<MovementType, Map<String, Double>> result = operationHistory.stream()
                    .collect(Collectors.groupingBy(
                            MoneyMovement::getType,
                            Collectors.toMap(
                                    MoneyMovement::getDescription,
                                    MoneyMovement::getAmount,
                                    Double::sum)));

            printResult(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static ArrayList<MoneyMovement> parseDataFile(String path) throws IOException {
        List<String> rawData = Files.readAllLines(Paths.get(path).toAbsolutePath().normalize());
        ArrayList<MoneyMovement> entry = new ArrayList<>();
        rawData.remove(0);
        for (String s : rawData
        ) {
            entry.add(parseDataString(s));
        }
        return entry;
    }

    static MoneyMovement parseDataString(String data) {
        //Приведем дробные числа к нужному формату и уберем ковычки
        data = data.replaceAll(",(?=[0-9]+\")", ".").replaceAll("\"", "");
        //Разделям строку на части и обработаем их
        String[] movementData = data.split(String.valueOf(DEFAULT_SEPARATOR));
        String reference = movementData[REFERENCE_POZ];
        String description = parseDescription(movementData[DESCRIPTION_POZ]);
        double amount = Double.parseDouble(movementData[INCOME_POZ]) - Double.parseDouble(movementData[OUTCOME_POZ]);
        MovementType type = (amount > 0) ? MovementType.INCOME : MovementType.OUTCOME;
        LocalDate date = LocalDate.parse(movementData[OPERATION_DATE_POZ], FORMATTER);

        return new MoneyMovement(type, date, reference, description, Math.abs(amount));
    }


    static String parseDescription(String input) {
        input = input.replaceAll("\\s+/", "/");
        String output = input.split("\\s{2,}")[1];
        return output;
    }

    static void printResult(Map<MovementType, Map<String, Double>> map) {
        System.out.printf("Общий доход: %.2f рублей%n", getTotalMoneyAmount(map, MovementType.INCOME));
        System.out.printf("Общий расход: %.2f рублей%n", getTotalMoneyAmount(map, MovementType.OUTCOME));
        System.out.println("\nДетальная расшифровка расходов:");
        System.out.printf("%-60s%-10s%n", "Описание платежа", "Сумма");
        map.get(MovementType.OUTCOME).forEach((key, value) -> {
            System.out.printf("%-60s%-10s рублей%n", key, value.toString());
        });
    }

    static double getTotalMoneyAmount(Map<MovementType, Map<String, Double>> map, MovementType type) {
        return map.get(type)
                .values()
                .stream()
                .reduce(Double::sum).orElseThrow();
    }
}
