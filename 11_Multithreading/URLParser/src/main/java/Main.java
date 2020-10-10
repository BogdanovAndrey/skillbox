import util.SiteMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

public class Main {
    final static Path file = Path.of("src/main/resources/output.txt").toAbsolutePath();
    final static String mainPage = "https://skillbox.ru/";
    public static void main(String[] args) {
        try {
            Set<String> siteMap = new TreeSet<String>();
            new ForkJoinPool().invoke(new SiteMapper(mainPage, mainPage, siteMap));

            printToFile(file, siteMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printToFile(Path file, Set<String> siteMap) throws IOException {
        Files.deleteIfExists(file);
        Files.createFile(file);
        siteMap.forEach(s -> {
            try {
                StringBuilder outStr = new StringBuilder();
                outStr.append("\t".repeat(Math.max(0, countLevel(s)))).append(s).append("\n");
                Files.writeString(file, outStr, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static int countLevel(String link) {
        char[] chars = link.toCharArray();
        int level = 0;
        for (char c : chars) {
            if (c == '/') {
                level++;
            }
        }
        //Отнимаем два слеша из https://
        level = level - 2;
        if (chars[chars.length - 1] == '/') {
            level--;
        }
        return level;
    }
}
