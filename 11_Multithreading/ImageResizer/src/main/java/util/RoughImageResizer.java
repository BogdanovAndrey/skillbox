package util;

import java.awt.image.BufferedImage;
import java.io.File;


public class RoughImageResizer extends AbstractResizer {

    public RoughImageResizer(File[] splittedFile, String dstFolder, int newWidth){
        super(splittedFile, dstFolder, newWidth);
    }

    @Override
    BufferedImage resizeMethod(BufferedImage image, int height) {
        BufferedImage newImage = new BufferedImage(
                getNewWidth(), height, BufferedImage.TYPE_INT_RGB
        );

        int widthStep = image.getWidth() / getNewWidth();
        int heightStep = image.getHeight() / height;

        for (int x = 0; x < getNewWidth(); x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x * widthStep, y * heightStep);
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
}
