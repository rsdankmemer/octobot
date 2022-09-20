package org.octobot.script.methods;

import org.octobot.bot.game.client.RSNPC;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.collection.NPCQuery;
import org.octobot.script.wrappers.NPC;
import org.octobot.script.wrappers.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * NPCs
 *
 * @author Pat-ji
 */
public class NPCs extends ContextProvider {
    public static final Filter<NPC> ALL_FILTER = new Filter<NPC>() {
        @Override
        public boolean accept(final NPC npc) {
            return true;
        }
    };

    public NPCs(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param ids the id of the {@link NPC}s to get
     * @return a {@link NPC} with a specific id
     */
    public NPC getNearest(final int... ids) {
        return getNearest(context().players.getLocal(), ids);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param locatable the {@link Locatable} to get the nearest {@link NPC} at
     * @param ids the id of the {@link NPC}s to get
     * @return a {@link NPC} with a specific id
     */
    public NPC getNearest(final Locatable locatable, final int... ids) {
        return getNearest(locatable.getLocation(), ids);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param tile the {@link Tile} to get the nearest {@link NPC} at
     * @param ids the id of the {@link NPC}s to get
     * @return a {@link NPC} with a specific id
     */
    public NPC getNearest(final Tile tile, final int... ids) {
        return getNearest(tile, new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(ids, npc.getId());
            }
        });
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param names the name of the {@link NPC}s to get
     * @return a {@link NPC} with a specific name
     */
    public NPC getNearest(final String... names) {
        return getNearest(context().players.getLocal(), names);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param locatable the {@link Locatable} to get the nearest {@link NPC} at
     * @param names the name of the {@link NPC}s to get
     * @return a {@link NPC} with a specific name
     */
    public NPC getNearest(final Locatable locatable, final String... names) {
        return getNearest(locatable.getLocation(), names);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param tile the {@link Tile} to get the nearest {@link NPC} at
     * @param names the name of the {@link NPC}s to get
     * @return a {@link NPC} with a specific name
     */
    public NPC getNearest(final Tile tile, final String... names) {
        return getNearest(tile, new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(names, npc.getName());
            }
        });
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param locatable the {@link Locatable} to get the nearest {@link NPC} at
     * @return a {@link NPC} nearest to the {@link Locatable}
     */
    public NPC getNearest(final Locatable locatable) {
        return getNearest(locatable.getLocation());
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param tile the {@link Tile} to get the nearest {@link NPC} at
     * @return a {@link NPC} nearest to the {@link Tile}
     */
    public NPC getNearest(final Tile tile) {
        return getNearest(tile, ALL_FILTER);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link NPC} selected by a {@link Filter}
     */
    public NPC getNearest(final Filter<NPC> filter) {
        return getNearest(context().players.getLocal().getLocation(), filter);
    }

    /**
     * This method is used to get a {@link NPC}
     *
     * @param tile the {@link Tile} to get the nearest {@link NPC} at
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link NPC} selected by a {@link Filter} nearest to the {@link Tile}
     */
    public NPC getNearest(final Tile tile, final Filter<NPC> filter) {
        final NPC[] loaded = getLoaded(filter);
        if (loaded == null || loaded.length < 1) return null;

        NPC result = null;
        double dist = Float.MAX_VALUE;
        for (final NPC object : loaded) {
            final double ddist = object.getLocation().distance(tile);
            if (ddist < dist) {
                dist = ddist;
                result = object;
            }
        }

        return result;
    }

    /**
     * This method is used to get all loaded {@link NPC}s
     *
     * @param ids the ids of the {@link NPC}s to get
     * @return an array with all loaded {@link NPC}s with specific ids
     */
    public NPC[] getLoaded(final int... ids) {
        return getLoaded(new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(ids, npc.getId());
            }
        });
    }

    /**
     * This method is used to get all loaded {@link NPC}s
     *
     * @param names the names of the {@link NPC}s to get
     * @return an array with all loaded {@link NPC}s with specific names
     */
    public NPC[] getLoaded(final String... names) {
        return getLoaded(new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(names, npc.getName());
            }
        });
    }

    /**
     * This method is used to get all loaded {@link NPC}s
     *
     * @return an array with all loaded {@link NPC}s
     */
    public NPC[] getLoaded() {
        return getLoaded(ALL_FILTER);
    }

    /**
     * This method is used to get all loaded {@link NPC}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an array with all loaded {@link NPC}s that are accepted by the {@link Filter}
     */
    public NPC[] getLoaded(final Filter<NPC> filter) {
        final List<NPC> result = new ArrayList<NPC>();
        for (final RSNPC index : context().client.getNpcArray()) {
            if (index == null) continue;

            final NPC npc = new NPC(context(), index);
            if (filter.accept(npc))
                result.add(npc);
        }

        return result.toArray(new NPC[result.size()]);
    }

    /**
     * This method is used to create a new {@link NPCQuery}
     *
     * @return a new {@link NPCQuery}
     */
    public final NPCQuery find() {
        return new NPCQuery(context());
    }

}
