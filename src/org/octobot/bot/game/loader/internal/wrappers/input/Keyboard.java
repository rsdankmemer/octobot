package org.octobot.bot.game.loader.internal.wrappers.input;

import org.octobot.bot.GameDefinition;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard
 *
 * @author Pat-ji
 */
public abstract class Keyboard extends Focus implements Input, KeyListener {

    public Keyboard() {
        final GameDefinition definition = context.getBot(this);
        definition.keyboard = this;
        definition.context.build(definition);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        _keyPressed(e);
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        _keyReleased(e);
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        _keyTyped(e);
    }

    public abstract void _keyPressed(final KeyEvent e);

    public abstract void _keyReleased(final KeyEvent e);

    public abstract void _keyTyped(final KeyEvent e);

}