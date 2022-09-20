package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * PlayerQuery
 *
 * @author Pat-ji
 */
public class PlayerQuery extends ActorQuery<Player, PlayerQuery> {

    public PlayerQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get all the {@link Player}s accepted by this {@link Query}
     *
     * @return all the {@link Player}s accepted by this {@link Query}
     */
    @Override
    public Player[] all() {
        final Filter<Player> filter = new Filter<Player>() {
            @Override
            public boolean accept(final Player player) {
                for (final Filter<Player> filter : filters)
                    if (!filter.accept(player)) return false;

                return true;
            }
        };

        final List<Player> list = new ArrayList<Player>();
        Collections.addAll(list, context().players.getLoaded(filter));
        if (list.size() > 0 && comparators.size() > 0)
            for (final Comparator<Player> comparator : comparators)
                Collections.sort(list, comparator);

        return list.toArray(new Player[list.size()]);
    }

}
