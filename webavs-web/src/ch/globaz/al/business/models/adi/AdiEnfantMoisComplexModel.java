package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * 
 * Mod�le complexe d'un adi par enfant et par mois (li� au d�compte)
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
     * Mod�le simple adi par enfant et par mois
     */
    private AdiEnfantMoisModel adiEnfantMoisModel = null;
    /**
     * Mod�le complexe du droit li� � l'adi enfant
     */
    private DroitComplexModel droitComplexModel = null;

    /**
     * Constructeur du mod�le complexe
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
     *            le mod�le adi enfant par mois
     */
    public void setAdiEnfantMoisModel(AdiEnfantMoisModel adiEnfantMoisModel) {
        this.adiEnfantMoisModel = adiEnfantMoisModel;
    }

    /**
     * @param droitComplexModel
     *            le mod�le droit complexe
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
