package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * 
 * Mod�le utilis� pour afficher les prestations dans la liste se trouvant dans le d�tail d'une r�cap
 * 
 * @author GMO
 * 
 */
public class EntetePrestationListRecapComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** mod�le complexe de l'allocataire li� � l'en-t�te */
    private AllocataireComplexModel allocataireComplexModel = null;
    /** mod�le du dossier li� � l'en-t�te */
    private DossierModel dossierModel = null;
    /** En-t�te de prestation */
    private EntetePrestationModel entetePrestationModel = null;

    /**
     * Constructeur
     */
    public EntetePrestationListRecapComplexModel() {
        super();
        allocataireComplexModel = new AllocataireComplexModel();
        dossierModel = new DossierModel();
        entetePrestationModel = new EntetePrestationModel();
    }

    /**
     * 
     * @return mod�le complexe de l'allocataire li� � l'en-t�te
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * 
     * @return mod�le du dossier li� � l'en-t�te
     */
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    /**
     * 
     * @return En-t�te de prestation
     */
    public EntetePrestationModel getEntetePrestationModel() {
        return entetePrestationModel;
    }

    @Override
    public String getId() {
        return entetePrestationModel.getId();
    }

    @Override
    public String getSpy() {
        return entetePrestationModel.getSpy();
    }

    public boolean isSaisieOuProvisoire() {
        String etatPrestation = getEntetePrestationModel().getEtatPrestation();
        if (ALCSPrestation.ETAT_SA.equals(etatPrestation) || ALCSPrestation.ETAT_PR.equals(etatPrestation)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param allocataireComplexModel
     *            mod�le complexe de l'allocataire li� � l'en-t�te
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }

    /**
     * 
     * @param dossierModel
     *            mod�le du dossier li� � l'en-t�te
     */
    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    /**
     * 
     * @param entetePrestationModel
     *            En-t�te de prestation
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
        entetePrestationModel.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        entetePrestationModel.setSpy(spy);

    }

}
