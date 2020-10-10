package util;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.RecursiveAction;


public class SiteMapper extends RecursiveAction {
    //Базовый адрес
    final String domain;
    //Общий список адресов
    final Set<String> links;
    //Текущий адрес
    final String url;


    public SiteMapper(String domain, String url, Set<String> links) {
        //Отрежем слэш для проверок
        this.domain = trimTail(domain);
        this.url = url;
        this.links = links;
    }

    @Override
    protected void compute() {
        //Список новых ссылок, чтобы избежать повторной проверки старых
        Set<String> newLinks = new TreeSet<>();

        try {
            Thread.sleep(150);
            Document page = Jsoup.connect(url).get();
            Elements rawLinks = page.select("a[abs:href]");

            boolean newStringsAdded = false;
            for (Element el : rawLinks) {
                //Получаем абсолютный адрес ссылки
                String link = el.attr("abs:href");
                //Если это базовый, пропускаем - добавим потом
                if (link.equals(domain)) {
                    continue;
                }
                //Добавим слэш для единообразия
                link = addTail(link);
                //Проверим содержит ли ссылка базовый адрес
                //и не обрабатывали ли мы ее ранее
                if (validLink(link)
                        && !links.contains(link)) {
                    //Новая ссылка нашлась
                    newStringsAdded = true;
                    //Запишем для дальнейшей обработки
                    newLinks.add(link);
                    System.out.println("Added " + link);
                    //Добавим в список с учетом иерархии
                    addLink(link, links);
                }
            }
            if (!newStringsAdded) {
                return;
            }
        } catch (HttpStatusException ignored) {
            //Пустные страницы игнорируем
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        //System.out.printf("Добавлено %d ссылок\n",newLinks.size());

        List<SiteMapper> subTasks = new LinkedList<>();
        //Запускаем обработку новых ссылок
        for (String link : newLinks) {
            SiteMapper task = new SiteMapper(domain, link, links);
            task.fork();
            subTasks.add(task);
        }
        //System.out.printf("\tЗапущено %d новых задач\n", subTasks.size());

        for (SiteMapper task : subTasks) {
            task.join();
        }
    }

    private boolean validLink(String link) {
        return (link.startsWith(domain)
                && !link.replaceFirst(domain, "").contains("."));
    }

    private void addLink(String link, Set<String> map) {
        //Сохраняем исходную ссылку
        map.add(link);
        //Отрежем последний слэш
        link = trimTail(link);

        //Будем добавлять подуровни ссылки до тех пор,
        // пока не дойдем до базового адреса
        while (!trimTail(link).equals(domain) || !link.equals(domain)) {
            link = link.substring(0, link.lastIndexOf("/"));
            map.add(link + "/");
        }

    }

    private String trimTail(String link) {
        if (link.endsWith("/")) {
            link = link.substring(0, link.lastIndexOf("/"));
        }
        return link;
    }

    private String addTail(String link) {
        if (!link.endsWith("/")) {
            link = link.concat("/");
        }
        return link;
    }
}
