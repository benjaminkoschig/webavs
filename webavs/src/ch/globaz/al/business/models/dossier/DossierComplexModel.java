package ch.globaz.al.business.models.dossier;

import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;

/**
 * Modèle complexe d'un dossier regroupant les données du dossier, celle de l'allocataire (modèle complexe) et celles de
 * l'affilié
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
     * Le modèle de l'allocataire complet
     */
    AllocataireComplexModel allocataireComplexModel = null;

    /**
     * Constructeur du modèle
     */
    public DossierComplexModel() {
        super();
        allocataireComplexModel = new AllocataireComplexModel();

    }

    /**
     * @return the allocataireComplexModel Le modèle allocataire
     */
    public AllocataireComplexModel getAllocataireComplexModel() {
        return allocataireComplexModel;
    }

    /**
     * @param allocataireComplexModel
     *            Le modèle allocataire
     */
    public void setAllocataireComplexModel(AllocataireComplexModel allocataireComplexModel) {
        this.allocataireComplexModel = allocataireComplexModel;
    }
}
