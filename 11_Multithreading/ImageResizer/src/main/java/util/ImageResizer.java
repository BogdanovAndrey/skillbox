package util;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Data
public class ImageResizer implements Runnable {
    File[] input;
    String dstFolder;
    int newWidth;
    ResizeMethod expression;


    public ImageResizer(File[] input, String dstFolder, int newWidth, ResizeMethod expression) {
        this.input = input;
        this.dstFolder = dstFolder;
        this.newWidth = newWidth;
        this.expression = expression;
    }


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

                BufferedImage newImage = expression.resizeMethod(image, newHeight, newWidth);

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
