/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.gui.helpers;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.scify.talkandplay.gui.grid.tiles.TilePanel;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;

/**
 *
 * @author christina
 */
public class GuiHelper {

    private User user;
    private final ResourceManager rm;

    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 150;

    public GuiHelper() {
        rm = ResourceManager.getInstance();
    }

    public GuiHelper(User user) {
        rm = ResourceManager.getInstance();
        this.user = user;
    }

    /**
     * Draw the default button
     *
     * @param button
     */
    public void drawButton(JButton button) {
        button.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        button.setBorder(new LineBorder(new Color(75, 161, 69), 20));
        button.setMargin(new Insets(10, 10, 10, 10));
    }

    /**
     * Get the icon for a user, or no photo
     *
     * @param image
     * @return
     */
    public ImageIcon getIcon(ImageResource image) {
        if (image != null) {
            return new ImageIcon(image.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        } else {
            return new ImageIcon(rm.getImage("no-photo.png", ResourceType.JAR).getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        }
    }

    /**
     * Set the style of the steps labels (like titles)
     *
     * @param label
     */
    public void setStepLabelFont(JLabel label) {
        label.setFont(new Font(UIConstants.mainFont, Font.BOLD, 14));
    }

    public void setStepExplLabelFont(JLabel label) {
        label.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 12));
    }

    /**
     * Set the style of the custom text fields
     *
     * @param textField
     */
    public void setCustomTextField(JTextField textField) {
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
        textField.setFont(new Font(UIConstants.mainFont, Font.ITALIC, 14));
        textField.setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Get a round icon
     *
     * @param imageResource
     * @return
     */
    public ImageIcon getRoundIcon(ImageResource imageResource) {
        //first scale the image to the desired dimensions
        BufferedImage master = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = master.createGraphics();
        if (imageResource == null) {
            g2d.drawImage(new ImageIcon(rm.getImage("no-photo.png", ResourceType.JAR).getScaledInstance(DEFAULT_WIDTH, DEFAULT_WIDTH, Image.SCALE_SMOOTH)).getImage(), 0, 0, null);
        } else {
            g2d.drawImage(new ImageIcon(imageResource.getImage().getScaledInstance(DEFAULT_WIDTH, DEFAULT_WIDTH, Image.SCALE_SMOOTH)).getImage(), 0, 0, null);
        }
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

    public JPanel createImagePanel(ImageResource image, String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createLineBorder(Color.white, 5));
        panel.add(new TilePanel(text, image, user.getConfiguration().hasImage(), user.getConfiguration().hasText()), BorderLayout.CENTER);
        /* else {
         panel.add(new TilePanel(text), BorderLayout.CENTER);
         }*/

        return panel;
    }
}
