package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

public class DetteCompenseOrdreVersement extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDetteComptatCompense simpleDetteComptatCompense = null;
    private SimpleOrdreVersement simpleOrdreVersement = null;

    public DetteCompenseOrdreVersement() {
        simpleDetteComptatCompense = new SimpleDetteComptatCompense();
        simpleOrdreVersement = new SimpleOrdreVersement();
    }

    @Override
    public String getId() {
        return simpleDetteComptatCompense.getId();
    }

    public SimpleDetteComptatCompense getSimpleDetteComptatCompense() {
        return simpleDetteComptatCompense;
    }

    public SimpleOrdreVersement getSimpleOrdreVersement() {
        return simpleOrdreVersement;
    }

    @Override
    public String getSpy() {
        return simpleDetteComptatCompense.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDetteComptatCompense.setId(id);
    }

    public void setSimpleDetteComptatCompense(SimpleDetteComptatCompense simpleDetteComptatCompense) {
        this.simpleDetteComptatCompense = simpleDetteComptatCompense;
    }

    public void setSimpleOrdreVersement(SimpleOrdreVersement simpleOrdreVersement) {
        this.simpleOrdreVersement = simpleOrdreVersement;
    }

    @Override
    public void setSpy(String spy) {
        simpleDetteComptatCompense.setSpy(spy);
    }

}
