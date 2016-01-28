package ch.globaz.vulpecula.web.views.decompte;

/**
 * @author Arnaud Geiser (AGE) | Créé le 16 mai 2014
 * 
 */
public class CotisationCalculeeView implements Comparable<CotisationCalculeeView> {
    private final String libelle;
    private final String masse;
    private final String taux;
    private final String cotisation;

    public CotisationCalculeeView(final String libelle, final String masse, final String taux, final String cotisation) {
        this.libelle = libelle;
        this.masse = masse;
        this.taux = taux;
        this.cotisation = cotisation;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMasse() {
        return masse;
    }

    public String getTaux() {
        return taux;
    }

    public String getCotisation() {
        return cotisation;
    }

    @Override
    public int compareTo(CotisationCalculeeView o) {
        return libelle.compareTo(o.getLibelle());
    }
}
