package util;

import java.awt.image.BufferedImage;

public interface ResizeMethod {
    BufferedImage resizeMethod(BufferedImage image, int height, int width);
}
