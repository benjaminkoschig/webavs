package ch.globaz.perseus.business.models.donneesfinancieres;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDonneeFinanciere extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemande = null;
    private String idDonneeFinanciere = null;
    private String idMembreFamille = null;
    private String type = null;
    private String valeur = null;
    private String valeurModifieeTaxateur = null;

    public SimpleDonneeFinanciere() {
        super();
    }

    @Override
    public String getId() {
        return idDonneeFinanciere;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDonneeFinanciere
     */
    public String getIdDonneeFinanciere() {
        return idDonneeFinanciere;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the valeur
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * @return the valeurModifieeTaxateur
     */
    public String getValeurModifieeTaxateur() {
        return valeurModifieeTaxateur;
    }

    @Override
    public void setId(String id) {
        idDonneeFinanciere = id;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDonneeFinanciere
     *            the idDonneeFinanciere to set
     */
    public void setIdDonneeFinanciere(String idDonneeFinanciere) {
        this.idDonneeFinanciere = idDonneeFinanciere;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param valeur
     *            the valeur to set
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    /**
     * @param valeurModifieeTaxateur
     *            the valeurModifieeTaxateur to set
     */
    public void setValeurModifieeTaxateur(String valeurModifieeTaxateur) {
        this.valeurModifieeTaxateur = valeurModifieeTaxateur;
    }

}
