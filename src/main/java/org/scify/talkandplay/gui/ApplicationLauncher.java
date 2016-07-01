package org.scify.talkandplay.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ConfigurationHandler;

public class ApplicationLauncher {

    public static void main(String[] args) {

        // testConfig();
        setUI();
        ConfigurationHandler conf = new ConfigurationHandler();
        MainFrame mainFrame = new MainFrame(conf);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public static void setUI() {

        ArrayList<Object> gradients = new ArrayList<Object>(5);
        gradients.add(0.00f);
        gradients.add(0.00f);
        gradients.add(new Color(0xFFFFFF));
        gradients.add(new Color(0xFFFFFF));
        gradients.add(new Color(0xFFFFFF));

        UIManager.put("CheckBox.border", Color.black);
        UIManager.put("CheckBox.gradient", gradients);

        //combobox
        UIManager.put("ComboBox.selectionBackground", Color.decode("#4BA145"));
        UIManager.put("ComboBox.selectionForeground", Color.white);
        UIManager.put("ComboBox.buttonDarkShadow", Color.black);

        UIManager.put("ScrollBar.background", Color.white);
        UIManager.put("ScrollBar.thumbShadow", Color.decode(UIConstants.getMainColor()));
        UIManager.put("ScrollBar.track", Color.decode(UIConstants.getMainColor()));
        UIManager.put("ScrollBar.trackHighlight", Color.decode(UIConstants.getMainColor()));

        /*
         ScrollBar.background
         ScrollBar.foreground
         ScrollBar.thumb
         ScrollBar.thumbDarkShadow
         ScrollBar.thumbHighlight
         ScrollBar.thumbShadow
         ScrollBar.track
         ScrollBar.trackHighlight
         */
        //tabs
        UIManager.put("TabbedPane.borderColor", Color.decode("#4BA145"));
        UIManager.put("TabbedPane.foreground", Color.decode("#4BA145"));
        UIManager.put("TabbedPane.selectedForeground", Color.white);
        UIManager.put("TabbedPane.darkShadow", Color.decode("#4BA145"));
        UIManager.put("TabbedPane.light", Color.white);
        UIManager.put("TabbedPane.selectHighlight", Color.decode("#4BA145"));
        UIManager.put("TabbedPane.borderHightlightColor", Color.decode("#4BA145"));
        UIManager.put("TabbedPane.selected", Color.decode("#4BA145"));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 1, 1));

        java.util.Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new Font(UIConstants.getMainFont(), Font.PLAIN, 12));
            }
        }
    }

    private static void testConfig() {

        ConfigurationHandler conf = new ConfigurationHandler();
        List<User> profiles = conf.getProfiles();

        for (User user : profiles) {

            System.out.println("Profile: " + user.getName());

            /*  for (Game game : user.getGameModule().getGames()) {

             System.out.println(game.getName());

             for (Game game1 : game.getGames()) {
             System.out.println(game1.getName());
             }
             }*/
        }
    }
}
