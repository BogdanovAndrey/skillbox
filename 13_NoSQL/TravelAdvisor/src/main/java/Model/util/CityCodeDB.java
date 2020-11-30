package Model.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Slf4j
public class CityCodeDB {
    private static final String CITY_NAME_DB_HOST = "http://autocomplete.travelpayouts.com/places2?";
    private static final String ENGLISH_NAME = "&locale=en";
    private static final String RUSSIAN_NAME = "&locale=ru";

    public static String getCityCode(String cityName) throws IOException {

        String request = CITY_NAME_DB_HOST + "term=" + encodeCityName(cityName) + "&types[]=city";
        log.info(request);
        JsonArray rawCityCode = DataGrabber.getDataFromServer(request).getAsJsonArray();
        log.info(rawCityCode.toString());
        if (rawCityCode.getAsJsonArray().size() != 0) {
            for (JsonElement el : rawCityCode) {
                JsonObject entry = el.getAsJsonObject();
                String responseCityName = entry.get("name").getAsString();
                if (responseCityName.contains(cityName)) {
                    return entry.get("code").getAsString();
                }
            }
        }
        throw new IOException("Не удается найти город. Проверьте правильность ввода.");

    }

    private static String encodeCityName (String cityName){
        String encodedCityName;

        if (isCyrillic(cityName)) {
            encodedCityName =  URLEncoder.encode(cityName, StandardCharsets.UTF_8) + RUSSIAN_NAME;
        } else {
            encodedCityName = cityName + ENGLISH_NAME;
        }
        return encodedCityName;
    }


    private static String getStringFromJson(String par, JsonObject obj) {
        return obj.get(par).isJsonNull() ?
                "" :
                obj.get(par).getAsString();
    }

    private static boolean isCyrillic(String s) {
        return Pattern.matches("[а-яА-ЯёЁ\\d\\s\\p{Punct}]*", s);
    }

}
