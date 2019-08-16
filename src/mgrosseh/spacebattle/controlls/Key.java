package mgrosseh.spacebattle.controlls;

import processing.core.PConstants;

import java.util.LinkedList;

public class Key {

    public static final int characterNum = 49;
    public static final int UNKNOWN = -1, SPACE = 10, A = 11, B = 12, C = 13, D = 14, E = 15,
            F = 16, G = 17, H = 18, I = 19, J = 20, K = 21, L = 22, M = 23,
            N = 24, O = 25, P = 26, Q = 27, R = 28, S = 29, T = 30, U = 31,
            V = 32, W = 33, X = 34, Y = 35, Z = 36, ENTER = 37, BACKSPACE = 38,
            SHIFT = 39, RSHIFT = 40, STRG = 41, TAB = 42, ALT = 43, ESC = 44,
            UP = 45, RIGHT = 46, DOWN = 47, LEFT = 48;

    private Controller parent;

    public boolean isPressed;
    private boolean coded;
    private char key;
    private int keyCode;

    public int keyID;

    public float timeCreated;

    private boolean pressed, released;

    private LinkedList<Boolean> logType; //true = press, false == release
    private LinkedList<Float> logTimer; //the time the, in logType specified, action occurred

    private Action action;

    public Key(char key, int keyCode) {
        coded = key == PConstants.CODED;
        timeCreated = System.nanoTime();
        this.key = key;
        this.keyCode = keyCode;

        keyID = calculateID();
        logType = new LinkedList<>();
        logTimer = new LinkedList<>();
    }

    public Key(Controller controller, int id) {
        keyID = id;
        this.parent = controller;
        logType = new LinkedList<>();
        logTimer = new LinkedList<>();
    }

    public void setAction(Action action) {
        this.action = action;
        action.setParent(this);
    }

    public void press() {
        pressed = true;
        isPressed = true;
        if (action != null) {
            action.trigger(true, false);
        }
        logType.add(true);
        logTimer.add(new Float(System.nanoTime()));
    }

    public void release() {
        released = true;
        isPressed = false;
        if (action != null) {
            action.trigger(false, true);
        }
        logType.add(false);
        logTimer.add(new Float(System.nanoTime()));
    }

    public boolean wasPressed() {
        if (pressed) {
            pressed = false;
            return true;
        }
        return false;
    }

    public boolean wasReleased() {
        if (released) {
            released = false;
            return true;
        }
        return false;
    }

    public boolean getJustPressed() {
        return pressed;
    }

    public boolean getJustReleased() {
        return released;
    }

    public int calculateID() {
        return value(coded ? key : keyCode);
    }

    public static final int getKeyID(int keyCode, char key) {
        if (key == PConstants.CODED) {
            return value(keyCode);
        } else {
            return value(key);
        }
    }

    public static int value(int keyCode) {
        switch (keyCode) {
            case ' ':
                return SPACE;
            case PConstants.ENTER:
                return ENTER;
            case PConstants.BACKSPACE:
                return BACKSPACE;
            case PConstants.SHIFT:
                return SHIFT;
            case PConstants.CONTROL:
                return STRG;
            case PConstants.TAB:
                return TAB;
            case PConstants.ALT:
                return ALT;
            case PConstants.ESC:
                return ESC;
            case PConstants.UP:
                return UP;
            case PConstants.RIGHT:
                return RIGHT;
            case PConstants.DOWN:
                return DOWN;
            case PConstants.LEFT:
                return LEFT;

        }
        return UNKNOWN;
    }

    public static int value(char key) {
        if (Character.isDigit(key)) return Integer.valueOf(key);
        switch (key) {
            case 'a':
            case 'A':
                return A;
            case 'b':
            case 'B':
                return B;
            case 'c':
            case 'C':
                return C;
            case 'd':
            case 'D':
                return D;
            case 'e':
            case 'E':
                return E;
            case 'f':
            case 'F':
                return F;
            case 'g':
            case 'G':
                return G;
            case 'h':
            case 'H':
                return H;
            case 'i':
            case 'I':
                return I;
            case 'j':
            case 'J':
                return J;
            case 'k':
            case 'K':
                return K;
            case 'l':
            case 'L':
                return L;
            case 'm':
            case 'M':
                return M;
            case 'n':
            case 'N':
                return N;
            case 'o':
            case 'O':
                return O;
            case 'p':
            case 'P':
                return P;
            case 'q':
            case 'Q':
                return Q;
            case 'r':
            case 'R':
                return R;
            case 's':
            case 'S':
                return S;
            case 't':
            case 'T':
                return T;
            case 'u':
            case 'U':
                return U;
            case 'v':
            case 'V':
                return V;
            case 'w':
            case 'W':
                return W;
            case 'x':
            case 'X':
                return X;
            case 'y':
            case 'Y':
                return Y;
            case 'z':
            case 'Z':
                return Z;
        }
        return UNKNOWN;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getKey() {
        return key;
    }

}
