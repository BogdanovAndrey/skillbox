package util;

import lombok.Data;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Data

public class FineImageResizer extends AbstractResizer {

    public FineImageResizer(File[] splittedFile, String dstFolder, int newWidth) {
        super(splittedFile, dstFolder, newWidth);
    }

    void resize(File[] input, String dstFolder, int newWidth) {
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

                BufferedImage newImage = Scalr.resize(
                        image,
                        Scalr.Method.ULTRA_QUALITY,
                        newWidth,
                        newHeight);

                image.flush();

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);

            }
            System.out.println("Duration: " + (System.currentTimeMillis() - start));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        resize(input, dstFolder, newWidth);
    }
}
