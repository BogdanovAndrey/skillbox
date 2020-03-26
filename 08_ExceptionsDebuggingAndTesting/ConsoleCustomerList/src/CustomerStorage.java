import java.util.EmptyStackException;
import java.util.HashMap;

public class CustomerStorage {
    private HashMap<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) {

        String[] components = data.split("\\s+");
        if (components.length != 4) {
            throw new IllegalArgumentException("Wrong format. Example:\n" +
                    "add Василий Петров vasily.petrov@gmail.com +79215637722");
        }
        String name = components[0] + " " + components[1];
        checkMail(components[2]);
        checkNumber(components[3]);
        storage.put(name, new Customer(name, components[3], components[2]));
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        if (storage.isEmpty()) {
            throw new EmptyStackException();
        }
        if (!storage.containsKey(name)) {
            throw new IllegalArgumentException("There is no such entry.");
        }
        storage.remove(name);
    }

    public int getCount() {
        return storage.size();
    }

    private void checkNumber(String num) {
        if (!num.matches("\\+7\\d{10}")) {
            throw new IllegalArgumentException("Wrong number entry.");
        }
    }

    private void checkMail(String mail) {
        if (!mail.matches("[\\w.]+@\\w+\\.[a-z]+")) {
            throw new IllegalArgumentException("Wrong email entry.");
        }
    }
}