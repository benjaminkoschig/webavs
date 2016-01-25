package ch.globaz.al.business.models.dossier;

import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;

/**
 * Modèle complexe d'un dossier regroupant les données du dossier, celle de l'allocataire agriculteur (modèle complexe)
 * et celles de l'affilié
 * 
 * @author jts
 * 
 */
public class DossierAgricoleComplexModel extends DossierComplexModelRoot {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle complexe de l'allocataire agricole
     */
    AllocataireAgricoleComplexModel allocataireAgricoleComplexModel = null;

    /**
     * Constructeur du modèle
     */
    public DossierAgricoleComplexModel() {
        super();
        allocataireAgricoleComplexModel = new AllocataireAgricoleComplexModel();

    }

    /**
     * @return the allocataireAgricoleComplexModel
     */
    public AllocataireAgricoleComplexModel getAllocataireAgricoleComplexModel() {
        return allocataireAgricoleComplexModel;
    }

    /**
     * @param allocataireAgricoleComplexModel
     *            the allocataireAgricoleComplexModel to set
     */
    public void setAllocataireComplexModel(AllocataireAgricoleComplexModel allocataireAgricoleComplexModel) {
        this.allocataireAgricoleComplexModel = allocataireAgricoleComplexModel;
    }

}
