package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class Lot extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleLot simpleLot = null;

    public Lot() {
        super();
        simpleLot = new SimpleLot();
    }

    @Override
    public String getId() {
        return simpleLot.getId();
    }

    public SimpleLot getSimpleLot() {
        return simpleLot;
    }

    @Override
    public String getSpy() {
        return simpleLot.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleLot.setId(id);

    }

    public void setSimpleLot(SimpleLot simpleLot) {
        this.simpleLot = simpleLot;
    }

    @Override
    public void setSpy(String spy) {
        simpleLot.setSpy(spy);
    }

}
