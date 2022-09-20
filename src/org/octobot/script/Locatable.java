package org.octobot.script;

import org.octobot.script.wrappers.Tile;

/**
 * Locatable
 *
 * @author Pat-ji
 */
public interface Locatable {

    /**
     * Gets the {@link Locatable}s location
     *
     * @return the {@link Locatable}s location as a {@link Tile}
     */
    public Tile getLocation();

}
