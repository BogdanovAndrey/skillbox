import util.AbstractResizer;
import util.RoughImageResizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String srcFolder = "resources/input";
        String roughDstFolder = "resources/output/rough";
        String fineDstFolder = "resources/output/fine";
        final int newWidth = 300;

        int availableCoreNum = Runtime.getRuntime().availableProcessors();


        File srcDir = new File(srcFolder);

        File[] files = srcDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Пустая папка!");
            System.exit(0);
        }
        try {
            roughResize(availableCoreNum, files, roughDstFolder, newWidth);
            fineResize(availableCoreNum, files, fineDstFolder, newWidth, );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void roughResize(int availableCoreNum, File[] files, String dstFolder, int newWidth) throws NullPointerException {
        ArrayList<Thread> threads = new ArrayList<>(availableCoreNum);
        int splitter = files.length / availableCoreNum;
        for (int i = 0; i < availableCoreNum; i++) {
            int start = i * (splitter + 1);
            int end = (i + 1) < availableCoreNum ? splitter * (i + 1) : files.length;
            File[] newFiles = Arrays.copyOfRange(files, start, end);
            threads.add(new Thread(new RoughImageResizer(newFiles, dstFolder, newWidth)));
        }
        System.out.printf("Создано потоков: %d.\n", threads.size());
        threads.forEach(Thread::start);

    }

    private static void fineResize(int availableCoreNum, File[] files, String dstFolder, int newWidth, AbstractResizer method) throws NullPointerException {
        ArrayList<Thread> threads = new ArrayList<>(availableCoreNum);
        ArrayList<File[]> splittedFiles = splitFiles(availableCoreNum, files);

        for (File[] splittedFile : splittedFiles) {

            // threads.add(new Thread(new method(splittedFile, dstFolder, newWidth) {
            // }));
        }
        System.out.printf("Создано потоков: %d.\n", threads.size());
        threads.forEach(Thread::start);
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
}


}
