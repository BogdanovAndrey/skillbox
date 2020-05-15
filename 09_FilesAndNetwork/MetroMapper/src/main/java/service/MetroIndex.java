package service;

import lombok.Data;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

@Data
public class MetroIndex {

    private TreeMap<String, MetroLine> lines;
    private HashSet<Map<String, String>> connections;

    public MetroIndex() {
        lines = new TreeMap<>(this::compareLineNumber);
        connections = new HashSet<>();
    }

    public void addStation(Map<String, String> inLines, String stationName) {
        inLines.forEach((lineNumber, lineName) -> {
            if (!this.lines.containsKey(lineNumber)) {
                this.lines.put(lineNumber, new MetroLine(lineName, LineColor.getColorByLine(lineNumber)));
            }
            addStationToLine(lineNumber, stationName);
        });
    }

    public void addLines(TreeMap<String, MetroLine> lines) {
        this.lines.putAll(lines);
    }

    public void addConnection(Map<String, String> connections) {
        this.connections.add(connections);
    }

    private void addStationToLine(String lineNumber, String stationName) {
        lines.get(lineNumber)
                .getStations()
                .add(stationName);
    }

    private int compareLineNumber(String s1, String s2) {
        //Если есть буквы в номере
        if (s1.matches(".+\\D") || s2.matches(".+\\D")) {
            //Сравним номера веток метро без букв
            String base1 = s1.replaceAll("\\D+", "");
            String base2 = s2.replaceAll("\\D+", "");

            int preComp = Integer.compare(Integer.parseInt(base1), Integer.parseInt(base2));
            //Если одинаковые, сравним длину номера
            if (preComp == 0) {
                return Integer.compare(s1.length(), s2.length());
            } else {
                return preComp;
            }
        } else {
            return Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2));
        }
    }


}
