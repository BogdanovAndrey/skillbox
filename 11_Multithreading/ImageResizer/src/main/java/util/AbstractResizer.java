package util;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Data
public abstract class AbstractResizer implements Runnable {
    File[] input;
    String dstFolder;
    int newWidth;



    AbstractResizer(File[] input, String dstFolder, int newWidth) {
        this.input = input;
        this.dstFolder = dstFolder;
        this.newWidth = newWidth;
    }

    abstract BufferedImage resizeMethod(BufferedImage image, int height);

    public void run() {
        try {
            long start = System.currentTimeMillis();
            for (File file : input) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );

                BufferedImage newImage = resizeMethod(image, newHeight);

                image.flush();

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);

            }
            System.out.println("Duration: " + (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
