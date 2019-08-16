package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Rectangle extends WindowAction {

    private float width, height;
    private float strokeWeight;
    private int fill, stroke;


    public Rectangle(Sprite sprite) {
        super(sprite.x, sprite.y);
        width = sprite.width;
        height = sprite.height;
        strokeWeight = sprite.strokeWeight;
        stroke = sprite.stroke;
        fill = sprite.fill;
    }

    public void display() {
        parent.rect(x + tx, y + ty, width, height, strokeWeight, fill, stroke);
    }
}
