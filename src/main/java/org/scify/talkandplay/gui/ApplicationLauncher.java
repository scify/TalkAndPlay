package org.scify.talkandplay.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import org.scify.talkandplay.gui.helpers.UIConstants;

public class ApplicationLauncher {

    public static void main(String[] args) {

        setUI();
        MainFrame mainFrame = new MainFrame();
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
        UIManager.put("ComboBox.selectionBackground", Color.decode(UIConstants.green));
        UIManager.put("ComboBox.selectionForeground", Color.white);
        UIManager.put("ComboBox.buttonDarkShadow", Color.black);

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

        UIManager.put("ToolTip.foreground", Color.black);
        UIManager.put("ToolTip.background", Color.white);
        UIManager.put("ToolTip.border", new LineBorder( Color.decode(UIConstants.green), 1));

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
