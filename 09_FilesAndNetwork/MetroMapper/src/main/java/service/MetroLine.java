package service;

import lombok.Data;

import java.util.LinkedHashSet;

@Data
public class MetroLine {

    private final String name;
    private final String color;

    private LinkedHashSet<String> stations = new LinkedHashSet<>();

//    @Override
//    public int compareTo(MetroLine o) {
//
//    }
//    public boolean equals(MetroLine o){
//        return this.number.equals(o.number);
//    }
}