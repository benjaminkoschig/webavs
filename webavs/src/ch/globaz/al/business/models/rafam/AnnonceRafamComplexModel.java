package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Modèle complexe d'une annonce RAFAM.
 * 
 * @author jts
 * 
 */
public class AnnonceRafamComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Allocataire lié à l'annonce
     */
    AllocataireComplexModel allocataireComplexModel = null;
    /**
     * Modèle simple de l'annonce
     */
    AnnonceRafamModel annonceRafamModel = null;
    /**
     * Dossier auquel est lié le droit
     */
    DossierModel dossierModel = null;
    /**
     * Droit lié à l'annonce
     */
    DroitComplexModel droitComplexModel = null;

    /**
     * Constructeur
     */
    public AnnonceRafamComplexModel() {
        allocataireComplexModel = new AllocataireComplexModel();
        annonceRafamModel = new AnnonceRafamModel();
        droitComplexModel = new DroitComplexModel();
        dossierModel = new DossierModel();
    }

    /**
     * @return the allocataireComplexModel
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * @return the annonceRafamModel
     */
    public AnnonceRafamModel getAnnonceRafamModel() {
        return annonceRafamModel;
    }

    /**
     * @return the dossierModel
     */
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    /**
     * @return the droitComplexModel
     */
    public DroitComplexModel getDroitComplexModel() {
        return droitComplexModel;
    }

    @Override
    public String getId() {
        return annonceRafamModel.getIdAnnonce();
    }

    @Override
    public String getSpy() {
        return annonceRafamModel.getSpy();
    }

    /**
     * @param allocataireComplexModel
     *            the allocataireComplexModel to set
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }

    /**
     * @param annonceRafamModel
     *            the annonceRafamModel to set
     */
    public void setAnnonceRafamModel(AnnonceRafamModel annonceRafamModel) {
        this.annonceRafamModel = annonceRafamModel;
    }

    /**
     * @param dossierModel
     *            the dossierModel to set
     */
    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    /**
     * @param droitComplexModel
     *            the droitComplexModel to set
     */
    public void setDroitComplexModel(DroitComplexModel droitComplexModel) {
        this.droitComplexModel = droitComplexModel;
    }

    @Override
    public void setId(String id) {
        annonceRafamModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        annonceRafamModel.setSpy(spy);
    }

}
