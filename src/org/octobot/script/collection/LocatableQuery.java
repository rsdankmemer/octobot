package org.octobot.script.collection;

import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Tile;

import java.util.Comparator;

/**
 * LocatableQuery
 *
 * @author Pat-ji
 */
@SuppressWarnings("unchecked")
public abstract class LocatableQuery<T extends Locatable, E extends LocatableQuery> extends Query<T, E> {

    public LocatableQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to add a location {@link Filter} to the {@link Query}
     *
     * @param locatable the {@link Locatable} to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for the location
     */
    public E at(final Locatable locatable) {
        return at(locatable.getLocation());
    }

    /**
     * This method is used to add a location {@link Filter} to the {@link Query}
     *
     * @param tile the {@link Tile} to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for the location
     */
    public E at(final Tile tile) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return tile.equals(t.getLocation());
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a range {@link Filter} to the {@link Query}
     *
     * @param range the range to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for range
     */
    public E range(final double range) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return t.getLocation().distance(context().players.getLocal().getLocation()) <= range;
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a range {@link Filter} to the {@link Query}
     *
     * @param locatable the {@link Locatable} to apply the {@link Filter} to
     * @param range the range to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for range
     */
    public E range(final Locatable locatable, final double range) {
        return range(locatable.getLocation(), range);
    }

    /**
     * This method is used to add a range {@link Filter} to the {@link Query}
     *
     * @param tile the {@link Tile} to apply the {@link Filter} to
     * @param range the range to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for range
     */
    public E range(final Tile tile, final double range) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return tile.distance(t) <= range;
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a nearest {@link java.util.Comparator} to the {@link Query}
     *
     * @return the same {@link Query} with an extra {@link java.util.Comparator} for nearest
     */
    public E nearest() {
        return nearest(context().players.getLocal().getLocation());
    }

    /**
     * This method is used to add a nearest {@link java.util.Comparator} to the {@link Query}
     *
     * @param locatable the {@link Locatable} to compare to
     * @return the same {@link Query} with an extra {@link java.util.Comparator} for nearest
     */
    public E nearest(final Locatable locatable) {
        return nearest(locatable.getLocation());
    }

    /**
     * This method is used to add a nearest {@link java.util.Comparator} to the {@link Query}
     *
     * @param tile the {@link Tile} to compare to
     * @return the same {@link Query} with an extra {@link java.util.Comparator} for nearest
     */
    public E nearest(final Tile tile) {
        compare(new Comparator<T>() {
            @Override
            public int compare(final T o1, final T o2) {
                return (int) (Math.round(tile.distance(o1)) - Math.round(tile.distance(o2)));
            }
        });
        return (E) this;
    }

}
