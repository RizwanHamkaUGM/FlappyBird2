public interface GameConstants {
    int BOARD_WIDTH = 1280;
    int BOARD_HEIGHT = 720;

    int BIRD_START_X = BOARD_WIDTH / 8;
    int BIRD_START_Y = BOARD_HEIGHT / 2;
    int BIRD_WIDTH = 34 * 3;
    int BIRD_HEIGHT = 34 * 3;

    int PIPE_WIDTH = 220;
    int PIPE_HEIGHT = 500;
    int PIPE_OPENING_SPACE = BOARD_HEIGHT / 4;
    int PIPE_SPAWN_DELAY_MS = 2500; // placePipeTimer delay
    int BIRD_ANIMATION_DELAY_MS = 200;

    int VELOCITY_X_PIPES = -10; // Kecepatan pipa bergerak ke kiri
    int GRAVITY = 1;
    int BIRD_JUMP_VELOCITY = -9;

    // Path ke aset (relatif terhadap classpath)
    String BG_IMG_PATH = "./assets/background/bg_level1.png";
    String BIRD_DEFAULT_IMG_PATH = "./assets/birds/bird_level1.png";
    String BIRD_FLY_IMG_PATH = "./assets/birds/bird_level1_fly.png";
    String TOP_PIPE_IMG_PATH = "./assets/pipes/toppipe.png";
    String BOTTOM_PIPE_IMG_PATH = "./assets/pipes/bottompipe.png";
}