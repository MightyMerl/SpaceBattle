package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.Constants;

public abstract class WindowAction implements Constants {

    protected Window parent;
    protected float x, y;
    protected float tx, ty;

    public boolean isReady;

    protected WindowAction(float x, float y) {
        this.x = x;
        this.y = y;
        tx = 0;
        ty = 0;
        isReady = false;
    }

    public abstract void display();

    public void setParent(Window window) {
        this.parent = window;
        isReady = true;
    }

    public void setTx(float tx) {
        this.tx = tx;
    }

    public void setTy(float ty) {
        this.ty = ty;
    }

    public void setPositionOnScreen(float x, float y) {
        tx = x;
        ty = y;
    }
}
