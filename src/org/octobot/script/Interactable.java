package org.octobot.script;

import java.awt.*;

/**
 * Interactable
 *
 * @author Pat-ji
 */
public interface Interactable {

    /**
     * This method is used to check of the {@link Interactable} is on screen, this includes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link Interactable} is on screen
     */
    public boolean isOnScreen();

    /**
     * This method is used to check if the {@link Interactable} is on the game screen, this excludes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link Interactable} is on the game screen
     */
    public boolean isOnGameScreen();

    /**
     * This method is used to get the central {@link java.awt.Point} of the {@link Interactable}
     *
     * @return the {@link Interactable}s central {@link java.awt.Point}
     */
    public Point getCentralPoint();

    /**
     * This method is used to hover the mouse over the {@link Interactable}
     *
     * @return <code>true</code> when the {@link Interactable} is successfully being hovered
     */
    public boolean hover();

    /**
     * This method is used to click on the {@link Interactable}
     *
     * @param left true if the click should be a left, false if it should be a right click
     * @return <code>true</code> if the {@link Interactable} has been successfully clicked
     */
    public boolean click(final boolean left);

    /**
     * This method is used to interact with the {@link Interactable}
     *
     * @param action what action the client should use to interact with the {@link Interactable} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link Interactable}
     */
    public boolean interact(final String action);

    /**
     * This method is used to interact with the {@link Interactable}, mostly by option with the same interaction name, such as <code>Take</code>
     *
     * @param action what action the client should use to interact with the {@link Interactable} as a {@link String}
     * @param option what option the client should use when interacting with the {@link Interactable} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted witht he {@link Interactable}
     */
    public boolean interact(final String action, final String option);

}
