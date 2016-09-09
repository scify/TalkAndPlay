package org.scify.talkandplay.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.scify.talkandplay.gui.configuration.ConfigurationPanel;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.Properties;

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        initCustomComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        contentPane = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        titlePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Talk&Play");

        contentPane.setBackground(new java.awt.Color(255, 255, 255));
        contentPane.setPreferredSize(new java.awt.Dimension(800, 720));

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/tp_logo_small.png"))); // NOI18N

        jLabel1.setBackground(new java.awt.Color(51, 51, 255));
        jLabel1.setFont(jLabel1.getFont());
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        // Properties needed to get the application's version
        // in order to display it on the bottom panel
        Properties prop = new Properties();
        jLabel1.setText("SciFY 2016 - version: " + prop.getVersion());

        titlePanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 618, Short.MAX_VALUE)
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setBorder(null);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(contentPanel);

        javax.swing.GroupLayout contentPaneLayout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPaneLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(logoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPaneLayout.createSequentialGroup()
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logoLabel))
                    .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage((new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/tp_logo_mini.png"))).getImage());

        contentPanel.add(new MainPanel(this), BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
        revalidate();
        repaint();
        pack();
    }

    public void changePanel(JPanel newPanel) {
        if (newPanel instanceof MainPanel) {
            titlePanel.removeAll();
            titlePanel.setBackground(Color.white);
        }
        contentPanel.removeAll();
        contentPanel.add(newPanel);
        revalidate();
        repaint();
    }

    public void setPanelTitle(String title) {
        titlePanel.removeAll();
        titlePanel.setBackground(Color.decode(UIConstants.green));
        //titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
        titlePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 20));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
    }
    /*
     public void setPanelTitleWithBackNext(final User user, String title) {
     titlePanel.removeAll();
     titlePanel.setBackground(Color.decode(UIConstants.green));
     //titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
     titlePanel.setLayout(new BorderLayout());

     JLabel titleLabel = new JLabel(title);
     titleLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 20));
     titleLabel.setForeground(Color.white);
     titleLabel.setHorizontalAlignment(JLabel.CENTER);

     /*  JPanel backPanel = new JPanel();
     backPanel.setBackground(Color.decode(UIConstants.green));
     backPanel.setBorder(new LineBorder(Color.white, 1));*/
    /*   JLabel backLabel = new JLabel("ΠΙΣΩ");
     backLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 20));
     backLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/left-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
     backLabel.setBorder(new LineBorder(Color.white, 1));
     /*  JLabel backIcon = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/left-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
     backPanel.add(backIcon);
     backPanel.add(backLabel);
     */
    /*
     JLabel nextLabel = new JLabel("ΕΠΟΜΕΝΟ");
     nextLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 20));
     nextLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/right-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
     nextLabel.setBorder(new LineBorder(Color.white, 1));

     titlePanel.add(backLabel, BorderLayout.LINE_START);
     titlePanel.add(titleLabel, BorderLayout.CENTER);
     titlePanel.add(nextLabel, BorderLayout.LINE_END);
     // titlePanel.add(Box.createHorizontalGlue());

     final MainFrame mainFrame = this;
     nextLabel.addMouseListener(new MouseAdapter() {
     public void mouseClicked(MouseEvent me) {
     changePanel(new ConfigurationPanel(user.getName(), mainFrame));

     }
     });
     }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPane;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
