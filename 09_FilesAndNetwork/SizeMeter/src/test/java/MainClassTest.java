import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class MainClassTest {
    private final String root = "testdata/";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void printResultTest() {
        Main.printResult(new File(root + "3b-file.txt"));
        assertTrue(outContent.toString().contains("3 байт"));
        outContent.reset();
        Main.printResult(new File(root + "2kb-file.txt"));
        assertTrue(outContent.toString().contains("2 кбайт"));
        outContent.reset();
        Main.printResult(new File(root + "1mb-file.txt"));
        assertTrue(outContent.toString().contains("1 Мбайт"));
        outContent.reset();
        Main.printResult(new File(root));
        assertTrue(outContent.toString().contains("3 Мбайт"));
    }
}
