import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;


public class Main {

    public static void main(String[] args) {

        LocalDateTime testTime = LocalDateTime.now();

        Airport.getInstance().getTerminals().stream().flatMap(terminal -> terminal.getFlights().stream())
                .filter(flight -> flight.getType() == Flight.Type.DEPARTURE
                        && testTime.isBefore(LocalDateTime.ofInstant(flight.getDate().toInstant(), ZoneId.systemDefault()))
                        && testTime.plusHours(2).isAfter(LocalDateTime.ofInstant(flight.getDate().toInstant(), ZoneId.systemDefault())))
                .sorted(Comparator.comparing(Flight::getDate))
                .forEach(flight -> {
                    System.out.println("Aircraft type: " + flight.getAircraft().getModel());
                    System.out.println("Departure time: " + new SimpleDateFormat("dd/MM/YYYY HH:mm").format(flight.getDate()));
                });
    }
}