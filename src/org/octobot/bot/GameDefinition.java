package org.octobot.bot;

import org.octobot.bot.event.EventQueue;
import org.octobot.bot.game.client.RSClient;
import org.octobot.bot.game.loader.Crawler;
import org.octobot.bot.game.loader.GameClassLoader;
import org.octobot.bot.game.loader.internal.wrappers.input.Canvas;
import org.octobot.bot.game.loader.internal.wrappers.input.Keyboard;
import org.octobot.bot.game.loader.internal.wrappers.input.Mouse;
import org.octobot.bot.handler.BreakHandler;
import org.octobot.bot.handler.RandomHandler;
import org.octobot.bot.handler.ScriptHandler;
import org.octobot.bot.internal.Account;
import org.octobot.bot.internal.Proxy;
import org.octobot.bot.internal.ui.component.TabBar;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.listeners.PaintListener;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client
 *
 * @author Pat-ji
 */
public class GameDefinition implements Runnable {
    public static final List<GameDefinition> definitions;

    static {
        definitions = new ArrayList<GameDefinition>();
    }

    public final ScriptHandler scriptHandler;
    public final RandomHandler randomHandler;
    public final BreakHandler breakHandler;

    public final EventQueue eventQueue;
    public final Map<String, PaintListener> renderEvents;

    public Keyboard keyboard;
    public Mouse mouse;
    public Canvas canvas;
    public RSClient client;
    public Applet applet;
    public GameClassLoader classLoader;

    public ScriptContext context;

    public Account account;
    public Proxy proxy;
    public boolean disableInput;

    public int index;

    public GameDefinition(final int index) {
        this.index = index;

        context = new ScriptContext();
        scriptHandler = new ScriptHandler(this);
        scriptHandler.start();
        randomHandler = new RandomHandler(this);
        randomHandler.start();
        breakHandler = new BreakHandler(this);

        eventQueue = new EventQueue();
        renderEvents = new HashMap<String, PaintListener>();

        definitions.add(this);
        new Thread(this).start();
    }

    public void renderEvent(final String name, final PaintListener event) {
        if (renderEvents.containsKey(name)) {
            renderEvents.remove(name);
        } else {
            renderEvents.put(name, event);
        }
    }

    public void stop() {
        scriptHandler.stopped = true;
        scriptHandler.interrupt();
        randomHandler.interrupt();
        eventQueue.stop();

        definitions.remove(this);
        System.gc();
    }

    @Override
    public void run() {
        try {
            applet = new Crawler().loadApplet(this, false);

            eventQueue.start();
            TabBar.instance.setIndex(index + 1);

            System.gc();
        } catch (final Exception e) {
            System.out.println("[GameDefinition] - Failed to create a new definition.");
            e.printStackTrace();
        }
    }

}
