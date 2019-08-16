package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;
import processing.core.PImage;

public class Image extends WindowAction {

    private PImage image;
    private int tint;

    public Image(PImage image, float x, float y) {
        super(x, y);
        this.image = image;
        tint = DEFAULT_TINT;
    }

    public Image(PImage image, float x, float y, int tint) {
        this(image, x, y);
        this.tint = tint;
    }

    public Image(Sprite sprite) {
        this(sprite.image, sprite.x, sprite.y);
    }

    public void display() {
        parent.image(image, x + ty, y + ty);
    }
}
