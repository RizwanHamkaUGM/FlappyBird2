import java.awt.Image;

public class Bird {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image img; 

    public Bird(Image initialImg, int startX, int startY, int width, int height) {
        this.img = initialImg;
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImg() {
        return img;
    }

    // Setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setImg(Image img) {
        this.img = img;
    }
}