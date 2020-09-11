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
        roughResize(availableCoreNum,files,roughDstFolder,newWidth);
        fineResize(availableCoreNum,files,fineDstFolder,newWidth);


    }
    private static void roughResize(int availableCoreNum, File[] files, String dstFolder, int newWidth){
        ArrayList<Thread> threads = new ArrayList<>(availableCoreNum);
        int splitter = files.length/availableCoreNum;
        for (int i = 0; i < availableCoreNum; i++){
            int start = i * (splitter + 1);
            int end = (i+1) < availableCoreNum ? splitter * (i+1) : files.length;
            File[] newFiles = Arrays.copyOfRange(files,start,end);
            threads.add(new Thread(new RoughImageResizer(newFiles, dstFolder, newWidth)));
        }
        System.out.printf("Создано потоков: %d.\n",threads.size());
        threads.forEach(Thread::start);

    }
    private static void fineResize (int availableCoreNum, File[] files, String dstFolder, int newWidth){
        ArrayList<Thread> threads = new ArrayList<>(availableCoreNum);
        int splitter = files.length/availableCoreNum;
        for (int i = 0; i < availableCoreNum; i++){
            int start = i * (splitter + 1);
            int end = (i+1) < availableCoreNum ? splitter * (i+1) : files.length;
            File[] newFiles = Arrays.copyOfRange(files,start,end);
            threads.add(new Thread(new FineImageResizer(newFiles, dstFolder, newWidth)));
        }
        System.out.printf("Создано потоков: %d.\n",threads.size());
        threads.forEach(Thread::start);

    }


}
