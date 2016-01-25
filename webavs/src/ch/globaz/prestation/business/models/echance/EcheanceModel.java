package ch.globaz.prestation.business.models.echance;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class EcheanceModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleEcheance simpleEcheance = new SimpleEcheance();
    private TiersSimpleModel simpleTiers = new TiersSimpleModel();

    public SimpleEcheance getSimpleEcheance() {
        return simpleEcheance;
    }

    public void setSimpleEcheance(SimpleEcheance simpleEcheance) {
        this.simpleEcheance = simpleEcheance;
    }

    public TiersSimpleModel getSimpleTiers() {
        return simpleTiers;
    }

    public void setSimpleTiers(TiersSimpleModel simpleTiers) {
        this.simpleTiers = simpleTiers;
    }

    @Override
    public String getId() {
        return simpleEcheance.getId();
    }

    @Override
    public String getSpy() {
        return simpleEcheance.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleEcheance.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        simpleEcheance.setSpy(spy);
    }

    @Override
    public String toString() {
        return "EcheanceModel [simpleEcheance=" + simpleEcheance + ", simpleTiers=" + simpleTiers + "]";
    }

}
