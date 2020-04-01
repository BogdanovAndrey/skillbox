import core.Line;
import core.Station;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class RouteCalculatorTest {

    StationIndex testIndex = new StationIndex();
    List<Station> routOneLine = new ArrayList<>();
    List<Station> routTwoLine = new ArrayList<>();
    List<Station> routThreeLine = new ArrayList<>();
    RouteCalculator testCalculator;

    @Before
    public void setUp() {
//Создаем тестовые линии
        Line line1 = new Line(1, "Красная");
        Line line2 = new Line(2, "Синяя");
        Line line3 = new Line(3, "Желтая");

//Станции для тестовых линий
        line1.addStation(new Station("L1_St1", line1));
        line1.addStation(new Station("L1_St2", line1));
        line1.addStation(new Station("L1_St3", line1));

        line2.addStation(new Station("L2_St1", line2));
        line2.addStation(new Station("L2_St2", line2));
        line2.addStation(new Station("L2_St3", line2));

        line3.addStation(new Station("L3_St1", line3));
        line3.addStation(new Station("L3_St2", line3));
        line3.addStation(new Station("L3_St3", line3));

//Наполняем тестовый индекс
        testIndex.addLine(line1);
        testIndex.addLine(line2);
        testIndex.addLine(line3);

        //первое соединение
        List<Station> connection = new ArrayList<>();
        connection.add(line1.getStations().get(line1.getStations().size() - 1));
        connection.add(line2.getStations().get(0));
        testIndex.addConnection(connection);

        //очистим предыдущее соединение
        connection.clear();

        //Второе соединение
        connection.add(line2.getStations().get(line2.getStations().size() - 1));
        connection.add(line3.getStations().get(0));
        testIndex.addConnection(connection);


        //Добавим станции в индекс
        for (int i = 1; i < 4; i++) {
            testIndex.getLine(i).getStations().forEach(station -> testIndex.addStation(station));
        }
        testCalculator = new RouteCalculator(testIndex);

        //Посчитаем маршруты
        routOneLine = testCalculator.getShortestRoute(
                testIndex.getStation("L1_St1"),
                testIndex.getStation("L1_St3"));

        routTwoLine = testCalculator.getShortestRoute(
                testIndex.getStation("L1_St1"),
                testIndex.getStation("L2_St3"));

        routThreeLine = testCalculator.getShortestRoute(
                testIndex.getStation("L1_St1"),
                testIndex.getStation("L3_St3"));
    }

    @After
    public void tearDown() {
        testIndex = null;
        routOneLine = null;
        routTwoLine = null;
        routThreeLine = null;
    }


    @Test
    public void testGetShortestRoute() {
        //Посчитаем маршрут в обратную сторону
        List<Station> reverseRoutOneLine = testCalculator.getShortestRoute(
                testIndex.getStation("L1_St3"),
                testIndex.getStation("L1_St1"));

        //Проверяем полученные маршруты
        List<Station> expectedRout = testIndex.getLine(1).getStations();
        Assert.assertEquals(expectedRout, routOneLine);

        expectedRout.sort(Comparator.reverseOrder());
        Assert.assertEquals(expectedRout, reverseRoutOneLine);

        expectedRout.sort(Comparator.naturalOrder());

        expectedRout.addAll(testIndex.getLine(2).getStations());
        Assert.assertEquals(expectedRout, routTwoLine);

        expectedRout.addAll(testIndex.getLine(3).getStations());
        Assert.assertEquals(expectedRout, routThreeLine);

    }

    @Test
    public void testCalculateDuration() {


        double testDuration = RouteCalculator.calculateDuration(routOneLine);
        double expectedDuration = 2.5 * (routOneLine.size() - 1);
        Assert.assertEquals(expectedDuration, testDuration, 0.0);

        testDuration = RouteCalculator.calculateDuration(routTwoLine);
        expectedDuration = 2.5 * (routTwoLine.size() - 2) + 3.5;
        Assert.assertEquals(expectedDuration, testDuration, 0.0);

        testDuration = RouteCalculator.calculateDuration(routThreeLine);
        expectedDuration = 2.5 * (routThreeLine.size() - 3) + 3.5 * 2;
        Assert.assertEquals(expectedDuration, testDuration, 0.0);

//Проверим пустой маршрут
        Assert.assertEquals(RouteCalculator.calculateDuration(new ArrayList<Station>()), 0.0, 0.0);

    }


}
