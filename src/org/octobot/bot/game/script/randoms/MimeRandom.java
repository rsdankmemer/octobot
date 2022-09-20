package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Locatable;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.NPC;
import org.octobot.script.wrappers.Tile;
import org.octobot.script.wrappers.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * MimeRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Mime",
        description = "Handles the Mime random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class MimeRandom extends RandomScript {
    private static final Map<Integer, String> EMOTE_MAP = new HashMap<Integer, String>();
    private static final Tile PERFORMANCE_TILE = new Tile(2008, 4762);

    static {
        EMOTE_MAP.put(857, "Think");
        EMOTE_MAP.put(860, "Cry");
        EMOTE_MAP.put(861, "Laugh");
        EMOTE_MAP.put(866, "Dance");
        EMOTE_MAP.put(1128, "Glass Wall");
        EMOTE_MAP.put(1129, "Lean on air");
        EMOTE_MAP.put(1130, "Climb Rope");
        EMOTE_MAP.put(1131, "Glass Box");
    }

    private int animation = -1;

    @Override
    public boolean validate() {
        if (!context().game.isLoggedIn()) return false;

        final Locatable player = context().players.getLocal();
        return player != null && PERFORMANCE_TILE.distance(player) < 10;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        animation = -1;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        final NPC mime = context().npcs.getNearest("Mime");
        if (mime != null) {
            final int mimeAnimation = mime.getAnimation();
            if (mimeAnimation != -1 && mimeAnimation != 858)
                animation = mimeAnimation;

            final Widget widget = context().widgets.get(188);
            if (widget != null) {
                final String text = EMOTE_MAP.get(animation);
                if (text != null)
                    for (final Component component : widget.getChildren())
                        if (component != null && component.getText().contains(text))
                            if (component.click(true)) return Random.nextInt(1200, 2000);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.MIME;
    }

}
