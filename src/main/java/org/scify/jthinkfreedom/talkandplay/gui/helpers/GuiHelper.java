package org.scify.jthinkfreedom.talkandplay.gui.helpers;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author christina
 */
public class GuiHelper {

    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 150;

    /**
     * Get the icon for a user, or no photo
     *
     * @param path
     * @return
     */
    public ImageIcon getIcon(String path) {
        if (path != null && new File(path).isFile()) {
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));

        } else {
            return new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/jthinkfreedom/talkandplay/resources/no-photo.png")).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        }
    }

    /**
     * Get a round icon
     *
     * @param path
     * @return
     */
    public ImageIcon getRoundIcon(String path) {
        if (path == null || path.isEmpty() || !new File(path).exists()) {
            path = getClass().getResource("/org/scify/jthinkfreedom/talkandplay/resources/no-photo.png").getPath();
        }

        //first scale the image to the desired dimensions
        BufferedImage master = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = master.createGraphics();
        g2d.drawImage(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(DEFAULT_WIDTH, DEFAULT_WIDTH, Image.SCALE_SMOOTH)).getImage(), 0, 0, null);
        g2d.dispose();

        //find the diameter and create an oval
        int diameter = Math.min(master.getWidth(), master.getHeight());
        BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);

        g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        //make image round
        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        applyQualityRenderingHints(g2d);
        int x = (diameter - master.getWidth()) / 2;
        int y = (diameter - master.getHeight()) / 2;
        g2d.drawImage(master, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(masked);
    }

    public static void applyQualityRenderingHints(Graphics2D g2d) {

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    }

    public JPanel createImagePanel(String imagePath, String text, JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());

        java.awt.Image image;
        try {
            image = ImageIO.read(new File(imagePath));
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH));

            return decorateImageIcon(imageIcon, text);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Η εικόνα της καρτέλας " + text + " δεν βρέθηκε",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
            return panel;
        }
    }

    public JPanel createResourceImagePanel(ImageIcon imageIcon, String text, JFrame frame) {
        return decorateImageIcon(imageIcon, text);
    }

    private JPanel decorateImageIcon(ImageIcon imageIcon, String text) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel imgLabel = new JLabel(imageIcon);
        JLabel txtLabel = new JLabel(text);
        txtLabel.setFont(new Font("Courier New", Font.PLAIN, 40));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.add(imgLabel, BorderLayout.CENTER);
        panel.add(txtLabel, BorderLayout.NORTH);
        txtLabel.setHorizontalAlignment(JLabel.CENTER);
        return panel;
    }

}
