/*
 * The MIT License
 *
 * Copyright 2015 Ahmad Zaky.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package visualcryptography;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import visualcryptography.util.ImageUtil;

/**
 *
 * @author azaky
 */
public class VC {

    private BufferedImage image;
    private String colorModel;

    public VC(BufferedImage image, String colorModel) {
        this.image = image;
        this.colorModel = colorModel;
    }

    /**
     * Generate shares from the secret image. Using (n, k) scheme.
     *
     * @param n The number of desired shares.
     * @param k The sufficient number of shares to recover the secret image.
     * @return The list of generated shares.
     */
    public List<BufferedImage> generateShares(int n, int k) throws UnsupportedOperationException {
        if (n != 2 || k != 2) {
            throw new UnsupportedOperationException("Currently, only (2, 2) scheme is supported");
        }

        // For convenience
        int width = image.getWidth();
        int height = image.getHeight();

        // Separate the image into three color channels. After this, each
        // channel may be processed separatedly as "grayscale" image.
        int[][][] originalChannels = new int[3][width][height];
        for (int i = 0; i < 3; ++i) {
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    originalChannels[i][x][y] = ImageUtil.getColor(image, x, y, i, colorModel);
                }
            }
        }

        // TODO: remove this part
        // save each channels into new images
        if (true) {
            for (int i = 0; i < 3; ++i) {
                BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        int color[] = new int[3];
                        color[i] = originalChannels[i][x][y];
                        ImageUtil.setColor(temp, x, y, color, "RGB");
                    }
                }
                try {
                    ImageIO.write(temp, "png", new File("test_channel_" + Integer.toString(i)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // Generate shares from each channels
        int[][][][] sharesChannels = new int[3][][][];
        for (int i = 0; i < 3; ++i) {
            sharesChannels[i] = generateSharesFromGrayscale(originalChannels[i], n, k);
        }

        // Combine the channels from each shares
        List<BufferedImage> shares = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            BufferedImage share = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    int color[] = new int[3];
                    for (int t = 0; t < 3; ++t) {
                        color[t] = sharesChannels[t][i][x][y];
                    }
                    ImageUtil.setColor(share, x, y, color, colorModel);
                }
            }
            shares.add(share);
        }

        return shares;
    }

    private int[][][] generateSharesFromGrayscale(int[][] originalChannel, int n, int k) {
        int width = originalChannel.length;
        int height = originalChannel[0].length;

        // TODO: halftone
        // 
        return null;
    }

    /**
     * Generate shares from the secret image, where each share looks like the
     * given image input. Using (n, k) scheme, where n is the number of image
     * input.
     *
     * @param input The input images that will be used for generating shares.
     * @param k The sufficient number of shares to recover the secret image.
     * @return The list of generated shares.
     */
    public List<BufferedImage> generateSharesFromImages(List<BufferedImage> input, int k) {

        return null;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return the colorModel
     */
    public String getColorModel() {
        return colorModel;
    }

    /**
     * @param colorModel the colorModel to set
     */
    public void setColorModel(String colorModel) {
        this.colorModel = colorModel;
    }

    public static void main(String args[]) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintStream out = System.out;
            out.print("Nama file: ");
            BufferedImage image = ImageIO.read(new File(in.readLine()));
            if (image == null) {
                out.println("NULL!");
            }

            VC vc = new VC(image, "CMY");
            List<BufferedImage> shares = vc.generateShares(2, 2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
