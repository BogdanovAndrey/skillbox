package util;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Data
@AllArgsConstructor
public class RoughImageResizer extends AbstractResizer {


    public RoughImageResizer(File[] splittedFile, String dstFolder, int newWidth) {
        super(splittedFile, dstFolder, newWidth);
    }

    void resize(File[] input, String dstFolder, int newWidth) {
        try {
            long start = System.currentTimeMillis();
            for (File file : input) {
                BufferedImage image;
                //synchronized (file) {
                image = ImageIO.read(file);
                //}

                if (image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = new BufferedImage(
                        newWidth, newHeight, BufferedImage.TYPE_INT_RGB
                );

                int widthStep = image.getWidth() / newWidth;
                int heightStep = image.getHeight() / newHeight;

                for (int x = 0; x < newWidth; x++) {
                    for (int y = 0; y < newHeight; y++) {
                        int rgb = image.getRGB(x * widthStep, y * heightStep);
                        newImage.setRGB(x, y, rgb);
                    }
                }

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);

            }
            System.out.println("Duration: " + (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        resize(input, dstFolder, newWidth);
    }
}
