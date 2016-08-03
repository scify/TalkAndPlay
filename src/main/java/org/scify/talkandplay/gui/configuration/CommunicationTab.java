package org.scify.talkandplay.gui.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.gui.users.UserPanel;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.CategoryService;
import org.scify.talkandplay.services.UserService;

public class CommunicationTab extends javax.swing.JPanel {

    private User user;
    private UserService userService;
    private CategoryService categoryService;
    private List<JPanel> panels;
    private ConfigurationPanel parent;
    private int row;

    private GridBagConstraints c;

    private static final int MARGIN = 20;

    public CommunicationTab(User user, ConfigurationPanel parent) {
        this.user = user;
        this.parent = parent;
        this.userService = new UserService();
        this.categoryService = new CategoryService();

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        panels = new ArrayList();
        //  setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        contentPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;

        row = 1;
        c.weightx = 1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = row;
        row++;

        setBorder(new EmptyBorder(20, 0, 0, 0));

        contentPanel.add(titlePanel(), c);

        drawCategories(user.getCommunicationModule().getCategories(), MARGIN);

    }

    /**
     * Recursively draw the categories
     *
     * @param categories
     * @param margin
     */
    private void drawCategories(List<Category> categories, int margin) {
        if (categories == null || categories.isEmpty()) {
            return;
        } else {
            margin += MARGIN;

            for (Category category : categories) {
                final JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(Color.white);

                JPanel controlsPanel = new JPanel();

                JLabel label = new JLabel(category.getName());
                label.setBorder(new EmptyBorder(0, margin, 0, 0));

                JLabel editLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/edit-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                JLabel deleteLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/delete-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                editLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

                controlsPanel.add(editLabel);
                controlsPanel.add(deleteLabel);
                controlsPanel.setVisible(false);
                controlsPanel.setBackground(Color.decode(UIConstants.green));

                if (category.getSubCategories().size() > 0) {
                    label.setFont(new Font(UIConstants.mainFont, Font.BOLD, 14));
                } else {
                    label.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 14));
                }

                setListeners(panel, editLabel, deleteLabel, category.getName());

                panel.add(label, BorderLayout.LINE_START);
                panel.add(controlsPanel, BorderLayout.LINE_END);

                //   c.weightx = 0.1;
                c.weighty = 0.1;
                c.gridy = row;
                c.ipady = 0;
                contentPanel.add(panel, c);
                panels.add(panel);
                row++;
                drawCategories(category.getSubCategories(), margin);
            }
            margin -= MARGIN;
        }
    }

    public void redrawCategoriesList() {
        panels = new ArrayList();
        contentPanel.removeAll();
        contentPanel.add(titlePanel(), c);

        user = userService.getUser(user.getName());
        drawCategories(user.getCommunicationModule().getCategories(), MARGIN);

        revalidate();
        repaint();
    }

    private JPanel titlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        panel.setBackground(Color.white);
        JLabel label = new JLabel(user.getCommunicationModule().getName());
        label.setFont(new Font(UIConstants.mainFont, Font.BOLD, 16));

        panel.add(label);

        return panel;
    }

    private void setListeners(final JPanel panel, JLabel editLabel, JLabel deleteLabel, final String category) {
        //add the green background when a panel is clicked
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {

                for (JPanel panel : panels) {
                    panel.setBackground(Color.white);
                    panel.getComponent(0).setForeground(Color.black);
                    panel.getComponent(1).setVisible(false);
                }
                panel.setBackground(Color.decode(UIConstants.green));
                panel.getComponent(0).setForeground(Color.white);
                panel.getComponent(1).setVisible(true);
            }
        });

        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                parent.addPanel(new WordFormPanel(user, categoryService.getCategory(category, user), parent));
            }
        });

        deleteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Είσαι σίγουρος/η ότι θες να διαγράψεις τη λέξη;", "Warning", 0);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try {
                            categoryService.delete(category, user);
                            redrawCategoriesList();
                            parent.redrawCategoriesDropDown();
                        } catch (Exception ex) {
                            Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(contentPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
