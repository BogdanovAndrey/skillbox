import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.TreeSet;

public class Main {
    final static Path file = Path.of("src/main/resources/output.txt").toAbsolutePath();

    public static void main(String[] args) {
        try {
            Document page = Jsoup.connect("https://lenta.ru").get();
            Elements links = page.select("a[abs:href]");
            TreeSet<String> strLinks = new TreeSet<>();
            links.forEach(element -> {
                String link = element.attr("abs:href");
                if (link.contains("https://lenta.ru")) {
                    strLinks.add(link);
                }
            });


            Files.deleteIfExists(file);
            Files.createFile(file);
            strLinks.forEach(s -> {
                try {
                    Files.writeString(file, s + "\n", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
