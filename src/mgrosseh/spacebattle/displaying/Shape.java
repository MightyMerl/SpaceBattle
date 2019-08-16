package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;
import processing.core.PVector;

public class Shape extends WindowAction {

    private boolean isParent, isChild;
    private float strokeWeight;
    private int fill, stroke;
    private boolean close;

    private PVector[] vectors;

    private Shape[] children;

    public Shape(Sprite sprite) {
        super(sprite.x, sprite.y);
        isParent = sprite.isParent;
        isChild = sprite.isChild;
        if (!isParent) {
            strokeWeight = sprite.strokeWeight;
            stroke = sprite.stroke;
            fill = sprite.fill;
            close = sprite.close;
            vectors = new PVector[sprite.vectors.length];
            for (int i = 0; i < sprite.vectors.length; i++) {
                vectors[i] = sprite.vectors[i].copy();
            }
        } else {
            children = new Shape[sprite.children.length];
            for (int i = 0; i < sprite.children.length; i++) {
                children[i] = new Shape(sprite.children[i]);
            }
        }
    }

    public void display() {
        parent.vertices(x + tx, y + ty, strokeWeight, fill, stroke, vectors, close);
    }
}
