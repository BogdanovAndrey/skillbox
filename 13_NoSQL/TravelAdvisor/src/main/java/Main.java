import Model.Advisor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Advisor adv = new Advisor("Москва");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
