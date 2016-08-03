package org.scify.talkandplay.gui.configuration;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameImage;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;

/**
 *
 * @author christina
 */
public class GamePanel extends javax.swing.JPanel {

    private static final int MAX_IMAGES = 4;
    private Game game;

    private List<JLabel> imgLabels;
    private List<JCheckBox> imgCheckboxes;
    private AudioMediaPlayerComponent audioPlayer;

    private Font activeFont, inactiveFont;

    public GamePanel(Game game) {
        this.game = game;
        this.imgLabels = new ArrayList();
        this.imgCheckboxes = new ArrayList();
        this.audioPlayer = new AudioMediaPlayerComponent();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {

        imgLabels.add(img1Label);
        imgLabels.add(img2Label);
        imgLabels.add(img3Label);
        imgLabels.add(img4Label);

        imgCheckboxes.add(checkBox1);
        imgCheckboxes.add(checkBox2);
        imgCheckboxes.add(checkBox3);
        imgCheckboxes.add(checkBox4);

        for (int i = 0; i < MAX_IMAGES; i++) {
            if (i < game.getImages().size()) {
                setImageIcon(imgLabels.get(i), game.getImages().get(i).getImage());
                imgCheckboxes.get(i).setEnabled(true);
                imgCheckboxes.get(i).setSelected(game.getImages().get(i).isEnabled());
                setCheckboxListener(imgCheckboxes.get(i), i);
            } else {
                setImageIcon(imgLabels.get(i), null);
                imgCheckboxes.get(i).setEnabled(false);
            }
            setImageListener(imgLabels.get(i), i);
        }

        activeFont = new Font(UIConstants.mainFont, Font.BOLD, 12);
        inactiveFont = new Font(UIConstants.mainFont, Font.PLAIN, 12);

        activeButton.setBackground(Color.white);
        activeButton.setMargin(new Insets(10, 10, 10, 10));

        inactiveButton.setBackground(Color.white);
        inactiveButton.setMargin(new Insets(3, 3, 3, 3));

        soundLabel.setHorizontalTextPosition(JLabel.CENTER);
        soundLabel.setVerticalTextPosition(JLabel.BOTTOM);

        if (game.getWinSound() == null || game.getWinSound().isEmpty()) {
            soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            soundLabel.setText("Προσθήκη ήχου");
            removeSoundLabel.setVisible(false);
        } else {
            soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            soundLabel.setText("");
            removeSoundLabel.setVisible(true);
        }

        if (game.isEnabled()) {
            activeButton.setForeground(Color.decode(UIConstants.green));
            activeButton.setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
            activeButton.setFont(activeFont);

            inactiveButton.setForeground(Color.decode(UIConstants.disabledColor));
            inactiveButton.setBorder(new LineBorder(Color.decode(UIConstants.disabledColor), 1));
            inactiveButton.setFont(inactiveFont);
        } else {
            inactiveButton.setForeground(Color.decode(UIConstants.green));
            inactiveButton.setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
            inactiveButton.setFont(activeFont);

            activeButton.setForeground(Color.decode(UIConstants.disabledColor));
            activeButton.setBorder(new LineBorder(Color.decode(UIConstants.disabledColor), 1));
            activeButton.setFont(inactiveFont);
        }

        setSoundListener();
    }

    private void setImageIcon(JLabel label, String path) {

        if (path == null || path.isEmpty() || !new File(path).exists()) {
            label.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png")).getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT)));
        } else {
            label.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
        }
    }

    public Game getGame() {
        return game;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        img1Label = new javax.swing.JLabel();
        checkBox1 = new javax.swing.JCheckBox();
        checkBox2 = new javax.swing.JCheckBox();
        img2Label = new javax.swing.JLabel();
        checkBox3 = new javax.swing.JCheckBox();
        img3Label = new javax.swing.JLabel();
        checkBox4 = new javax.swing.JCheckBox();
        img4Label = new javax.swing.JLabel();
        activeButton = new javax.swing.JButton();
        inactiveButton = new javax.swing.JButton();
        soundLabel = new javax.swing.JLabel();
        removeSoundLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        img1Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img1Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img1Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img1Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img1Label.setVerifyInputWhenFocusTarget(false);

        img2Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img2Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img2Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img2Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img2Label.setVerifyInputWhenFocusTarget(false);

        img3Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img3Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img3Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img3Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img3Label.setVerifyInputWhenFocusTarget(false);

        img4Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img4Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img4Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img4Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img4Label.setVerifyInputWhenFocusTarget(false);

        activeButton.setText("Διαθέσιμο");
        activeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                activeButtonMouseClicked(evt);
            }
        });

        inactiveButton.setText("Μη διαθέσιμο");
        inactiveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inactiveButtonMouseClicked(evt);
            }
        });

        soundLabel.setText("Προσθήκη ήχου");

        removeSoundLabel.setText("Αφαίρεση ήχου");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(img1Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(checkBox1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(img2Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(checkBox2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(img3Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(checkBox3)
                        .addGap(42, 42, 42)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(img4Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(checkBox4)
                        .addGap(50, 50, 50)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inactiveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(activeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soundLabel)
                    .addComponent(removeSoundLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(img3Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(img2Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkBox2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(img1Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkBox1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(activeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3)
                                .addComponent(inactiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(img4Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(soundLabel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBox4)
                            .addComponent(removeSoundLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void activeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_activeButtonMouseClicked
        if (!game.isEnabled()) {
            game.setEnabled(true);

            activeButton.setForeground(Color.decode(UIConstants.green));
            activeButton.setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
            activeButton.setFont(activeFont);

            inactiveButton.setForeground(Color.decode(UIConstants.disabledColor));
            inactiveButton.setBorder(new LineBorder(Color.decode(UIConstants.disabledColor), 1));
            inactiveButton.setFont(inactiveFont);
        }
    }//GEN-LAST:event_activeButtonMouseClicked

    private void inactiveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inactiveButtonMouseClicked
        if (game.isEnabled()) {
            game.setEnabled(false);

            inactiveButton.setForeground(Color.decode(UIConstants.green));
            inactiveButton.setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
            inactiveButton.setFont(activeFont);

            activeButton.setForeground(Color.decode(UIConstants.disabledColor));
            activeButton.setBorder(new LineBorder(Color.decode(UIConstants.disabledColor), 1));
            activeButton.setFont(inactiveFont);
        }
    }//GEN-LAST:event_inactiveButtonMouseClicked

    private void setImageListener(final JLabel image, final int i) {
        final JFileChooser chooser = new JFileChooser();

        image.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                chooser.setDialogTitle("Διάλεξε εικόνα");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "JPG", "JPEG", "gif"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();

                    if (i > game.getImages().size() - 1) {
                        game.getImages().add(new GameImage(path, imgCheckboxes.get(i).isSelected(), i + 1));
                    } else {
                        game.getImages().get(i).setImage(path);
                    }
                    image.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                }
            }
        });
    }

    private void setCheckboxListener(final JCheckBox checkbox, final int i) {

        checkbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                game.getImages().get(i).setEnabled(checkbox.isSelected());
            }
        });
    }

    private void setSoundListener() {

        soundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (game.getWinSound() == null || game.getWinSound().isEmpty()) {
                    soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    audioPlayer.getMediaPlayer().playMedia(game.getWinSound());
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (game.getWinSound() == null || game.getWinSound().isEmpty()) {
                    soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                }
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle("Διάλεξε ήχο");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Sound Files", "mp3", "wav", "wma", "mid"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    game.setWinSound(chooser.getSelectedFile().getAbsolutePath());
                    soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                }
            }
        });

        removeSoundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                game.setWinSound("");
                soundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                removeSoundLabel.setVisible(false);
                soundLabel.setText("Προσθήκη ήχου");
            }
        });
    }

    public void stopPlayer() {
        audioPlayer.getMediaPlayer().stop();
        audioPlayer.getMediaPlayer().release();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activeButton;
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.JCheckBox checkBox2;
    private javax.swing.JCheckBox checkBox3;
    private javax.swing.JCheckBox checkBox4;
    private javax.swing.JLabel img1Label;
    private javax.swing.JLabel img2Label;
    private javax.swing.JLabel img3Label;
    private javax.swing.JLabel img4Label;
    private javax.swing.JButton inactiveButton;
    private javax.swing.JLabel removeSoundLabel;
    private javax.swing.JLabel soundLabel;
    // End of variables declaration//GEN-END:variables
}
