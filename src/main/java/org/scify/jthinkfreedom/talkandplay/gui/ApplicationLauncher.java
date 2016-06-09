package org.scify.jthinkfreedom.talkandplay.gui;

import java.awt.Font;
import java.io.IOException;
import java.util.List;
import javax.swing.UIManager;
import org.scify.jthinkfreedom.talkandplay.models.Category;
import org.scify.jthinkfreedom.talkandplay.models.User;
import org.scify.jthinkfreedom.talkandplay.utils.ConfigurationHandler;

/**
 *
 * @author peustr
 */
public class ApplicationLauncher {

    public static void main(String[] args){
        
        setUIFont(new javax.swing.plaf.FontUIResource("DejaVu Sans", Font.PLAIN, 12));
        ConfigurationHandler conf = new ConfigurationHandler();
        MainFrame mainFrame = new MainFrame(conf);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {

        java.util.Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private void testConfig() {

        ConfigurationHandler conf = new ConfigurationHandler();
        List<User> profiles = conf.getProfiles();

        for (User user : profiles) {

            System.out.println("Profile: " + user.getName());

            for (Category cat : user.getCommunicationModule().getCategories()) {
                System.out.println("cat name:" + cat.getName());
            }
        }
    }
}
