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
import java.nio.file.Paths;
import java.util.Scanner;


public class Saver {
    final static String SITE = "http://lenta.ru/";
    final static String BASE_NAME = "LentaPic_";

    public static void main(String[] args) {
        System.out.println("Введите папку для сохранения изображения:");
        System.out.print(">> ");
        String path = new Scanner(System.in).nextLine();

        try {
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

    public static void copyLink(String dest, String baseName, String src) {
        try {
            if (src.contains("http")) {
                if (!dest.endsWith("/")) {
                    dest += "/";
                }
                URL path = new URL(src);
                String ending = src.substring(src.lastIndexOf('.'));
                ReadableByteChannel readableByteChannel = Channels.newChannel(path.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(dest + baseName + ending);
                fileOutputStream.getChannel()
                        .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
