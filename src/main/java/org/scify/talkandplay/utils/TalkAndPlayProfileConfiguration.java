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
package org.scify.talkandplay.utils;

import java.io.File;

public class TalkAndPlayProfileConfiguration {

    protected XMLConfigurationHandler xmlConfigurationHandler;
    protected File dataDir;
    protected LoginManager loginManager;
    protected static TalkAndPlayProfileConfiguration instance;

    public boolean isShapesMode() {
        return shapesMode;
    }

    protected boolean shapesMode;

    public TalkAndPlayProfileConfiguration(File dataDir, ParametersFromRestAPI parametersFromRestAPI) {
        this.dataDir = dataDir;
        loginManager = new ShapesLoginManager();
        shapesMode = ((ShapesLoginManager)loginManager).isInShapesMode();
        if (shapesMode && parametersFromRestAPI != null) {
            ((ShapesLoginManager) loginManager).setSignInUrl(parametersFromRestAPI.shapesSignInUrl);
            ((ShapesLoginManager) loginManager).setSignUpUrl(parametersFromRestAPI.shapesSignUpUrl);
            ((ShapesLoginManager) loginManager).setXShapesKey(parametersFromRestAPI.shapesKey);

        }
        xmlConfigurationHandler = null;
        instance = this;
    }

    public boolean isInShapesMode() {
        return shapesMode;
    }
    
    public XMLConfigurationHandler getConfigurationHandler() {
        if (xmlConfigurationHandler == null)
            xmlConfigurationHandler = new XMLConfigurationHandler(dataDir, "");
        return xmlConfigurationHandler;
    }

    public XMLConfigurationHandler getConfigurationHandler(String userOfAccount) {
        if (xmlConfigurationHandler == null)
            xmlConfigurationHandler = new XMLConfigurationHandler(dataDir, userOfAccount);
        return xmlConfigurationHandler;
    }

    public static TalkAndPlayProfileConfiguration getInstance() {
        return instance;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }
}
