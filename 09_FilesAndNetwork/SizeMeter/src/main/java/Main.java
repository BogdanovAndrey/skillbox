import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        for (; ; ) {
            try {
                System.out.println("Введите путь для подсчета размера папки или файла:");
                System.out.print(">> ");
                File fl = new File(input.nextLine());
                if (fl.exists()) {
                    System.out.println("~идет подсчет~");
                    printResult(fl);
                } else {
                    throw new FileNotFoundException("Путь введен неверно.");
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void printResult(@NotNull File fl) {
        StringBuilder output = new StringBuilder("Размер ");

        if (fl.isDirectory()) {
            output.append("папки ");
        } else {
            output.append("файла ");
        }
        long result = countSize(fl);
        if (result < 1000) {
            output.append(result).append(" байт");
        } else if (result < 1000000) {
            output.append(result / 1000).append(" кбайт");
        } else if (result < 1000000000) {
            output.append(result / 1000000).append(" Мбайт");
        } else if (result < 1000000000000L) {
            output.append(result / 1000000000).append(" Гбайт");
        }
        System.out.println(output.toString());

    }

    private static long countSize(File fl) {
        Optional<Long> totalSize;

        if (fl.isDirectory()) {
            File[] folder = fl.listFiles();
            if (folder == null) {
                return 0;
            } else {
                totalSize = Arrays.stream(folder).map(Main::countSize).reduce((total, fileSize) -> total += fileSize);
            }
        } else {
            return fl.length();
        }

        return totalSize.orElse((long) 0);
    }
}
