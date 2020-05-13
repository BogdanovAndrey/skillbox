import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.LineColor;
import service.MetroStation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;


public class MetroMapper {
    final static String WIKI_PAGE = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    //Столбец с номером линии
    final static int LINE_POZ = 0;
    //Столбец с названием станции
    final static int NAME_POZ = 1;
    //Столбец с номером линии для пересадки
    final static int CONNECTION_POZ = 3;
    //Путь к выходному файлу
    final static String OUTPUT_PATH = "target/MoscowMetroMap.json";

    final static boolean OFFLINE = false;

    public static void main(String[] args) {
        try {
            //Создаем аналог таблицы с сайта wiki
            Set<MetroStation> rawLineTable = fillStationSet(parseWikiPage());
            //Создаем финальный JSON объект
            JSONObject map = tableToJson(rawLineTable);

            jsonToFile(OUTPUT_PATH, map);
            //Выводим полученный результат
            printAnalyzeResult(OUTPUT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Elements parseWikiPage() throws IOException {
        Document content = null;
        if (OFFLINE) {
            try {
                File savedPage = new File("example/wiki.html");
                content = Jsoup.parse(savedPage, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            content = Jsoup.connect(WIKI_PAGE).get();
        }
        Elements table = content.select("table.standard.sortable:contains(Список может) tr:has(td)");
        //htmlToFile(table);
        return table;
    }

    private static Set<MetroStation> fillStationSet(Elements htmlContent) {
        //Разбираем строчку на элементу
        return htmlContent.parallelStream()
                .map(el -> {
                    //Выбираем строчку
                    Elements row = el.select("td");
                    //Имя станции
                    String name = row.get(NAME_POZ).select("a").first().text().trim();
                    //Список линий,куда она входит
                    Map<String, String> lines = getLinesInCell(row.get(LINE_POZ));
                    //Список пересадок с этой станции
                    Map<String, String> connections = getConnectionsInCell(row.get(CONNECTION_POZ));
                    return new MetroStation(name, lines, connections, false);
                }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static JSONObject tableToJson(Set<MetroStation> set) {
        JSONObject map = new JSONObject();
        map.put("lines", getLinesFromSet(set));
        map.put("stations", getStationMap(set));
        map.put("connections", getConnectionsFromSet(set));
        return map;
    }

    private static JSONArray getConnectionsFromSet(Set<MetroStation> set) {
        JSONArray connections = new JSONArray();
        ArrayList<Map<String, String>> entry;

        for (MetroStation ms : set
        ) {
            //Проверим обходили ли мы эти станции и есть ли там соединения
            if (!ms.isChecked() && !ms.getConnection().isEmpty()) {
                entry = new ArrayList<>();
                Map<String, String> connection;
                //Добавляем свои линии
                for (String line : ms.getLines().keySet()
                ) {
                    connection = new HashMap<>(2, 1);
                    connection.put("line", line);
                    connection.put("station", ms.getName());
                    entry.add(connection);
                }

                ms.setChecked(true);

                //Добавляем соединенные станции
                for (Map.Entry<String, String> line : ms.getConnection().entrySet()
                ) {
                    connection = new HashMap<>(2, 1);
                    connection.put("line", line.getKey());
                    connection.put("station", line.getValue());
                    entry.add(connection);
                    for (MetroStation connectedMS : set
                    ) {
                        if (connectedMS.getName().equals(line.getValue())) {
                            connectedMS.setChecked(true);
                        }
                    }
                }
                connections.add(entry);
            }
        }

        return connections;
    }

    //Создает список стаций по веткам
    private static TreeMap<String, ArrayList<String>> getStationMap(Set<MetroStation> set) {
        TreeMap<String, ArrayList<String>> map = new TreeMap<>(MetroMapper::compareLineName);

        for (MetroStation ms : set
        ) {
            for (String line : ms.getLines().keySet()
            ) {
                //Проверяем была ли такая ветка
                if (!map.containsKey(line)) {
                    //Если нет, создаем и добавляем туда стацию
                    ArrayList<String> stationsInLine = new ArrayList<>();
                    stationsInLine.add(ms.getName());
                    map.put(line, stationsInLine);
                } else {
                    //Если была, просто добавим стацию
                    map.get(line).add(ms.getName());
                }
            }
        }

        return map;
    }

    //Создает список линий метро с именем и цветом
    private static JSONArray getLinesFromSet(Set<MetroStation> set) {
        Set<Map<String, String>> s = new TreeSet<>((o1, o2) -> compareLineName(o1.get("number"), o2.get("number")));
        JSONArray lines = new JSONArray();
        set.stream().flatMap(metroStation -> metroStation.getLines().entrySet().stream())
                .forEach(fields -> {
                    Map<String, String> entry = new HashMap<>();
                    entry.put("number", fields.getKey());
                    entry.put("name", fields.getValue());
                    entry.put("color", LineColor.getColorByLine(Integer.parseInt(fields.getKey()
                            .replaceAll("\\D+", ""))));
                    s.add(entry);
                });

        lines.addAll(s);

        return lines;
    }

    private static int compareLineName(String s1, String s2) {
        //Если есть буквы в номере
        if (s1.matches(".+\\D") || s2.matches(".+\\D")) {
            //Сравним номера веток метро без букв
            String base1 = s1.replaceAll("\\D+", "");
            String base2 = s2.replaceAll("\\D+", "");

            int preComp = Integer.compare(Integer.parseInt(base1), Integer.parseInt(base2));
            //Если одинаковые, сравним длину номера
            if (preComp == 0) {
                return Integer.compare(s1.length(), s2.length());
            } else {
                return preComp;
            }
        } else {
            return Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2));
        }
    }

    private static Map<String, String> getLinesInCell(Element el) throws NullPointerException {

        Map<String, String> lines = new HashMap<>();

        Elements cellContent = el.select("span");

        if (cellContent.size() >= 2) {
            //Получаем цифры из ячейки (номера линий + номер станции в подгруппе)
            String[] lineNumbers = cellContent.select(".sortkey").stream()
                    .map(Element::text)
                    .map(MetroMapper::trimLeadingZero)
                    .toArray(String[]::new);
            //Получаем список имен линий
            String[] lineNames = cellContent.select("[title]:has(img)")
                    .stream().map(element -> element.attr("title"))
                    .toArray(String[]::new);
            //Поскольку список имен стации двойной, берем каждый второй
            for (int i = 0; i < lineNumbers.length - 1; i++) {
                lines.put(lineNumbers[i], lineNames[i * 2]);
            }
        }

        return lines;
    }

    //Создаем перечень пересечений
    private static HashMap<String, String> getConnectionsInCell(Element el) throws NullPointerException {
        HashMap<String, String> connections = new HashMap<>();
        Elements cellContent = el.select("span");

        if (cellContent.size() >= 2) {
            //Получаем линии, которые пересекаются на этой станции
            String[] connectedLines = cellContent.select(".sortkey").stream()
                    .map(Element::text)
                    .map(MetroMapper::trimLeadingZero)
                    .toArray(String[]::new);
            //Имена станций, которые соединяются
            String[] connectedStations = cellContent.select("[title]:not([href])")
                    .stream().map(element -> getConnectedStationName(element.attr("title")))
                    .toArray(String[]::new);
            for (int i = 0; i < connectedLines.length; i++) {
                connections.put(connectedLines[i], connectedStations[i]);
            }
        }
        return connections;
    }

    private static String trimLeadingZero(String name) {
        return name.replaceAll("^0+", "");
    }

    //Вырезает имя станции из предложения
    private static String getConnectedStationName(String text) {
        char[] chars = text.toCharArray();
        ArrayList<Integer> upperLettersIndex = new ArrayList<>();
        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i]) && chars[i - 1] == ' ') {
                upperLettersIndex.add(i);
            }
        }
        return text.substring(upperLettersIndex.get(0), upperLettersIndex.get(upperLettersIndex.size() - 1)).trim();
    }

    static private void jsonToFile(String inpath, JSONObject json) {
        try {
            Path path = Path.of(inpath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(Path.of(inpath), gson.toJson(json), StandardOpenOption.APPEND);

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


    private static void printAnalyzeResult(String outputPath) {
        try {

            getLinesData(outputPath).forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static ArrayList<String> getLinesData(String outPath) throws ParseException {
        JSONParser parser = new JSONParser();

        ContainerFactory containerFactory = new ContainerFactory() {
            @Override
            public Map createObjectContainer() {
                return new LinkedHashMap<>();
            }

            @Override
            public List creatArrayContainer() {
                return new LinkedList<>();
            }
        };

        Map jsonData = (Map) parser.parse(getJsonFile(outPath), containerFactory);
        ArrayList<String> lineInformation = new ArrayList<>();
        List linesArray = (List) jsonData.get("lines");
        Map stationObject = (Map) jsonData.get("stations");

        for (Object obj : stationObject.keySet()
        ) {
            StringBuilder out = new StringBuilder();
            String lineNumber = (String) obj;

            int stationNum = ((List) stationObject.get(obj)).size();
            try {
                String lineName = getLineName(linesArray, lineNumber);
                out.append("\nЛиния - ").append(lineName);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            out.append("\n\tНомер - ").append(lineNumber);
            out.append("\n\tКоличество станций на линии - ").append(stationNum);
            lineInformation.add(out.toString());
        }
        return lineInformation;
    }

    private static String getLineName(List linesArray, String lineNumber) throws IllegalArgumentException {
        String output = "";
        for (Object obj : linesArray
        ) {
            Map lineObject = (Map) obj;
            if (lineObject.get("number").equals(lineNumber)) {
                return (String) lineObject.get("name");
            }
        }
        throw new IllegalArgumentException(lineNumber + "not found;");
    }


    private static String getJsonFile(String outputPath) {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(outputPath));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

}
