package org.octobot.bot.game.loader.internal.wrappers.input;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.game.script.Task;
import org.octobot.bot.game.script.TaskScript;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Mouse
 *
 * @author Pat-ji
 */
public abstract class Mouse implements Input, MouseListener, MouseMotionListener {
    private final GameDefinition definition;
    private final Point location;

    private long clickTime;

    public Mouse() {
        definition = context.getBot(this);
        definition.mouse = this;
        definition.context.build(definition);

        location = new Point(0, 0);
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (!definition.disableInput)
            _mouseClicked(e);

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseListener)
                ((MouseListener) script).mouseClicked(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseListener)
                        ((MouseListener) task).mouseClicked(e);
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        if (!definition.disableInput)
            _mouseEntered(e);

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseListener)
                ((MouseListener) script).mouseEntered(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseListener)
                        ((MouseListener) task).mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        if (!definition.disableInput)
            _mouseExited(e);

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseListener)
                ((MouseListener) script).mouseExited(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseListener)
                        ((MouseListener) task).mouseExited(e);
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (!definition.disableInput) {
            clickTime = System.currentTimeMillis();
            _mousePressed(e);
        }

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseListener)
                ((MouseListener) script).mousePressed(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseListener)
                        ((MouseListener) task).mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (!definition.disableInput)
            _mouseReleased(e);

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseListener)
                ((MouseListener) script).mouseReleased(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseListener)
                        ((MouseListener) task).mouseReleased(e);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (!definition.disableInput) {
            location.setLocation(e.getX(), e.getY());
            _mouseDragged(e);
        }

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseMotionListener)
                ((MouseMotionListener) script).mouseDragged(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseMotionListener)
                        ((MouseMotionListener) task).mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        if (!definition.disableInput) {
            location.setLocation(e.getX(), e.getY());
            _mouseMoved(e);
        }

        final GameScript script = definition.scriptHandler.getScript();
        if (script != null) {
            if (script instanceof MouseMotionListener)
                ((MouseMotionListener) script).mouseMoved(e);

            if (script instanceof TaskScript)
                for (final Task task : ((TaskScript) script).getTasks())
                    if (task instanceof MouseMotionListener)
                        ((MouseMotionListener) task).mouseMoved(e);
        }
    }

    public void sendEvent(final MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                _mouseClicked(e);
                break;
            case MouseEvent.MOUSE_PRESSED:
                clickTime = System.currentTimeMillis();
                _mousePressed(e);
                break;
            case MouseEvent.MOUSE_RELEASED:
                _mouseReleased(e);
                break;
            case MouseEvent.MOUSE_MOVED:
                location.setLocation(e.getX(), e.getY());
                _mouseMoved(e);
                break;
        }
    }

    public long getClickTime() {
        return clickTime;
    }

    public abstract void _mouseClicked(final MouseEvent event);

    public abstract void _mouseEntered(final MouseEvent event);

    public abstract void _mouseExited(final MouseEvent event);

    public abstract void _mousePressed(final MouseEvent event);

    public abstract void _mouseReleased(final MouseEvent event);

    public abstract void _mouseDragged(final MouseEvent event);

    public abstract void _mouseMoved(final MouseEvent event);

}