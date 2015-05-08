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
package visualcryptography.util;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * This class contains helper functions to make image processing easier.
 *
 * @author Ahmad Zaky
 */
public class ImageUtil {

    private static final String ICC_PROFILE_PATH = "color_profiles/UncoatedFOGRA29.icc";
    private static ColorSpace iccInstance = null;

    static {
        try {
            iccInstance = new ICC_ColorSpace(ICC_Profile.getInstance(ICC_PROFILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getDepth(BufferedImage image) {
        int bitsPerPixel = 0;
        switch (image.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY:
            case BufferedImage.TYPE_BYTE_INDEXED:
                bitsPerPixel = 1;
                break;

            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_INT_RGB:
                bitsPerPixel = 3;
                break;

            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                bitsPerPixel = 4;
                break;

            default:
                bitsPerPixel = 0;
                break;
        }
        return bitsPerPixel;
    }

    public static int[] getColor(BufferedImage image, int x, int y, String colorModel) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return null;
        } else {
            switch (colorModel) {
                case "CMY":
                case "CMYK": {
                    int rgb[] = getColor(image, x, y, "RGB");
                    float cmykF[] = rgbToCmyk(1F * rgb[0] / 255, 1F * rgb[1] / 255, 1F * rgb[2] / 255);
                    int cmyk[] = new int[cmykF.length];
                    for (int i = 0; i < cmyk.length; ++i) {
                        cmyk[i] = Math.round(cmykF[i] * 255);
                    }
                    return cmyk;
                }

                case "RGB":
                default: {
                    int rgb = image.getRGB(x, y);
                    int result[] = new int[3];
                    for (int i = 0; i < 3; ++i) {
                        result[i] = (rgb >> (i << 3)) & 0xFF;
                    }
                    return result;
                }
            }
        }
    }

    public static int getColor(BufferedImage image, int x, int y, int k, String colorModel) {
        return getColor(image, x, y, colorModel)[k];
    }

    public static void setColor(BufferedImage image, int x, int y, int[] color, String colorModel) {
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return;
        }

        WritableRaster raster = image.getRaster();

        switch (colorModel) {
            case "CMY":
            case "CMYK":
                float[] rgb = cmykToRgb(1F * color[0] / 255, 1F * color[1] / 255, 1F * color[2] / 255);
                for (int i = 0; i < 3; ++i) {
                    raster.setSample(x, y, i, Math.round(rgb[i] * 255));
                }
                break;

            case "RGB":
            default:
                for (int i = 0; i < 3; ++i) {
                    raster.setSample(x, y, i, color[i]);
                }
        }

//        int rgb = image.getRGB(x, y);
//        int oldrgb = rgb;
//        rgb &= ~(0xFF << (k * 8));
//        rgb |= color << (k * 8);
//        image.setRGB(x, y, rgb);
    }

    public static float[] rgbToCmyk(float... rgb) {
        if (rgb.length != 3) {
            throw new IllegalArgumentException();
        }
        float[] fromRGB = iccInstance.fromRGB(rgb);
        return fromRGB;
    }

    public static float[] cmykToRgb(float... cmyk) {
        if (cmyk.length != 4) {
            throw new IllegalArgumentException();
        }
        float[] fromRGB = iccInstance.toRGB(cmyk);
        return fromRGB;
    }
}
