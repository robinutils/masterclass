import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccueilPanel extends JPanel {
    private final MasterMindGame game;
    private JLabel classementLabel;
    private List<Circle> circles; // Liste des cercles colorés
    private Timer animationTimer; // Timer pour l'animation des cercles

    public AccueilPanel(MasterMindGame game) {
        this.game = game;
        setLayout(new GridBagLayout());
        setBackground(new Color(40, 44, 52));

        circles = new ArrayList<>();
        initCircles();

        // Créer un timer pour l'animation
        animationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCircles();
                repaint(); // Redessine les cercles
            }
        });
        animationTimer.start(); // Démarrer l'animation

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("MasterClass");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeLabel.setForeground(Color.WHITE);
        add(welcomeLabel, gbc);

        gbc.gridy++;
        classementLabel = new JLabel();
        afficherClassementAccueil();
        classementLabel.setForeground(Color.WHITE);
        classementLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        add(classementLabel, gbc);

        gbc.gridy++;
        JLabel themeLabel = new JLabel("Choisissez un thème :");
        themeLabel.setForeground(Color.WHITE);
        themeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(themeLabel, gbc);

        gbc.gridy++;
        String[] themes = {"Retro", "Light", "Dark"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        themeComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        themeComboBox.setPreferredSize(new Dimension(200, 40));
        themeComboBox.addActionListener(e -> {
            game.setTheme((String) themeComboBox.getSelectedItem());
            applyThemeToAllComponents(); 
        });
        add(themeComboBox, gbc);

        gbc.gridy++;
        JLabel instructionLabel = new JLabel("Entrez votre nom et choisissez le niveau de difficulte pour commencer.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        instructionLabel.setForeground(Color.LIGHT_GRAY);
        add(instructionLabel, gbc);

        gbc.gridy++;
        JTextField nomJoueurField = new JTextField(20); 
        nomJoueurField.setFont(new Font("Arial", Font.PLAIN, 24));
        nomJoueurField.setMinimumSize(new Dimension(400, 40)); 
        add(nomJoueurField, gbc);

        gbc.gridy++;
        JLabel difficultyLabel = new JLabel("Choisissez le niveau de difficulte :");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        difficultyLabel.setForeground(Color.WHITE);
        add(difficultyLabel, gbc);

        gbc.gridy++;
        JPanel difficultyPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        difficultyPanel.setBackground(new Color(40, 44, 52));

        JButton facileBtn = createCustomButton("Facile");
        facileBtn.addActionListener(e -> game.commencerPartie(nomJoueurField.getText(), MasterMindGame.FACILE_MAX_TENTATIVES));
        difficultyPanel.add(facileBtn);

        JButton moyenBtn = createCustomButton("Moyen");
        moyenBtn.addActionListener(e -> game.commencerPartie(nomJoueurField.getText(), MasterMindGame.MOYEN_MAX_TENTATIVES));
        difficultyPanel.add(moyenBtn);

        JButton difficileBtn = createCustomButton("Difficile");
        difficileBtn.addActionListener(e -> game.commencerPartie(nomJoueurField.getText(), MasterMindGame.DIFFICILE_MAX_TENTATIVES));
        difficultyPanel.add(difficileBtn);

        add(difficultyPanel, gbc);
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 26));
        button.setPreferredSize(new Dimension(200, 80));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }

    private void afficherClassementAccueil() {
        StringBuilder classementStr = new StringBuilder("<html><body><h2>Classement:</h2>");
        for (int i = 0; i < game.getClassement().size(); i++) {
            Score score = game.getClassement().get(i);
            classementStr.append("<p>").append((i + 1)).append(". ").append(score.getNom())
                .append(" - ").append(score.getTemps()).append("s</p>");
        }
        classementStr.append("</body></html>");
        classementLabel.setText(classementStr.toString());
    }

    private void applyThemeToAllComponents() {
        game.setTheme(this); 
    }

    private void initCircles() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int radius = 20 + rand.nextInt(40); 
            int x = rand.nextInt(800); 
            int y = rand.nextInt(600); 
            int dx = 1 + rand.nextInt(5); 
            int dy = 1 + rand.nextInt(5); 
            Color color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)); 
            circles.add(new Circle(x, y, radius, dx, dy, color));
        }
    }

    private void moveCircles() {
        for (Circle circle : circles) {
            circle.move(getWidth(), getHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Circle circle : circles) {
            circle.draw(g2d);
        }
    }

    class Circle {
        private int x, y, radius, dx, dy;
        private Color color;

        public Circle(int x, int y, int radius, int dx, int dy, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }

        public void move(int panelWidth, int panelHeight) {
            x += dx;
            y += dy;

            if (x < 0 || x + radius * 2 > panelWidth) {
                dx = -dx;
            }
            if (y < 0 || y + radius * 2 > panelHeight) {
                dy = -dy;
            }
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval(x, y, radius * 2, radius * 2);
        }
    }
}
