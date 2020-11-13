package Model;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static Model.util.CityCodeDB.getCityCode;

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

    TreeMap<String,Integer> cityList = new TreeMap<String, Integer>();

    public Advisor(String originCity ) throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("src/main/resources/token.yml")));
        TOKEN = props.getProperty("token");
        PARAMS = props.entrySet()
                .stream()
                .map(entry -> {
                    return entry.getKey().toString() + entry.getValue().toString();
                })
                .collect(Collectors.joining("&"));
        ORIGIN = getCityCode(originCity);
    }

    public TreeMap<String,Integer> parseResponse (String jsonResponse){

        return cityList;
    }

    public void getDataFromServer (String startPoint, HashSet<String> cities){

    }
}
