package mgrosseh.spacebattle;

import mgrosseh.spacebattle.controlls.Action;
import mgrosseh.spacebattle.controlls.Controller;
import mgrosseh.spacebattle.controlls.Key;
import mgrosseh.spacebattle.displaying.Window;
import mgrosseh.spacebattle.io.FileManager;
import mgrosseh.spacebattle.io.Sprite;
import mgrosseh.spacebattle.ui.UserInterface;
import mgrosseh.spacebattle.world.World;
import mgrosseh.spacebattle.world.WorldObject;
import processing.core.PImage;

import static mgrosseh.spacebattle.Constants.SECOND_TO_NANOSECONDS;

public class Game {

    public static final int STARTUP = 0, TITLE = 1, IN_GAME = 2;
    public int gamestate;

    private Main app;
    private Window window;
    private World world;
    private UserInterface userInterface;
    private FileManager fileManager;
    private Controller controller;

    public String sketch;

    public float deltaT, lastDeltaT;
    private float nanosLast;

    private boolean logOn, autoReload;
    public static int autoReloadFreq = 240;

    private int menuID, levelID;

    public Game(Main app) {
        this.app = app;
    }

    public void setup() {
        logOn = true;
        fileManager = new FileManager(this);
        window = new Window(this);
        world = new World(this);
        userInterface = new UserInterface(this);
        controller = new Controller(this);


        autoReload = true;

        gamestate = IN_GAME;

        world.addObject(new WorldObject(this, "ship", 50, 50, 10));

        controller.addAction(Key.R, new Action(Action.ON_PRESS, this) {
            public void run() {
                this.obj.reload();
            }
        }, Controller.SUPER);
    }

    public void loop() {
        lastDeltaT = deltaT;
        deltaT = (System.nanoTime() - nanosLast) / SECOND_TO_NANOSECONDS;
        nanosLast = System.nanoTime();
        world.loop();
        userInterface.loop();
        window.frame();
        if (autoReload && window.frameCount % autoReloadFreq == 0) {
            if (logOn)
                System.out.println("\nINFO: Frame " + Integer.toHexString(window.frameCount) + ": Reloading data...");
            reload();
        }
    }

    public void reload() {
        fileManager.reload();
        world.reload();
    }

    public Main getApp() {
        return app;
    }

    public Window getWindow() {
        return window;
    }

    public World getWorld() {
        return world;
    }

    public Controller getController() {
        return controller;
    }

    public boolean isLogOn() {
        return logOn;
    }

    public void setLogOn(boolean state) {
        logOn = state;
    }

    public Sprite getSprite(String name) {
        return fileManager.getSprite(name);
    }

    public PImage loadImage(String filename) {
        return app.loadImage(filename);
    }
}
