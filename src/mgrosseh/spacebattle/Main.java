package mgrosseh.spacebattle;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {

    private Game game;

    public static void main(String[] args) {
        PApplet.main("mgrosseh.spacebattle.Main");
    }

    public void settings() {
        size(700, 700, P2D);
        smooth();
    }

    public void setup() {
        game = new Game(this);
        game.sketch = calcSketchPath() + "\\";
        game.setup();
    }

    public void draw() {
        game.loop();
    }

    public void keyPressed() {
        game.getController().keyPressed(keyCode, key);
    }

    public void keyReleased() {
        game.getController().keyReleased(keyCode, key);
    }

    public PImage loadImage(String filename) {
        return super.loadImage(filename);
    }
}
