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
import java.util.concurrent.RecursiveAction;

public class SiteMapper extends RecursiveAction {
    final String domain;
    Set<String> links;
    final String url;

    public SiteMapper(String domain, String url, Set<String> links) {
        this.domain = domain;
        this.url = url;
        this.links = links;
    }


    @Override
    protected void compute() {

        try {
            Thread.sleep(150);
            Document page = Jsoup.connect(url).get();
            Elements rawLinks = page.select("a[abs:href]");

            boolean newStringsAdded = false;
            for (Element el : rawLinks) {
                String link = el.attr("abs:href");
                if (validLink(link)) {
                    newStringsAdded = addLink(link, links);
                }
            }
            if (!newStringsAdded) {
                return;
            }
        } catch (HttpStatusException ex) {
            System.out.println("Ошибка " + ex.getStatusCode() + " на странице " + ex.getUrl());
            links.remove(url);
            return;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }


        List<SiteMapper> subTasks = new LinkedList<>();

        for (String link : links) {
            SiteMapper task = new SiteMapper(domain, link, links);
            task.fork(); // запустим асинхронно
            subTasks.add(task);
        }

        for (SiteMapper task : subTasks) {
            task.join();
        }
    }

    private boolean validLink(String link) {
        return (link.startsWith(domain)
                && !link.contains(".pdf")
                && !link.contains(".xls"));
    }

    private boolean addLink(String link, Set<String> map) {
        boolean update = false;
        String tail = link.replaceFirst(domain, "");
        String[] subLinks = tail.split("/");
        StringBuilder newLink = new StringBuilder(domain);
        for (String s : subLinks) {
            if (!s.isEmpty()) {
                newLink.append(s);
            }
            newLink.append("/");
            update = map.add(newLink.toString());
        }
        return update;
    }
}
