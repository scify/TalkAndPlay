/**
* Copyright 2016 SciFY
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.scify.talkandplay.gui.grid.tiles;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;

public class TilePanel extends javax.swing.JPanel {

    private String text;
    private ImageResource image;
    private boolean hasImage;
    private boolean hasText;
    protected ResourceManager rm;

    public TilePanel(String text, ImageResource image, boolean hasImage, boolean hasText) {
        this.text = text;
        this.image = image;
        this.hasImage = hasImage;
        this.hasText = hasText;
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.white);

        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 24));
        textPane.setEditable(false);
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.white);
        textPanel.add(textPane);
        add(textPanel);

        if (text == null || text.isEmpty() || !hasText) {
            textPanel.setVisible(false);
        }

        Image img;
        if (!hasImage) {
            img = rm.getImage("empty_pixel.png", ResourceType.JAR);
        } else {
            img = rm.getImage(image.getPath(), image.getResourceType());
        }

        JLabel image;

        int imageHeight, imageWidth;

        if (calculateImageHeight() > 0) {
            int imageRatio = img.getHeight(this) / img.getWidth(this);
            imageHeight = calculateImageHeight();
            imageWidth = imageRatio == 0 ? imageHeight : imageHeight / imageRatio;
        } else {
            imageHeight = calculateImageHeight();
            imageWidth = calculateImageWidth();
        }

        image = new JLabel(new ImageIcon((img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH))));

        JPanel imagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        imagePanel.add(image, gbc);
        imagePanel.setBorder(new EmptyBorder(1, 1, 1, 1));
        imagePanel.setBackground(Color.white);

        add(imagePanel);

        /*   setPreferredSize(new Dimension(imageWidth, imageHeight));
         setMaximumSize(new Dimension(imageWidth, imageHeight));
         setMinimumSize(new Dimension(imageWidth, imageHeight));*/
    }

    private int calculateImageWidth() {
        return (UIConstants.getInstance().getWidth() / UIConstants.getInstance().getColumns() - 20 /*- (20 * UIConstants.getInstance().getRows())*/);
    }

    private int calculateImageHeight() {
        return (UIConstants.getInstance().getHeight() / UIConstants.getInstance().getRows() - 20 /*- (20 * UIConstants.getInstance().getRows())*/);
    }

    public void setSelected() {
        ((JPanel) getComponent(0)).setBackground(Color.decode(UIConstants.lightBlue));
        (((JPanel) getComponent(0)).getComponent(0)).setBackground(Color.decode(UIConstants.lightBlue));
        ((JPanel) getComponent(1)).setBackground(Color.decode(UIConstants.lightBlue));
        setBackground(Color.decode(UIConstants.lightBlue));
    }

    public void setUnSelected() {
        ((JPanel) getComponent(0)).setBackground(Color.white);
        (((JPanel) getComponent(0)).getComponent(0)).setBackground(Color.white);
        ((JPanel) getComponent(1)).setBackground(Color.white);
        setBackground(Color.white);
    }

    public String getName() {
        return ((JTextPane) ((JPanel) getComponent(0)).getComponent(0)).getText();
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
