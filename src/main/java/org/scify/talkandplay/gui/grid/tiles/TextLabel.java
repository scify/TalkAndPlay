/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.tiles;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.gui.helpers.UIConstants;

/**
 *
 * @author christina
 */
public class TextLabel extends JLabel {

    private static final int SIZE = 35;
    private BufferedImage image;
    private String text;
    private GuiHelper guiHelper;

    public TextLabel(String text) {
        super(text);
        this.text = text;
        this.guiHelper = new GuiHelper();
        // createImage();
    }

    private void createImage() {
        Font font = new Font(UIConstants.getMainFont(), Font.PLAIN, SIZE);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle r = layout.getPixelBounds(null, 0, 0);

        System.out.println(r);

        BufferedImage bi = new BufferedImage(r.width + 1, r.height + 1, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g2d.setColor(getForeground());
        layout.draw(g2d, 0, -r.y);
        g2d.dispose();
        image = bi;
    }

    protected void paintComponent(Graphics g) {
        /*  super.paintComponent(g);
         int w = getWidth();
         int h = getHeight();

         guiHelper.applyQualityRenderingHints((Graphics2D) g);
         g.drawImage(image, 0, 0, w, h, this);*/
        setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 35));
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2d);
    }

}
