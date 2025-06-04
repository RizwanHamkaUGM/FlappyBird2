import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel; 
    private static MainMenu mainMenu;
    private static FlappyBird flappyBirdGame;

    public static final String MAIN_MENU_PANEL = "MainMenu";
    public static final String FLAPPY_BIRD_PANEL = "FlappyBird";

    public static void main(String[] args) {
        frame = new JFrame("Flappy Bird Extended");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenu = new MainMenu();
        flappyBirdGame = new FlappyBird(); 

        mainPanel.add(mainMenu, MAIN_MENU_PANEL);
        mainPanel.add(flappyBirdGame, FLAPPY_BIRD_PANEL);

        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("start_game".equals(e.getActionCommand())) {
                    String selectedSkin = mainMenu.getSelectedSkinIdentifier();
                    flappyBirdGame.applySkinAndRestart(selectedSkin); 
                    
                    cardLayout.show(mainPanel, FLAPPY_BIRD_PANEL);
                    flappyBirdGame.requestFocusInWindow();
                }
            }
        });

        // Listener for FlappyBird panel changes (e.g., Esc to menu)
        flappyBirdGame.addPanelChangeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("go_to_main_menu".equals(e.getActionCommand())) {
                    // Ensure game timers are stopped if FlappyBird doesn't handle it fully on setGameOver
                    // (setGameOver should already be called in FlappyBird before notifying)
                    cardLayout.show(mainPanel, MAIN_MENU_PANEL);
                    mainMenu.requestFocusInWindow();
                }
            }
        });

        frame.getContentPane().add(mainPanel);
        frame.pack(); 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        cardLayout.show(mainPanel, MAIN_MENU_PANEL);
        mainMenu.requestFocusInWindow();
    }
}