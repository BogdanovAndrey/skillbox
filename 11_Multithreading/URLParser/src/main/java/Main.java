import util.SiteMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    final static Path file = Path.of("src/main/resources/output.txt").toAbsolutePath();
    final static Path file2 = Path.of("src/main/resources/output2.txt").toAbsolutePath();
    final static String mainPage = "https://lenta.ru";
    public static void main(String[] args) {
        try {
            Set<String> siteMap = new TreeSet<String>();
            Set<String> siteMap2 = Collections.synchronizedSortedSet(new TreeSet<String>());
            new ForkJoinPool().invoke(new SiteMapper(mainPage, mainPage, siteMap));


            Files.deleteIfExists(file);
            Files.createFile(file);
            siteMap.forEach(s -> {
                try {
                    Files.writeString(file, s + "\n", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            new ForkJoinPool().invoke(new SiteMapper(mainPage, mainPage, siteMap2));
            Files.deleteIfExists(file2);
            Files.createFile(file2);
            siteMap.forEach(s -> {
                try {
                    Files.writeString(file2, s + "\n", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
