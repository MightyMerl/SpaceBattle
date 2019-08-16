package mgrosseh.spacebattle.world;

import mgrosseh.spacebattle.Constants;
import mgrosseh.spacebattle.Game;
import mgrosseh.spacebattle.io.Sprite;

public class WorldObject implements Constants {

    protected Game game;

    protected String name;

    protected float x, y;
    protected float width, height;
    protected float radius;
    protected boolean useRadius;

    protected int priority;
    protected Sprite sprite;
    protected String spriteName;

    public boolean isLoaded;

    public WorldObject(Game game) {
        this.game = game;
        priority = OBJECT_LEVEL_0;
        x = -1;
        y = -1;
        width = 1;
        height = 1;
        radius = 1;
        useRadius = false;
        sprite = new Sprite();
        spriteName = UNKNOWN_NAME;
        isLoaded = false;
    }

    public WorldObject(Game game, String spriteName, float x, float y) {
        this(game);
        setSprite(spriteName);
        setPosition(x, y);
    }

    public WorldObject(Game game, String spriteName, float x, float y, float width, float height) {
        this(game, spriteName, x, y);
        setDimensions(width, height);
    }

    public WorldObject(Game game, String spriteName, float x, float y, float radius) {
        this(game, spriteName, x, y);
        setDimensions(radius);
    }

    public float dist(WorldObject obj) {
        return (float) Math.sqrt(Math.pow(obj.getX() - x, 2) + Math.pow(obj.getY() - y, 2));
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        useRadius = false;
    }

    public void setDimensions(float radius) {
        this.radius = radius;
        useRadius = true;
    }

    public void setSprite() {
        sprite = game.getSprite(spriteName);
    }

    public void setSprite(String name) {
        spriteName = name;
        sprite = game.getSprite(name);
        isLoaded = true;
        if (game.isLogOn()) System.out.println("Sprite: " + name + " loaded into WorldObject.");
        else
            System.out.println("WARNING: Sprite " + name + " was not detected correctly or is corrupted: object might not be displayed.");
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getPriority() {
        return priority;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRadius() {
        return radius;
    }

    public boolean isUsingRadius() {
        return useRadius;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        String string = super.toString();
        string += ": WorldObject: " + name + " (x: " + x + ", y: " + y + ", ";
        if (useRadius) {
            string += "radius: " + radius + "; ";
        } else {
            string += "width: " + width + ", height: " + height + ";";
        }
        string += "priority: " + priority + ", sprite name: " + spriteName + "; loaded: " + isLoaded + ")";
        return string;
    }

}
