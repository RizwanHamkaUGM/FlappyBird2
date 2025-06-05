import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipeManager {
    private ArrayList<Pipe> pipes;
    private Random random;
    private Image topPipeImg;
    private Image bottomPipeImg;

    public PipeManager(Image topPipeImg, Image bottomPipeImg, Random random) {
        this.pipes = new ArrayList<>();
        this.topPipeImg = topPipeImg;
        this.bottomPipeImg = bottomPipeImg;
        this.random = random;
    }

    public void placePipes() {
        // pipeY (0) adalah bagian atas area gambar.
        // randomPipeY akan negatif atau nol, merepresentasikan tepi atas pipa atas.
        int randomPipeY = (int) (0 - GameConstants.PIPE_HEIGHT / 4 - random.nextDouble() * (GameConstants.PIPE_HEIGHT / 2));

        Pipe topPipe = new Pipe(topPipeImg, GameConstants.BOARD_WIDTH, randomPipeY, GameConstants.PIPE_WIDTH, GameConstants.PIPE_HEIGHT);
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg, GameConstants.BOARD_WIDTH, topPipe.getY() + GameConstants.PIPE_HEIGHT + GameConstants.PIPE_OPENING_SPACE, GameConstants.PIPE_WIDTH, GameConstants.PIPE_HEIGHT);
        pipes.add(bottomPipe);
    }

    /**
     * Memperbarui posisi pipa dan mengecek skor.
     * @param bird Objek Bird untuk pengecekan skor.
     * @return Mengembalikan jumlah skor yang didapat pada frame ini (0 atau 0.5 per pipa yang dilewati).
     */
    public double updatePipesAndGetScore(Bird bird) {
        double scoreGainedThisFrame = 0;
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setX(pipe.getX() + GameConstants.VELOCITY_X_PIPES);

            if (!pipe.isPassed() && bird.getX() > pipe.getX() + pipe.getWidth()) {
                scoreGainedThisFrame += 5; 
                pipe.setPassed(true);
            }
        }
        pipes.removeIf(pipe -> pipe.getX() + pipe.getWidth() < 0);
        return scoreGainedThisFrame;
    }

    /**
     * Mengecek kolisi antara burung dan salah satu pipa.
     * @param bird Objek Bird.
     * @return true jika terjadi kolisi, false jika tidak.
     */
    public boolean checkCollision(Bird bird) {
        for (Pipe pipe : pipes) {
            if (collision(bird, pipe)) {
                return true;
            }
        }
        return false;
    }

    // Method collision
    private boolean collision(Bird a, Pipe b) {
        return a.getX() < b.getX() + b.getWidth() &&
            a.getX() + a.getWidth() > b.getX() &&
            a.getY() < b.getY() + b.getHeight() &&
            a.getY() + a.getHeight() > b.getY();
    }

    public List<Pipe> getPipes() {
        return pipes;
    }

    public void resetPipes() {
        pipes.clear();
    }
}