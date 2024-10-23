import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MasterMindGame game = new MasterMindGame();
            game.setVisible(true);
        });
    }
}
