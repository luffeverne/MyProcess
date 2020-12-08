package com.chapter.three;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class ImagePanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage sourceImage;
    private BufferedImage destImage;

    public ImagePanel() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (sourceImage != null) {
            g2d.drawImage(sourceImage, 0, 0, sourceImage.getWidth(),
                    sourceImage.getHeight(), null);
            if (destImage != null) {
                g2d.drawImage(destImage, sourceImage.getWidth() + 10, 0,
                        destImage.getWidth(), destImage.getHeight(), null);
            }
        }
    }

    public void process()
    {
        // do nothing
        System.out.println("Processing");
    }


    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public BufferedImage getDestImage() {
        return destImage;
    }

    public void setDestImage(BufferedImage destImage) {
        this.destImage = destImage;
    }

}
