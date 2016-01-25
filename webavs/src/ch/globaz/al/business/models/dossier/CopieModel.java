package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Copies auxquelles les documents d'un dossier doivent être envoyées
 * 
 * @author jts
 */
public class CopieModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant de la copie
     */
    private String idCopie = null;
    /**
     * identifiant du dossier lié à la copie
     */
    private String idDossier = null;
    /**
     * identifiant du tiers destinataire de la copie
     */
    private String idTiersDestinataire = null;
    /**
     * dans la file d'attente pour impression batch ou non
     */
    private Boolean impressionBatch = false;
    /**
     * ordre de priorité de la copie (priorité 1: original, priorité 2: copie à ...)
     */
    private String ordreCopie = null;

    /**
     * type de copie
     */
    private String typeCopie = null;

    /**
     * Retourne l'id de la copie
     */
    @Override
    public String getId() {
        return idCopie;
    }

    /**
     * @return the idCopie
     */
    public String getIdCopie() {
        return idCopie;
    }

    /**
     * Retourne l'id du dossier auquel la copie appartient
     * 
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiersDestinataire() {
        return idTiersDestinataire;
    }

    /**
     * @return impressionBatch
     */
    public Boolean getImpressionBatch() {
        return impressionBatch;
    }

    /**
     * @return the prioriteDestCopie
     */
    public String getOrdreCopie() {
        return ordreCopie;
    }

    /**
     * @return the typeCopie
     */
    public String getTypeCopie() {
        return typeCopie;
    }

    /**
     * Définit l'id de la copie
     */
    @Override
    public void setId(String id) {
        idCopie = id;
    }

    /**
     * @param idCopie
     *            the idCopie to set
     */
    public void setIdCopie(String idCopie) {
        this.idCopie = idCopie;
    }

    /**
     * Définit l'id du dossier auquel la copie appartient
     * 
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiersDestinataire(String idTiers) {
        idTiersDestinataire = idTiers;
    }

    /**
     * @param impressionBatch
     *            the impressionBatch to set
     */
    public void setImpressionBatch(Boolean impressionBatch) {
        this.impressionBatch = impressionBatch;
    }

    /**
     * @param ordreCopie
     *            the prioriteDestCopie to set
     */
    public void setOrdreCopie(String ordreCopie) {
        this.ordreCopie = ordreCopie;
    }

    /**
     * @param typeCopie
     *            the typeCopie to set
     */
    public void setTypeCopie(String typeCopie) {
        this.typeCopie = typeCopie;
    }
}
