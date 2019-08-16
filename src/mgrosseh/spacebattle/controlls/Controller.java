package mgrosseh.spacebattle.controlls;

import mgrosseh.spacebattle.Constants;
import mgrosseh.spacebattle.Game;

public class Controller implements Constants {

    public static final int GAME = 0, USER_INTERFACE = 1, CHEAT_CODE = 2, SUPER = 3;

    private Game game;

    private Key[] allKeys;
    private int charNum;

    public boolean userInterface, cheatCode;

    public Controller(Game game) {
        this.game = game;

        charNum = Key.characterNum;
        allKeys = new Key[charNum + 1];

        allKeys[charNum] = new Key(this, Key.UNKNOWN);
        for (int i = 0; i < charNum; i++) {
            allKeys[i] = new Key(this, i);
        }
        userInterface = false;
        cheatCode = false;
    }

    public void addAction(int keyID, Action action, int type) {
        getByKeyID(keyID).setAction(action);
    }

    private Key getByKeyID(int id) {
        if (id >= 0 && id < charNum) return allKeys[id];
        else return allKeys[charNum];
    }

    public void keyPressed(int keyCode, char key) {
        getByKeyID(Key.getKeyID(keyCode, key)).press();
    }

    public void keyReleased(int keyCode, char key) {
        getByKeyID(Key.getKeyID(keyCode, key)).release();
    }
}
