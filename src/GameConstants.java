public interface GameConstants {
    int BOARD_WIDTH = 1280;
    int BOARD_HEIGHT = 720;

    int BIRD_START_X = BOARD_WIDTH / 8;
    int BIRD_START_Y = BOARD_HEIGHT / 2;
    int BIRD_WIDTH = 34 * 3; // 102
    int BIRD_HEIGHT = 38 * 3; // 102

    int PIPE_WIDTH = 200;
    int PIPE_HEIGHT = 500;
    int PIPE_OPENING_SPACE = 300; // BOARD_HEIGHT/3 = 240
    int PIPE_SPAWN_DELAY_MS = 2500;
    int BIRD_ANIMATION_DELAY_MS = 200;

    int VELOCITY_X_PIPES = -10;
    int GRAVITY = 1;
    int BIRD_JUMP_VELOCITY = -12;

    String[] SKIN_IDENTIFIERS = {"level1", "level2", "level3"};
    String DEFAULT_SKIN_IDENTIFIER = SKIN_IDENTIFIERS[0];

    // File-based paths (without leading slash for relative file access)
    String BG_PATH_PREFIX = "assets/background/bg_";
    String BIRD_DEFAULT_PATH_PREFIX = "assets/birds/bird_";
    String BIRD_FLY_PATH_PREFIX = "assets/birds/bird_";
    String TOP_PIPE_PATH_PREFIX = "assets/pipes/toppipe_";
    String BOTTOM_PIPE_PATH_PREFIX = "assets/pipes/bottompipe_";
    String IMG_EXTENSION = ".png";
    String BIRD_FLY_SUFFIX = "_fly";

    // Main Menu specific constants
    String DEFAULT_MAIN_MENU_BG_PATH = "assets/menu/main_background.png";
    String MAIN_MENU_BG_SKIN_PREFIX = "assets/menu/main_background_";
    String MAIN_MENU_TITLE_PATH = "assets/menu/title.png";
    String START_BUTTON_PATH = "assets/menu/start_button.png";
    String START_BUTTON_HOVER_PATH = "assets/menu/start_button_hover.png";
    String SKIN_BUTTON_PATH = "assets/menu/skin_button.png";
    String SKIN_BUTTON_HOVER_PATH = "assets/menu/skin_button_hover.png";
    
    // Game Over Image
    String GAME_OVER_IMG_PATH = "assets/menu/game_over.png";
}