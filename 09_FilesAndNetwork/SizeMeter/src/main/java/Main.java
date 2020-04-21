import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
                String checkPath = input.nextLine();
                /*File fl = new File(checkPath);
                if (fl.exists()) {
                    System.out.println("~идет подсчет~");
                    printResult(fl);
                } else {
                    throw new FileNotFoundException("Путь введен неверно.");
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }*/
                System.out.println(sizeConverter(Files.walk(Path.of(checkPath))
                        .filter(Files::isRegularFile)
                        .mapToLong(path -> path.toFile().length())
                        //.mapToLong(Main::trySize)
                        .sum()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static long trySize(Path path) {
        try {
            return Files.size(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void printResult(@NotNull File fl) {
        StringBuilder output = new StringBuilder("Размер ");

        if (fl.isDirectory()) {
            output.append("папки ");
        } else {
            output.append("файла ");
        }
        System.out.println(output.append(sizeConverter(countSize(fl))));

    }

    private static String sizeConverter(long size) {

        if (size < 1024) return size + " Б";

        int exp = (int) (Math.log(size) / (Math.log(1024)));

        char unitsPrefix = "КМГТПЭ".charAt(exp - 1);

        return String.format("%.2f %sБ", size / Math.pow(1024, exp), unitsPrefix);

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
