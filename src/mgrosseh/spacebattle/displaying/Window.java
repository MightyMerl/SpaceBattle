package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.Constants;
import mgrosseh.spacebattle.Game;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Window implements Constants {

    private Game game;
    private PApplet app;
    public int frameCount;

    private WindowAction[][] actions;
    private int[] index;

    public Window(Game game) {
        this.game = game;
        app = game.getApp();
        index = new int[SCREEN_LAYERS];
        actions = new WindowAction[SCREEN_LAYERS][SPRITE_LIMIT];
        frameCount = 0;
    }

    public void frame() {
        app.background(BACKGROUND);
        display();
        clear();
        frameCount++;
    }

    private void display() {
        for (int i = 0; i < SCREEN_LAYERS; i++) {
            for (int j = 0; j < index[i]; j++) {
                if (actions[i][j] == null) {
                    continue;
                }
                actions[i][j].display();
            }
        }
    }

    private void clear() {
        for (int i = 0; i < SCREEN_LAYERS; i++) {
            index[i] = 0;
        }
    }

    public void addAction(WindowAction action, int priority, float tx, float ty) {
        actions[priority][index[priority]] = action;
        index[priority]++;
        action.setParent(this);
        action.setTx(tx);
        action.setTy(ty);
    }

    //DRAW METHODS
    public void text(String text, float x, float y, int color) {
        app.fill(color);
        if (app.alpha(color) == 0) app.noFill();
        app.textAlign(PConstants.LEFT, PConstants.TOP);
        app.text(text, x, y);
    }

    public void point(float x, float y, float size, int color) {
        app.stroke(color);
        if (app.alpha(color) == 0) app.noStroke();
        app.strokeWeight(size);
        app.point(x, y);
    }

    public void line(float x1, float y1, float x2, float y2, float thickness, int stroke) {
        app.stroke(stroke);
        if (app.alpha(stroke) == 0) app.noStroke();
        app.strokeWeight(thickness);
        app.line(x1, y1, x2, y2);
    }

    public void rect(float x, float y, float width, float height, float strokeWeight, int fill, int stroke) {
        app.stroke(stroke);
        app.fill(fill);
        if (app.alpha(fill) == 0) app.noFill();
        if (app.alpha(stroke) == 0) app.noFill();
        app.strokeWeight(strokeWeight);
        app.rectMode(PConstants.CORNER);
        app.rect(x, y, width, height);
    }

    public void ellipse(float x, float y, float width, float height, float strokeWeight, int fill, int stroke) {
        app.stroke(stroke);
        app.fill(fill);
        if (app.alpha(fill) == 0) app.noFill();
        if (app.alpha(stroke) == 0) app.noFill();
        app.strokeWeight(strokeWeight);
        app.ellipseMode(PConstants.CORNER);
        app.ellipse(x, y, width, height);
    }

    public void vertices(float x, float y, float strokeWeight, int fill, int stroke, PVector[] points, boolean close) {
        app.pushMatrix();
        app.translate(x, y);
        app.beginShape();
        app.strokeWeight(strokeWeight);
        app.fill(fill);
        app.stroke(stroke);
        if (app.alpha(fill) == 0) app.noFill();
        if (app.alpha(stroke) == 0) app.noFill();
        for (int i = 0; i < points.length; i++) {
            app.vertex(points[i].x, points[i].y);
        }
        if (close) app.endShape(PConstants.CLOSE);
        else app.endShape();
        app.popMatrix();
    }

    public void image(PImage image, float x, float y) {
        app.imageMode(PConstants.CORNER);
        app.image(image, x, y);
    }
}
