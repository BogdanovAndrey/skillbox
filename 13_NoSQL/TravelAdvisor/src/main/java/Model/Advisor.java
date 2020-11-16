package Model;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Collectors;

@Data
public class Advisor {
    //Пример запроса цен
    //http://api.travelpayouts.com/v2/prices/
    // latest?currency=rub&
    // period_type=year&
    // page=1&limit=30&
    // show_to_affiliates=true&
    // sorting=price&
    // token=РазместитеЗдесьВашТокен

    final String HOST = "http://api.travelpayouts.com/v2/prices/latest?";
    final String PARAMS;
    final String TOKEN;
    final String ORIGIN;


    public Advisor(String originCityCode) throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("src/main/resources/token.yml")));
        TOKEN = props.getProperty("token");
        PARAMS = props.entrySet()
                .stream()
                .map(entry -> {
                    return entry.getKey().toString() + entry.getValue().toString();
                })
                .collect(Collectors.joining("&"));
        ORIGIN = originCityCode;
    }


    public void getDataFromServer(String destinationPoint) {

    }
}
