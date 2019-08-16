package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Animation extends WindowAction {

    public Animation(Shape[] shapes, float x, float y) {
        super(x, y);
    }

    public Animation(Sprite sprite) {
        super(sprite.x, sprite.y);
    }

    public void display() {

    }
}
