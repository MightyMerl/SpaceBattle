package mgrosseh.spacebattle.world;

import mgrosseh.spacebattle.Game;
import mgrosseh.spacebattle.displaying.Window;
import mgrosseh.spacebattle.displaying.WindowAction;

import java.util.LinkedList;

public class World {

    private Game game;

    private LinkedList<WorldObject> objects;

    public World(Game game) {
        this.game = game;
        objects = new LinkedList<>();
    }

    public void loop() {
        display();
    }

    public void display() {
        for (WorldObject obj : objects) {
            if (obj.getSprite() == null || !obj.isLoaded) continue;
            if (obj.getSprite().getAction() == null) continue;
            WindowAction action = obj.getSprite().getAction();
            action.setPositionOnScreen(obj.x, obj.y);
            int priority = obj.getPriority();
            float x = obj.getX();
            float y = obj.getY();
            Window window = game.getWindow();
            window.addAction(action, priority, x, y);
        }
    }

    public void reload() {
        for (WorldObject obj : objects) {
            obj.setSprite();
        }
    }

    public void addObject(WorldObject object) {
        objects.add(object);
    }
}
