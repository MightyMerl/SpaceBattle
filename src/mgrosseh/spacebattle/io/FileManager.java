package mgrosseh.spacebattle.io;

import mgrosseh.spacebattle.Constants;
import mgrosseh.spacebattle.Game;

import java.io.File;

public class FileManager implements Constants {

    private Game game;

    private Sprite[] sprites;

    public FileManager(Game game) {
        this.game = game;
        sprites = new Sprite[0];
        reload();
    }

    public void reload() {
        File file = new File(game.sketch + FOLDER_SPRITES);
        File[] spriteData = file.listFiles();
        sprites = new Sprite[spriteData.length];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = Sprite.convert(spriteData[i], game);
        }
        if (game.isLogOn()) System.out.println("\nFiles reloaded.\n");
    }

    public Sprite getSprite(String name) {
        for (int i = 0; i < sprites.length; i++) {
            if (sprites[i] == null) continue;

            if (sprites[i].name.equals(name)) {
                return sprites[i];
            }
        }
        return null;
    }
}
