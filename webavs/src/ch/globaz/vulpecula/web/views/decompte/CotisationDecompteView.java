package ch.globaz.vulpecula.web.views.decompte;

/**
 * Représentant d'une vue représentant une cotisation d'un décompte salaire
 * 
 */
public class CotisationDecompteView {
    private final String id;
    private final String caisseSociale;
    private final String libelle;
    private final String masse;
    private final String taux;
    private final String cotisation;
    private final String franchise;

    public CotisationDecompteView(final String id, final String caisseSociale, final String libelle,
            final String masse, final String taux, final String cotisation, final String franchise) {
        this.id = id;
        this.caisseSociale = caisseSociale;
        this.libelle = libelle;
        this.masse = masse;
        this.taux = taux;
        this.cotisation = cotisation;
        this.franchise = franchise;
    }

    public String getCaisseSociale() {
        return caisseSociale;
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

    public String getId() {
        return id;
    }

    public String getFranchise() {
        return franchise;
    }
}
