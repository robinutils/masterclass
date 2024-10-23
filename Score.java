public class Score {
    private final String nom;
    private final long temps;

    public Score(String nom, long temps) {
        this.nom = nom;
        this.temps = temps;
    }

    public String getNom() {
        return nom;
    }

    public long getTemps() {
        return temps;
    }
}
