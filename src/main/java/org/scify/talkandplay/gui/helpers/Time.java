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
