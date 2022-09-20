package org.octobot.script;

/**
 * Condition
 *
 * @author Pat-ji
 */
public interface Condition {

    /**
     * This method is used to check if the {@link Condition} is valid
     *
     * @return <code>true</code> if the {@link Condition} is valid
     */
    public boolean validate();

}
