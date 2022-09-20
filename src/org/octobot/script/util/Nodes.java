package org.octobot.script.util;

import org.octobot.bot.game.client.RSHashTable;
import org.octobot.bot.game.client.RSNode;

import java.lang.ref.SoftReference;

/**
 * Nodes
 *
 * @author Pat-ji
 */
public class Nodes {

    /**
     * This method is used to get a {@link RSNode} from a {@link RSHashTable}
     *
     * @param table the {@link RSHashTable} to get the {@link RSNode} from
     * @param id the id of the {@link RSNode}
     * @return a {@link RSNode} from a {@link RSHashTable}
     */
    public static Object lookup(final RSHashTable table, final long id) {
        final RSNode[] buckets;
        if (table == null || (buckets = table.getBuckets()) == null || id < 0) return null;

        final RSNode node = buckets[(int) (id & buckets.length - 1)];
        for (RSNode next = node.getNext(); next != node && next != null; next = next.getNext())
            if (next.getId() == id) return next instanceof SoftReference ? ((SoftReference) next).get() : next;

        return null;
    }

}
