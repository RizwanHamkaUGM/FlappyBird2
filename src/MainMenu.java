import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
    private final String[] skinDisplayNames = {"Theme: Classic", "Theme: Retro", "Theme: Snowy"};


    private static final int PANEL_WIDTH = GameConstants.BOARD_WIDTH;
    private static final int PANEL_HEIGHT = GameConstants.BOARD_HEIGHT;

    public MainMenu() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setFocusable(true);

        loadMenuImagesAndInitialBackground(); 
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
            // Load static menu images using file paths
            File titleFile = new File("assets/menu/title.png");
            File startButtonFile = new File("assets/menu/start_button.png");
            File skinButtonFile = new File("assets/menu/skin_button.png");
            File startHoverFile = new File("assets/menu/start_button_hover.png");
            File skinHoverFile = new File("assets/menu/skin_button_hover.png");

            if (titleFile.exists()) {
                titleImage = new ImageIcon(titleFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Title image not found: " + titleFile.getAbsolutePath());
                titleImage = null;
            }

            if (startButtonFile.exists()) {
                startButtonImage = new ImageIcon(startButtonFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Start button image not found: " + startButtonFile.getAbsolutePath());
                startButtonImage = null;
            }

            if (skinButtonFile.exists()) {
                skinButtonImage = new ImageIcon(skinButtonFile.getAbsolutePath()).getImage();
            } else {
                System.err.println("Skin button image not found: " + skinButtonFile.getAbsolutePath());
                skinButtonImage = null;
            }

            if (startHoverFile.exists()) {
                startButtonHoverImage = new ImageIcon(startHoverFile.getAbsolutePath()).getImage();
            } else {
                startButtonHoverImage = startButtonImage; 
            }

            if (skinHoverFile.exists()) {
                skinButtonHoverImage = new ImageIcon(skinHoverFile.getAbsolutePath()).getImage();
            } else {
                skinButtonHoverImage = skinButtonImage;
            }

        } catch (Exception e) {
            System.err.println("Error loading static menu images: " + e.getMessage());
            e.printStackTrace();
        }
        loadSkinnedMainMenuBackground(GameConstants.SKIN_IDENTIFIERS[currentSkinIndex]);
    }
    
    private void loadSkinnedMainMenuBackground(String skinIdentifier) {
        try {
            String bgPath = "assets/menu/main_background_" + skinIdentifier + ".png";
            File bgFile = new File(bgPath);
            
            if (bgFile.exists()) {
                this.backgroundImage = new ImageIcon(bgFile.getAbsolutePath()).getImage();
            } else {
                // Fallback to the original generic main menu background if specific one not found
                File defaultBgFile = new File("assets/menu/main_background.png");
                if (defaultBgFile.exists()) {
                    this.backgroundImage = new ImageIcon(defaultBgFile.getAbsolutePath()).getImage();
                    System.err.println("Skinned main menu background not found: " + bgPath + ". Using default.");
                } else {
                    System.err.println("Default main menu background not found: " + defaultBgFile.getAbsolutePath());
                    this.backgroundImage = null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading skinned main menu background for " + skinIdentifier + ": " + e.getMessage());
            // Fallback to default if any error
            try {
                File defaultBgFile = new File("assets/menu/main_background.png");
                if (defaultBgFile.exists()) {
                    this.backgroundImage = new ImageIcon(defaultBgFile.getAbsolutePath()).getImage();
                } else {
                    this.backgroundImage = null;
                }
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
            int fallBackStartY = (int) (PANEL_HEIGHT * 0.55);
            int fallBackHeight = 50; 
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