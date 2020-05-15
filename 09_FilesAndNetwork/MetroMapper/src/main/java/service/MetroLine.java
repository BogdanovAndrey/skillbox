package service;

import lombok.Data;

import java.util.LinkedHashSet;

@Data
public class MetroLine {

    private final String name;
    private final String color;

    private LinkedHashSet<String> stations = new LinkedHashSet<>();

}