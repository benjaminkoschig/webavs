package ch.globaz.prestation.business.models.demande;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDemandePrestation extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etat = "";
    private String idDemande = "";
    private String idMetaDossier = "";
    private String idTiers = "";
    private String typeDemande = "";

    /**
     * Retourne l'état de la demande de prestation
     * 
     * @return the etat ???
     */
    public String getEtat() {
        return etat;
    }

    /**
     * Retourne l'identifiant de la demande de prestation
     * 
     * @return id dans la table de la demande
     */
    @Override
    public String getId() {
        return idDemande;
    }

    /**
     * Retourne l'identifiant de la demande de prestation
     * 
     * @return id dans la table de la demande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * Retourne l'id du meta dossier de la demande de prestation
     * 
     * @return the idMetaDossier
     */
    public String getIdMetaDossier() {
        return idMetaDossier;
    }

    /**
     * Retourne l'id du tiers associé
     * 
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the typeDemande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * @param etat
     *            the etat to set
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String id) {
        idDemande = id;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idMetaDossier
     *            the idMetaDossier to set
     */
    public void setIdMetaDossier(String idMetaDossier) {
        this.idMetaDossier = idMetaDossier;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param typeDemande
     *            the typeDemande to set
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
