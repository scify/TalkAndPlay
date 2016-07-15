package org.scify.talkandplay.gui.grid.tiles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
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

public class TilePanel extends javax.swing.JPanel {

    private String text;
    private String imageString;
    private URL imageUrl;
    private int width, height, rows, columns, imageWidth, imageHeight;

    public TilePanel(String text, String imageString, int width, int height, int rows, int columns) {
        this.text = text;
        this.imageString = imageString;
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;

        initComponents();
        initCustomComponents();
    }

    public TilePanel(String text, URL imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;

        this.width = 100;
        this.height = 100;
        this.rows = 2;
        this.columns = 3;

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        //  setBorder(new LineBorder(Color.white, 10));
        setBorder(new EmptyBorder(3, 3, 3, 3));

        //default
       /*add(new TextLabel(text));
         JLabel textLabel = new JLabel("<html><span style='font-size:100%'>koko</span></html>");
         textLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 35));
         add(textLabel );*/
        /*
         JLabel textLabel = new JLabel(text);
         textLabel.setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 30));
         textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
         JPanel textPanel = new JPanel();
         textPanel.add(textLabel);
         add(textPanel);
                
         */
        /*   JTextArea textArea = new JTextArea();
         textArea.setText(text);
         textArea.setEditable(false);
         textArea.setLineWrap(true);
         textArea.setWrapStyleWord(true);
         textArea.setBorder(javax.swing.BorderFactory.createEmptyBorder());
         textArea.setRows(2);
         textArea.setColumns(1);
         textArea.setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 25));
         textArea.setBorder(new EmptyBorder(0, 0, 3, 0));
         add(textArea, BorderLayout.CENTER);
         */
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 20));
        textPane.setBorder(new EmptyBorder(0, 0, 1, 0));
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.white);
        textPanel.add(textPane);
        add(textPanel);

        /*   JTextPane textPane = new JTextPane();
         textPane.setText(text);
         textPane.setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 30));

         JPanel noWrapPanel = new JPanel(new BorderLayout());
         noWrapPanel.add(textPane);
         JScrollPane scrollPane = new JScrollPane(noWrapPanel);
         add(noWrapPanel);
         */
        if (imageString != null && !imageString.isEmpty()) {
            //  JPanel imagePanel = new ImagePanel(imageString);
            //  imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel image;

            int imageHeight, imageWidth;

            if (calculateImageHeight() > 0) {
                int imageRatio = new ImageIcon(imageString).getImage().getHeight(this) / new ImageIcon(imageString).getImage().getWidth(this);
                imageHeight = calculateImageHeight();
                imageWidth = imageHeight / imageRatio;
            } else {
                imageHeight = calculateImageHeight();
                imageWidth = calculateImageHeight();
            }
            System.out.println(imageHeight);

            image = new JLabel(new ImageIcon(new ImageIcon(imageString).getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH)));

            JPanel imagePanel = new JPanel();
            imagePanel.add(image);
            imagePanel.setBackground(Color.white);

            add(imagePanel);
        } else {
            JPanel imagePanel = new ImagePanel(imageUrl);
            imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(imagePanel);
        }
        /*  setPreferredSize(new Dimension(calculatePanelWidth(), calculatePanelHeight()));
         setMaximumSize(new Dimension(calculatePanelWidth(), calculatePanelHeight()));
         setMinimumSize(new Dimension(calculatePanelWidth(), calculatePanelHeight()));*/
    }

    private int calculatePanelWidth() {
        return (width / columns) - 100;
    }

    private int calculatePanelHeight() {
        return (height / rows) - 100;
    }

    private int calculateImageWidth() {
        return (width / columns) - 50;
    }

    private int calculateImageHeight() {         
        return (height / rows)-20-(20*rows);
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
