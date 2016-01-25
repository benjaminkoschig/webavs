package ch.globaz.perseus.business.models.informationfacture;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.dossier.Dossier;

public class InformationFacture extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dossier dossier = null;
    private SimpleInformationFacture simpleinformationfacture = null;

    public InformationFacture() {
        super();
        dossier = new Dossier();
        simpleinformationfacture = new SimpleInformationFacture();
    }

    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return simpleinformationfacture.getId();
    }

    public SimpleInformationFacture getSimpleInformationFacture() {
        return simpleinformationfacture;
    }

    @Override
    public String getSpy() {
        return simpleinformationfacture.getSpy();
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        simpleinformationfacture.setId(id);
    }

    public void setSimpleInformationFacture(SimpleInformationFacture simpleInformationFacture) {
        simpleinformationfacture = simpleInformationFacture;
    }

    @Override
    public void setSpy(String spy) {
        simpleinformationfacture.setSpy(spy);
    }
}
