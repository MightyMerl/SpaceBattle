package mgrosseh.spacebattle.controlls;

import mgrosseh.spacebattle.Game;

public class Action {

    public static final int ON_PRESS = 0, ON_RELEASE = 1, ON_BOTH = 2;

    public int type;
    public Key parent;
    public Game obj;

    public Action(int type, Game obj) {
        this.type = type;
        this.obj = obj;
    }

    public void trigger(boolean press, boolean release) {
        if ((type == ON_BOTH && (press || release)) || (type == ON_PRESS && press) || (type == ON_RELEASE && release))
            run();
    }

    protected void run() {

    }

    public void setParent(Key parent) {
        this.parent = parent;
    }
}
