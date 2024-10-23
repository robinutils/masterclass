import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import java.util.stream.Collectors;

public class MasterMindGame extends JFrame {
    private static final int NB_PIONS = 4;
    private static final String[] COULEURS = {"Rouge", "Vert", "Bleu", "Jaune", "Noir", "Blanc"};
    static final int FACILE_MAX_TENTATIVES = 20;
    static final int MOYEN_MAX_TENTATIVES = 15;
    static final int DIFFICILE_MAX_TENTATIVES = 10;

    private String[] combinaisonSecrete;
    private JPanel mainPanel;
    private JButton[] jetons;
    private JPanel historiquePanel;
    private JButton validerBtn;
    private JLabel timerLabel;
    private Timer timer;
    private long startTime;
    private int maxTentatives;
    private String nomJoueur;
    private int tentative = 0;

    private List<Score> classement = new ArrayList<>();
    private String currentTheme = "Retro";

    public MasterMindGame() {
        super("MasterMind");
        setSize(800, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        showAccueil();
    }

    public void showAccueil() {
        getContentPane().removeAll();
        repaint();
        revalidate();

        AccueilPanel accueilPanel = new AccueilPanel(this);
        add(accueilPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void commencerPartie(String nomJoueur, int maxTentatives) {
        this.maxTentatives = maxTentatives;
        this.nomJoueur = nomJoueur;
        tentative = 0; 
        getContentPane().removeAll();
        repaint();
        revalidate();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setTheme(mainPanel);

        historiquePanel = new JPanel();
        historiquePanel.setLayout(new BoxLayout(historiquePanel, BoxLayout.Y_AXIS));
        setTheme(historiquePanel);
        JScrollPane scrollPane = new JScrollPane(historiquePanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2), "Historique des Tentatives", 0, 0, new Font("Press Start 2P", Font.BOLD, 14), Color.WHITE));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel jetonsPanel = new JPanel();
        jetonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setTheme(jetonsPanel);
        jetons = new JButton[NB_PIONS];
        for (int i = 0; i < NB_PIONS; i++) {
            jetons[i] = new JButton();
            jetons[i].setPreferredSize(new Dimension(80, 80));
            jetons[i].setBackground(Color.DARK_GRAY);
            jetons[i].setActionCommand("Jeton" + i);
            jetons[i].setFocusPainted(false);
            jetons[i].addActionListener(new JetonListener());
            jetons[i].setFont(new Font("Press Start 2P", Font.BOLD, 16));
            jetons[i].setForeground(Color.WHITE);
            jetons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            jetons[i].setOpaque(true);
            jetons[i].setBorderPainted(false);
            jetons[i].setUI(new CircleButtonUI());
            jetonsPanel.add(jetons[i]);
        }

        validerBtn = new JButton("Valider");
        validerBtn.setPreferredSize(new Dimension(200, 80));
        validerBtn.setFont(new Font("Press Start 2P", Font.BOLD, 18));
        validerBtn.setBackground(new Color(70, 130, 180));
        validerBtn.setForeground(Color.WHITE);
        validerBtn.setFocusPainted(false);
        validerBtn.addActionListener(new ValiderListener());
        jetonsPanel.add(validerBtn);

        mainPanel.add(jetonsPanel, BorderLayout.SOUTH);

        timerLabel = new JLabel("Temps : 0s");
        timerLabel.setFont(new Font("Press Start 2P", Font.BOLD, 18));
        timerLabel.setForeground(Color.WHITE);
        mainPanel.add(timerLabel, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

        genererCombinaisonSecrete();

        startTime = System.currentTimeMillis();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
                timerLabel.setText("Temps : " + elapsedSeconds + "s");
            }
        });
        timer.start();
    }

    private void genererCombinaisonSecrete() {
        Random random = new Random();
        combinaisonSecrete = new String[NB_PIONS];
        for (int i = 0; i < NB_PIONS; i++) {
            combinaisonSecrete[i] = COULEURS[random.nextInt(COULEURS.length)];
        }
    }

    private class JetonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String command = source.getActionCommand();
            int index = Integer.parseInt(command.replace("Jeton", ""));

            String currentColor = source.getText();
            int nextColorIndex = 0;
            if (!currentColor.isEmpty()) {
                for (int i = 0; i < COULEURS.length; i++) {
                    if (COULEURS[i].equals(currentColor)) {
                        nextColorIndex = (i + 1) % COULEURS.length;
                        break;
                    }
                }
            }
            source.setText(COULEURS[nextColorIndex]);
            source.setBackground(getColorFromName(COULEURS[nextColorIndex]));
        }
    }

    private Color getColorFromName(String colorName) {
        switch (colorName) {
            case "Rouge":
                return Color.RED;
            case "Vert":
                return Color.GREEN;
            case "Bleu":
                return Color.BLUE;
            case "Jaune":
                return Color.YELLOW;
            case "Noir":
                return Color.BLACK;
            case "Blanc":
                return Color.WHITE;
            default:
                return Color.DARK_GRAY;
        }
    }

    public void setTheme(String theme) {
        this.currentTheme = theme;
        applyThemeToComponents();
    }

    private void applyThemeToComponents() {
        if (mainPanel != null) setTheme(mainPanel);
        if (historiquePanel != null) setTheme(historiquePanel);
        if (jetons != null) {
            for (JButton jeton : jetons) {
                if (jeton != null) setTheme(jeton);
            }
        }
        if (validerBtn != null) setTheme(validerBtn);
        if (timerLabel != null) setTheme(timerLabel);
        repaint();
    }

    public void setTheme(JComponent component) {
        if (currentTheme.equals("Retro")) {
            component.setBackground(new Color(40, 44, 52));
            component.setForeground(Color.WHITE);
        } else if (currentTheme.equals("Light")) {
            component.setBackground(new Color(230, 230, 230));
            component.setForeground(Color.BLACK); 
        } else if (currentTheme.equals("Dark")) {
            component.setBackground(new Color(30, 30, 30));
            component.setForeground(Color.LIGHT_GRAY);
        }
    }

    private class ValiderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] proposition = new String[NB_PIONS];
            for (int i = 0; i < NB_PIONS; i++) {
                proposition[i] = jetons[i].getText();
            }
            verifierCombinaison(proposition);
        }
    }

    private void verifierCombinaison(String[] proposition) {
        tentative++;
        if (tentative > maxTentatives) {
            JOptionPane.showMessageDialog(this, "Vous avez depasse le nombre maximum de tentatives!", "Perdu!", JOptionPane.ERROR_MESSAGE);
            finPartie(false);
            return;
        }

        int bienPlaces = 0;
        int malPlaces = 0;
        boolean[] dejaVerifie = new boolean[NB_PIONS];

        for (int i = 0; i < NB_PIONS; i++) {
            if (proposition[i].equalsIgnoreCase(combinaisonSecrete[i])) {
                bienPlaces++;
                dejaVerifie[i] = true;
            }
        }

        for (int i = 0; i < NB_PIONS; i++) {
            if (!proposition[i].equalsIgnoreCase(combinaisonSecrete[i])) {
                for (int j = 0; j < NB_PIONS; j++) {
                    if (!dejaVerifie[j] && proposition[i].equalsIgnoreCase(combinaisonSecrete[j])) {
                        malPlaces++;
                        dejaVerifie[j] = true;
                        break;
                    }
                }
            }
        }

        JPanel tentativePanel = new JPanel();
        tentativePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setTheme(tentativePanel);

        for (int i = 0; i < NB_PIONS; i++) {
            JLabel jetonLabel = new JLabel();
            jetonLabel.setOpaque(true);
            jetonLabel.setPreferredSize(new Dimension(30, 30));
            jetonLabel.setBackground(getColorFromName(proposition[i]));
            jetonLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            jetonLabel.setUI(new CircleLabelUI());
            tentativePanel.add(jetonLabel);
        }

        JLabel resultLabel = new JLabel("Bien places: " + bienPlaces + " | Mal places: " + malPlaces);
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setFont(new Font("Press Start 2P", Font.PLAIN, 16));
        tentativePanel.add(resultLabel);

        historiquePanel.add(tentativePanel);
        historiquePanel.revalidate();
        historiquePanel.repaint();

        if (bienPlaces == NB_PIONS) {
            timer.stop();
            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
            JOptionPane.showMessageDialog(this, "Felicitations ! Vous avez trouve la combinaison secrete en " + elapsedSeconds + " secondes!", "Gagne!", JOptionPane.INFORMATION_MESSAGE);
            classement.add(new Score(nomJoueur, elapsedSeconds));
            classement = classement.stream().sorted(Comparator.comparingLong(Score::getTemps)).collect(Collectors.toList());
            finPartie(true);
        }
    }

    private void finPartie(boolean victoire) {
        if (victoire) {
            afficherClassement();
        }
        showAccueil();
    }

    private void afficherClassement() {
        StringBuilder classementStr = new StringBuilder("Classement :\n");
        for (int i = 0; i < classement.size(); i++) {
            Score score = classement.get(i);
            classementStr.append((i + 1)).append(". ").append(score.getNom()).append(" - ").append(score.getTemps()).append("s\n");
        }
        JOptionPane.showMessageDialog(this, classementStr.toString(), "Classement", JOptionPane.INFORMATION_MESSAGE);
    }

    public List<Score> getClassement() {
        return classement;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MasterMindGame game = new MasterMindGame();
            game.setVisible(true);
        });
    }
}
