package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Line extends WindowAction {

    private float x2, y2;
    private float strokeWeight;
    private int stroke;

    public Line(Sprite sprite) {
        super(sprite.x, sprite.y);
        x2 = sprite.x2;
        y2 = sprite.y2;
        strokeWeight = sprite.strokeWeight;
        stroke = sprite.stroke;
    }

    public void display() {
        parent.line(x + tx, y + ty, x2 + tx, y2 + ty, strokeWeight, stroke);
    }
}
