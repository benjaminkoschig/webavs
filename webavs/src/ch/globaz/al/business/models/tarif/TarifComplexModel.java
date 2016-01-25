package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * mod�le complexe PrestationTarif
 * 
 * @author PTA
 * 
 */
public class TarifComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Cat�gorie de tarif
     * 
     * @see CategorieTarifModel
     */
    private CategorieTarifModel categorieTarifModel = null;
    /**
     * Crit�re de s�lection sur l'�ge
     * 
     * @see CritereTarifModel
     */
    private CritereTarifModel critereTarifModelAge = null;
    /**
     * Crit�re de s�lection sur le nombre
     * 
     * @see CritereTarifModel
     */
    private CritereTarifModel critereTarifModelNbr = null;
    /**
     * Crit�re de s�lection sur l'�ge
     * 
     * @see CritereTarifModel
     */
    private CritereTarifModel critereTarifModelRang = null;
    /**
     * Crit�re de s�lection sur l'�ge
     * 
     * @see CritereTarifModel
     */
    private CritereTarifModel critereTarifModelRevenuIndependant = null;
    /**
     * Crit�re de s�lection sur l'�ge
     * 
     * @see CritereTarifModel
     */
    private CritereTarifModel critereTarifModelRevenuNonActif = null;
    /**
     * L�gislation
     * 
     * @see LegislationTarifModel
     */
    private LegislationTarifModel legislationTarifModel = null;
    /**
     * Prestation
     * 
     * @see PrestationTarifModel
     */
    private PrestationTarifModel prestationTarifModel = null; // entit� ma�tre

    /**
     * constructeurs
     */
    public TarifComplexModel() {
        super();
        categorieTarifModel = new CategorieTarifModel();
        critereTarifModelAge = new CritereTarifModel();
        critereTarifModelNbr = new CritereTarifModel();
        critereTarifModelRang = new CritereTarifModel();
        critereTarifModelRevenuIndependant = new CritereTarifModel();
        critereTarifModelRevenuNonActif = new CritereTarifModel();
        legislationTarifModel = new LegislationTarifModel();
        prestationTarifModel = new PrestationTarifModel();

    }

    /**
     * @return the categorieTarifModel
     */
    public CategorieTarifModel getCategorieTarifModel() {
        return categorieTarifModel;
    }

    /**
     * @return the critereTarifModel
     */
    public CritereTarifModel getCritereTarifModelAge() {
        return critereTarifModelAge;
    }

    /**
     * @return the critereTarifModelNbre
     */
    public CritereTarifModel getCritereTarifModelNbr() {
        return critereTarifModelNbr;
    }

    /**
     * @return the critereTarifModelRang
     */
    public CritereTarifModel getCritereTarifModelRang() {
        return critereTarifModelRang;
    }

    /**
     * @return the critereTarifModelRevenuIndependant
     */
    public CritereTarifModel getCritereTarifModelRevenuIndependant() {
        return critereTarifModelRevenuIndependant;
    }

    /**
     * @return the critereTarifModelRevenuNonActif
     */
    public CritereTarifModel getCritereTarifModelRevenuNonActif() {
        return critereTarifModelRevenuNonActif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return prestationTarifModel.getId();
    }

    /**
     * @return the legislationTarifModel
     */
    public LegislationTarifModel getLegislationTarifModel() {
        return legislationTarifModel;
    }

    /**
     * @return the prestationTarifModel
     */
    public PrestationTarifModel getPrestationTarifModel() {
        return prestationTarifModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {

        return prestationTarifModel.getSpy();
    }

    /**
     * @param categorieTarifModel
     *            the categorieTarifModel to set
     */
    public void setCategorieTarifModel(CategorieTarifModel categorieTarifModel) {
        this.categorieTarifModel = categorieTarifModel;
    }

    /**
     * @param critereTarifModelAge
     *            the critereTarifModel to set
     */
    public void setCritereTarifModel(CritereTarifModel critereTarifModelAge) {
        this.critereTarifModelAge = critereTarifModelAge;
    }

    /**
     * @param critereTarifModelNbre
     *            the critereTarifModelNbre to set
     */
    public void setCritereTarifModelNbr(CritereTarifModel critereTarifModelNbre) {
        critereTarifModelNbr = critereTarifModelNbre;
    }

    /**
     * @param critereTarifModelRang
     *            the critereTarifModelRang to set
     */
    public void setCritereTarifModelRang(CritereTarifModel critereTarifModelRang) {
        this.critereTarifModelRang = critereTarifModelRang;
    }

    /**
     * @param critereTarifModelRevenuIndependant
     *            the critereTarifModelRevenuIndependant to set
     */
    public void setCritereTarifModelRevenuIndependant(CritereTarifModel critereTarifModelRevenuIndependant) {
        this.critereTarifModelRevenuIndependant = critereTarifModelRevenuIndependant;
    }

    /**
     * @param critereTarifModelRevenuNonActif
     *            the critereTarifModelRevenuNonActif to set
     */
    public void setCritereTarifModelRevenuNonActif(CritereTarifModel critereTarifModelRevenuNonActif) {
        this.critereTarifModelRevenuNonActif = critereTarifModelRevenuNonActif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        prestationTarifModel.setId(id);

    }

    /**
     * @param legislationTarifModel
     *            the legislationTarifModel to set
     */
    public void setLegislationTarifModel(LegislationTarifModel legislationTarifModel) {
        this.legislationTarifModel = legislationTarifModel;
    }

    /**
     * @param prestationTarifModel
     *            the prestationTarifModel to set
     */
    public void setPrestationTarifModel(PrestationTarifModel prestationTarifModel) {
        this.prestationTarifModel = prestationTarifModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        prestationTarifModel.setSpy(spy);

    }

}
