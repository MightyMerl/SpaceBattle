package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Point extends WindowAction {

    private float strokeWeight;
    private int stroke;

    public Point(Sprite sprite) {
        super(sprite.x, sprite.y);
        strokeWeight = sprite.strokeWeight;
        stroke = sprite.stroke;
    }

    public void display() {
        parent.point(x + tx, y + ty, strokeWeight, stroke);
    }
}
