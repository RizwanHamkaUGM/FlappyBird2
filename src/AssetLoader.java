import java.awt.Image;
import java.io.File;
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
            File gameOverFile = new File("assets/menu/game_over.png");
            if (gameOverFile.exists()) {
                gameOverImg = new ImageIcon(gameOverFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Game over image file not found: " + gameOverFile.getAbsolutePath());
                gameOverImg = null;
            }
        } catch (Exception e) {
            System.err.println("Error loading game over image: " + e.getMessage());
            e.printStackTrace();
            gameOverImg = null; // Set to null if loading fails
        }
    }

    public void loadAssetsForSkin(String skinIdentifier) {
        this.currentSkinIdentifier = skinIdentifier;
        try {
            // Build file paths
            String bgPath = "assets/background/bg_" + skinIdentifier + ".png";
            String birdDefaultPath = "assets/birds/bird_" + skinIdentifier + ".png";
            String birdFlyPath = "assets/birds/bird_" + skinIdentifier + "_fly.png";
            String topPipePath = "assets/pipes/toppipe_" + skinIdentifier + ".png";
            String bottomPipePath = "assets/pipes/bottompipe_" + skinIdentifier + ".png";

            // Check if files exist and load them
            File bgFile = new File(bgPath);
            File birdDefaultFile = new File(birdDefaultPath);
            File birdFlyFile = new File(birdFlyPath);
            File topPipeFile = new File(topPipePath);
            File bottomPipeFile = new File(bottomPipePath);

            if (bgFile.exists()) {
                backgroundImg = new ImageIcon(bgFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Background image not found: " + bgFile.getAbsolutePath());
                backgroundImg = null;
            }

            if (birdDefaultFile.exists()) {
                birdDefaultImg = new ImageIcon(birdDefaultFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Bird default image not found: " + birdDefaultFile.getAbsolutePath());
                birdDefaultImg = null;
            }

            if (birdFlyFile.exists()) {
                birdFlyImg = new ImageIcon(birdFlyFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Bird fly image not found: " + birdFlyFile.getAbsolutePath());
                birdFlyImg = null;
            }

            if (topPipeFile.exists()) {
                topPipeImg = new ImageIcon(topPipeFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Top pipe image not found: " + topPipeFile.getAbsolutePath());
                topPipeImg = null;
            }

            if (bottomPipeFile.exists()) {
                bottomPipeImg = new ImageIcon(bottomPipeFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Bottom pipe image not found: " + bottomPipeFile.getAbsolutePath());
                bottomPipeImg = null;
            }

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