package ch.globaz.al.business.models.prestation.radiation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;

public class PrestationRadiationDossierComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DossierComplexModel dossierComplexModel = null;
    private EntetePrestationModel entetePrestationModel = null;

    public PrestationRadiationDossierComplexModel() {
        super();
        dossierComplexModel = new DossierComplexModel();
        entetePrestationModel = new EntetePrestationModel();
    }

    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    public EntetePrestationModel getEntetePrestationModel() {
        return entetePrestationModel;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    public void setEntetePrestationModel(EntetePrestationModel entetePrestationModel) {
        this.entetePrestationModel = entetePrestationModel;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }

}
