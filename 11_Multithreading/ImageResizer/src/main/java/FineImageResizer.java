import lombok.AllArgsConstructor;
import lombok.Data;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
@Data
@AllArgsConstructor
public class FineImageResizer implements Runnable{
    private File[] input;
    private String dstFolder;
    private int newWidth;

    void resize(File[] input, String dstFolder, int newWidth){
        try
        {
            long start = System.currentTimeMillis();
            for(File file : input)
            {
                BufferedImage image = ImageIO.read(file);
                if(image == null) {
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
