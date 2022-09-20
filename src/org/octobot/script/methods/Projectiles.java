package org.octobot.script.methods;

import org.octobot.bot.game.client.RSNode;
import org.octobot.bot.game.client.RSNodeList;
import org.octobot.bot.game.client.RSProjectile;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.collection.ProjectileQuery;
import org.octobot.script.wrappers.Projectile;

import java.util.ArrayList;
import java.util.List;

/**
 * Projectiles
 *
 * @author Pat-ji
 */
public class Projectiles extends ContextProvider {
    public static final Filter<Projectile> ALL_FILTER = new Filter<Projectile>() {
        @Override
        public boolean accept(final Projectile projectile) {
            return true;
        }
    };

    public Projectiles(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get all loaded {@link Projectile}s
     *
     * @return an array with all loaded {@link Projectile}s
     */
    public Projectile[] getLoaded() {
        return getLoaded(ALL_FILTER);
    }

    /**
     * This method is used to get all loaded {@link Projectile}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an array with all loaded {@link Projectile}s that are accepted by the {@link Filter}
     */
    public Projectile[] getLoaded(final Filter<Projectile> filter) {
        final List<Projectile> result = new ArrayList<Projectile>();

        final RSNodeList list = context().client.getProjectileList();
        RSNode head;
        if ((head = list.getHead()) != null) {
            RSNode next = null;
            while ((next = (next != null ? next.getNext() : head.getNext())) != null && next instanceof RSProjectile) {
                final Projectile instance = new Projectile(context(), (RSProjectile) next);
                if (filter.accept(instance))
                    result.add(instance);
            }
        }

        return result.toArray(new Projectile[result.size()]);
    }

    /**
     * This method is used to create a new {@link ProjectileQuery}
     *
     * @return a new {@link ProjectileQuery}
     */
    public final ProjectileQuery find() {
        return new ProjectileQuery(context());
    }

}
