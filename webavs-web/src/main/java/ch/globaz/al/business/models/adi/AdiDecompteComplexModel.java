package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * ADI-Modele d'un d�compte complexId
 * 
 * @author PTA
 * 
 */
public class AdiDecompteComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * D�compte model
     */
    private DecompteAdiModel decompteAdiModel = null;
    /**
     * Mod�le dossier li� au d�compte
     */
    private DossierComplexModel dossierComplexModel = null;

    /**
     * Constructeur de la classe
     */
    public AdiDecompteComplexModel() {
        super();
        decompteAdiModel = new DecompteAdiModel();
        dossierComplexModel = new DossierComplexModel();

    }

    /**
     * @return the decompteAdiModel
     */
    public DecompteAdiModel getDecompteAdiModel() {
        return decompteAdiModel;
    }

    /**
     * 
     * @return dossierComplexModel
     */
    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return decompteAdiModel.getId();
    }

    @Override
    public String getSpy() {
        return decompteAdiModel.getSpy();
    }

    /**
     * @param decompteAdiModel
     *            the decomtpeAdiModel to set
     */
    public void setDecompteAdiModel(DecompteAdiModel decompteAdiModel) {
        this.decompteAdiModel = decompteAdiModel;
    }

    /**
     * 
     * @param dossierComplexModel
     *            le dossier mod�le � d�finir
     */
    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        decompteAdiModel.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        decompteAdiModel.setSpy(spy);
    }

}
