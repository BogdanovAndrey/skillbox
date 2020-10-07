package util;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class TreeMapper extends RecursiveTask<SortedSet<String>> {
    SortedSet<String> links;
    final String url;

    public TreeMapper(String url, SortedSet<String> links) {
        this.url = url;
        this.links = links;
    }

    @SneakyThrows
    @Override
    protected SortedSet<String> compute() {
        Thread.sleep(150);
        Document page = Jsoup.connect(url).get();
        Elements rawLinks = page.select("a[abs:href]");

        boolean newStringsAdded = false;
        for(Element el: rawLinks){
            String link = el.attr("abs:href");
            if (link.contains("https://lenta.ru")) {
                newStringsAdded = links.add(link);
            }
        }
            if (!newStringsAdded){
                return null;
            }
        List<TreeMapper> subTasks = new LinkedList<>();

        for (String link : links) {
            TreeMapper task = new TreeMapper(link, links);
            task.fork(); // запустим асинхронно
            subTasks.add(task);
        }

        for (TreeMapper task : subTasks) {
            links.addAll(task.join()); // дождёмся выполнения задачи и прибавим результат
        }
        return links;
    }
}
