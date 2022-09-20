package org.octobot.script.event.listeners;

import java.awt.*;

/**
 * PaintListener
 *
 * @author Pat-ji
 */
public interface PaintListener {

    /**
     * This method is used to render a {@link PaintListener}
     *
     * @param g the {@link java.awt.Graphics} to render with
     */
    public void render(final Graphics g);

}
