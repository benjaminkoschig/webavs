package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexModel;

/**
 * Mod�le complexe d'une saisie adi
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
     * Mod�le de la saisie ADI
     */
    private AdiSaisieModel adiSaisieModel = null;

    /**
     * Mod�le de l'enfant li� � la saisie ADI
     */
    private EnfantComplexModel enfantComplexModel = null;

    /**
     * Constructeur du mod�le complexe
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
     *            le mod�le saisie adi
     */
    public void setAdiSaisieModel(AdiSaisieModel adiSaisieModel) {
        this.adiSaisieModel = adiSaisieModel;
    }

    /**
     * @param enfantComplexModel
     *            le mod�le enfant complexe
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
