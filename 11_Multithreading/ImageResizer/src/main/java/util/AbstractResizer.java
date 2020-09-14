package util;

import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public abstract class AbstractResizer implements Runnable {
    File[] input;
    String dstFolder;
    int newWidth;

    void resize(File[] input, String dstFolder, int newWidth) {

    }

    public void run() {

    }
}
