package Model.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class CityCodeDB {
    private static final String CITY_NAME_DB_HOST = "http://autocomplete.travelpayouts.com/places2?";
    private static final String ENGLISH_NAME = "&locale=en";
    private static final String RUSSIAN_NAME = "&locale=ru";

    public static String getCityCode(String cityName) throws IOException {

        String request = CITY_NAME_DB_HOST + "term=" + encodeCityName(cityName) + "&types[]=city";

        JsonArray rawCityCode = DataGrabber.getDataFromServer(request).getAsJsonArray();

        if (rawCityCode.getAsJsonArray().size() != 0) {
            for (JsonElement el : rawCityCode) {
                JsonObject entry = el.getAsJsonObject();
                String responseCityName = entry.get("name").getAsString();
                if (responseCityName.equals(cityName)) {
                    return entry.get("code").getAsString();
                }
            }
        } else {
            throw new IOException("Can't find city code in: " + CITY_NAME_DB_HOST + cityName);
        }

        return null;
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

//    private static String getCodeFromResult(JsonElement cityName) {
//
//        return code;
//    }

    /*
     * Метод получает данные из базы кодов городов
     *  и передает сырые данные для заполнения двух таблиц:
     * 1) Английское имя города - код;
     * 2) Русское имя города - английское имя города.
     */
//    private static void getJsonData() throws IOException {
//        JsonElement data = DataGrabber.getDataFromServer(CITY_NAME_DB_HOST);
//        parseResponse(data.getAsJsonArray());
//    }


//    private static void parseResponse(JsonArray array) {
//        array.forEach(el -> {
//            JsonObject entry = el.getAsJsonObject();
//            String ruName = getStringFromJson("name", entry);
//            JsonObject translations = entry.get("name_translations").getAsJsonObject();
//            String enName = getStringFromJson("en", translations);
//            String code = getStringFromJson("code", entry);
//            cityCodes.put(enName, code);
//            if (!ruName.isBlank()) {
//                cityDictionary.put(ruName, enName);
//            }
//        });
//
//    }

    private static String getStringFromJson(String par, JsonObject obj) {
        return obj.get(par).isJsonNull() ?
                "" :
                obj.get(par).getAsString();
    }

    private static boolean isCyrillic(String s) {
        return Pattern.matches("[а-яА-ЯёЁ\\d\\s\\p{Punct}]*", s);
    }

}
