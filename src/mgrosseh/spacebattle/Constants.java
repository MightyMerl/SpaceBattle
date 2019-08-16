package mgrosseh.spacebattle;

public interface Constants {
    float SECOND_TO_NANOSECONDS = 1000000000;

    int SPRITE_LIMIT = 1000, SCREEN_LAYERS = 6;
    int BACKGROUND_LEVEL_0 = 0, BACKGROUND_LEVEL_1 = 1, OBJECT_LEVEL_0 = 2, OBJECT_LEVEL_1 = 3, UI_LEVEL_0 = 4, UI_LEVEL_1 = 5;

    int TEXT_COLOR = 0xFF000000, BACKGROUND = 0xFF666666, DEFAULT_TINT = 0x00000000;

    float SCAN_MAX_DURATION = 10; //in sec
    int SCAN_MAX_MEMORY = 8;
    int NONE = 0, ANIMATION = 1, IMAGE = 2, SHAPE = 3, TEXT = 4, RECT = 5, ELLIPSE = 6, LINE = 7, POINT = 8, GROUP = 9, UNKNOWN_TYPE = 10;
    String UNKNOWN_NAME = "Unknown";

    String FOLDER_DATA = "data\\", FOLDER_SPRITES = FOLDER_DATA + "sprites\\";
}