public class VerificationResult {
    private final int bienPlaces;
    private final int malPlaces;

    public VerificationResult(int bienPlaces, int malPlaces) {
        this.bienPlaces = bienPlaces;
        this.malPlaces = malPlaces;
    }

    public int getBienPlaces() {
        return bienPlaces;
    }

    public int getMalPlaces() {
        return malPlaces;
    }
}
