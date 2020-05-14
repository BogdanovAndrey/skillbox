package service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class LineColor {
    final static ArrayList<String> lineColors = new ArrayList<>();
    final static String COLOR_LIST = "https://www.htmlcsscolor.com/hex/";

    static {
        lineColors.add("Красная");
        lineColors.add("Зеленая");
        lineColors.add("Синяя");
        lineColors.add("Голубая");
        lineColors.add("Коричневая");
        lineColors.add("Оранжевая");
        lineColors.add("Фиолетовая");
        lineColors.add("Желтая");
        lineColors.add("Серая");
        lineColors.add("Салатовая");
        lineColors.add("Бирюзовая");
        lineColors.add("Серо-голубая");
        lineColors.add("Голубая-синяя");
        lineColors.add("Красно-белая");
        lineColors.add("Розовая");
        lineColors.add("Физалис");
        lineColors.add("Фуксия");
    }

    public static String getColorByLine(String number) {
        int line = Integer.parseInt(number.replaceAll("\\D+", ""));
        return getColorByLine(line);
    }

    public static String getColorByLine(int line) {
        if (line > 0 && line < lineColors.size()) {
            return lineColors.get(line - 1);
        } else {
            throw new IllegalArgumentException("Wrong line number: " + line);
        }
    }


    public static String getLineColorFromHex(int hex) {
        String color = null;
        try {
            Document siteContent = Jsoup.connect(COLOR_LIST + hex).get();
            color = siteContent.select("div.page-header small").text();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return color;

    }
}
