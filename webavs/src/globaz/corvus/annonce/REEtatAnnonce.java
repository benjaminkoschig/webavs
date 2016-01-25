package globaz.corvus.annonce;

/**
 * Repr�sente les diff�rents �tats possible pour les annonces des rentes
 * Ces �tats sont li� � des codes syst�me
 * 
 * @author LGA
 * 
 */
public enum REEtatAnnonce {

    OUVERT(52838001),
    ENVOYE(52838002);

    private int codeSystemEtatAnnonce;

    private REEtatAnnonce(int codeSystemEtatAnnonce) {
        this.codeSystemEtatAnnonce = codeSystemEtatAnnonce;
    }

    public int getCodeSystemEtatAnnonce() {
        return codeSystemEtatAnnonce;
    }

    public String getCodeSystemEtatAnnonceAsString() {
        return String.valueOf(codeSystemEtatAnnonce);
    }
}