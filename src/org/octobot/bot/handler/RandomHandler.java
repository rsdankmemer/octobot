package org.octobot.bot.handler;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.script.randoms.*;
import org.octobot.script.Executable;
import org.octobot.script.event.RandomEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RandomHandler
 *
 * @author Pat-ji
 */
public class RandomHandler extends Thread implements Runnable {
    public volatile boolean[] disabled;

    private final GameDefinition definition;
    public final List<String> randomNames;
    public final List<RandomScript> randoms;
    public final Map<RandomEvent.Event, Executable> claims;

    private RandomScript active;
    public boolean initialized, paused;

    public RandomHandler(final GameDefinition definition) {
        this.definition = definition;

        randomNames = new ArrayList<String>();
        randoms = new ArrayList<RandomScript>();
        randomNames.add("Welcome screen");
        randoms.add(new WelcomeScreenRandom());
        randomNames.add("Bank pin");
        randoms.add(new BankPinRandom());
        //randomNames.add("Bee keeper");
        //randoms.add(new BeeKeeperRandom());
        //randomNames.add("Certers");
        //randoms.add(new CertersRandom());
        //randomNames.add("Drill sergeant");
        //randoms.add(new DrillSergeantRandom());
        randomNames.add("Exp lamp");
        randoms.add(new ExpLampRandom());
        //randomNames.add("Freaky forester");
        //randoms.add(new FreakyForesterRandom());
        //randomNames.add("Frog cave");
        //randoms.add(new FrogCaveRandom());
        //randomNames.add("Jekyll and Hide");
        //randoms.add(new JekyllAndHydeRandom());
        randomNames.add("Login");
        randoms.add(new LoginRandom());
        //randomNames.add("Lost and found");
        //randoms.add(new LostAndFoundRandom());
        //randomNames.add("Maze");
        //randoms.add(new MazeRandom());
        //randomNames.add("Mime");
        //randoms.add(new MimeRandom());
        //randomNames.add("Molly");
        //randoms.add(new MollyRandom());
        //randomNames.add("Mysterious box");
        //randoms.add(new MysteriousBoxRandom());
        //randomNames.add("Pillory");
        //randoms.add(new PilloryRandom());
        //randomNames.add("Pinball");
        //randoms.add(new PinballRandom());
        //randomNames.add("Prison Pete");
        //randoms.add(new PrisonPeteRandom());
        //randomNames.add("Quiz");
        //randoms.add(new QuizRandom());
        //randomNames.add("Run from combat");
        //randoms.add(new RunFromCombatRandom());
        //randomNames.add("Sandwich lady");
        //randoms.add(new SandwichLadyRandom());
        //randomNames.add("Scape rune");
        //randoms.add(new ScapeRuneRandom());
        //randomNames.add("Strange plant");
        //randoms.add(new StrangePlantRandom());
        //randomNames.add("Surprise exam");
        //randoms.add(new SurpriseExamRandom());
        //randomNames.add("Talk to");
        //randoms.add(new TalkToRandom());
        randomNames.add("World switch");
        randoms.add(new WorldSwitchRandom());

        for (final RandomScript random : randoms)
            random.initialize(definition);

        claims = new HashMap<RandomEvent.Event, Executable>();

        disabled = new boolean[randoms.size()];
        paused = true;
    }

    public void setEnabled(final RandomEvent.Event event, final boolean enabled) {
        for (int i = 0; i < RandomEvent.Event.values.length; i++)
            if (RandomEvent.Event.values[i].equals(event)) {
                disabled[i] = !enabled;
                break;
            }
    }

    public RandomScript getActive() {
        return active;
    }

    public void initialize(final ScriptHandler scriptHandler) {
        for (final RandomScript script : randoms)
            script.setScriptHandler(scriptHandler);

        initialized = true;
    }

    public RandomScript getRandom(final String name) {
        return randoms.get(randomNames.indexOf(name));
    }

    public ScriptHandler getScriptHandler() {
        return definition.scriptHandler;
    }

    public void getActiveRandom() {
        if (!initialized) return;

        if (active != null) {
            if (!active.validate()) {
                System.out.println("[RandomHandler] - Deactivating random: " + active.manifest().name());
                definition.eventQueue.dispatchEvent(new RandomEvent(1, active.getEvent()));
                definition.eventQueue.removeListener(active);

                active.onStop();
                active = null;

                definition.scriptHandler.interrupt();
            } else {
                return;
            }
        }

        int index = 0;
        for (final RandomScript random : randoms) {
            if (!disabled[index++] && random.validate()) {
                active = random;
                System.out.println("[RandomHandler] - Activating random: " + active.manifest().name());

                definition.eventQueue.addListener(active);
                definition.eventQueue.dispatchEvent(new RandomEvent(0, active.getEvent()));

                definition.scriptHandler.interrupt();
            }
        }
    }

    @Override
    public void run() {
        while (!definition.scriptHandler.stopped) {
            try {
                if (paused || definition.scriptHandler.paused) {
                    Thread.sleep(10000);
                } else {
                    getActiveRandom();
                    if (active != null) {
                        if (claims.isEmpty()) {
                            Thread.sleep(active.execute());
                        } else {
                            final Executable executable = claims.get(active.getEvent());
                            Thread.sleep(executable != null ? executable.execute() : active.execute());
                        }
                    } else {
                        Thread.sleep(1200);
                    }
                }
            } catch (final InterruptedException ignored) {
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

}
