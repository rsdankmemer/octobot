package org.octobot.script.methods;

import org.octobot.bot.game.client.RSPlayer;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.collection.PlayerQuery;
import org.octobot.script.wrappers.Player;
import org.octobot.script.wrappers.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Players
 *
 * @author Pat-ji
 */
public class Players extends ContextProvider {
    public static final Filter<Player> ALL_FILTER = new Filter<Player>() {
        @Override
        public boolean accept(final Player player) {
            return true;
        }
    };

    private Player local;

    public Players(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the local player index
     *
     * @return the local player index
     */
    public int getLocalPlayerIndex() {
        return context().client.getLocalPlayerIndex();
    }

    /**
     * This method is used to get the local {@link Player}
     *
     * @return the local {@link Player}
     */
    public Player getLocal() {
        if (local != null && context().client.getLocalPlayer().equals(local.getAccessor())) return local;

        return (local = new Player(context(), context().client.getLocalPlayer()));
    }

    /**
     * This method is used to get a {@link Player}
     *
     * @param name the name of the {@link Player} to get
     * @return a {@link Player} with a specific name
     */
    public Player getNearest(final String name) {
        return getNearest(getLocal().getLocation(), new Filter<Player>() {
            @Override
            public boolean accept(final Player player) {
                return name.equals(player.getName());
            }
        });
    }

    /**
     * This method is used to get a {@link Player}
     *
     * @param locatable the {@link Locatable} to get the nearest {@link Player} at
     * @return a {@link Player} nearest to the {@link Locatable}
     */
    public Player getNearest(final Locatable locatable) {
        return getNearest(locatable.getLocation());
    }

    /**
     * This method is used to get a {@link Player}
     *
     * @param tile the {@link Tile} to get the nearest {@link Player} at
     * @return a {@link Player} nearest to the {@link Tile}
     */
    public Player getNearest(final Tile tile) {
        return getNearest(tile, ALL_FILTER);
    }

    /**
     * This method is used to get a {@link Player}
     *
     * @param tile the {@link Tile} to get the nearest {@link Player} at
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link Player} selected by a {@link Filter} nearest to the {@link Tile}
     */
    public Player getNearest(final Tile tile, final Filter<Player> filter) {
        final Comparator<Player> comparator = new Comparator<Player>() {
            @Override
            public int compare(final Player o1, final Player o2) {
                return (int) (tile.distance(o1) - tile.distance(o2));
            }
        };

        final List<Player> list = new ArrayList<Player>();
        Collections.addAll(list, getLoaded(filter));
        if (list.size() > 0) {
            Collections.sort(list, comparator);
            return list.get(0);
        }

        return null;
    }

    /**
     * This method is used to get all loaded {@link Player}s
     *
     * @return an array with all loaded {@link Player}s
     */
    public Player[] getLoaded() {
        return getLoaded(ALL_FILTER);
    }

    /**
     * This method is used to get all loaded {@link Player}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an array with all loaded {@link Player}s that are accepted by the {@link Filter}
     */
    public Player[] getLoaded(final Filter<Player> filter) {
        final List<Player> result = new ArrayList<Player>();
        for (final RSPlayer index : context().client.getPlayerArray()) {
            if (index == null) continue;

            final Player player = new Player(context(), index);
            if (filter.accept(player))
                result.add(player);
        }

        return result.toArray(new Player[result.size()]);
    }

    /**
     * This method is used to create a new {@link PlayerQuery}
     *
     * @return a new {@link PlayerQuery}
     */
    public final PlayerQuery find() {
        return new PlayerQuery(context());
    }

}
