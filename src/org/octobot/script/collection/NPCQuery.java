package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.NPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * NPCQuery
 *
 * @author Pat-ji
 */
public class NPCQuery extends ActorQuery<NPC, NPCQuery> {

    public NPCQuery(ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to add a id {@link Filter} to the {@link Query}
     *
     * @param ids the ids to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for ids
     */
    public NPCQuery ids(final int... ids) {
        filter(new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(ids, npc.getId());
            }
        });
        return this;
    }

    /**
     * This method is used to add a action {@link Filter} to the {@link Query}
     *
     * @param action the action to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for the action
     */
    public NPCQuery action(final String action) {
        filter(new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(npc.getActions(), action);
            }
        });
        return this;
    }

    /**
     * This method is used to get all the {@link NPC}s accepted by this {@link Query}
     *
     * @return all the {@link NPC}s accepted by this {@link Query}
     */
    @Override
    public NPC[] all() {
        final Filter<NPC> filter = new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                for (final Filter<NPC> filter : filters)
                    if (!filter.accept(npc)) return false;

                return true;
            }
        };

        final List<NPC> list = new ArrayList<NPC>();
        Collections.addAll(list, context().npcs.getLoaded(filter));
        if (list.size() > 0 && comparators.size() > 0)
            for (final Comparator<NPC> comparator : comparators)
                Collections.sort(list, comparator);

        return list.toArray(new NPC[list.size()]);
    }
    
}
