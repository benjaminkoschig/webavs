package ch.globaz.al.business.models.dossier;

import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;

/**
 * Mod�le complexe d'un dossier regroupant les donn�es du dossier, celle de l'allocataire agriculteur (mod�le complexe)
 * et celles de l'affili�
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
     * Mod�le complexe de l'allocataire agricole
     */
    AllocataireAgricoleComplexModel allocataireAgricoleComplexModel = null;

    /**
     * Constructeur du mod�le
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
