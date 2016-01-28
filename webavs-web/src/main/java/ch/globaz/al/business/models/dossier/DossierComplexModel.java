package ch.globaz.al.business.models.dossier;

import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;

/**
 * Mod�le complexe d'un dossier regroupant les donn�es du dossier, celle de l'allocataire (mod�le complexe) et celles de
 * l'affili�
 * 
 * @author jts
 * 
 */
public class DossierComplexModel extends DossierComplexModelRoot {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Le mod�le de l'allocataire complet
     */
    AllocataireComplexModel allocataireComplexModel = null;

    /**
     * Constructeur du mod�le
     */
    public DossierComplexModel() {
        super();
        allocataireComplexModel = new AllocataireComplexModel();

    }

    /**
     * @return the allocataireComplexModel Le mod�le allocataire
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * @param allocataireComplexModel
     *            Le mod�le allocataire
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }
}
