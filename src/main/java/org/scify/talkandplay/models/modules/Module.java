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
package org.scify.talkandplay.models.modules;

import java.net.URL;

/**
 * The Module class represents the information of an executable module (function)
 * of TalkNPlay. The basic information related to the name, the representative
 * image and the representative sound indicative of a module.
 * Essentially the Module class allows serialization of the configuration data 
 * of a module.
 * <i>(This is not to be mixed with the Configuration class.)</i>
 *
 * Subclasses should specialize by adding subclass-specific configuration data.
 * @author snik
 */
public class Module {

    private String name;
    private String image;
    private String sound;
    private boolean enabled;

    //used only to display default ImageIcons from the app jar
    private URL imageURL;

    public Module() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }

}
