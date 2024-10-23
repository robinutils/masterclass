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
    private List<Circle> circles;
    private Timer animationTimer;
    private Image backgroundImage;
    
    // Références des composants
    private JTextField nomJoueurField;
    private JButton commencerBtn;
    private JPanel difficultyPanel;
    private JLabel instructionLabel;
    private JButton jouerBtn;
    
    public AccueilPanel(MasterMindGame game) {
        this.game = game;
        setLayout(new GridBagLayout());
        setBackground(new Color(40, 44, 52));

        circles = new ArrayList<>();
        initCircles();

        animationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCircles();
                repaint();
            }
        });
        animationTimer.start();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        ImageIcon settingsIcon = new ImageIcon("./gear_icon.png"); 
        JButton settingsButton = new JButton(settingsIcon);
        settingsButton.setPreferredSize(new Dimension(200, 200));
        settingsButton.setBorder(BorderFactory.createEmptyBorder());
        settingsButton.setContentAreaFilled(false);
        settingsButton.addActionListener(e -> openThemeDialog());
        add(settingsButton, gbc);

        // Centrer le reste des éléments
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;

        JLabel welcomeLabel = new JLabel("MasterClass");
        welcomeLabel.setFont(loadCustomFont("./mario_font.ttf", 48f));
        welcomeLabel.setForeground(Color.WHITE);
        add(welcomeLabel, gbc);

        gbc.gridy++;

        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(50, 50, 50));
        instructionPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        
        JLabel instructionTitle = new JLabel("Consignes du Jeu");
        instructionTitle.setFont(loadCustomFont("./mario_font.ttf", 24f));
        instructionTitle.setForeground(Color.WHITE);
        instructionTitle.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel instructionText = new JLabel("<html><body style='text-align:center;'>"
                + "1. Choisissez la difficulté.<br>"
                + "2. Devinez la combinaison de couleurs secrète.<br>"
                + "3. Utilisez les indices bien placés et mal placés.<br>"
                + "4. Trouvez la combinaison avant d'épuiser vos tentatives!"
                + "</body></html>");
        instructionText.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionText.setForeground(Color.LIGHT_GRAY);
        instructionText.setAlignmentX(CENTER_ALIGNMENT);

        instructionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        instructionPanel.add(instructionTitle);
        instructionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        instructionPanel.add(instructionText);
        instructionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        gbc.gridy++;
        add(instructionPanel, gbc);

        gbc.gridy++;
        classementLabel = new JLabel();
        afficherClassementAccueil();
        classementLabel.setForeground(Color.WHITE);
        classementLabel.setFont(loadCustomFont("./mario_font.ttf", 20f));
        add(classementLabel, gbc);

        gbc.gridy++;
        difficultyPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        difficultyPanel.setBackground(new Color(40, 44, 52));

        // Boutons de difficulté
        JButton facileBtn = createCustomButton("Facile");
        facileBtn.addActionListener(e -> afficherNomEtJouerBtn(MasterMindGame.FACILE_MAX_TENTATIVES));
        difficultyPanel.add(facileBtn);

        JButton moyenBtn = createCustomButton("Moyen");
        moyenBtn.addActionListener(e -> afficherNomEtJouerBtn(MasterMindGame.MOYEN_MAX_TENTATIVES));
        difficultyPanel.add(moyenBtn);

        JButton difficileBtn = createCustomButton("Difficile");
        difficileBtn.addActionListener(e -> afficherNomEtJouerBtn(MasterMindGame.DIFFICILE_MAX_TENTATIVES));
        difficultyPanel.add(difficileBtn);

        add(difficultyPanel, gbc);

        // Initialement, on ne montre pas le champ pour le nom et le bouton commencer
        gbc.gridy++;
        nomJoueurField = new JTextField(20);
        nomJoueurField.setFont(loadCustomFont("./mario_font.ttf", 24f));
        nomJoueurField.setMinimumSize(new Dimension(400, 40));
        nomJoueurField.setVisible(false); // Masqué initialement
        add(nomJoueurField, gbc);

        gbc.gridy++;
        jouerBtn = new JButton("Commencer");
        jouerBtn.setFont(loadCustomFont("./mario_font.ttf", 26f));
        jouerBtn.setPreferredSize(new Dimension(200, 80));
        jouerBtn.setBackground(new Color(70, 130, 180));
        jouerBtn.setForeground(Color.WHITE);
        jouerBtn.setVisible(false); // Masqué initialement
        jouerBtn.addActionListener(e -> {
            String nomJoueur = nomJoueurField.getText().trim();
            if (nomJoueur.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nom de joueur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
            int maxTentatives = (int) jouerBtn.getClientProperty("maxTentatives");
            game.commencerPartie(nomJoueur, maxTentatives);
            }
        });
        add(jouerBtn, gbc);
    }

    // Méthode pour afficher le champ de nom et le bouton "Commencer" après sélection de la difficulté
    private void afficherNomEtJouerBtn(int maxTentatives) {
        // On cache les boutons de difficulté et affiche le champ de nom et le bouton "Commencer"
        difficultyPanel.setVisible(false);

        nomJoueurField.setVisible(true);
        jouerBtn.setVisible(true);

        jouerBtn.putClientProperty("maxTentatives", maxTentatives);

        // Réarranger et rafraîchir la mise en page
        revalidate();
        repaint();
    }

    // Ouvrir la boîte de dialogue pour changer le thème
    private void openThemeDialog() {
        String[] themes = {"Retro", "Light", "Dark"};
        String selectedTheme = (String) JOptionPane.showInputDialog(this,
                "Sélectionnez un thème :",
                "Choix du Thème",
                JOptionPane.PLAIN_MESSAGE,
                null,
                themes,
                themes[0]);

        if (selectedTheme != null) {
            game.setTheme(selectedTheme);
            applyThemeToAllComponents();
        }
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setFont(loadCustomFont("./mario_font.ttf", 26f));
        button.setPreferredSize(new Dimension(200, 80));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
        }

        private void afficherClassementAccueil() {
            List<Score> classement = game.getClassement();
            if (classement.isEmpty()) {
                classementLabel.setVisible(false);
            } else {
                StringBuilder classementStr = new StringBuilder("<html><body><h2>Classement:</h2>");
                for (int i = 0; i < Math.min(classement.size(), 5); i++) {
                    Score score = classement.get(i);
                    if (i == 0) {
                        classementStr.append("<p><b>").append((i + 1)).append(". ").append(score.getNom())
                            .append(" <img src='file:./crown_icon.png' width='16' height='16'/> - ")
                            .append(score.getTemps()).append("s</b></p>");
                    } else {
                        classementStr.append("<p>").append((i + 1)).append(". ").append(score.getNom())
                            .append(" - ").append(score.getTemps()).append("s</p>");
                    }
                }
                classementStr.append("</body></html>");
                classementLabel.setText(classementStr.toString());
                classementLabel.setVisible(true);
            }
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
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        for (Circle circle : circles) {
            circle.draw(g2d);
        }
    }

    private Font loadCustomFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(path));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size);
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
