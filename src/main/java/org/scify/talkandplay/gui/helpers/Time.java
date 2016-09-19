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
package org.scify.talkandplay.gui.helpers;

/**
 * Helper class to get and format the media times
 *
 * @author christina
 */
public final class Time {

    /**
     * Get the time based on the given data
     *
     * @return
     */
    public static String getTime(int hrs, int mins, int secs) {

        String seconds, minutes, hours;
        if (secs < 10) {
            seconds = "0" + String.valueOf(secs);
        } else {
            seconds = String.valueOf(secs);
        }
        if (mins < 10) {
            minutes = "0" + String.valueOf(mins);
        } else {
            minutes = String.valueOf(mins);
        }
        if (hrs < 10) {
            hours = "0" + String.valueOf(hrs);
        } else {
            hours = String.valueOf(hrs);
        }
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * Code by vlcj
     *
     * @param value
     * @return
     */
    public static String formatTime(long value) {
        value /= 1000;
        int hours = (int) value / 3600;
        int remainder = (int) value - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int seconds = remainder;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }


}
