package org.octobot.script.wrappers;

import org.octobot.script.Condition;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;

/**
 * Path
 *
 * @author Pat-ji
 */
public class Path implements Condition {
    protected final ScriptContext context;

    protected Tile[] tiles;
    protected Tile end, next;

    protected boolean run;

    public Path(final ScriptContext context, final Tile[] tiles) {
        this.context = context;

        this.tiles = tiles;
        this.end = tiles != null && tiles.length > 0 ? tiles[tiles.length - 1] : null;

        run = true;
    }

    /**
     * This method is used to get the {@link Path}s next {@link Tile}
     *
     * @return the {@link Path}s next {@link Tile}
     */
    public Tile getNext() {
        final Player local = context.players.getLocal();
        if (tiles == null || local == null) return null;

        final Tile location = local.getLocation();
        if (location == null) return null;

        for (int i = tiles.length - 1; i >= 0; i--)
            if (tiles[i] != null && (tiles[i].getMatrix(context).isOnMiniMap() || tiles[i].distance(location) < 20)) return tiles[i];

        return null;
    }

    /**
     * This method is used to get the {@link Path}s {@link Tile}s
     *
     * @return the {@link Path}s {@link Tile}s
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     * This method is used to get the {@link Path}s end {@link Tile}
     *
     * @return the {@link Path}s end {@link Tile}
     */
    public Tile getEnd() {
        return end;
    }

    /**
     * This method is used to check if the {@link Path} is traversable
     *
     * @return <code>true</code> if the {@link Path} is traversable
     */
    @Override
    public boolean validate() {
        return end != null && end.distance(context.players.getLocal()) > 5 && (next = getNext()) != null;
    }

    /**
     * This method is used to set the running enabled during traversal flag
     *
     * @param enabled use <code>true</code> to enable rhe flag
     */
    public void setRunEnabled(final boolean enabled) {
        run = enabled;
    }

    /**
     * This method is used to traverse the {@link Path}
     *
     * @return <code>true</code> if successfully traversed to the next {@link Tile} in the {@link Path}
     */
    public boolean traverse() {
        return traverse(1);
    }

    /**
     * This method is used to traverse the {@link Path}
     *
     * @param range the maximum range to randomize the next {@link Tile} with
     * @return <code>true</code> if successfully traversed to the next {@link Tile} in the {@link Path}
     */
    public boolean traverse(final int range) {
        if (run && !context.movement.isRunEnabled() && context.movement.getEnergy() > 50 && context.widgets.getClosableWidget() == null) {
            if (context.movement.setRun(true))
                Time.sleep(400, 600);

            return traverse(range);
        }

        if (next == null)
            next = getNext();

        if (next != null && range > 0)
            next = next.randomize(range);

        return context.movement.walkTileOnMap(next);
    }

    /**
     * This method is used to get a reversed {@link Path}
     *
     * @return a reversed {@link Path} with this data
     */
    public Path reverse() {
        if (tiles == null) return this;

        final Tile[] reversed = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; ++i)
            reversed[i] = tiles[tiles.length - 1 - i];

        end = tiles[0];
        tiles = reversed;
        return this;
    }

}
