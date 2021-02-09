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

import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;

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
    private ImageResource imageResource;
    private SoundResource soundResource;
    private boolean enabled;
    protected ResourceManager rm;

    public Module() {
        this.rm = ResourceManager.getInstance();
    }

    public String getName() {
        return rm.decodeTextIfRequired(name);
    }

    public String getNameUnmodified() {
        return name;
    }

    public boolean isAltered(Module module) {
        if (!name.equals(module.name))
            return true;
        if ((imageResource != null && module.imageResource != null && imageResource.isAltered(module.imageResource)) ||
                (imageResource != null && module.imageResource == null) ||
                (imageResource == null && module.imageResource != null))
            return true;

        if ((soundResource != null && module.soundResource != null && soundResource.isAltered(module.soundResource)) ||
                (soundResource != null && module.soundResource == null) ||
                (soundResource == null && module.soundResource != null))
            return true;
        if (enabled != module.enabled)
            return true;
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageResource getImageResource() {
        return imageResource;
    }

    public void setImage(ImageResource imageResource) {
        this.imageResource = imageResource;
    }

    public SoundResource getSoundResource() {
        return soundResource;
    }

    public void setSound(SoundResource soundResource) {
        this.soundResource = soundResource;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
