import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.MetroIndex;
import service.parsers.MetroIndexAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MetroMapper {
    private final static String WIKI_PAGE = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    //Столбец с номером линии
    private final static int LINE_POZ = 0;
    //Столбец с названием станции
    private final static int NAME_POZ = 1;
    //Столбец с номером линии для пересадки
    private final static int CONNECTION_POZ = 3;
    //Путь к выходному файлу
    private final static String OUTPUT_PATH = "target/newMoscowMetroMap.json";

    public static void main(String[] args) {

        try {
            //Создаем карту метро
            MetroIndex index = fillMetroIndex(parseWikiPage());

            jsonToFile(index);
            //Выводим полученный результат
            printAnalyzeResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MetroIndex fillMetroIndex(Elements htmlContent) {
        if (htmlContent == null) {
            throw new IllegalArgumentException("Не удалось обработать страницу Википедии");
        }
        MetroIndex index = new MetroIndex();
        htmlContent.forEach(el -> {
            //Выбираем строчку
            Elements row = el.select("td");
            //Имя станции
            String name = row.get(NAME_POZ).select("a").first().text().trim();

            //Список линий,куда она входит
            Map<String, String> lines = getLinesInCell(row.get(LINE_POZ));
            index.addStation(lines, name);
            //Список пересадок с этой станции
            Map<String, String> connections = getConnectionsInCell(row.get(CONNECTION_POZ));
            if (connections.size() > 0) {
                lines.forEach((lineNumber, lineName) -> connections.put(lineNumber, name));
                index.addConnection(connections);
            }

        });
        return index;
    }


    private static Elements parseWikiPage() {
        Document content = null;
        try {
            content = Jsoup.connect(WIKI_PAGE).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content != null ? content.select("table.standard.sortable tr:has(td)") : null;
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
        String midName = text.substring(upperLettersIndex.get(0), upperLettersIndex.get(upperLettersIndex.size() - 1)).trim();

        return midName.replaceAll("\\(.+\\)", "")
                .trim();
    }

    static private void jsonToFile(MetroIndex index) {
        try {
            Path path = Path.of(MetroMapper.OUTPUT_PATH);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(MetroIndex.class, new MetroIndexAdapter())
                    .create();
            Files.writeString(Path.of(MetroMapper.OUTPUT_PATH), gson.toJson(index), StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void printAnalyzeResult() throws IOException {

        getLinesData().forEach(System.out::println);

    }


    private static ArrayList<String> getLinesData() throws IOException {
        ArrayList<String> lineInformation = new ArrayList<>();
        String json = getJsonFile();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MetroIndex.class, new MetroIndexAdapter())
                .create();

        MetroIndex index = gson.fromJson(json, MetroIndex.class);

        index.getLines().forEach((s, metroLine) -> {
            String out = "\nЛиния - " + metroLine.getName() +
                    "\n\tНомер - " + s +
                    "\n\tКоличество станций на линии - " + metroLine.getStations().size();
            lineInformation.add(out);
        });

        return lineInformation;
    }


    private static String getJsonFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get(MetroMapper.OUTPUT_PATH));
        lines.forEach(builder::append);

        return builder.toString();
    }

}
