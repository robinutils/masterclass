import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView {
    private final JPanel mainPanel;
    private final JTextField[] inputs;
    private final JTextArea historique;
    private final JButton validerBtn;
    private final GameEngine gameEngine;

    public GameView(int nbPions, GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JPanel combinaisonPanel = new JPanel();
        combinaisonPanel.setLayout(new FlowLayout());
        inputs = new JTextField[nbPions];
        for (int i = 0; i < nbPions; i++) {
            inputs[i] = new JTextField(5);
            combinaisonPanel.add(inputs[i]);
        }
        mainPanel.add(combinaisonPanel);

        validerBtn = new JButton("Valider");
        validerBtn.addActionListener(new ValiderListener());
        mainPanel.add(validerBtn);

        historique = new JTextArea();
        historique.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historique);
        mainPanel.add(scrollPane);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private class ValiderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] proposition = new String[inputs.length];
            for (int i = 0; i < inputs.length; i++) {
                proposition[i] = inputs[i].getText();
            }
            VerificationResult result = gameEngine.verifierCombinaison(proposition);
            historique.append("Proposition : " + String.join(", ", proposition) + " | Bien placés: " + result.getBienPlaces() + " | Mal placés: " + result.getMalPlaces() + "\n");

            if (result.getBienPlaces() == inputs.length) {
                JOptionPane.showMessageDialog(mainPanel, "Félicitations ! Vous avez trouvé la combinaison secrète !");
                gameEngine.genererCombinaisonSecrete();
                historique.setText("");
                for (JTextField input : inputs) {
                    input.setText("");
                }
            }
        }
    }

    public void updateHistorique(String message) {
        historique.append(message);
    }
}
