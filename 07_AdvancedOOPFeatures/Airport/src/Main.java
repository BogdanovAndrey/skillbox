import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;


public class Main {

    public static void main(String[] args) {
        Airport airport = Airport.getInstance();
        LocalDateTime testTime = LocalDateTime.now();

        airport.getTerminals().forEach(terminal -> terminal.getFlights().stream().
                filter(flight -> {
                    LocalDateTime flightTime = LocalDateTime.ofInstant(flight.getDate().toInstant(), ZoneId.systemDefault());
                    return flight.getType() == Flight.Type.DEPARTURE &&
                            flightTime.isAfter(testTime) && flightTime.isBefore(testTime.plusHours(2));
                }).sorted(Comparator.comparing(Flight::getDate)).forEach(flight -> {
            System.out.println("Aircraft type: " + flight.getAircraft().getModel());
            System.out.println("Departure time: " + flight.getDate());
        }));
    }
}
