package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Projectile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ProjectileQuery
 *
 * @author Pat-ji
 */
public class ProjectileQuery extends SceneNodeQuery<Projectile, ProjectileQuery> {

    public ProjectileQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get all the {@link Projectile}s accepted by this {@link Query}
     *
     * @return all the {@link Projectile}s accepted by this {@link Query}
     */
    @Override
    public Projectile[] all() {
        final Filter<Projectile> filter = new Filter<Projectile>() {
            @Override
            public boolean accept(final Projectile projectile) {
                for (final Filter<Projectile> filter : filters)
                    if (!filter.accept(projectile)) return false;

                return true;
            }
        };

        final List<Projectile> list = new ArrayList<Projectile>();
        Collections.addAll(list, context().projectiles.getLoaded(filter));
        if (list.size() > 0 && comparators.size() > 0)
            for (final Comparator<Projectile> comparator : comparators)
                Collections.sort(list, comparator);

        return list.toArray(new Projectile[list.size()]);
    }

}