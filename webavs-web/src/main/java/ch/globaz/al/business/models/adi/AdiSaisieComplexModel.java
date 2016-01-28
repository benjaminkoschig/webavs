package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexModel;

/**
 * Modèle complexe d'une saisie adi
 * 
 * @author GMO
 * 
 */
public class AdiSaisieComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Modèle de la saisie ADI
     */
    private AdiSaisieModel adiSaisieModel = null;

    /**
     * Modèle de l'enfant lié à la saisie ADI
     */
    private EnfantComplexModel enfantComplexModel = null;

    /**
     * Constructeur du modèle complexe
     */
    public AdiSaisieComplexModel() {
        super();
        adiSaisieModel = new AdiSaisieModel();
        enfantComplexModel = new EnfantComplexModel();
    }

    /**
     * @return adiSaisieModel
     */
    public AdiSaisieModel getAdiSaisieModel() {
        return adiSaisieModel;
    }

    /**
     * @return enfantComplexModel
     */
    public EnfantComplexModel getEnfantComplexModel() {
        return enfantComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return adiSaisieModel.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return adiSaisieModel.getSpy();
    }

    /**
     * @param adiSaisieModel
     *            le modèle saisie adi
     */
    public void setAdiSaisieModel(AdiSaisieModel adiSaisieModel) {
        this.adiSaisieModel = adiSaisieModel;
    }

    /**
     * @param enfantComplexModel
     *            le modèle enfant complexe
     */
    public void setEnfantComplexModel(EnfantComplexModel enfantComplexModel) {
        this.enfantComplexModel = enfantComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        adiSaisieModel.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        adiSaisieModel.setSpy(spy);

    }

}
