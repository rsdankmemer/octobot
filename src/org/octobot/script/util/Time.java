package org.octobot.script.util;

import org.octobot.script.Condition;

/**
 * Time
 *
 * @author Pat-ji
 */
public class Time {

    /**
     * This method is used to let the {@link Thread} sleep
     *
     * @param time the time for the {@link Thread} to sleep in milliseconds
     */
    public static void sleep(final int time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException ignored) { }
    }

    /**
     * This method is used to let the {@link Thread} sleep
     *
     * @param min the minimum time for the {@link Thread} to sleep in milliseconds
     * @param max the maximum time for the {@link Thread} to sleep in milliseconds
     */
    public static void sleep(final int min, final int max) {
        sleep(Random.nextInt(min, max));
    }

    /**
     * This method is used to let the {@link Thread} sleep
     *
     * @param condition the {@link Condition} to let the {@link Thread} sleep for
     * @param time the maximum time for the {@link Thread} to sleep in milliseconds
     */
    public static void sleep(final Condition condition, final int time) {
        try {
            final long start = System.currentTimeMillis();
            while (condition.validate() && (System.currentTimeMillis() - start) < time) {
                sleep(100);
            }
        } catch (final Exception ignored) { }
    }

    /**
     * This method is used to format the time
     *
     * @param time the time in milliseconds to format
     * @return a formatted time in HH:MM:SS
     */
    public static String format(final long time) {
        final StringBuilder builder = new StringBuilder();
        final long totalSeconds = time / 1000;
        final long totalMinutes = totalSeconds / 60;
        final int hours = (int) (totalMinutes / 60) % 60;
        if (hours < 10)
            builder.append("0");

        builder.append(hours);

        final int minutes = (int) totalMinutes % 60;
        builder.append(":");
        if (minutes < 10)
            builder.append("0");

        builder.append(minutes);

        final int seconds = (int) totalSeconds % 60;
        builder.append(":");
        if (seconds < 10)
            builder.append("0");

        builder.append(seconds);
        return builder.toString();
    }

}
