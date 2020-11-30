package Model;

import Model.util.CityCodeDB;
import Model.util.DataGrabber;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.TreeSet;


@Slf4j
@Data
public class Adviser {
    //Пример запроса цен
    //http://api.travelpayouts.com/v1/prices/direct?
    // origin=MOW&
    // destination=HKT&
    // depart_date=2017-11&
    // return_date=2017-12&
    // token=РазместитеЗдесьВашТокен

    final String HOST = "http://api.travelpayouts.com/v1/prices/direct?";
    final String TOKEN;
    final String ORIGIN;
    final String DEPART_DATE;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("yyyy-MM"));


    public Adviser(String originCityCode) throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("src/main/resources/token.yml")));
        TOKEN = props.getProperty("token");
        ORIGIN = originCityCode;
        DEPART_DATE = LocalDate.now().format(formatter);
    }

    public Double getPrice(String destinationPoint) throws IOException {
        String destinationPointCode = CityCodeDB.getCityCode(destinationPoint);
        String request = cheapestTicketRequestBuilder(destinationPointCode);
        log.info("Отправлен запрос");
        log.info(request);
        JsonElement data = DataGrabber.getDataFromServer(request);
        log.info("Получен ответ");
        log.info(data.toString());
        TreeSet<Double> lowestPrice = parseResponse(data, destinationPointCode);
        return lowestPrice.first();
    }

    TreeSet<Double> parseResponse(JsonElement data, String destinationPointCode) {
        TreeSet<Double> tickets = new TreeSet<>();
        JsonObject response = data.getAsJsonObject();
        if (response.get("success").getAsString().equals("true")) {
            JsonElement uncheckedResultList = response.get("data").getAsJsonObject()  //Получили набор направлений
                    .get(destinationPointCode);
            if (uncheckedResultList == null) {
                throw new IllegalArgumentException("Данные по данному маршруту не найдены. Проверьте ввод или попробуйте ввести название города на английском.");
            }
            JsonObject resultList = uncheckedResultList.getAsJsonObject();
            for (String s : resultList.getAsJsonObject().keySet()) {
                JsonObject ticketDetails = resultList.get(s).getAsJsonObject();
                tickets.add(Double.valueOf(ticketDetails.get("price").toString()));
            }

        }
        return tickets;
    }

    private String cheapestTicketRequestBuilder(String destinationPointCode) throws IOException {
        return HOST +
                "origin=" +
                ORIGIN +
                "&" +
                "destination=" +
                destinationPointCode +
                "&" +
                "depart_date=" +
                DEPART_DATE +
                "&" +
                "token=" +
                TOKEN;
    }
}
