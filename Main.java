import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MasterMindGame game = new MasterMindGame();
            game.setSize(800, 600);  // Définir une taille initiale
            game.setResizable(true); // Permettre le redimensionnement
            game.setVisible(true);
        });
    }
}
