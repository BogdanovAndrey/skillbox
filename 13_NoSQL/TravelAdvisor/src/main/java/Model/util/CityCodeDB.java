package Model.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CityCodeDB {
    private static final String CITY_NAME_DB_HOST = "http://api.travelpayouts.com/data/ru/cities.json";
    private static final Map<String, String> cityCodes = new HashMap<>();
    private static final Map<String, String> cityDictionary = new HashMap<>();

    public static String getCityCode(String cityName) throws IOException {
        if (cityCodes.isEmpty() || cityDictionary.isEmpty()) {
            getJsonData();
        }

        return getCodeFromResult(cityName);
    }

    private static String getCodeFromResult(String cityName) {
        if (isCyrillic(cityName)) {
            cityName = cityDictionary.getOrDefault(cityName, null);
        }
        String code = cityCodes.getOrDefault(cityName, null);
        if (code == null) {
            throw new IllegalArgumentException("Город не найден.");
        }
        return code;
    }

    /*
     * Метод получает данные из базы кодов городов
     *  и передает сырые данные для заполнения двух таблиц:
     * 1) Английское имя города - код;
     * 2) Русское имя города - английское имя города.
     */
    private static void getJsonData() throws IOException {
        JsonElement data = DataGrabber.getDataFromServer(CITY_NAME_DB_HOST);
        parseResponse(data.getAsJsonArray());
    }


    private static void parseResponse(JsonArray array) {
        array.forEach(el -> {
            JsonObject entry = el.getAsJsonObject();
            String ruName = getStringFromJson("name", entry);
            JsonObject translations = entry.get("name_translations").getAsJsonObject();
            String enName = getStringFromJson("en", translations);
            String code = getStringFromJson("code", entry);
            cityCodes.put(enName, code);
            if (!ruName.isBlank()) {
                cityDictionary.put(ruName, enName);
            }
        });

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
