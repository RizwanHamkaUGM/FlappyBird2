import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends JPanel {

    private Image backgroundImage;
    private Image titleImage;
    private Image startButtonImage;
    private Image startButtonHoverImage;
    private Image currentStartButtonImage;

    private Image skinButtonImage;
    private Image skinButtonHoverImage;
    private Image currentSkinButtonImage;

    private Rectangle startButtonBounds;
    private Rectangle skinButtonBounds;
    private List<ActionListener> listeners = new ArrayList<>();

    private int currentSkinIndex = 0;
    // Friendly names for skins, ensure this array length matches GameConstants.SKIN_IDENTIFIERS
    private final String[] skinDisplayNames = {"Theme: Classic", "Theme: Retro", "Theme: Snowy"};


    private static final int PANEL_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int PANEL_HEIGHT = GameConstants.BOARD_HEIGHT;

    public MainMenu() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setFocusable(true);

        loadMenuImagesAndInitialBackground(); // Renamed for clarity
        calculateButtonBounds();

        currentStartButtonImage = startButtonImage;
        currentSkinButtonImage = skinButtonImage;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (startButtonBounds != null && startButtonBounds.contains(e.getPoint())) {
                    notifyListeners("start_game");
                } else if (skinButtonBounds != null && skinButtonBounds.contains(e.getPoint())) {
                    currentSkinIndex = (currentSkinIndex + 1) % GameConstants.SKIN_IDENTIFIERS.length;
                    loadSkinnedMainMenuBackground(GameConstants.SKIN_IDENTIFIERS[currentSkinIndex]);
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean needsRepaint = false;
                if (startButtonBounds != null && startButtonBounds.contains(e.getPoint())) {
                    if (currentStartButtonImage != startButtonHoverImage && startButtonHoverImage != null) {
                        currentStartButtonImage = startButtonHoverImage;
                        needsRepaint = true;
                    }
                } else {
                    if (currentStartButtonImage != startButtonImage) {
                        currentStartButtonImage = startButtonImage;
                        needsRepaint = true;
                    }
                }

                if (skinButtonBounds != null && skinButtonBounds.contains(e.getPoint())) {
                    if (currentSkinButtonImage != skinButtonHoverImage && skinButtonHoverImage != null) {
                        currentSkinButtonImage = skinButtonHoverImage;
                        needsRepaint = true;
                    }
                } else {
                    if (currentSkinButtonImage != skinButtonImage) {
                        currentSkinButtonImage = skinButtonImage;
                        needsRepaint = true;
                    }
                }

                if (needsRepaint) {
                    repaint();
                }
            }
        });
    }

    private void loadMenuImagesAndInitialBackground() {
        try {
            // Load static menu images
            titleImage = new ImageIcon(getClass().getResource(GameConstants.MAIN_MENU_TITLE_PATH)).getImage();
            startButtonImage = new ImageIcon(getClass().getResource(GameConstants.START_BUTTON_PATH)).getImage();
            skinButtonImage = new ImageIcon(getClass().getResource(GameConstants.SKIN_BUTTON_PATH)).getImage();

            java.net.URL startHoverImgURL = getClass().getResource(GameConstants.START_BUTTON_HOVER_PATH);
            startButtonHoverImage = (startHoverImgURL != null) ? new ImageIcon(startHoverImgURL).getImage() : startButtonImage;

            java.net.URL skinHoverImgURL = getClass().getResource(GameConstants.SKIN_BUTTON_HOVER_PATH);
            skinButtonHoverImage = (skinHoverImgURL != null) ? new ImageIcon(skinHoverImgURL).getImage() : skinButtonImage;

        } catch (Exception e) {
            System.err.println("Error loading static menu images: " + e.getMessage());
            e.printStackTrace();
        }
        // Load initial background based on default skin
        loadSkinnedMainMenuBackground(GameConstants.SKIN_IDENTIFIERS[currentSkinIndex]);
    }
    
    private void loadSkinnedMainMenuBackground(String skinIdentifier) {
        try {
            String bgPath = GameConstants.MAIN_MENU_BG_SKIN_PREFIX + skinIdentifier + GameConstants.IMG_EXTENSION;
            java.net.URL bgURL = getClass().getResource(bgPath);
            if (bgURL != null) {
                this.backgroundImage = new ImageIcon(bgURL).getImage();
            } else {
                // Fallback to the original generic main menu background if specific one not found
                this.backgroundImage = new ImageIcon(getClass().getResource(GameConstants.DEFAULT_MAIN_MENU_BG_PATH)).getImage();
                System.err.println("Skinned main menu background not found: " + bgPath + ". Using default: " + GameConstants.DEFAULT_MAIN_MENU_BG_PATH);
            }
        } catch (Exception e) {
            System.err.println("Error loading skinned main menu background for " + skinIdentifier + ": " + e.getMessage());
            // Fallback to default if any error
            try {
                 this.backgroundImage = new ImageIcon(getClass().getResource(GameConstants.DEFAULT_MAIN_MENU_BG_PATH)).getImage();
            } catch (Exception e2) {
                System.err.println("Error loading default main menu background: " + e2.getMessage());
                this.backgroundImage = null; 
            }
        }
    }


    private void calculateButtonBounds() {
        int buttonSpacing = 20;

        if (startButtonImage != null) {
            int startButtonWidth = startButtonImage.getWidth(null);
            int startButtonHeight = startButtonImage.getHeight(null);
            int startButtonX = (PANEL_WIDTH - startButtonWidth) / 2;
            int startButtonY = (int) (PANEL_HEIGHT * 0.55); // Adjusted position
            startButtonBounds = new Rectangle(startButtonX, startButtonY, startButtonWidth, startButtonHeight);

            if (skinButtonImage != null) {
                int skinButtonWidth = skinButtonImage.getWidth(null);
                int skinButtonHeight = skinButtonImage.getHeight(null);
                int skinButtonX = (PANEL_WIDTH - skinButtonWidth) / 2;
                int skinButtonY = startButtonY + startButtonHeight + buttonSpacing;
                skinButtonBounds = new Rectangle(skinButtonX, skinButtonY, skinButtonWidth, skinButtonHeight);
            }
        }  else {
            // Fallback if startButtonImage is null (e.g. path error)
            // So skin button can still be positioned relative to a default spot
            int fallBackStartY = (int) (PANEL_HEIGHT * 0.55);
            int fallBackHeight = 50; // Arbitrary height
             if (skinButtonImage != null) {
                int skinButtonWidth = skinButtonImage.getWidth(null);
                int skinButtonHeight = skinButtonImage.getHeight(null);
                int skinButtonX = (PANEL_WIDTH - skinButtonWidth) / 2;
                int skinButtonY = fallBackStartY + fallBackHeight + buttonSpacing;
                skinButtonBounds = new Rectangle(skinButtonX, skinButtonY, skinButtonWidth, skinButtonHeight);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        } else {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        }

        if (titleImage != null) {
            int titleX = (PANEL_WIDTH - titleImage.getWidth(null)) / 2;
            int titleY = (int) (PANEL_HEIGHT * 0.20);
            g2d.drawImage(titleImage, titleX, titleY, null);
        }

        if (currentStartButtonImage != null && startButtonBounds != null) {
            g2d.drawImage(currentStartButtonImage, startButtonBounds.x, startButtonBounds.y, null);
        }

        if (currentSkinButtonImage != null && skinButtonBounds != null) {
            g2d.drawImage(currentSkinButtonImage, skinButtonBounds.x, skinButtonBounds.y, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String skinText = (currentSkinIndex < skinDisplayNames.length) ? skinDisplayNames[currentSkinIndex] : "Skin: N/A";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(skinText);
            int textX = skinButtonBounds.x + (skinButtonBounds.width - textWidth) / 2;
            int textY = skinButtonBounds.y + skinButtonBounds.height + fm.getAscent() + 5;
             if (currentSkinButtonImage.getHeight(null) < 60 && currentSkinButtonImage.getHeight(null) > 0) { // If button image is short
                 textY = skinButtonBounds.y + (skinButtonBounds.height - fm.getHeight()) / 2 + fm.getAscent();
            } else if (currentSkinButtonImage.getHeight(null) == 0) { // No image, draw text inside fallback rect
                 textY = skinButtonBounds.y + (skinButtonBounds.height - fm.getHeight()) / 2 + fm.getAscent();
            }

            g2d.drawString(skinText, textX, textY);
        } else if (skinButtonBounds != null) {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(skinButtonBounds.x, skinButtonBounds.y, skinButtonBounds.width, skinButtonBounds.height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            String skinText = (currentSkinIndex < skinDisplayNames.length) ? skinDisplayNames[currentSkinIndex] : "Skin: N/A";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(skinText);
            int textX = skinButtonBounds.x + (skinButtonBounds.width - textWidth) / 2;
            int textY = skinButtonBounds.y + (skinButtonBounds.height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(skinText, textX, textY);
        }
    }

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String command) {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(event);
        }
    }

    public String getSelectedSkinIdentifier() {
        return GameConstants.SKIN_IDENTIFIERS[currentSkinIndex];
    }
}