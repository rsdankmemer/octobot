package org.octobot.script.util;

/**
 * Timer
 *
 * @author Pat-ji
 */
public class Timer {
    private final long period;

    private long start, end;

    public Timer(final long period) {
        this.period = period;

        this.start = System.currentTimeMillis();
        this.end = start + period;
    }

    /**
     * This method is used to get the start time
     *
     * @return the start time
     */
    public long getStart() {
        return start;
    }

    /**
     * This method is used to get the end time
     *
     * @return the end time
     */
    public long getEnd() {
        return end;
    }

    /**
     * This method is used to get the elapsed time
     *
     * @return the elapsed time
     */
    public long getElapsed() {
        return System.currentTimeMillis() - start;
    }

    /**
     * This method is used to get the remaining time
     *
     * @return the remaining time
     */
    public long getRemaining() {
        return isRunning() ? end - System.currentTimeMillis() : 0;
    }

    /**
     * This method is used to check if the {@link Timer} is running
     *
     * @return <code>true</code> if the {@link Timer} is running
     */
    public boolean isRunning() {
        return System.currentTimeMillis() < end;
    }

    /**
     * This method is used to reset the {@link Timer}
     */
    public void reset() {
        if (period == 0) {
            start = System.currentTimeMillis();
        } else {
            end = System.currentTimeMillis() + period;
        }
    }

    /**
     * This method is used to get the formatted elapsed time
     *
     * @return the formatted elapsed time
     */
    public String toElapsedString() {
        return Time.format(getElapsed());
    }

    /**
     * This method is used to get the formatted remaining time
     *
     * @return the formatted remaining time
     */
    public String toRemainingString() {
        return Time.format(getRemaining());
    }

}
