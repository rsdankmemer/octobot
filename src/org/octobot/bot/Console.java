package org.octobot.bot;

import org.octobot.bot.internal.ui.component.ConsolePanel;
import org.octobot.bot.internal.ui.component.UtilityBar;

import java.awt.*;
import java.io.PrintStream;

/**
 * Console
 *
 * @author Pat-ji
 */
public class Console {

    /**
     * ConsoleStream
     *
     * @author Pat-ji
     */
    private static class ConsoleStream extends PrintStream {

        public ConsoleStream(final PrintStream stream) {
            super(stream);
        }

        @Override
        public void println(final boolean value) {
            println("" + value);
        }

        @Override
        public void println(final char value) {
            println("" + value);
        }

        @Override
        public void println(final int value) {
            println("" + value);
        }

        @Override
        public void println(final long value) {
            println("" + value);
        }

        @Override
        public void println(final float value) {
            println("" + value);
        }

        @Override
        public void println(final double value) {
            println("" + value);
        }

        @Override
        public void println(final Object object) {
            println(String.valueOf(object));
        }

    }

    /**
     * OutStream
     *
     * @author Pat-ji
     */
    public static class OutStream extends ConsoleStream {

        public OutStream() {
            super(System.out);
        }

        @Override
        public void println(final String text) {
            println(text, Color.BLACK);
        }

        public void println(final String text, final Color color) {
            super.println(text);

            UtilityBar.instance.statusLabel.setText(text);
            ConsolePanel.instance.println(text, color);
        }

    }

    /**
     * ErrStream
     *
     * @author Pat-ji
     */
    public static class ErrStream extends ConsoleStream {

        public ErrStream() {
            super(System.err);
        }

        @Override
        public void println(final String text) {
            ((OutStream) System.out).println(text, Color.RED);
        }

    }

}
