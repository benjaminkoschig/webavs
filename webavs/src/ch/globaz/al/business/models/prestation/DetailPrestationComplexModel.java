package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * 
 * Modèle complexe d'un détail de prestation, utilisé pour afficher toutes les infos qu'il faut à l'écran
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
     * Modèle simple du détail de la prestation (modèle root)
     */
    private DetailPrestationModel detailPrestationModel = null;
    /**
     * Modèle complexe du droit lié au détail de la prestation
     */
    private DroitComplexModel droitComplexModel = null;
    /**
     * Modèle simple de l'entête prestation liée au détail prestation
     */
    private EntetePrestationModel entetePrestationModel = null;
    /**
     * Modèle simple représentant l'éventuel bénéficiaire du droit
     */
    private TiersSimpleModel tiersBeneficiaireModel = null;

    /**
     * Constructeur du modèle du détail prestation
     */
    public DetailPrestationComplexModel() {
        super();
        detailPrestationModel = new DetailPrestationModel();
        droitComplexModel = new DroitComplexModel();
        entetePrestationModel = new EntetePrestationModel();
        tiersBeneficiaireModel = new TiersSimpleModel();
    }

    /**
     * Retourne le modèle détail prestation (root)
     * 
     * @return detailPrestationModel
     */
    public DetailPrestationModel getDetailPrestationModel() {
        return detailPrestationModel;
    }

    /**
     * Retourne le modèle complexe du droit lié au détail prestation
     * 
     * @return droitComplexModel
     */
    public DroitComplexModel getDroitComplexModel() {
        return droitComplexModel;
    }

    /**
     * Retourne le modèle de l'entête prestation lié au détail prestation
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
     * Retourne le modèle de l'eventuel bénéficiaire de l'allocation
     * 
     * @return tiersBeneficiaireModel
     */
    public TiersSimpleModel getTiersBeneficiaireModel() {
        return tiersBeneficiaireModel;
    }

    /**
     * Définit le détail de prestation
     * 
     * @param detailPrestationModel
     *            détail de la prestation
     */
    public void setDetailPrestationModel(DetailPrestationModel detailPrestationModel) {
        this.detailPrestationModel = detailPrestationModel;
    }

    /**
     * Définit le droit lié au détail prestation
     * 
     * @param droitComplexModel
     *            modèle du droit
     */
    public void setDroitComplexModel(DroitComplexModel droitComplexModel) {
        this.droitComplexModel = droitComplexModel;
    }

    /**
     * Définit l'entête prestation lié au détail prestation
     * 
     * @param entetePrestationModel
     *            En-tête de la prestation
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
     * Définit le tiers bénéficiaire lié au détail prestation
     * 
     * @param tiersBeneficiaireModel
     *            tiers bénéficiaire
     */
    public void setTiersBeneficiaireModel(TiersSimpleModel tiersBeneficiaireModel) {
        this.tiersBeneficiaireModel = tiersBeneficiaireModel;
    }
}
