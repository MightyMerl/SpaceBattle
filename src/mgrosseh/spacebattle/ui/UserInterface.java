package mgrosseh.spacebattle.ui;

import mgrosseh.spacebattle.Constants;
import mgrosseh.spacebattle.Game;
import mgrosseh.spacebattle.controlls.Controller;
import mgrosseh.spacebattle.displaying.Text;
import mgrosseh.spacebattle.displaying.Window;

public class UserInterface implements Constants {

    private Game game;
    private Window window;
    private Controller controller;

    public UserInterface(Game game) {
        this.game = game;
        window = game.getWindow();


    }

    public void loop() {
        if (game.isLogOn())
            window.addAction(new Text(Math.round(game.getApp().frameRate) + "", 0, 0), UI_LEVEL_1, 0, 0);
    }

}
