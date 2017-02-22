package ch.globaz.naos.ree.tools;

public class InfoCaisse {

    private int numeroCaisse;
    private int numeroAgence;
    private String numeroCaisseFormate;
    private String numeroAgenceFormate;

    /**
     * @param numeroCaisse
     * @param numeroAgence
     * @param numeroCaisseFormate
     * @param numeroAgenceFormate
     */
    public InfoCaisse(int numeroCaisse, int numeroAgence, String numeroCaisseFormate, String numeroAgenceFormate) {
        this.numeroCaisse = numeroCaisse;
        this.numeroAgence = numeroAgence;
        this.numeroCaisseFormate = numeroCaisseFormate;
        this.numeroAgenceFormate = numeroAgenceFormate;
    }

    public int getNumeroAgence() {
        return numeroAgence;
    }

    public String getNumeroAgenceFormate() {
        return numeroAgenceFormate;
    }

    public int getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getNumeroCaisseFormate() {
        return numeroCaisseFormate;
    }

}
