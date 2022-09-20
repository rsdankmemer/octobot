package org.octobot.bot.handler;

import org.octobot.bot.Controller;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.bot.internal.Rank;
import org.octobot.bot.internal.User;
import org.octobot.bot.internal.ui.component.UtilityBar;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.listeners.GameEventListener;
import org.octobot.script.util.Timer;

/**
 * ScriptHandler
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class ScriptHandler extends Thread implements Runnable {
    private final GameDefinition definition;

    private GameScript script;
    public boolean stopped, paused;

    public ScriptHandler(final GameDefinition definition) {
        this.definition = definition;
    }

    public GameScript getScript() {
        return script;
    }

    public ScriptContext context() {
        return definition.context;
    }

    public void addGameEventListner(final GameEventListener listener) {
        definition.eventQueue.addListener(listener);
    }

    public void setScriptContext(final ScriptContext context) {
        definition.context = context;
        context.build(definition);
    }

    public RandomHandler getRandomHandler() {
        return definition.randomHandler;
    }

    public void setScript(final GameScript script) {
        System.out.println("[ScriptHandler] - Clearing EventQueue and RandomEvent claims.");
        definition.eventQueue.clear();
        definition.randomHandler.claims.clear();

        if (!getRandomHandler().initialized) {
            System.out.println("[ScriptHandler] - Initializing RandomHandler.");
            getRandomHandler().initialize(this);
        }

        if (script != null) {
            script.setScriptHandler(this);

            System.out.println("[ScriptHandler] - Starting RandomHandler.");
            getRandomHandler().paused = false;

            final ScriptManifest manifest = script.manifest();
            System.out.println("[ScriptHandler] - Attempting to start script: " + manifest.name() + ".");
            if (manifest.vip()) {
                System.out.println("[ScriptHandler] - VIP script detected, authenticating.");
                if (!User.user.isVip() && !User.user.isStaff()) {
                    System.out.println("[ScriptHandler] - You do not have a VIP rank, you will not be able to run the script.");
                    setScript(null);
                    return;
                }

                System.out.println("[ScriptHandler] - VIP authentication successful.");
            }

            final boolean lock = !Controller.isServer && User.user.getMaxClients() == Rank.MEMBER.maxClients;
            if (script.manifest().maxInstances() != Integer.MAX_VALUE || lock) {
                System.out.println("[ScriptHandler] - Max instance script detected, checking for multiple instances.");
                int instanceCount = 0;
                for (final GameDefinition definition : GameDefinition.definitions) {
                    final ScriptHandler scriptHandler = definition.scriptHandler;
                    if (scriptHandler.getScript() != null && scriptHandler.getScript().manifest().name().equals(manifest.name()))
                        instanceCount++;
                }

                if (instanceCount >= script.manifest().maxInstances() || (lock && instanceCount > 0)) {
                    System.out.println("[ScriptHandler] - The max amount of instances have been reached, you will not be able to run the script.");
                    setScript(null);
                    return;
                }

                System.out.println("[ScriptHandler] - Multiple instance check successful.");
            }

            script.setRunTime(new Timer(0));
            if (!script.start()) {
                System.err.println("[ScriptHandler] - Failed to start the script, script.start() returned false.");
                setScript(null);
                return;
            }

            System.out.println("[ScriptHandler] - Disabling mouse input.");
            definition.disableInput = true;
            UtilityBar.instance.updateTextures();

            System.out.println("[ScriptHandler] - Setting up the break handler.");
            definition.breakHandler.setup();

            addGameEventListner(script);
            System.out.println("[ScriptHandler] - Successfully started " + manifest.name() + ".");
        } else {
            System.out.println("[ScriptHandler] - Pausing RandomHandler.");
            getRandomHandler().paused = true;
        }

        this.script = script;
        getRandomHandler().interrupt();
        interrupt();

        UtilityBar.instance.updateTextures();
    }

    public void stopScript() {
        if (script != null) {
            System.out.println("[ScriptHandler] - Stopping script: " + script.manifest().name() + ".");
            script.onStop();

            System.out.println("[ScriptHandler] - Resetting to old data.");
            setScriptContext(new ScriptContext());
            definition.disableInput = false;
        }

        setScript(null);
        System.gc();
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                if (paused) {
                    Thread.sleep(1000);
                } else if (script != null && getRandomHandler().getActive() == null) {
                    if (definition.breakHandler.validate()) {
                        Thread.sleep(definition.breakHandler.execute());
                    } else {
                        final int execute = script.execute();
                        switch (execute) {
                            case -1:
                                stopScript();
                                break;
                            default:
                                Thread.sleep(execute);
                        }
                    }
                } else {
                    Thread.sleep(10000);
                }
            } catch (final InterruptedException ignored) {
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

}
