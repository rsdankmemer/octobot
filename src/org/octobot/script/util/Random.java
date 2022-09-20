package org.octobot.script.util;

/**
 * Random
 *
 * @author Pat-ji
 */
public class Random {
    private static final java.util.Random RANDOM = new java.util.Random();

    /**
     * This method is used to get a random boolean
     *
     * @return a random boolean
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * This method is used to get a random int
     *
     * @param min the minimum value to get
     * @param max the maximum value to get
     * @return a random int between min and max
     */
    public static int nextInt(final int min, final int max) {
        if (max < min) {
            return max + RANDOM.nextInt(min - max);
        }

        return min + (max == min ? 0 : RANDOM.nextInt(max - min));
    }

    /**
     * This method is used to get a random double
     *
     * @param min the minimum value to get
     * @param max the maximum value to get
     * @return a random double between min and max
     */
    public static double nextDouble(final double min, final double max) {
        return min + RANDOM.nextDouble() * (max - min);
    }

    /**
     * This method is used to get a random float
     *
     * @param min the minimum value to get
     * @param max the maximum value to get
     * @return a random float between min and max
     */
    public static float nextFloat(final float min, final float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }

}
