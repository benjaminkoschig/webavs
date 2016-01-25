package ch.globaz.al.business.models.allocataire;

/**
 * modèle de l'allocataire agricole (modèle complexe)
 * 
 * @author PTA
 * 
 */
public class AllocataireAgricoleComplexModel extends AllocataireComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * modèle agricole
     */
    private AgricoleModel agricoleModel = null;

    /**
     * Constructeur du modèle
     */
    public AllocataireAgricoleComplexModel() {
        super();
        agricoleModel = new AgricoleModel();
    }

    /**
     * @return the agricoleModel
     */
    public AgricoleModel getAgricoleModel() {
        return agricoleModel;

    }

    /**
     * @param agricoleModel
     *            the agricoleModel to set
     */
    public void setAgricoleModel(AgricoleModel agricoleModel) {
        this.agricoleModel = agricoleModel;
    }
}
