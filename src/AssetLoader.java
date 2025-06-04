import java.awt.Image;
import javax.swing.ImageIcon;

public class AssetLoader {
    private Image backgroundImg;
    private Image birdDefaultImg;
    private Image birdFlyImg;
    private Image topPipeImg;
    private Image bottomPipeImg;

    public AssetLoader() {
        try {
            backgroundImg = new ImageIcon(getClass().getResource(GameConstants.BG_IMG_PATH)).getImage();
            birdDefaultImg = new ImageIcon(getClass().getResource(GameConstants.BIRD_DEFAULT_IMG_PATH)).getImage();
            birdFlyImg = new ImageIcon(getClass().getResource(GameConstants.BIRD_FLY_IMG_PATH)).getImage();
            topPipeImg = new ImageIcon(getClass().getResource(GameConstants.TOP_PIPE_IMG_PATH)).getImage();
            bottomPipeImg = new ImageIcon(getClass().getResource(GameConstants.BOTTOM_PIPE_IMG_PATH)).getImage();
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
            // Pertimbangkan untuk throw exception atau menggunakan gambar placeholder
        }
    }

    public Image getBackgroundImg() { return backgroundImg; }
    public Image getBirdDefaultImg() { return birdDefaultImg; }
    public Image getBirdFlyImg() { return birdFlyImg; }
    public Image getTopPipeImg() { return topPipeImg; }
    public Image getBottomPipeImg() { return bottomPipeImg; }
}