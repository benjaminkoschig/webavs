package globaz.corvus.annonce;

/**
 * Représente les différents états possible pour les annonces des rentes
 * Ces états sont lié à des codes système
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