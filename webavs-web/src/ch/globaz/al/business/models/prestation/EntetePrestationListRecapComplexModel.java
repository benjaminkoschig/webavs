package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * 
 * Modèle utilisé pour afficher les prestations dans la liste se trouvant dans le détail d'une récap
 * 
 * @author GMO
 * 
 */
public class EntetePrestationListRecapComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** modèle complexe de l'allocataire lié à l'en-tête */
    private AllocataireComplexModel allocataireComplexModel = null;
    /** modèle du dossier lié à l'en-tête */
    private DossierModel dossierModel = null;
    /** En-tête de prestation */
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
     * @return modèle complexe de l'allocataire lié à l'en-tête
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * 
     * @return modèle du dossier lié à l'en-tête
     */
    public DossierModel getDossierModel() {
        return dossierModel;
    }

    /**
     * 
     * @return En-tête de prestation
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
     *            modèle complexe de l'allocataire lié à l'en-tête
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }

    /**
     * 
     * @param dossierModel
     *            modèle du dossier lié à l'en-tête
     */
    public void setDossierModel(DossierModel dossierModel) {
        this.dossierModel = dossierModel;
    }

    /**
     * 
     * @param entetePrestationModel
     *            En-tête de prestation
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
