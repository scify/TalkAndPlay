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
/*
 * Copyright 2016 scify.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author christina
 */
public class WindowsAdminMessageFrame extends javax.swing.JFrame {

    public WindowsAdminMessageFrame() {
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel();
        messageLabel.setText("Υπάρχει διαθέσιμη νέα ενημέρωση για την εφαρμογή.");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.getContentPane().add(messageLabel);

        JLabel messageLabel2 = new JLabel();
        messageLabel2.setText("<html><p style=\"text-align: justify;  text-justify: inter-word; text-align: center\">Για να λάβετε την ενημέρωση, παρακαλώ βγείτε από την εφαρμογή, και εκκινήστε την πάλι σαν Διαχερισιτής (Administrator).</p></html>");
        messageLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel2.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.getContentPane().add(messageLabel2);

        JButton okBtn = new JButton("OK");
        okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        okBtn.setPreferredSize(new Dimension(50, 30));
        okBtn.addActionListener(e -> this.dispose());
        this.getContentPane().add(okBtn);
    }

    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 528, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 287, Short.MAX_VALUE)
        );

        pack();
    }
}
