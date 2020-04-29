import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Saver {
    final static String SITE = "http://lenta.ru/";
    final static String BASE_NAME = "LentaPic_";

    public static void main(String[] args) {
        System.out.println("Введите папку для сохранения изображения:");
        String path = null;
        boolean pathIsOk = false;
        try {
            while (!pathIsOk) {
                System.out.print(">> ");
                path = new Scanner(System.in).nextLine();
                pathIsOk = pathCheck(path);
                if (!pathIsOk) {
                    System.out.println("Указан не верный пусть или папка не пуста. Проверьте ввод");
                }
            }
            Document doc = Jsoup.connect(SITE).get();
            Elements img = doc.getElementsByTag("img");
            if (!Files.exists(Paths.get(path))) {
                Files.createDirectory(Paths.get(path));
            }

            for (int x = 0; x < img.size(); x++) {
                Element el = img.get(x);
                copyLink(path, BASE_NAME + x, el.attr("src"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyLink(String dest, String baseName, String src) {
        try {
            String ending;
            if (!src.contains("https:")) {
                src = "https:" + src;
                ending = ".gif";
            } else {
                ending = src.substring(src.lastIndexOf('.'));
            }
            if (!dest.endsWith("/")) {
                dest += "/";
            }
            URL path = new URL(src);
            ReadableByteChannel readableByteChannel = Channels.newChannel(path.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(dest + baseName + ending);
            fileOutputStream.getChannel()
                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean pathCheck(String path) throws IOException {
        return path != null &&
                !path.isEmpty() &&
                Files.isDirectory(Path.of(path)) &&
                Files.walk(Path.of(path)).count() == 1;
    }
}
