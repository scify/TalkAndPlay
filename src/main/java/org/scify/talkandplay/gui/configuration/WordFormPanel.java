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
package org.scify.talkandplay.gui.configuration;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.CategoryService;
import org.scify.talkandplay.services.UserService;
import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class WordFormPanel extends javax.swing.JPanel {

    private GuiHelper guiHelper;
    private User user;
    private Category category;
    private List<String> categories;
    private ImageResource imageResource;
    private SoundResource soundResource;
    private CategoryService categoryService;
    private UserService userService;
    private ConfigurationPanel parent;
    private AudioPlayerComponent audioPlayer;
    private File currentDirectory;
    protected ResourceManager rm;

    public WordFormPanel(User user, Category category, ConfigurationPanel parent) {
        this.guiHelper = new GuiHelper();
        this.user = user;
        this.imageResource = null;
        this.soundResource = null;
        this.categoryService = new CategoryService();
        this.userService = new UserService();
        this.category = category;
        this.parent = parent;
        this.audioPlayer = new AudioPlayerComponent();
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        categories = categoryService.getLinearCategories(user);
        enabledCheckbox.setSelected(true);
        setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
        setUI();
        setListeners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        step1Label = new javax.swing.JLabel();
        wordTextField = new javax.swing.JTextField();
        step2Label = new javax.swing.JLabel();
        step3Label = new javax.swing.JLabel();
        step3ExplLabel = new javax.swing.JLabel();
        uploadSoundLabel = new javax.swing.JLabel();
        step4Label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        step4ExplTextArea = new javax.swing.JTextArea();
        uploadImageLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        editStepsPanel = new javax.swing.JPanel();
        rowsTextField = new javax.swing.JTextField();
        xLabel = new javax.swing.JLabel();
        columnsTextField = new javax.swing.JTextField();
        step7Label = new javax.swing.JLabel();
        imageCheckBox = new javax.swing.JCheckBox();
        textCheckBox = new javax.swing.JCheckBox();
        soundCheckBox = new javax.swing.JCheckBox();
        step6Label = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        closeLabel = new javax.swing.JLabel();
        categoriesComboBox = new javax.swing.JComboBox();
        errorLabel = new javax.swing.JLabel();
        enabledCheckbox = new javax.swing.JCheckBox();

        backButton.setBackground(new java.awt.Color(75, 161, 69));
        backButton.setFont(backButton.getFont());
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("Πίσω");
        backButton.setBorder(null);

        setBackground(new java.awt.Color(255, 255, 255));

        titleLabel.setText("ΠΡΟΣΘΗΚΗ ΝΕΑΣ ΛΕΞΗΣ");

        step1Label.setText("1. Γράψε λέξη");

        wordTextField.setBackground(new java.awt.Color(255, 255, 255));
        wordTextField.setForeground(new java.awt.Color(51, 51, 51));
        wordTextField.setText("Λέξη");

        step2Label.setText("2. Ανέβασε φωτογραφία");

        step3Label.setText("3. Ανέβασε ηχητικό");

        step3ExplLabel.setText("Εάν δεν ανεβάσεις ηχητικό, θα παίζει προκαθορισμένος ήχος.");

        uploadSoundLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        uploadSoundLabel.setText("upload");

        step4Label.setText("4. Επίλεξε πού ανήκει η λέξη");

        step4ExplTextArea.setEditable(false);
        step4ExplTextArea.setBackground(new java.awt.Color(255, 255, 255));
        step4ExplTextArea.setColumns(20);
        step4ExplTextArea.setRows(5);
        step4ExplTextArea.setText("Επίλεξε την λέξη στην οποία υπάγεται η λέξη που προσθέτεις εδώ, ή επίλεξε “ΕΠΙΚΟΙΝΩΝΙΑ” εάν πρόκειται για εντελώς νέα κατηγορία λέξεων.\n");
        step4ExplTextArea.setBorder(null);
        jScrollPane1.setViewportView(step4ExplTextArea);

        uploadImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        uploadImageLabel.setText("upload");

        saveButton.setBackground(new java.awt.Color(75, 161, 69));
        saveButton.setFont(saveButton.getFont());
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Αποθήκευση λέξης");
        saveButton.setBorder(null);
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
        });

        editStepsPanel.setBackground(new java.awt.Color(255, 255, 255));

        xLabel.setText("x");

        step7Label.setText("6. Εμφάνιση πίνακα");

        imageCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        imageCheckBox.setText("Εικόνα");

        textCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        textCheckBox.setText("Λεκτικό");

        soundCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        soundCheckBox.setText("Ήχος");

        step6Label.setText("5. Καθόρισε μέγεθος πίνακα");

        jLabel1.setText("(άφησε κενό για προεπιλέγμενο μέγεθος πίνακα)");

        javax.swing.GroupLayout editStepsPanelLayout = new javax.swing.GroupLayout(editStepsPanel);
        editStepsPanel.setLayout(editStepsPanelLayout);
        editStepsPanelLayout.setHorizontalGroup(
                editStepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(editStepsPanelLayout.createSequentialGroup()
                                .addGroup(editStepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(editStepsPanelLayout.createSequentialGroup()
                                                .addGap(64, 64, 64)
                                                .addGroup(editStepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(editStepsPanelLayout.createSequentialGroup()
                                                                .addComponent(rowsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(xLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(columnsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(editStepsPanelLayout.createSequentialGroup()
                                                                .addComponent(soundCheckBox)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(textCheckBox)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(imageCheckBox))
                                        .addComponent(step6Label)
                                        .addComponent(step7Label))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editStepsPanelLayout.setVerticalGroup(
                editStepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(editStepsPanelLayout.createSequentialGroup()
                                .addComponent(step6Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addGap(12, 12, 12)
                                .addGroup(editStepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(rowsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(xLabel)
                                        .addComponent(columnsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(step7Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(editStepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(soundCheckBox)
                                        .addComponent(textCheckBox)
                                        .addComponent(imageCheckBox)))
        );

        closeLabel.setText("closeLabel");

        categoriesComboBox.setBackground(new java.awt.Color(255, 255, 255));
        categoriesComboBox.setBorder(null);

        errorLabel.setBackground(new java.awt.Color(255, 255, 255));
        errorLabel.setForeground(new java.awt.Color(153, 0, 0));
        errorLabel.setText("error");

        enabledCheckbox.setBackground(new java.awt.Color(255, 255, 255));
        enabledCheckbox.setText("Ενεργοποιημένη");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(titleLabel)
                                                                .addGap(106, 106, 106)
                                                                .addComponent(closeLabel))
                                                        .addComponent(saveButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(editStepsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(uploadImageLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(uploadSoundLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(categoriesComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(step3ExplLabel)
                                                                        .addComponent(errorLabel)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(16, 16, 16)
                                                                                .addComponent(wordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(enabledCheckbox))
                                                                        .addComponent(step1Label)
                                                                        .addComponent(step2Label)
                                                                        .addComponent(step3Label)
                                                                        .addComponent(step4Label)
                                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(closeLabel)
                                        .addComponent(titleLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(step1Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(wordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(enabledCheckbox))
                                .addGap(16, 16, 16)
                                .addComponent(step2Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(uploadImageLabel)
                                .addGap(11, 11, 11)
                                .addComponent(step3Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(step3ExplLabel)
                                .addGap(18, 18, 18)
                                .addComponent(uploadSoundLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(step4Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(categoriesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(editStepsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(errorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveButtonMouseClicked

        if (validateCategory()) {

            Category newCategory = new Category();
            newCategory.setName(wordTextField.getText());
            newCategory.setImage(imageResource);
            newCategory.setSound(soundResource);
            newCategory.setEnabled(enabledCheckbox.isSelected());
            newCategory.setParentCategory(new Category(categoriesComboBox.getSelectedItem().toString()));
            /*  newCategory.setHasSound(soundCheckBox.isSelected());
             newCategory.setHasImage(imageCheckBox.isSelected());
             newCategory.setHasText(textCheckBox.isSelected());*/

            if (rowsTextField.getText() != null && !rowsTextField.getText().isEmpty()) {
                newCategory.setRows(Integer.parseInt(rowsTextField.getText()));
            }

            if (columnsTextField.getText() != null && !columnsTextField.getText().isEmpty()) {
                newCategory.setColumns(Integer.parseInt(columnsTextField.getText()));
            }

            try {
                if (category == null) {
                    categoryService.save(newCategory, user);
                } else {
                    categoryService.update(newCategory, user, category.getName(), category.getParentCategory().getName());
                }
                category = newCategory;
                updateCategoriesComboBox();
                parent.redrawCategoriesList();
                parent.displayMessage(rm.getTextOfXMLTag("wordWasSaved"));
                parent.showInfoPanel();
            } catch (Exception ex) {
                Logger.getLogger(WordFormPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_saveButtonMouseClicked

    public void updateCategoriesComboBox() {
        categoriesComboBox.removeAllItems();
        categoriesComboBox.addItem(rm.getTextOfXMLTag("chooseWord"));
        List<String> categories = categoryService.getLinearCategories(userService.getUser(user.getName()));
        for (String category : categories) {
            categoriesComboBox.addItem(category);
        }

        if (category != null) {
            categoriesComboBox.removeItem(category.getName());
            categoriesComboBox.setSelectedItem(category.getParentCategory().getName());
        }
    }

    private boolean validateCategory() {

        String name = wordTextField.getText();

        //word should not be null
        if (name == null || name.isEmpty()) {
            errorLabel.setText(rm.getTextOfXMLTag("wordMustNotBeEmpty"));
            errorLabel.setVisible(true);
            return false;
        }

        //word should be unique
        if (category == null || (category != null && !category.getName().equals(name))) {
            for (String category : categories) {
                if (category.equals(name)) {
                    errorLabel.setText(rm.getTextOfXMLTag("wordMustBeUnique"));
                    errorLabel.setVisible(true);
                    return false;
                }
            }
        }

        //image should be uploaded
        if (imageResource == null) {
            //TODO this is not correct, image might not be visible due to settings
            errorLabel.setText(rm.getTextOfXMLTag("wordMustHaveImage"));
            errorLabel.setVisible(true);
            return false;
        }

        if (categoriesComboBox.getSelectedIndex() == 0) {
            errorLabel.setText(rm.getTextOfXMLTag("wordMustBelongToAnotherWord"));
            errorLabel.setVisible(true);
            return false;
        }

        /*  if (category != null && category.getSubCategories().size() > 0 && !imageCheckBox.isSelected() && !textCheckBox.isSelected()) {
         errorLabel.setText("Η λέξη θα πρέπει να έχει τουλάχιστον ένα από τα δύο: λεκτικό, εικόνα");
         errorLabel.setVisible(true);
         return false;
         }*/
        return true;
    }

    private void setUI() {
        errorLabel.setVisible(false);

        closeLabel.setIcon(new ImageIcon(rm.getImage("close-icon.png", ResourceType.JAR).getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
        closeLabel.setText("");

        titleLabel.setText(rm.getTextOfXMLTag("addNewWord"));
        titleLabel.setFont(new Font(UIConstants.mainFont, Font.BOLD, 16));
        titleLabel.setForeground(Color.decode(UIConstants.green));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        guiHelper.drawButton(saveButton);
        guiHelper.setStepLabelFont(step1Label);
        guiHelper.setStepLabelFont(step2Label);
        guiHelper.setStepLabelFont(step3Label);
        guiHelper.setStepLabelFont(step4Label);
        guiHelper.setStepLabelFont(step6Label);
        guiHelper.setStepLabelFont(step7Label);
        guiHelper.setStepExplLabelFont(step3ExplLabel);

        wordTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
        wordTextField.setFont(new Font(UIConstants.mainFont, Font.ITALIC, 14));
        wordTextField.setHorizontalAlignment(JTextField.CENTER);

        uploadImageLabel.setIcon(new ImageIcon(rm.getImage("add-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
        uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("add-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
        uploadImageLabel.setText("");
        uploadSoundLabel.setText("");
        uploadImageLabel.setHorizontalAlignment(JLabel.CENTER);
        uploadSoundLabel.setHorizontalAlignment(JLabel.CENTER);

        //set up the textarea to look like a text field
        step4ExplTextArea.setEditable(false);
        step4ExplTextArea.setLineWrap(true);
        step4ExplTextArea.setWrapStyleWord(true);
        step4ExplTextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jScrollPane1.setBorder(null);

        updateCategoriesComboBox();

        categoriesComboBox.setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
        categoriesComboBox.setFont(new Font(UIConstants.green, Font.PLAIN, 14));

        //temporarily disabling the following fields, not important to set by category
        step7Label.setVisible(false);
        soundCheckBox.setVisible(false);
        imageCheckBox.setVisible(false);
        textCheckBox.setVisible(false);

        if (category == null) {
            editStepsPanel.setVisible(false);
        } else {
            titleLabel.setText(rm.getTextOfXMLTag("editWord"));
            //if the category has subcategories, fill the appropriate fields
            if (category.getSubCategories().size() > 0) {
                editStepsPanel.setVisible(true);

                if (category.getRows() == null) {
                    rowsTextField.setText("");
                } else {
                    rowsTextField.setText(String.valueOf(category.getRows()));
                }

                if (category.getColumns() == null) {
                    columnsTextField.setText("");
                } else {
                    columnsTextField.setText(String.valueOf(category.getColumns()));
                }
            } else {
                editStepsPanel.setVisible(false);
            }

            enabledCheckbox.setSelected(category.isEnabled());
            wordTextField.setText(category.getName());

            Image image = rm.getImage(category.getImage());
            if (image == null) {
                uploadImageLabel.setIcon(new ImageIcon(rm.getImage("add-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            } else {
                uploadImageLabel.setIcon(new ImageIcon(image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            }

            File sound = rm.getSound(category.getSound());
            if (sound == null) {
                uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("add-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            } else {
                uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("sound-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            }

            categoriesComboBox.setSelectedItem(category.getParentCategory().getName());

            imageResource = category.getImage();
            soundResource = category.getSound();
        }
    }

    private void setListeners() {
        uploadImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent me) {
                if (imageResource == null) {
                    uploadImageLabel.setIcon(new ImageIcon(rm.getImage("add-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if (imageResource == null) {
                    uploadImageLabel.setIcon(new ImageIcon(rm.getImage("add-icon-hover.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                }
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                if (currentDirectory != null) {
                    chooser.setCurrentDirectory(currentDirectory);
                }
                chooser.setDialogTitle(rm.getTextOfXMLTag("selectImage"));
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "JPG", "JPEG", "gif"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    imageResource = new ImageResource(chooser.getSelectedFile().getAbsolutePath(), ResourceType.LOCAL);
                    uploadImageLabel.setIcon(new ImageIcon(rm.getImage(imageResource).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    currentDirectory = chooser.getCurrentDirectory();
                }
            }
        });

        uploadSoundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (soundResource == null) {
                    uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("add-icon-hover.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("sound-icon-hover.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    audioPlayer.mediaPlayer().media().play(soundResource.getSound().getAbsolutePath());
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (soundResource == null) {
                    uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("add-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("sound-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                }
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                if (currentDirectory != null) {
                    chooser.setCurrentDirectory(currentDirectory);
                }

                chooser.setDialogTitle(rm.getTextOfXMLTag("selectSound"));
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Sound Files", "mp3", "wav", "wma", "mid"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    soundResource = new SoundResource(chooser.getSelectedFile().getAbsolutePath(), ResourceType.LOCAL);
                    uploadSoundLabel.setIcon(new ImageIcon(rm.getImage("sound-icon.png", ResourceType.JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    currentDirectory = chooser.getCurrentDirectory();
                }
            }
        });

        wordTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if (rm.getTextOfXMLTag("word").equals(wordTextField.getText())) {
                    wordTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (wordTextField.getText().isEmpty()) {
                    wordTextField.setText(rm.getTextOfXMLTag("word"));
                }
            }
        });

        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                parent.hideInfoPanel();
                parent.showInfoPanel();
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JComboBox categoriesComboBox;
    private javax.swing.JLabel closeLabel;
    private javax.swing.JTextField columnsTextField;
    private javax.swing.JPanel editStepsPanel;
    private javax.swing.JCheckBox enabledCheckbox;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JCheckBox imageCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField rowsTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JCheckBox soundCheckBox;
    private javax.swing.JLabel step1Label;
    private javax.swing.JLabel step2Label;
    private javax.swing.JLabel step3ExplLabel;
    private javax.swing.JLabel step3Label;
    private javax.swing.JTextArea step4ExplTextArea;
    private javax.swing.JLabel step4Label;
    private javax.swing.JLabel step6Label;
    private javax.swing.JLabel step7Label;
    private javax.swing.JCheckBox textCheckBox;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel uploadImageLabel;
    private javax.swing.JLabel uploadSoundLabel;
    private javax.swing.JTextField wordTextField;
    private javax.swing.JLabel xLabel;
    // End of variables declaration//GEN-END:variables
}
