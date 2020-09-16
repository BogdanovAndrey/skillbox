package util;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.File;

public class FineImageResizer extends AbstractResizer {

    public FineImageResizer(File[] splittedFile, String dstFolder, int newWidth){
        super(splittedFile, dstFolder, newWidth);
    }

    @Override
    BufferedImage resizeMethod(BufferedImage image, int height) {
        return Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                getNewWidth(),
                height);
    }

}