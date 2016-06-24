package org.scify.talkandplay.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.List;
import javax.swing.UIManager;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ConfigurationHandler;

/**
 *
 * @author peustr
 */
public class ApplicationLauncher {

    public static void main(String[] args) {

        // testConfig();
        setUIFont(new javax.swing.plaf.FontUIResource("DejaVu Sans", Font.PLAIN, 12));
        ConfigurationHandler conf = new ConfigurationHandler();
        MainFrame mainFrame = new MainFrame(conf);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {

        UIManager.put("TabbedPane.borderColor", Color.decode("#4BA145"));
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
                UIManager.put(key, f);
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
