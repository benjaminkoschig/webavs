package ch.globaz.al.business.models.allocataire;

/**
 * mod�le de l'allocataire agricole (mod�le complexe)
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
     * mod�le agricole
     */
    private AgricoleModel agricoleModel = null;

    /**
     * Constructeur du mod�le
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
