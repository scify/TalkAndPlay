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

import java.util.List;
import org.jdom.Element;
import org.scify.talkandplay.models.User;

// singleton class. There exists only on configuration on the app
// that holds the user information.
// its basically a wrapper of the Configuration handler.
public class TalkAndPlayProfileConfiguration {

    protected XMLConfigurationHandler xmlConfigurationHandler;

    private static TalkAndPlayProfileConfiguration instance = new TalkAndPlayProfileConfiguration();

    protected TalkAndPlayProfileConfiguration() {
        xmlConfigurationHandler = new XMLConfigurationHandler();

    }
    
    public XMLConfigurationHandler getConfigurationHandler()
    {
        return xmlConfigurationHandler;
    }

    public static TalkAndPlayProfileConfiguration getInstance() {
        return instance;
    }
}
