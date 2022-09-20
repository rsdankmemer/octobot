package org.octobot.script.wrappers;

import org.octobot.script.Interactable;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.Validatable;

import java.awt.*;

/**
 * SceneNode
 *
 * @author Pat-ji
 */
public abstract class SceneNode<T> extends Wrapper<T> implements Validatable, Locatable, Interactable {
    protected final ScriptContext context;

    public SceneNode(final ScriptContext context, final T accessor) {
        super(accessor);

        this.context = context;
    }

    /**
     * This method is used to get the model of the {@link SceneNode}
     *
     * @return the {@link SceneNode}s model
     */
    public abstract Model getModel();

    /**
     * This method is used to check of the {@link SceneNode} is on screen, this includes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link SceneNode} is on screen
     */
    @Override
    public boolean isOnScreen() {
        final Model model = getModel();
        return model != null && model.isOnScreen();
    }

    /**
     * This method is used to check of the {@link SceneNode} is on game screen, this excludes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link SceneNode} is on game screen
     */
    @Override
    public boolean isOnGameScreen() {
        final Model model = getModel();
        return model != null && model.isOnGameScreen();
    }

    /**
     * This method is used to get the central {@link java.awt.Point} of the {@link SceneNode}
     *
     * @return the {@link SceneNode}s central {@link java.awt.Point}
     */
    @Override
    public Point getCentralPoint() {
        final Model model = getModel();
        return model != null ? model.getCentralPoint() : null;
    }

    /**
     * This method is used to hover the mouse over the {@link SceneNode}
     *
     * @return <code>true</code> when the {@link SceneNode} is successfully being hovered
     */
    @Override
    public boolean hover() {
        final Model model = getModel();
        return model != null && model.hover();
    }

    /**
     * This method is used to click on the {@link SceneNode}
     *
     * @param left true if the click should be a left, false if it should be a right click
     * @return <code>true</code> if the {@link SceneNode} has been successfully clicked
     */
    @Override
    public boolean click(final boolean left) {
        final Model model = getModel();
        return model != null && model.click(left);
    }

    /**
     * This method is used to interact with the {@link SceneNode}
     *
     * @param action what action the client should use to interact with the {@link SceneNode} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link SceneNode}
     */
    @Override
    public boolean interact(final String action) {
        final Model model = getModel();
        return model != null && model.interact(action);
    }

    /**
     * This method is used to interact with the {@link SceneNode}, mostly by option with the same interaction name, such as <code>Take</code>
     *
     * @param action what action the client should use to interact with the {@link SceneNode} as a {@link String}
     * @param option what option the client should use when interacting with the {@link SceneNode} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted witht he {@link SceneNode}
     */
    @Override
    public boolean interact(final String action, final String option) {
        final Model model = getModel();
        return model != null && model.interact(action, option);
    }

    /**
     * This method is used to check if the {@link SceneNode} is equal to this
     *
     * @param obj the object you want to compare to the {@link SceneNode}
     * @return true if the {@link SceneNode} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof SceneNode) {
            final SceneNode sceneNode = (SceneNode) obj;
            return sceneNode.getAccessor() == getAccessor();
        }

        return false;
    }

}
