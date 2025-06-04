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

    private Rectangle startButtonBounds;
    private List<ActionListener> listeners = new ArrayList<>();

    private static final int PANEL_WIDTH = 1280;
    private static final int PANEL_HEIGHT = 720;

    public MainMenu() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setFocusable(true); 

        loadImages();
        calculateButtonBounds();

        currentStartButtonImage = startButtonImage;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (startButtonBounds != null && startButtonBounds.contains(e.getPoint())) {
                    // Tombol ditekan
                    notifyListeners("start_game");
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (startButtonBounds != null && startButtonBounds.contains(e.getPoint())) {
                    if (startButtonHoverImage != null) {
                        currentStartButtonImage = startButtonHoverImage;
                    }
                    // Anda bisa menambahkan efek lain di sini jika tidak ada gambar hover
                    // misalnya, mengubah cursor: setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    currentStartButtonImage = startButtonImage;
                    // setCursor(Cursor.getDefaultCursor());
                }
                repaint(); // Gambar ulang untuk menampilkan perubahan tombol
            }
        });
    }

    private void loadImages() {
        try {
            backgroundImage = new ImageIcon(getClass().getResource("./assets/menu/main_background.png")).getImage();
            titleImage = new ImageIcon(getClass().getResource("./assets/menu/title.png")).getImage();
            startButtonImage = new ImageIcon(getClass().getResource("./assets/menu/start_button.png")).getImage();

            // Coba muat gambar hover, jika tidak ada, startButtonHoverImage akan null
            java.net.URL hoverImgURL = getClass().getResource("./assets/menu/start_button_hover.png");
            if (hoverImgURL != null) {
                startButtonHoverImage = new ImageIcon(hoverImgURL).getImage();
            } else {
                startButtonHoverImage = null; // Tidak ada gambar hover spesifik
            }

        } catch (Exception e) {
            System.err.println("Error loading menu images: " + e.getMessage());
            e.printStackTrace();
            // Jika gambar gagal dimuat, komponen mungkin tidak terlihat benar.
            // Anda bisa mengatur gambar fallback atau menampilkan pesan error.
        }
    }

    private void calculateButtonBounds() {
        if (startButtonImage != null) {
            int buttonWidth = startButtonImage.getWidth(null);
            int buttonHeight = startButtonImage.getHeight(null);

            // Posisi tombol start (misalnya, tengah bawah)
            // Sesuaikan nilai ini untuk mendapatkan tata letak yang diinginkan
            int buttonX = (PANEL_WIDTH - buttonWidth) / 2;
            // Perkirakan Y berdasarkan posisi title atau persentase tinggi panel
            int buttonY = PANEL_HEIGHT / 2 + 50; // Sesuaikan angka 50 ini
            if (titleImage != null) {
                 // Posisi tombol Y sedikit di bawah tengah, setelah title
                 // Asumsikan title ditaruh di sekitar PANEL_HEIGHT / 3 atau PANEL_HEIGHT * 0.35
                buttonY = (int) (PANEL_HEIGHT * 0.55); // Atau (int) (PANEL_HEIGHT * 0.35 + titleImage.getHeight(null) + 20);
            }


            startButtonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Gambar background
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        } else {
            g2d.setColor(Color.DARK_GRAY); // Fallback jika background tidak ada
            g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        }

        // Gambar title
        if (titleImage != null) {
            int titleX = (PANEL_WIDTH - titleImage.getWidth(null)) / 2;
            // Sesuaikan nilai Y ini untuk posisi vertikal title
            int titleY = (int) (PANEL_HEIGHT * 0.25); // Misalnya 25% dari atas
            g2d.drawImage(titleImage, titleX, titleY, null);
        }

        // Gambar tombol start
        if (currentStartButtonImage != null && startButtonBounds != null) {
            g2d.drawImage(currentStartButtonImage, startButtonBounds.x, startButtonBounds.y, null);
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

    // Main method untuk testing (opsional)
    public static void main(String[] args) {
        JFrame frame = new JFrame("Main Menu Test");
        MainMenu mainMenu = new MainMenu();
        
        mainMenu.addActionListener(e -> {
            if ("start_game".equals(e.getActionCommand())) {
                System.out.println("Start button clicked!");
                // Di sini Anda akan mengganti panel atau memulai game
                JOptionPane.showMessageDialog(frame, "Start Game Clicked!");
            }
        });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainMenu);
        frame.pack(); // Mengatur ukuran frame sesuai preferredSize dari panel
        frame.setLocationRelativeTo(null); // Frame di tengah layar
        frame.setVisible(true);
    }
}