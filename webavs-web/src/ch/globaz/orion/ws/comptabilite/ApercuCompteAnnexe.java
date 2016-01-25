package ch.globaz.orion.ws.comptabilite;

/**
 * Représente un aperçu de compte annexe (ne contient donc pas l'ensemble des paramètres relatifs à un compte annexe
 * webavs)
 * 
 * @author bjo
 * 
 */
public class ApercuCompteAnnexe {
    private String idCompteAnnexe;
    private String libelle;
    private String solde;

    public ApercuCompteAnnexe() {
    }

    public ApercuCompteAnnexe(String idCompteAnnexe, String libelle, String solde) {
        super();
        this.idCompteAnnexe = idCompteAnnexe;
        this.libelle = libelle;
        this.solde = solde;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getSolde() {
        return solde;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

}
