package service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MetroStation {
    private String name;
    private Map<String, String> lines;
    private Map<String, String> connection;
    private boolean isChecked;

}

