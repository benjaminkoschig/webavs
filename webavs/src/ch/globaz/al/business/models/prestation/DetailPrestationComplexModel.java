package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * 
 * Mod�le complexe d'un d�tail de prestation, utilis� pour afficher toutes les infos qu'il faut � l'�cran
 * 
 * @author GMO
 * 
 */
public class DetailPrestationComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Mod�le simple du d�tail de la prestation (mod�le root)
     */
    private DetailPrestationModel detailPrestationModel = null;
    /**
     * Mod�le complexe du droit li� au d�tail de la prestation
     */
    private DroitComplexModel droitComplexModel = null;
    /**
     * Mod�le simple de l'ent�te prestation li�e au d�tail prestation
     */
    private EntetePrestationModel entetePrestationModel = null;
    /**
     * Mod�le simple repr�sentant l'�ventuel b�n�ficiaire du droit
     */
    private TiersSimpleModel tiersBeneficiaireModel = null;

    /**
     * Constructeur du mod�le du d�tail prestation
     */
    public DetailPrestationComplexModel() {
        super();
        detailPrestationModel = new DetailPrestationModel();
        droitComplexModel = new DroitComplexModel();
        entetePrestationModel = new EntetePrestationModel();
        tiersBeneficiaireModel = new TiersSimpleModel();
    }

    /**
     * Retourne le mod�le d�tail prestation (root)
     * 
     * @return detailPrestationModel
     */
    public DetailPrestationModel getDetailPrestationModel() {
        return detailPrestationModel;
    }

    /**
     * Retourne le mod�le complexe du droit li� au d�tail prestation
     * 
     * @return droitComplexModel
     */
    public DroitComplexModel getDroitComplexModel() {
        return droitComplexModel;
    }

    /**
     * Retourne le mod�le de l'ent�te prestation li� au d�tail prestation
     * 
     * @return entetePrestationModel
     */
    public EntetePrestationModel getEntetePrestationModel() {
        return entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return detailPrestationModel.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return detailPrestationModel.getSpy();
    }

    /**
     * Retourne le mod�le de l'eventuel b�n�ficiaire de l'allocation
     * 
     * @return tiersBeneficiaireModel
     */
    public TiersSimpleModel getTiersBeneficiaireModel() {
        return tiersBeneficiaireModel;
    }

    /**
     * D�finit le d�tail de prestation
     * 
     * @param detailPrestationModel
     *            d�tail de la prestation
     */
    public void setDetailPrestationModel(DetailPrestationModel detailPrestationModel) {
        this.detailPrestationModel = detailPrestationModel;
    }

    /**
     * D�finit le droit li� au d�tail prestation
     * 
     * @param droitComplexModel
     *            mod�le du droit
     */
    public void setDroitComplexModel(DroitComplexModel droitComplexModel) {
        this.droitComplexModel = droitComplexModel;
    }

    /**
     * D�finit l'ent�te prestation li� au d�tail prestation
     * 
     * @param entetePrestationModel
     *            En-t�te de la prestation
     */
    public void setEntetePrestationModel(EntetePrestationModel entetePrestationModel) {
        this.entetePrestationModel = entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        detailPrestationModel.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        detailPrestationModel.setSpy(spy);

    }

    /**
     * D�finit le tiers b�n�ficiaire li� au d�tail prestation
     * 
     * @param tiersBeneficiaireModel
     *            tiers b�n�ficiaire
     */
    public void setTiersBeneficiaireModel(TiersSimpleModel tiersBeneficiaireModel) {
        this.tiersBeneficiaireModel = tiersBeneficiaireModel;
    }
}
