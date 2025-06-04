import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    private AssetLoader assetLoader;
    private Bird bird;
    private PipeManager pipeManager;

    private int velocityY = 0; // Kecepatan vertikal burung

    private Timer gameLoop;
    private Timer placePipeTimer;
    private Timer birdAnimationTimer;

    private boolean gameOver = false;
    private double score = 0;
    private Random random = new Random();


    public FlappyBird() {
        setPreferredSize(new Dimension(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        assetLoader = new AssetLoader(); // Muat semua aset
        
        bird = new Bird(assetLoader.getBirdDefaultImg(),
                        GameConstants.BIRD_START_X,
                        GameConstants.BIRD_START_Y,
                        GameConstants.BIRD_WIDTH,
                        GameConstants.BIRD_HEIGHT);

        pipeManager = new PipeManager(assetLoader.getTopPipeImg(), assetLoader.getBottomPipeImg(), random);

        placePipeTimer = new Timer(GameConstants.PIPE_SPAWN_DELAY_MS, e -> pipeManager.placePipes());
        
        birdAnimationTimer = new Timer(GameConstants.BIRD_ANIMATION_DELAY_MS, e -> {
            if (!gameOver) bird.setImg(assetLoader.getBirdDefaultImg());
        });
        birdAnimationTimer.setRepeats(false);
        
        // Game loop utama
        gameLoop = new Timer(1000 / 60, this); // 'this' akan memanggil actionPerformed

        startGame(); // Memulai game untuk pertama kali
    }

    private void startGame() {
        bird.setY(GameConstants.BIRD_START_Y);
        bird.setImg(assetLoader.getBirdDefaultImg());
        velocityY = 0;
        
        pipeManager.resetPipes();
        pipeManager.placePipes(); 

        score = 0;
        gameOver = false;

        gameLoop.start();
        placePipeTimer.start();
    }

    private void restartGame() {
        startGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Background
        g.drawImage(assetLoader.getBackgroundImg(), 0, 0, GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT, null);

        // Bird
        g.drawImage(bird.getImg(), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight(), null);

        // Pipes
        for (Pipe pipe : pipeManager.getPipes()) {
            g.drawImage(pipe.getImg(), pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + (int) score, 10, 35);
                g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press SPACE to restart", GameConstants.BOARD_WIDTH/2 - 120, GameConstants.BOARD_HEIGHT/2 + 50);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    private void move() {
        // Bird
        velocityY += GameConstants.GRAVITY;
        bird.setY(bird.getY() + velocityY);
        bird.setY(Math.max(bird.getY(), 0)); // Batasi di atas layar

        // Pipes & Score
        score += pipeManager.updatePipesAndGetScore(bird);

        // Cek Kolisi
        if (pipeManager.checkCollision(bird)) {
            setGameOver();
        }

        // Cek jatuh ke bawah
        if (bird.getY() + bird.getHeight() > GameConstants.BOARD_HEIGHT) {
            setGameOver();
            bird.setY(GameConstants.BOARD_HEIGHT - bird.getHeight()); // Letakkan di dasar
        }
    }
    
    private void setGameOver() {
        if (gameOver) return; // Hanya set sekali

        gameOver = true;
        gameLoop.stop();
        placePipeTimer.stop();
        if (birdAnimationTimer.isRunning()) {
            birdAnimationTimer.stop();
        }
        // Pastikan gambar burung kembali ke default saat game over, bukan saat terbang
        bird.setImg(assetLoader.getBirdDefaultImg()); 
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Dipanggil oleh gameLoop
        if (!gameOver) {
            move();
        }
        repaint(); // Selalu repaint untuk menampilkan state terbaru (termasuk layar game over)
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                velocityY = GameConstants.BIRD_JUMP_VELOCITY;
                bird.setImg(assetLoader.getBirdFlyImg());
                if (birdAnimationTimer.isRunning()) {
                    birdAnimationTimer.restart();
                } else {
                    birdAnimationTimer.start();
                }
            } else {
                // Jika game over, spasi akan merestart game
                restartGame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}