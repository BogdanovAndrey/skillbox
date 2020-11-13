package Model.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CityCodeDB {
    final static String CITY_NAME_DB_HOST = "http://api.travelpayouts.com/data/ru/cities.json";
    final static Path RESULT_FILE = Paths.get("src/main/resources/cityCodes.txt");
    static Map<CityName, String> cityCodes = readFromFile();


    public static String getCityCode(String cityName) throws IOException {
        if (cityCodes == null) {
            cityCodes = getJsonData();
            writeToFile(cityCodes);
        }

        return getCodeFromResult(cityName);
    }

    private static String getCodeFromResult(String cityName) {
        System.out.println(cityCodes.containsKey(new CityName(cityName,null)));
        return null;
    }

    private static Map<CityName, String> getJsonData() throws IOException {
        URL url = new URL(CITY_NAME_DB_HOST);
        URLConnection request = url.openConnection();
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element


        return parseResponse(root.getAsJsonArray());
    }

    private static Map<CityName, String> parseResponse(JsonArray array) {
        Map<CityName, String> data = new HashMap<>();
        array.forEach(el -> {
            JsonObject entry = el.getAsJsonObject();
            String ruName = getStringFromJson("name", entry);
            JsonObject translations = entry.get("name_translations").getAsJsonObject();
            String enName = getStringFromJson("en", translations);
            String code = getStringFromJson("code", entry);
            data.put(new CityName(ruName, enName), code);
        });
        return data;
    }

    private static String getStringFromJson(String par, JsonObject obj) {
        return obj.get(par).isJsonNull() ?
                "" :
                obj.get(par).getAsString();
    }

    private static void writeToFile(Map<CityName, String> result) throws IOException {

        Files.deleteIfExists(RESULT_FILE);

        result.forEach((cityName, s) -> {
            String entry = "ruName/" + cityName.getRuName() +
                    "/enName/" + cityName.getEnName() +
                    "/code/" + s + "\n";
            try {
                Files.writeString(RESULT_FILE, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static Map<CityName, String> readFromFile() {
        Map<CityName, String> result;
        try {
            result = Files.readAllLines(RESULT_FILE).stream()
                    .map(s -> s.split("/"))
                    .collect(Collectors.toMap(
                            strings -> new CityName(strings[1], strings[3]),
                            strings -> strings[5]
                    ));
        } catch (IOException e) {
            result = null;
        }
        return result;
    }

}
