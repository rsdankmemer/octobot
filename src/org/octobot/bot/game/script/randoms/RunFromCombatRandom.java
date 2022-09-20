package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.PathFinder;
import org.octobot.script.wrappers.Player;
import org.octobot.script.wrappers.Tile;
import org.octobot.script.wrappers.TileMatrix;

/**
 * RunFromCombatRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "RunFromCombat",
        description = "Handles the RunFromCombat random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class RunFromCombatRandom extends RandomScript {
    private static final String[] NAMES = { "Evil Chicken", "Swarm", "River Troll", "River troll", "Tree Spirit", "Tree spirit", "Rock Golem", "Rock golem", "Poison Gas" };

    private Tile base, dest;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && context().npcs.find().name(NAMES).interacting(context().players.getLocal()).nearest().first() != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        base = dest = null;
    }

    @Override
    public int execute() {
        final Player player = context().players.getLocal();
        if (player == null) return 100;

        if (base == null)
            base = player.getLocation();

        if (dest == null) {
            dest = getSaveTile();
            System.out.println("[RunFromCombatRandom] - Chosen " + dest.toString() + " to run to.");
        } else if (context().movement.walkTileOnMap(dest)) {
            return Random.nextInt(500, 800);
        }

        return 100;
    }

    private Tile getSaveTile() {
        Tile tile = null;

        final int x = base.getX();
        final int y = base.getY();
        int tries = 0;
        do {
            tries++;
            final Tile check = new Tile(x + Random.nextInt(-10, 10) + 5, y + Random.nextInt(-10, 10) + 5, base.getPlane());
            final TileMatrix matrix = check.getMatrix(context());
            if (!matrix.isOnMiniMap() || !matrix.isWalkable() || matrix.isWater() || base.distance(check) < 5) continue;

            final PathFinder pathFinder = new PathFinder(context(), base, check);
            if (pathFinder.getPath() != null && pathFinder.getPath().length > 12)
                tile = check;
        } while (tile == null && tries < 25);

        if (tile == null) tile = base;
        return tile;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.RUN_FROM_COMBAT;
    }

}
