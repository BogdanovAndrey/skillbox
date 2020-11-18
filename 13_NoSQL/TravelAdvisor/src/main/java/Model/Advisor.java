package Model;

import Model.util.CityCodeDB;
import Model.util.DataGrabber;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.TreeMap;

@Data
public class Advisor {
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

    public Advisor(String originCityCode) throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("src/main/resources/token.yml")));
        TOKEN = props.getProperty("token");
        ORIGIN = originCityCode;
        DEPART_DATE = LocalDate.now().format(formatter);
    }


    public double getPrice(String destinationPoint) throws IOException {
        String destinationPointCode = CityCodeDB.getCityCode(destinationPoint);
        String request = cheapestTicketRequestBuilder(destinationPointCode);
        JsonElement data = DataGrabber.getDataFromServer(request);
        TreeMap<Double, Integer> lowesPrice = parseResponse(data, destinationPointCode);
        return lowesPrice.firstKey();
    }

    TreeMap<Double, Integer> parseResponse(JsonElement data, String destinationPointCode) {
        JsonObject response = data.getAsJsonObject();
        if (response.get("success").getAsString().equals("true")) {
            JsonObject resultList = response.get("data").getAsJsonObject()  //Получили набор направлений
                    .get(destinationPointCode).getAsJsonObject();
        }

        return new TreeMap<>();
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
