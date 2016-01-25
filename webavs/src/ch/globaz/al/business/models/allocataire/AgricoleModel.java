package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle agricole. Contient les informations complémentaires nécessaires au calcul des allocations d'un allocataire
 * agriculteur
 * 
 * @author jts
 * 
 */
public class AgricoleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * valeur booléenne domaine en montagne
     */
    private Boolean domaineMontagne = null;
    /**
     * identifiant de l'agricole
     */
    private String idAgricole = null;
    /**
     * identifiant de l'allocataire
     */
    private String idAllocataire = null;

    /**
     * Indique si le domaine est en montagne ou en plaine
     * 
     * @return the domaineMontagne
     */
    public Boolean getDomaineMontagne() {
        return domaineMontagne;
    }

    /**
     * Retourne l'id agricole
     */
    @Override
    public String getId() {
        return idAgricole;
    }

    /**
     * @return the idAgricole
     */
    public String getIdAgricole() {
        return idAgricole;
    }

    /**
     * @return the idAllocataire
     */
    public String getIdAllocataire() {
        return idAllocataire;
    }

    /**
     * @param domaineMontagne
     *            the domaineMontagne to set
     */
    public void setDomaineMontagne(Boolean domaineMontagne) {
        this.domaineMontagne = domaineMontagne;
    }

    /**
     * Définit l'id agricole
     */
    @Override
    public void setId(String id) {
        idAgricole = id;
    }

    /**
     * @param idAgricole
     *            the idAgricole to set
     */
    public void setIdAgricole(String idAgricole) {
        this.idAgricole = idAgricole;
    }

    /**
     * @param idAllocataire
     *            the idAllocataire to set
     */
    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }
}
