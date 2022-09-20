package org.octobot.script;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.client.RSClient;
import org.octobot.script.methods.*;

/**
 * ScriptContext
 *
 * @author Pat-ji
 */
public class ScriptContext {
    public Bank bank;
    public Calculations calculations;
    public Camera camera;
    public RSClient client;
    public Combat combat;
    public DepositBox depositBox;
    public Environment environment;
    public Equipment equipment;
    public Game game;
    public GameObjects objects;
    public GroundItems groundItems;
    public Hints hints;
    public Inventory inventory;
    public Keyboard keyboard;
    public Landscape landscape;
    public Magic magic;
    public Menu menu;
    public Mouse mouse;
    public Movement movement;
    public Navigation navigation;
    public NPCs npcs;
    public Players players;
    public Prayer prayer;
    public Projectiles projectiles;
    public Settings settings;
    public Shop shop;
    public Skills skills;
    public Tabs tabs;
    public Trading trading;
    public Widgets widgets;
    public Worlds worlds;

    /**
     * This method is used to initialize the fields
     * this is automatically done by the client, and should not be used by users
     *
     * @param definition the {@link GameDefinition} to initialize with
     */
    public void build(final GameDefinition definition) {
        bank = new Bank(this);
        calculations = new Calculations(this);
        camera = new Camera(this);
        client = definition.client;
        combat = new Combat(this);
        depositBox = new DepositBox(this);
        environment = new Environment(this, definition.randomHandler);
        equipment = new Equipment(this);
        game = new Game(this);
        objects = new GameObjects(this);
        groundItems = new GroundItems(this);
        hints = new Hints(this);
        inventory = new Inventory(this);
        keyboard = new Keyboard(this, definition.applet, definition.keyboard);
        landscape = new Landscape(this);
        magic = new Magic(this);
        menu = new Menu(this);
        mouse = new Mouse(this, definition.applet, definition.mouse);
        movement = new Movement(this);
        navigation = new Navigation(this);
        npcs = new NPCs(this);
        players = new Players(this);
        prayer = new Prayer(this);
        projectiles = new Projectiles(this);
        settings = new Settings(this);
        shop = new Shop(this);
        skills = new Skills(this);
        tabs = new Tabs(this);
        trading = new Trading(this);
        widgets = new Widgets(this);
        worlds = new Worlds(this);
    }

}
