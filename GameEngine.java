import java.io.*;
import java.util.Random;

public class GameEngine {
    private static final String[] COULEURS = {"Rouge", "Vert", "Bleu", "Jaune", "Noir", "Blanc"};
    private final int nbPions;
    private String[] combinaisonSecrete;

    public GameEngine(int nbPions) {
        this.nbPions = nbPions;
        genererCombinaisonSecrete();
    }

    public void genererCombinaisonSecrete() {
        Random random = new Random();
        combinaisonSecrete = new String[nbPions];
        for (int i = 0; i < nbPions; i++) {
            combinaisonSecrete[i] = COULEURS[random.nextInt(COULEURS.length)];
        }
    }

    public VerificationResult verifierCombinaison(String[] proposition) {
        int bienPlaces = 0;
        int malPlaces = 0;
        boolean[] dejaVerifie = new boolean[nbPions];

        for (int i = 0; i < nbPions; i++) {
            if (proposition[i].equalsIgnoreCase(combinaisonSecrete[i])) {
                bienPlaces++;
                dejaVerifie[i] = true;
            }
        }

        for (int i = 0; i < nbPions; i++) {
            if (!proposition[i].equalsIgnoreCase(combinaisonSecrete[i])) {
                for (int j = 0; j < nbPions; j++) {
                    if (!dejaVerifie[j] && proposition[i].equalsIgnoreCase(combinaisonSecrete[j])) {
                        malPlaces++;
                        dejaVerifie[j] = true;
                        break;
                    }
                }
            }
        }

        return new VerificationResult(bienPlaces, malPlaces);
    }

    public void sauvegarderPartie() throws IOException {
        File file = new File("sauvegarde_partie.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(String.join(",", combinaisonSecrete));
        writer.close();
    }

    public void chargerPartie() throws IOException {
        File file = new File("sauvegarde_partie.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        combinaisonSecrete = line.split(",");
        reader.close();
    }
}
