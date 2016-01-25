package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle simple d'une QD principale.
 * 
 * @author JSI
 * 
 */
public class SimpleQDAnnuelle extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String excedantRevenu = null;
    private String excedantRevenuCompense = null;
    private String idDossier = null;
    private String idQDAnnuelle = null;

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the excedantRevenu
     */
    public String getExcedantRevenu() {
        return excedantRevenu;
    }

    /**
     * @return the excedantRevenuCompense
     */
    public String getExcedantRevenuCompense() {
        return excedantRevenuCompense;
    }

    @Override
    public String getId() {
        return idQDAnnuelle;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idQDAnnuelle
     */
    public String getIdQDAnnuelle() {
        return idQDAnnuelle;
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param excedantRevenu
     *            the excedantRevenu to set
     */
    public void setExcedantRevenu(String excedantRevenu) {
        this.excedantRevenu = excedantRevenu;
    }

    /**
     * @param excedantRevenuCompense
     *            the excedantRevenuCompense to set
     */
    public void setExcedantRevenuCompense(String excedantRevenuCompense) {
        this.excedantRevenuCompense = excedantRevenuCompense;
    }

    @Override
    public void setId(String id) {
        idQDAnnuelle = id;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idQDAnnuelle
     *            the idQDAnnuelle to set
     */
    public void setIdQDAnnuelle(String idQDAnnuelle) {
        this.idQDAnnuelle = idQDAnnuelle;
    }

}
