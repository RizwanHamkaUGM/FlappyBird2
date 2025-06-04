import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    private AssetLoader assetLoader;
    private Bird bird;
    private PipeManager pipeManager;

    private int velocityY = 0;

    private Timer gameLoop;
    private Timer placePipeTimer;
    private Timer birdAnimationTimer;

    private boolean gameOver = false;
    private double score = 0;
    private Random random = new Random();

    private List<ActionListener> panelChangeListeners = new ArrayList<>(); // For Esc key

    public FlappyBird() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        assetLoader = new AssetLoader();
        initializeGameComponents(GameConstants.DEFAULT_SKIN_IDENTIFIER);

        placePipeTimer = new Timer(GameConstants.PIPE_SPAWN_DELAY_MS, e -> {
            if (!gameOver && pipeManager != null) pipeManager.placePipes();
        });
        
        birdAnimationTimer = new Timer(GameConstants.BIRD_ANIMATION_DELAY_MS, e -> {
            if (!gameOver && bird != null && assetLoader.getBirdDefaultImg() != null) {
                bird.setImg(assetLoader.getBirdDefaultImg());
            }
        });
        birdAnimationTimer.setRepeats(false);
        
        gameLoop = new Timer(1000 / 60, this);
    }

    private void initializeGameComponents(String skinIdentifier) {
        assetLoader.loadAssetsForSkin(skinIdentifier);

        if (assetLoader.getBirdDefaultImg() != null) {
            bird = new Bird(assetLoader.getBirdDefaultImg(),
                            GameConstants.BIRD_START_X,
                            GameConstants.BIRD_START_Y,
                            GameConstants.BIRD_WIDTH,
                            GameConstants.BIRD_HEIGHT);
        } else {
            System.err.println("Bird default image is null. Bird not initialized.");
            bird = null; // Or a placeholder if you have one
        }

        if (assetLoader.getTopPipeImg() != null && assetLoader.getBottomPipeImg() != null) {
            pipeManager = new PipeManager(assetLoader.getTopPipeImg(), assetLoader.getBottomPipeImg(), random);
        } else {
            System.err.println("Pipe images are null. PipeManager not initialized.");
            pipeManager = null; // Or a placeholder
        }
    }
    
    public void applySkinAndRestart(String skinIdentifier) {
        initializeGameComponents(skinIdentifier);
        startGame();
    }

    public void startGame() {
        if (bird == null || pipeManager == null) {
            System.err.println("Cannot start game: bird or pipeManager is null. Attempting re-initialization.");
            initializeGameComponents(assetLoader.getCurrentSkinIdentifier() != null ? assetLoader.getCurrentSkinIdentifier() : GameConstants.DEFAULT_SKIN_IDENTIFIER);
            if (bird == null || pipeManager == null) {
                 System.err.println("Re-initialization failed. Game cannot start.");
                 gameOver = true; // Prevent game from running with null components
                 repaint();
                 return;
            }
        }

        bird.setY(GameConstants.BIRD_START_Y);
        if (assetLoader.getBirdDefaultImg() != null) {
            bird.setImg(assetLoader.getBirdDefaultImg());
        }
        velocityY = 0;
        
        pipeManager.resetPipes();
        score = 0;
        gameOver = false;

        gameLoop.start();
        placePipeTimer.stop(); 
        placePipeTimer.start();
        pipeManager.placePipes(); 
    }

    private void restartGame() {
        applySkinAndRestart(assetLoader.getCurrentSkinIdentifier());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (assetLoader.getBackgroundImg() == null && !gameOver) {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Error: Background asset not loaded!", 50, 50);
        } else if (assetLoader.getBackgroundImg() != null) {
             g.drawImage(assetLoader.getBackgroundImg(), 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);
        }


        if (!gameOver) {
            if (bird != null && bird.getImg() != null) {
                g.drawImage(bird.getImg(), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight(), null);
            }
            if (pipeManager != null) {
                for (Pipe pipe : pipeManager.getPipes()) {
                    if (pipe.getImg() != null) {
                        g.drawImage(pipe.getImg(), pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight(), null);
                    }
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString(String.valueOf((int) score), 10, 35);
        } else { // Game Over Screen
             // Fallback background if main one failed and game is over
            if (assetLoader.getBackgroundImg() == null) {
                g.setColor(Color.DARK_GRAY); // Darker background for game over if assets failed
                g.fillRect(0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT);
            }

            Image gameOverImage = assetLoader.getGameOverImg();
            if (gameOverImage != null) {
                int imgX = (GameConstants.BOARD_WIDTH - gameOverImage.getWidth(null)) / 2;
                int imgY = (GameConstants.BOARD_HEIGHT - gameOverImage.getHeight(null)) / 3;
                g.drawImage(gameOverImage, imgX, imgY, null);
            } else {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 72));
                FontMetrics fmTitle = g.getFontMetrics();
                String titleText = "GAME OVER";
                int titleWidth = fmTitle.stringWidth(titleText);
                g.drawString(titleText, (GameConstants.BOARD_WIDTH - titleWidth) / 2, GameConstants.BOARD_HEIGHT / 3);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics fmScore = g.getFontMetrics();
            String scoreText = "Score: " + (int) score;
            int scoreWidth = fmScore.stringWidth(scoreText);
            g.drawString(scoreText, (GameConstants.BOARD_WIDTH - scoreWidth) / 2, GameConstants.BOARD_HEIGHT / 2 + 50);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            FontMetrics fmInstructions = g.getFontMetrics();
            String restartText = "Press SPACE to restart";
            String menuText = "Press ESC for Main Menu";
            int restartTextWidth = fmInstructions.stringWidth(restartText);
            int menuTextWidth = fmInstructions.stringWidth(menuText);

            g.drawString(restartText, (GameConstants.BOARD_WIDTH - restartTextWidth) / 2, GameConstants.BOARD_HEIGHT / 2 + 110);
            g.drawString(menuText, (GameConstants.BOARD_WIDTH - menuTextWidth) / 2, GameConstants.BOARD_HEIGHT / 2 + 150);
        }
    }

    private void move() {
        if (bird == null || pipeManager == null) return;

        velocityY += GameConstants.GRAVITY;
        bird.setY(bird.getY() + velocityY);
        bird.setY(Math.max(bird.getY(), 0));

        score += pipeManager.updatePipesAndGetScore(bird);

        if (pipeManager.checkCollision(bird)) {
            setGameOver();
        }

        if (bird.getY() + bird.getHeight() >= GameConstants.BOARD_HEIGHT) {
            setGameOver();
            bird.setY(GameConstants.BOARD_HEIGHT - bird.getHeight());
        }
    }
    
    private void setGameOver() {
        if (gameOver) return;
        gameOver = true;
        gameLoop.stop();
        placePipeTimer.stop();
        if (birdAnimationTimer.isRunning()) {
            birdAnimationTimer.stop();
        }
        if (bird != null && assetLoader.getBirdDefaultImg() != null) {
           bird.setImg(assetLoader.getBirdDefaultImg()); 
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
        }
        repaint();
    }

    // Methods for panel change listener (for Esc key)
    public void addPanelChangeListener(ActionListener listener) {
        panelChangeListeners.add(listener);
    }

    private void notifyPanelChangeListeners(String command) {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        for (ActionListener listener : panelChangeListeners) {
            listener.actionPerformed(event);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                restartGame();
            } else if (bird != null && assetLoader.getBirdFlyImg() != null) {
                velocityY = GameConstants.BIRD_JUMP_VELOCITY;
                bird.setImg(assetLoader.getBirdFlyImg());
                if (birdAnimationTimer.isRunning()) {
                    birdAnimationTimer.restart();
                } else {
                    birdAnimationTimer.start();
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!gameOver) { // If game is running, set to game over first
                setGameOver();
                repaint(); // Repaint to show game over screen briefly if desired, or remove for instant switch
            }
            // Notify App to switch to MainMenu
            notifyPanelChangeListeners("go_to_main_menu");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}