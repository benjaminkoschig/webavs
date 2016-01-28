package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le repr�sentant une ligne historis�e d'un traitement
 * 
 * @author GMO
 * 
 */
public class TraitementHistoriqueModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * l'identifiant de l'entit�
     */
    private String cleEntite = null;
    /**
     * Id de la ligne historis�e
     */
    private String idHistorique = null;
    /**
     * id du traitement ayant engendr� l'historisation
     */
    private String idTraitementPeriodique = null;
    /**
     * le status de la "modification" pour l'entit�
     */
    private String status = null;
    /**
     * le type de l'entit� impact�e par le traitement
     */
    private String typeEntite = null;

    /**
     * 
     * @return l'identifiant de l'entit� impact�e
     */
    public String getCleEntite() {
        return cleEntite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idHistorique;
    }

    /**
     * 
     * @return idTraitementPeriodique
     */
    public String getIdTraitementPeriodique() {
        return idTraitementPeriodique;
    }

    /**
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return typeEntite
     */
    public String getTypeEntite() {
        return typeEntite;
    }

    /**
     * 
     * @param cleEntite
     *            l'identifiant de l'entit� impact�e
     */
    public void setCleEntite(String cleEntite) {
        this.cleEntite = cleEntite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idHistorique = id;

    }

    /**
     * 
     * @param idTraitementPeriodique
     *            - id du traitement p�riodique
     */
    public void setIdTraitementPeriodique(String idTraitementPeriodique) {
        this.idTraitementPeriodique = idTraitementPeriodique;
    }

    /**
     * 
     * @param status
     *            status de la modification
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @param typeEntite
     *            - le type d'entit� impact�e par le traitement
     */
    public void setTypeEntite(String typeEntite) {
        this.typeEntite = typeEntite;
    }

}
