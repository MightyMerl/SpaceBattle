package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Text extends WindowAction {

    private String text;
    private int color;

    public Text(String text, float x, float y) {
        super(x, y);
        this.text = text;
        color = TEXT_COLOR;
    }

    public Text(String text, float x, float y, int color) {
        this(text, x, y);
        this.color = color;
    }

    public Text(Sprite sprite) {
        this(sprite.text, sprite.x, sprite.y, sprite.fill);
    }

    public void display() {
        parent.text(text, x + tx, y + ty, color);
    }

}
