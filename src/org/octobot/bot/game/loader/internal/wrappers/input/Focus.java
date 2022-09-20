package org.octobot.bot.game.loader.internal.wrappers.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Focus
 *
 * @author Pat-ji
 */
public abstract class Focus implements FocusListener {

    @Override
    public void focusGained(final FocusEvent e) {
        _focusGained(e);
    }

    @Override
    public void focusLost(final FocusEvent e) {
        _focusLost(e);
    }

    public abstract void _focusGained(final FocusEvent e);

    public abstract void _focusLost(final FocusEvent e);

}
