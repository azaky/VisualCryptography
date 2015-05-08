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
import java.awt.image.ColorModel;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import visualcryptography.applet.MainFrame;

/**
 *
 * @author Ahmad Zaky
 */
public class VisualCryptography {

    private BufferedImage image;
    private ColorModel colorModel;
    
    public VisualCryptography(BufferedImage image, ColorModel colorModel) {
        this.image = image;
        this.colorModel = colorModel;
    }
    
    /**
     * Generate shares from the secret image. Using (n, k) scheme. Currently,
     * it only supports (2, 2) scheme.
     * 
     * @param n The number of desired shares.
     * @param k The sufficient number of shares to recover the secret image.
     * @return The list of generated shares.
     */
    public List<BufferedImage> generateShares(int n, int k) {
        
        
        return null;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Set the look and feel to users OS LaF.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
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
    public ColorModel getColorModel() {
        return colorModel;
    }

    /**
     * @param colorModel the colorModel to set
     */
    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }
}
