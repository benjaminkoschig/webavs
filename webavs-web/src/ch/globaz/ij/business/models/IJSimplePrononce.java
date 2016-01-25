package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class IJSimplePrononce extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Genre du prononc�. Associ� au champ : XBTGEN
     */
    private String csGenreReadaptation;
    /**
     * Date de d�but du prononc�. Associ� au champ : XBDDDR
     */
    private String dateDebutPrononce;
    /**
     * Date de fin du prononc�. Associ� au champ : XBDFDR
     */
    private String dateFinPrononce;
    /**
     * Id de la demande associ� � ce prononc�
     */
    private String idDemande;
    /**
     * L'idParent correspond � l'id d'un prononc� dans le cas ou ce prononc� serait corrig� par un autre
     */
    private String idParent;

    /**
     * Id du prononc�. Associ� au champ : XBIPAI
     */
    private String idPrononce;

    private String prononceSelectionne;

    public IJSimplePrononce() {
    }

    /**
     * Retourne le code syst�me du genre de r�adaptation du prononc�
     * 
     * @return le code syst�me du genre de r�adaptation du prononc�
     */
    public String getCsGenreReadaptation() {
        return csGenreReadaptation;
    }

    /**
     * Retourne la date de d�but du prononce
     * 
     * @return la date de d�but du prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * Retourne la date de fin du prononc�
     * 
     * @return la date de fin du prononc�
     */
    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    /**
     * Retourne l'id du prononc�
     * 
     * @return l'id du prononc�
     */
    @Override
    public String getId() {
        return idPrononce;
    }

    public String getIdDemande() {
        return idDemande;
    }

    /**
     * Retourne l'id du prononc� parent si existant sinon null L'idParent correspond � l'id d'un autre prononc� dans le
     * cas ou ce prononc� serait corrig� par un autre.
     * 
     * @return Retourne l'id du prononc� parent si existant sinon null
     */
    public String getIdParent() {
        return idParent;
    }

    /**
     * Retourne l'id du prononc�
     * 
     * @return l'id du prononc�
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public final String getPrononceSelectionne() {
        return prononceSelectionne;
    }

    /**
     * @param csGenreReadaptation
     *            le code syst�me du genre de r�adaptation du prononc�
     */
    public void setCsGenreReadaptation(String csGenreReadaptation) {
        this.csGenreReadaptation = csGenreReadaptation;
    }

    /**
     * @param dateDebutPrononce
     *            la date de d�but du prononc�
     */
    public void setDateDebutPrononce(String dateDebutPrononce) {
        this.dateDebutPrononce = dateDebutPrononce;
    }

    /**
     * 
     * @param dateFinPrononce
     *            la date de fin du prononc�
     */
    public void setDateFinPrononce(String dateFinPrononce) {
        this.dateFinPrononce = dateFinPrononce;
    }

    /**
     * D�finit l'id du prononc�
     * 
     * @param idPrononce
     *            l'id du prononc�
     */
    @Override
    public void setId(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * D�finit l'id du prononc� parent L'idParent correspond � l'id d'un autre prononc� dans le cas ou ce prononc�
     * serait corrig� par un autre.
     * 
     * @param idParentid
     *            du prononc� de correction de ce prononc�.
     */
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    /**
     * D�finit l'id du prononc�
     * 
     * @param idPrononce
     *            l'id du prononc�
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public final void setPrononceSelectionne(String prononceSelectionne) {
        this.prononceSelectionne = prononceSelectionne;
    }

}
