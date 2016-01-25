/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * @author dhi
 * 
 *         Modèle complexe représentant un envoi avec ses différents liens et valeurs
 * 
 */
public class EnvoiComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AllocataireComplexModel allocataireComplexModel = null;

    private DossierModel dossierModel = null;

    private EnvoiItemSimpleModel envoiItemSimpleModel = null;

    private EnvoiJobSimpleModel envoiJobSimpleModel = null;

    /**
     * Default constructor
     */
    public EnvoiComplexModel() {
        envoiJobSimpleModel = new EnvoiJobSimpleModel();
        envoiItemSimpleModel = new EnvoiItemSimpleModel();
        dossierModel = new DossierModel();
        allocataireComplexModel = new AllocataireComplexModel();
    }

    /**
     * @return the allocataireComplexModel
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * @return the dossierModel
     */
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    /**
     * @return the envoiItemSimpleModel
     */
    public EnvoiItemSimpleModel getEnvoiItemSimpleModel() {
        return envoiItemSimpleModel;
    }

    /**
     * @return the envoiJobSimpleModel
     */
    public EnvoiJobSimpleModel getEnvoiJobSimpleModel() {
        return envoiJobSimpleModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getEnvoiItemSimpleModel().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return getEnvoiItemSimpleModel().getSpy();
    }

    /**
     * @param allocataireComplexModel
     *            the allocataireComplexModel to set
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }

    /**
     * @param dossierModel
     *            the dossierModel to set
     */
    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    /**
     * @param envoiItemSimpleModel
     *            the envoiItemSimpleModel to set
     */
    public void setEnvoiItemSimpleModel(EnvoiItemSimpleModel envoiItemSimpleModel) {
        this.envoiItemSimpleModel = envoiItemSimpleModel;
    }

    /**
     * @param envoiJobSimpleModel
     *            the envoiJobSimpleModel to set
     */
    public void setEnvoiJobSimpleModel(EnvoiJobSimpleModel envoiJobSimpleModel) {
        this.envoiJobSimpleModel = envoiJobSimpleModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        getEnvoiItemSimpleModel().setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        getEnvoiItemSimpleModel().setSpy(spy);
    }

}
