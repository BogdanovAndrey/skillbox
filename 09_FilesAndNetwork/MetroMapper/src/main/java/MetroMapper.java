import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetroMapper {
    final static String SITE = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    //Число столбцов в исходной таблице
    final static int COLUMN_NUM = 8;
    //Столбец с номером линии
    final static int LINE_POZ = 0;
    //Столбец с названием станции
    final static int NAME_POZ = 1;
    //Столбец с номером линии для пересадки
    final static int CHANGE_POZ = 3;

    public static void main(String[] args) {
        try {

            JSONObject map = createJSONMap(parseHtml(SITE));
            //JSONParser parser = new JSONParser();
            // JSONObject testMap = (JSONObject) parser.parse(getJsonFile());
            //jsonToFile("out.json",testMap);
            //table.stream().flatMap(Collectors
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createJSONMap(Elements table) {
        JSONObject map = new JSONObject();
        JSONObject stations = new JSONObject();
        String currentLine = "";
        JSONArray lines = new JSONArray();
        JSONArray lineStations = new JSONArray();
        ArrayList<Map<String, String>> connections = new ArrayList<>();
        JSONObject newLine = new JSONObject();
        for (int i = 0; i < table.size(); i += COLUMN_NUM) {

            Element el = table.get(i);
            //номер линии
            String line = el.getElementsByTag("span").get(0).text();
            if (!line.equals(currentLine)) {

                //Название станции
                newLine.put("number", line);
                newLine.put("name", el.select("a[title]").first().attr("title"));
                newLine.put("color", "color");
                lines.add(newLine);
                if (!lineStations.isEmpty()) {
                    stations.put(currentLine, lineStations);
                }
                currentLine = line;
                //Номер станции
                newLine = new JSONObject();
                lineStations = new JSONArray();

            }
            lineStations.add(table.get((i + NAME_POZ)).select("a").first().text());
        }

        map.put("lines", lines);
        map.put("connections", connections);
        map.put("stations", stations);
        jsonToFile("out.json", map);

        return map;
    }


    static private void jsonToFile(String inpath, JSONObject json) {
        try {
            Path path = Path.of(inpath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);
            Files.writeString(path, json.toJSONString(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void htmlToFile(Elements table) {
        try {
            Path pathRaw = Path.of("outHtmlRaw.html");
            Path pathEdit = Path.of("outHtmlEdit.txt");
            if (Files.exists(pathRaw)) {
                Files.delete(pathRaw);
            }
            if (Files.exists(pathEdit)) {
                Files.delete(pathEdit);
            }
            Files.createFile(pathEdit);
            Files.createFile(pathRaw);
            table.forEach(element -> {
                try {
                    Files.writeString(pathRaw, element.toString() + "\n", StandardOpenOption.APPEND);
                    Files.writeString(pathEdit, element.text() + "\n", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Elements parseHtml(String site) throws IOException {
        Document content = Jsoup.connect(site).get();
        Elements table = content.select("table.standard.sortable:contains(Список может) td");
        htmlToFile(table);
        return table;
    }

    private static String getLineNum(Element el) {
        return null;
    }

    private static String getStationNum(Element el) {
        return null;
    }

    private static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get("example/map.json"));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
