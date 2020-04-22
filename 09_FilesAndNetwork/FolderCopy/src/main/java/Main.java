import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        try {

            System.out.println("Введите путь папки или файла, который нужно скопировать:");
            System.out.print(">> ");
            Path from = Path.of(input.nextLine());
            System.out.println("Введите путь папки или файла, который нужно скопировать:");
            System.out.print(">> ");
            Path to = Path.of(input.nextLine());

            //Проверим, что на входе. Если папка, то добавим ее имя к целевому пути
            Path dest = (Files.isDirectory(to)) ? to.resolve(from.getFileName()) : to;
            //Экземпляр класса-копира
            TreeCopier treeCopier = new TreeCopier(from, dest);

            Files.walkFileTree(from, treeCopier);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}