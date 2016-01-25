package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * classe représentant les prestations des tarifs
 * 
 * @author PTA
 * 
 */
public class PrestationTarifBaseModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id de la catégorie de tarif
     */
    private String idCategorieTarif = null;
    /**
     * Id du critère d'âge
     * 
     * @see CritereTarifModel
     */
    private String idCritereAge = null;
    /**
     * Id du critère de nombre
     * 
     * @see CritereTarifModel
     */
    private String idCritereNombre = null;
    /**
     * Id du critère de rang
     * 
     * @see CritereTarifModel
     */
    private String idCritereRang = null;
    /**
     * Id du critère de revenu d'indépendant
     * 
     * @see CritereTarifModel
     */
    private String idCritereRevenuIndependant = null;
    /**
     * Id du critère de non-actif
     * 
     * @see CritereTarifModel
     */
    private String idCritereRevenuNonActif = null;
    /**
     * Id (clé primaire)
     */
    private String idPrestationTarif = null;
    /**
     * Montant de la prestation
     */
    private String montant = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPrestationTarif;

    }

    /**
     * @return the idCategorieTarif
     * @see CritereTarifModel
     */
    public String getIdCategorieTarif() {
        return idCategorieTarif;
    }

    /**
     * @return the idCritereAge
     * @see CritereTarifModel
     */
    public String getIdCritereAge() {
        return idCritereAge;
    }

    /**
     * @return the idCritereNombre
     * @see CritereTarifModel
     */
    public String getIdCritereNombre() {
        return idCritereNombre;
    }

    /**
     * @return the idCritereRang
     * @see CritereTarifModel
     */
    public String getIdCritereRang() {
        return idCritereRang;
    }

    /**
     * @return the idCritereRevenuIndependant
     * @see CritereTarifModel
     */
    public String getIdCritereRevenuIndependant() {
        return idCritereRevenuIndependant;
    }

    /**
     * @return the idCritereRevenuNonActif
     * @see CritereTarifModel
     */
    public String getIdCritereRevenuNonActif() {
        return idCritereRevenuNonActif;
    }

    /**
     * @return the idPrestationTarif
     */
    public String getIdPrestationTarif() {
        return idPrestationTarif;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPrestationTarif = id;

    }

    /**
     * @param idCategorieTarif
     *            the idCategorieTarif to set
     */
    public void setIdCategorieTarif(String idCategorieTarif) {
        this.idCategorieTarif = idCategorieTarif;
    }

    /**
     * @param idCritereAge
     *            the idCritereAge to set
     */
    public void setIdCritereAge(String idCritereAge) {
        this.idCritereAge = idCritereAge;
    }

    /**
     * @param idCritereNombre
     *            the idCritereNombre to set
     */
    public void setIdCritereNombre(String idCritereNombre) {
        this.idCritereNombre = idCritereNombre;
    }

    /**
     * @param idCritereRang
     *            the idCritereRang to set
     */
    public void setIdCritereRang(String idCritereRang) {
        this.idCritereRang = idCritereRang;
    }

    /**
     * @param idCritereRevenuIndependant
     *            the idCritereRevenuIndependant to set
     */
    public void setIdCritereRevenuIndependant(String idCritereRevenuIndependant) {
        this.idCritereRevenuIndependant = idCritereRevenuIndependant;
    }

    /**
     * @param idCritereRevenuNonActif
     *            the idCritereRevenuNonActif to set
     */
    public void setIdCritereRevenuNonActif(String idCritereRevenuNonActif) {
        this.idCritereRevenuNonActif = idCritereRevenuNonActif;
    }

    /**
     * @param idPrestationTarif
     *            the idPrestationTarif to set
     */
    public void setIdPrestationTarif(String idPrestationTarif) {
        this.idPrestationTarif = idPrestationTarif;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }
}
