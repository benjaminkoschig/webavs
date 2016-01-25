package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class IJSimplePrononce extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Genre du prononcé. Associé au champ : XBTGEN
     */
    private String csGenreReadaptation;
    /**
     * Date de début du prononcé. Associé au champ : XBDDDR
     */
    private String dateDebutPrononce;
    /**
     * Date de fin du prononcé. Associé au champ : XBDFDR
     */
    private String dateFinPrononce;
    /**
     * Id de la demande associé à ce prononcé
     */
    private String idDemande;
    /**
     * L'idParent correspond à l'id d'un prononcé dans le cas ou ce prononcé serait corrigé par un autre
     */
    private String idParent;

    /**
     * Id du prononcé. Associé au champ : XBIPAI
     */
    private String idPrononce;

    private String prononceSelectionne;

    public IJSimplePrononce() {
    }

    /**
     * Retourne le code système du genre de réadaptation du prononcé
     * 
     * @return le code système du genre de réadaptation du prononcé
     */
    public String getCsGenreReadaptation() {
        return csGenreReadaptation;
    }

    /**
     * Retourne la date de début du prononce
     * 
     * @return la date de début du prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * Retourne la date de fin du prononcé
     * 
     * @return la date de fin du prononcé
     */
    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    /**
     * Retourne l'id du prononcé
     * 
     * @return l'id du prononcé
     */
    @Override
    public String getId() {
        return idPrononce;
    }

    public String getIdDemande() {
        return idDemande;
    }

    /**
     * Retourne l'id du prononcé parent si existant sinon null L'idParent correspond à l'id d'un autre prononcé dans le
     * cas ou ce prononcé serait corrigé par un autre.
     * 
     * @return Retourne l'id du prononcé parent si existant sinon null
     */
    public String getIdParent() {
        return idParent;
    }

    /**
     * Retourne l'id du prononcé
     * 
     * @return l'id du prononcé
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public final String getPrononceSelectionne() {
        return prononceSelectionne;
    }

    /**
     * @param csGenreReadaptation
     *            le code système du genre de réadaptation du prononcé
     */
    public void setCsGenreReadaptation(String csGenreReadaptation) {
        this.csGenreReadaptation = csGenreReadaptation;
    }

    /**
     * @param dateDebutPrononce
     *            la date de début du prononcé
     */
    public void setDateDebutPrononce(String dateDebutPrononce) {
        this.dateDebutPrononce = dateDebutPrononce;
    }

    /**
     * 
     * @param dateFinPrononce
     *            la date de fin du prononcé
     */
    public void setDateFinPrononce(String dateFinPrononce) {
        this.dateFinPrononce = dateFinPrononce;
    }

    /**
     * Définit l'id du prononcé
     * 
     * @param idPrononce
     *            l'id du prononcé
     */
    @Override
    public void setId(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * Définit l'id du prononcé parent L'idParent correspond à l'id d'un autre prononcé dans le cas ou ce prononcé
     * serait corrigé par un autre.
     * 
     * @param idParentid
     *            du prononcé de correction de ce prononcé.
     */
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    /**
     * Définit l'id du prononcé
     * 
     * @param idPrononce
     *            l'id du prononcé
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public final void setPrononceSelectionne(String prononceSelectionne) {
        this.prononceSelectionne = prononceSelectionne;
    }

}
