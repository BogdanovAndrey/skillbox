import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.LineColor;
import service.MetroStation;

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


    public static void main(String[] args) {
        try {
            Set<MetroStation> rawLineTable = fillStationSet(parseWikiPage());
            rawLineTable.forEach(metroStation -> System.out.println(metroStation.toString() + "\n"));
            JSONObject map = tableToJson(rawLineTable);
            jsonToFile("target/MoscowMetroMap.json", map);
            //RouteCalculator rc = TestClass.getRouteCalculator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Elements parseWikiPage() throws IOException {
        Document content = Jsoup.connect(WIKI_PAGE).get();
        Elements table = content.select("table.standard.sortable:contains(Список может) tr:has(td)");
        htmlToFile(table);
        return table;
    }

    private static Set<MetroStation> fillStationSet(Elements htmlContent) {
        return htmlContent.parallelStream()
                .map(el -> {
                    Elements row = el.select("td");
                    String name = row.get(NAME_POZ).select("a").first().text().trim();
                    Map<String, String> lines = getLinesInCell(row.get(LINE_POZ));
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
            if (!ms.isChecked() && !ms.getConnection().isEmpty()) {
                entry = new ArrayList<>();
                Map<String, String> connection;
                for (String line : ms.getLines().keySet()
                ) {
                    connection = new HashMap<>(2, 1);
                    connection.put("line", line);
                    connection.put("station", ms.getName());
                    entry.add(connection);
                }
                ms.setChecked(true);
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

    private static int compareConnection(ArrayList<Map<String, String>> arr1, ArrayList<Map<String, String>> arr2) {
        if (arr1.size() != arr2.size()) {
            return Integer.compare(arr1.size(), arr2.size());
        } else {
            boolean equal = true;
            for (Map<String, String> map1 : arr1
            ) {
                equal &= arr2.stream().anyMatch(map2 -> map2.equals(map1));
            }
            if (equal) {
                return 0;
            } else {
                return arr1.toString().compareTo(arr2.toString());
            }
        }
    }

    private static TreeMap<String, ArrayList<String>> getStationMap(Set<MetroStation> set) {
        TreeMap<String, ArrayList<String>> map = new TreeMap<>(MetroMapper::compareLineName);

        for (MetroStation ms : set
        ) {
            for (String line : ms.getLines().keySet()
            ) {
                if (!map.containsKey(line)) {
                    ArrayList<String> stationsInLine = new ArrayList<>();
                    stationsInLine.add(ms.getName());
                    map.put(line, stationsInLine);
                } else {
                    map.get(line).add(ms.getName());
                }
            }
        }

        return map;
    }

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
        //TODO: add filter to stream and change return type to ArrayList
        lines.addAll(s);

        return lines;
    }

    private static int compareLineName(String s1, String s2) {
        if (s1.matches(".+\\D") || s2.matches(".+\\D")) {
            String base1 = s1.replaceAll("\\D+", "");
            String base2 = s2.replaceAll("\\D+", "");
            int preComp = Integer.compare(Integer.parseInt(base1), Integer.parseInt(base2));
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
            String[] lineNumbers = cellContent.select(".sortkey").stream()
                    //.limit(cellContent.size() - 1)
                    .map(Element::text)
                    .map(MetroMapper::trimLeadingZero)
                    .toArray(String[]::new);
            String[] lineNames = cellContent.select("[title]:has(img)")
                    .stream().map(element -> element.attr("title"))
                    .toArray(String[]::new);
            for (int i = 0; i < lineNumbers.length - 1; i++) {
                lines.put(lineNumbers[i], lineNames[i * 2]);
            }
        }

        return lines;
    }

    private static HashMap<String, String> getConnectionsInCell(Element el) throws NullPointerException {
        HashMap<String, String> connections = new HashMap<>();
        Elements cellContent = el.select("span");

        if (cellContent.size() >= 2) {
            String[] connectedLines = cellContent.select(".sortkey").stream()
                    .map(Element::text)
                    .map(MetroMapper::trimLeadingZero)
                    .toArray(String[]::new);
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
