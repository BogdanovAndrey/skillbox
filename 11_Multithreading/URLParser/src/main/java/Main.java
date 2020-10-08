import util.SiteMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    final static Path file = Path.of("src/main/resources/output.txt").toAbsolutePath();
    final static String mainPage = "https://lenta.ru";
    public static void main(String[] args) {
        try {
            SortedSet<String> siteMap = Collections.synchronizedSortedSet(new TreeSet<String>());

            new ForkJoinPool().invoke(new SiteMapper(mainPage, siteMap));


            Files.deleteIfExists(file);
            Files.createFile(file);
            siteMap.forEach(s -> {
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
