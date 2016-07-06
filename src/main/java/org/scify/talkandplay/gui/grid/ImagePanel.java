package org.scify.talkandplay.gui.grid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ImagePanel extends javax.swing.JPanel {

    private String imageString;
    private URL imageUrl;

    public ImagePanel(String imageString) {
        this.imageString = imageString;
        initComponents();
        initCustomComponents();
    }

    public ImagePanel(URL imageUrl) {
        this.imageUrl = imageUrl;
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setBackground(Color.white);
        setBorder(new EmptyBorder(0, 20, 0, 20));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        Image image;

        if (imageString != null && !imageString.isEmpty()) {
            image = (new ImageIcon(imageString)).getImage();
        } else {
            image = (new ImageIcon(imageUrl)).getImage();
        }

        g.drawImage(image, 0, 0, w, h, this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
