import util.FineImageResizer;
import util.FolderType;
import util.RoughImageResizer;

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

        final int newWidth = 300;
        final int AVAILABLE_CORE_NUM = Runtime.getRuntime().availableProcessors();
        final String inputMsg = "Введите исходную папку:";
        final String outputMsg = "Введите конечную папку:";
        final String typeMsg = "Выберете тип операции:\n\t R - грубое маштабирование;" +
                "\n\t F - масштабирование, с сохранением качества";

        Scanner input = new Scanner(System.in);

        try {
            File srcDir = new File(validateFolder(input,inputMsg, FolderType.INPUT));

            File[] files = srcDir.listFiles();

            String dstFolder = validateFolder(input, outputMsg, FolderType.OUTPUT);

            String operationType = getFolder(input, typeMsg);
            switch (operationType){
                case "F":{
                    roughResize(files, dstFolder, newWidth, AVAILABLE_CORE_NUM);
                    break;
                }
                case "R":{
                    fineResize(files, dstFolder, newWidth,AVAILABLE_CORE_NUM);
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



    private static void roughResize(File[] files, String dstFolder, int newWidth, int availableCoreNum) throws NullPointerException {
        ArrayList<File[]> splittedFiles = splitFiles(availableCoreNum, files);
        splittedFiles.forEach(filesPart -> new Thread(new RoughImageResizer(filesPart, dstFolder, newWidth)).start());
        System.out.printf("Создано потоков: %d.\n", splittedFiles.size());
    }

    private static void fineResize(File[] files, String dstFolder, int newWidth, int availableCoreNum) throws NullPointerException {
        ArrayList<File[]> splittedFiles = splitFiles(availableCoreNum, files);
        splittedFiles.forEach(filesPart -> new Thread(new FineImageResizer(filesPart, dstFolder, newWidth)).start());
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
    private static String getFolder (Scanner input, String msg) throws IOException {
        System.out.println(msg);
        System.out.print(">>");
        return input.nextLine();
    }

    private static String validateFolder(Scanner input, String msg, FolderType type) throws IllegalArgumentException, IOException {
        Path folder = Path.of(getFolder(input, msg));
        if (!Files.exists(folder)) {
            throw new IllegalArgumentException("Папки не существует");
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

