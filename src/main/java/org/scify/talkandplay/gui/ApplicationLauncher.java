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
package org.scify.talkandplay.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import com.sun.javafx.application.PlatformImpl;
import io.sentry.Sentry;
import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

public class ApplicationLauncher {
    static Logger logger = Logger.getLogger(ApplicationLauncher.class);
    protected static TalkAndPlayProfileConfiguration talkAndPlayProfileConfiguration;

    public static void main(String[] args) {
        //PropertiesConfigurator is used to configure logger from properties file
        PropertyConfigurator.configure("log4j.properties");
        //Log in console in and log file
        logger.debug("Log4j appender configuration is successful !!");

        Sentry.init();
        setUI();

        String dataPath = System.getProperty("user.home") + File.separator + "Talk and Play";
        File dataDir = new File (dataPath);
        if (!dataDir.exists())
            dataDir.mkdir();

        talkAndPlayProfileConfiguration = new TalkAndPlayProfileConfiguration(dataDir);

        MainFrame mainFrame = new MainFrame();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        PlatformImpl.startup(() -> {});
        Platform.setImplicitExit(false);

    }

    /**
     * The general look and feel of the app
     */
    public static void setUI() {

        ArrayList<Object> gradients = new ArrayList<Object>(5);
        gradients.add(0.00f);
        gradients.add(0.00f);
        gradients.add(new Color(0xFFFFFF));
        gradients.add(new Color(0xFFFFFF));
        gradients.add(new Color(0xFFFFFF));

        //checkbox
        UIManager.put("CheckBox.border", Color.black);
        UIManager.put("CheckBox.gradient", gradients);

        //combobox
        UIManager.put("ComboBox.selectionBackground", Color.decode(UIConstants.green));
        UIManager.put("ComboBox.selectionForeground", Color.white);
        UIManager.put("ComboBox.buttonDarkShadow", Color.black);

        //scrollbar
        UIManager.put("ScrollBar.background", Color.white);
        UIManager.put("ScrollBar.thumbShadow", Color.decode(UIConstants.green));
        UIManager.put("ScrollBar.track", Color.decode(UIConstants.green));
        UIManager.put("ScrollBar.trackHighlight", Color.decode(UIConstants.green));

        //tabs
        UIManager.put("TabbedPane.borderColor", Color.decode(UIConstants.green));
        UIManager.put("TabbedPane.borderHightlightColor", Color.decode(UIConstants.green));
        UIManager.put("TabbedPane.foreground", Color.decode(UIConstants.green));
        UIManager.put("TabbedPane.darkShadow", Color.decode(UIConstants.green));
        UIManager.put("TabbedPane.light", Color.white);
        UIManager.put("TabbedPane.selected", Color.decode(UIConstants.green));
        UIManager.put("TabbedPane.selectHighlight", Color.decode(UIConstants.green));
        UIManager.put("TabbedPane.selectedForeground", Color.white);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 1, 1));

        //tooltip
        UIManager.put("ToolTip.foreground", Color.black);
        UIManager.put("ToolTip.background", Color.white);
        UIManager.put("ToolTip.border", new LineBorder(Color.decode(UIConstants.green), 1));

        java.util.Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new Font(UIConstants.mainFont, Font.PLAIN, 12));
            }
        }
    }
}
