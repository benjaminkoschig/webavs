package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * 
 * Modèle complexe d'un adi par enfant et par mois (lié au décompte)
 * 
 * @author GMO
 * 
 */
public class AdiEnfantMoisComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle simple adi par enfant et par mois
     */
    private AdiEnfantMoisModel adiEnfantMoisModel = null;
    /**
     * Modèle complexe du droit lié à l'adi enfant
     */
    private DroitComplexModel droitComplexModel = null;

    /**
     * Constructeur du modèle complexe
     */
    public AdiEnfantMoisComplexModel() {
        super();
        adiEnfantMoisModel = new AdiEnfantMoisModel();
        droitComplexModel = new DroitComplexModel();
    }

    /**
     * 
     * @return adiEnfantMoisModel
     */
    public AdiEnfantMoisModel getAdiEnfantMoisModel() {
        return adiEnfantMoisModel;
    }

    /**
     * @return droitComplexModel
     */
    public DroitComplexModel getDroitComplexModel() {
        return droitComplexModel;
    }

    @Override
    public String getId() {
        return adiEnfantMoisModel.getId();
    }

    @Override
    public String getSpy() {
        return adiEnfantMoisModel.getSpy();
    }

    /**
     * @param adiEnfantMoisModel
     *            le modèle adi enfant par mois
     */
    public void setAdiEnfantMoisModel(AdiEnfantMoisModel adiEnfantMoisModel) {
        this.adiEnfantMoisModel = adiEnfantMoisModel;
    }

    /**
     * @param droitComplexModel
     *            le modèle droit complexe
     */
    public void setDroitComplexModel(DroitComplexModel droitComplexModel) {
        this.droitComplexModel = droitComplexModel;
    }

    @Override
    public void setId(String id) {
        adiEnfantMoisModel.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        adiEnfantMoisModel.setSpy(spy);

    }

}
