package Model.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class CityName {
    private String ruName;
    private String enName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        CityName cityName = (CityName) o;
        return Objects.equals(enName, cityName.enName) ||
                Objects.equals(ruName, cityName.ruName);
    }
}
