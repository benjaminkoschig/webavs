package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeComplexModel;

public class RenteAdapationDemande extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDemandeCentrale simpleDemandeCentrale;
    private SimpleRenteAdaptation simpleRenteAdaptation;

    public RenteAdapationDemande() {
        simpleDemandeCentrale = new SimpleDemandeCentrale();
        simpleRenteAdaptation = new SimpleRenteAdaptation();
    }

    @Override
    public String getId() {
        return simpleRenteAdaptation.getId();
    }

    public SimpleDemandeCentrale getSimpleDemandeCentrale() {
        return simpleDemandeCentrale;
    }

    public SimpleRenteAdaptation getSimpleRenteAdaptation() {
        return simpleRenteAdaptation;
    }

    @Override
    public String getSpy() {
        return simpleRenteAdaptation.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleRenteAdaptation.setId(id);
    }

    public void setSimpleDemandeCentrale(SimpleDemandeCentrale simpleDemandeCentrale) {
        this.simpleDemandeCentrale = simpleDemandeCentrale;
    }

    public void setSimpleRenteAdaptation(SimpleRenteAdaptation simpleRenteAdaptation) {
        this.simpleRenteAdaptation = simpleRenteAdaptation;
    }

    @Override
    public void setSpy(String spy) {
        simpleRenteAdaptation.setSpy(spy);

    }

}
