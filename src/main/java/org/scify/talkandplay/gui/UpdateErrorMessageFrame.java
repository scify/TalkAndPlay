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

/**
 *
 * @author christina
 */
public class UpdateErrorMessageFrame extends JFrame {

    public UpdateErrorMessageFrame() {
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel();
        messageLabel.setText("Συνέβη ένα λάθος κατά την ενημέρωση της εφαρμογής.");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.getContentPane().add(messageLabel);

        JLabel messageLabel2 = new JLabel();
        messageLabel2.setText("Για να συνεχίσετε στην εφαρμογή, παρακαλώ πατήστε ΟΚ.");
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

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 528, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 287, Short.MAX_VALUE)
        );

        pack();
    }
}
