import java.awt.Image;
import javax.swing.ImageIcon;

public class AssetLoader {
    private Image backgroundImg;
    private Image birdDefaultImg;
    private Image birdFlyImg;
    private Image topPipeImg;
    private Image bottomPipeImg;
    private Image gameOverImg; // New image for Game Over

    private String currentSkinIdentifier;

    public AssetLoader() {
        loadAssetsForSkin(GameConstants.DEFAULT_SKIN_IDENTIFIER); // Load default skin assets
        try {
            // Load non-skin specific assets like game over image
            gameOverImg = new ImageIcon(getClass().getResource(GameConstants.GAME_OVER_IMG_PATH)).getImage();
        } catch (Exception e) {
            System.err.println("Error loading game over image: " + e.getMessage());
            e.printStackTrace();
            gameOverImg = null; // Set to null if loading fails
        }
    }

    public void loadAssetsForSkin(String skinIdentifier) {
        this.currentSkinIdentifier = skinIdentifier;
        try {
            backgroundImg = new ImageIcon(getClass().getResource(GameConstants.BG_PATH_PREFIX + skinIdentifier + GameConstants.IMG_EXTENSION)).getImage();
            birdDefaultImg = new ImageIcon(getClass().getResource(GameConstants.BIRD_DEFAULT_PATH_PREFIX + skinIdentifier + GameConstants.IMG_EXTENSION)).getImage();
            birdFlyImg = new ImageIcon(getClass().getResource(GameConstants.BIRD_FLY_PATH_PREFIX + skinIdentifier + GameConstants.BIRD_FLY_SUFFIX + GameConstants.IMG_EXTENSION)).getImage();
            topPipeImg = new ImageIcon(getClass().getResource(GameConstants.TOP_PIPE_PATH_PREFIX + skinIdentifier + GameConstants.IMG_EXTENSION)).getImage();
            bottomPipeImg = new ImageIcon(getClass().getResource(GameConstants.BOTTOM_PIPE_PATH_PREFIX + skinIdentifier + GameConstants.IMG_EXTENSION)).getImage();
        } catch (Exception e) {
            System.err.println("Error loading images for skin " + skinIdentifier + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("Attempting to load default skin assets as fallback...");
            if (!skinIdentifier.equals(GameConstants.DEFAULT_SKIN_IDENTIFIER)) {
                loadAssetsForSkin(GameConstants.DEFAULT_SKIN_IDENTIFIER);
            } else {
                 System.err.println("Failed to load default skin assets. Game might not display correctly.");
                 // Set images to null or placeholders if even default fails
                 backgroundImg = null;
                 birdDefaultImg = null;
                 birdFlyImg = null;
                 topPipeImg = null;
                 bottomPipeImg = null;
            }
        }
    }

    public Image getBackgroundImg() { return backgroundImg; }
    public Image getBirdDefaultImg() { return birdDefaultImg; }
    public Image getBirdFlyImg() { return birdFlyImg; }
    public Image getTopPipeImg() { return topPipeImg; }
    public Image getBottomPipeImg() { return bottomPipeImg; }
    public Image getGameOverImg() { return gameOverImg; } // Getter for game over image
    public String getCurrentSkinIdentifier() { return currentSkinIdentifier; }
}