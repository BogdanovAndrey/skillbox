package task03;

import task03.clients.Client;
import task03.clients.EntityClient;
import task03.clients.EntrepreneurClient;
import task03.clients.IndividualClient;

import java.util.ArrayList;



public class ClientTest {
    final static double startMoneyAmount = 10000.0;
    public static void main(String[] args) {
        ArrayList<Client> bankClients = new ArrayList<>();
        bankClients.add(new IndividualClient(startMoneyAmount));
        bankClients.add(new EntityClient(startMoneyAmount));
        bankClients.add(new EntrepreneurClient(startMoneyAmount));

        for (Client client: bankClients
             ) {
            System.out.println(client.getClass());
            System.out.println("Money on bank account: " + client.getMoneyAmount());
            client.addMoney(100);
            System.out.println("Money on bank account after adding 100 RUB: " + client.getMoneyAmount());
            client.addMoney(2000);
            System.out.println("Money on bank account after adding 2000 RUB: " + client.getMoneyAmount());
            client.withdrawMoney(1000);
            System.out.println("Money on bank account after withdrawing 1000 RUB: " + client.getMoneyAmount() + "\n");


        }
    }
}
