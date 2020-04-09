import java.io.IOException;
import java.nio.file.*;

import java.util.EnumSet;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        try {
            /*
            System.out.println("Введите путь папки или файла, который нужно скопировать:");
            System.out.print(">> ");
            String from = input.nextLine();
            System.out.println("Введите путь папки или файла, который нужно скопировать:");
            System.out.print(">> ");
            String to = input.nextLine();
            */
            Path from = Paths.get("inputdata/test.txt");
            Path to = Paths.get("outputdata");

            // check if target is a directory
            boolean isDir = Files.isDirectory(to);

            Path dest = (isDir) ? to.resolve(from.getFileName()) : to;

            // follow links when copying files
            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            TreeCopier tc = new TreeCopier(from, dest);
            Files.walkFileTree(from, opts, Integer.MAX_VALUE, tc);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
