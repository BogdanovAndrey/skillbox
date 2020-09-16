import org.imgscalr.Scalr;
import util.FolderType;
import util.ImageResizer;
import util.ResizeMethod;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String srcFolder = "resources/input";
        String roughDstFolder = "resources/output/rough";
        String fineDstFolder = "resources/output/fine";

        final int AVAILABLE_CORE_NUM = Runtime.getRuntime().availableProcessors();
        final String inputMsg = "Введите исходную папку:";
        final String outputMsg = "Введите конечную папку:";
        final String fine = "F";
        final String rough = "R";
        final String typeMsg = "Выберете тип операции:\n\t" + rough + "- грубое масштабирование;" +
                "\n\t" + fine + "- масштабирование, с сохранением качества";
        final String widthMsg = "Введите новое значение ширины картинки";
        Scanner input = new Scanner(System.in);


        try {
            File srcDir = new File(validateFolder(input, inputMsg, FolderType.INPUT));

            File[] files = srcDir.listFiles();

            String dstFolder = validateFolder(input, outputMsg, FolderType.OUTPUT);

            int newWidth = Integer.parseInt(getInputString(input, widthMsg));

            String operationType = getInputString(input, typeMsg);


            switch (operationType) {
                case "F": {
                    ResizeMethod method = (image, height, width) -> Scalr.resize(
                            image,
                            Scalr.Method.ULTRA_QUALITY,
                            width,
                            height);
                    resizeFiles(files, dstFolder, newWidth, AVAILABLE_CORE_NUM, method);
                    break;
                }
                case "R": {
                    ResizeMethod method = (image, height, width) -> {
                        BufferedImage newImage = new BufferedImage(
                                width, height, BufferedImage.TYPE_INT_RGB
                        );

                        int widthStep = image.getWidth() / width;
                        int heightStep = image.getHeight() / height;

                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                int rgb = image.getRGB(x * widthStep, y * heightStep);
                                newImage.setRGB(x, y, rgb);
                            }
                        }
                        return newImage;
                    };
                    resizeFiles(files, dstFolder, newWidth, AVAILABLE_CORE_NUM, method);
                    break;
                }
                default:{
                    throw new IllegalArgumentException("Неверно введен тип операции");
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void resizeFiles(File[] files, String dstFolder, int newWidth, int availableCoreNum, ResizeMethod exp) throws NullPointerException {
        ArrayList<File[]> splittedFiles = splitFiles(availableCoreNum, files);
        splittedFiles.forEach(filesPart -> new Thread(new ImageResizer(filesPart, dstFolder, newWidth, exp)).start());
        System.out.printf("Создано потоков: %d.\n", splittedFiles.size());
    }

    private static ArrayList<File[]> splitFiles(int availableCoreNum, File[] files) {
        ArrayList<File[]> splittedFiles = new ArrayList<>(availableCoreNum);
        int splitter = files.length / availableCoreNum;
        for (int i = 0; i < availableCoreNum; i++) {
            int start = i * (splitter + 1);
            int end = (i + 1) < availableCoreNum ? splitter * (i + 1) : files.length;
            File[] newFiles = Arrays.copyOfRange(files, start, end);
            splittedFiles.add(newFiles);
        }
        return splittedFiles;
    }

    private static String getInputString(Scanner input, String msg) {
        System.out.println(msg);
        System.out.print(">>");
        return input.nextLine();
    }

    private static String validateFolder(Scanner input, String msg, FolderType type) throws IllegalArgumentException, IOException {
        Path folder = Path.of(getInputString(input, msg));
        if (!Files.exists(folder)) {
            if (type == FolderType.OUTPUT) {
                Files.createDirectory(folder);
                System.out.println("Папка создана");
            } else {
                throw new IllegalArgumentException("Папки не существует");
            }
        }
        if (!Files.isDirectory(folder)) {
            throw new IllegalArgumentException("Введеный путь не является папкой");
        }
        if (type == FolderType.INPUT) {
            if (!Files.isReadable(folder)) {
                throw new IllegalArgumentException("Невозможно прочесть содержимое");
            }
        } else {
            if (!Files.isWritable(folder)) {
                throw new IllegalArgumentException("Невозможно записать в папку");
            }
            if (Files.newDirectoryStream(folder).iterator().hasNext()){
                throw new IllegalArgumentException("Папку не пуста");
            }
        }
        return folder.toString();
    }
}

