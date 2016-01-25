package ch.globaz.perseus.business.models.parametres;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class Loyer extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleLoyer simpleLoyer = null;
    private SimpleZone simpleZone = null;

    public Loyer() {
        super();
        simpleLoyer = new SimpleLoyer();
        simpleZone = new SimpleZone();
    }

    @Override
    public String getId() {
        return simpleLoyer.getId();
    }

    public Float getMontant() {
        return Float.parseFloat(simpleLoyer.getMontant());
    }

    public SimpleLoyer getSimpleLoyer() {
        return simpleLoyer;
    }

    public SimpleZone getSimpleZone() {
        return simpleZone;
    }

    @Override
    public String getSpy() {
        return simpleLoyer.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleLoyer.setId(id);
    }

    public void setSimpleLoyer(SimpleLoyer simpleLoyer) {
        this.simpleLoyer = simpleLoyer;
    }

    public void setSimpleZone(SimpleZone simpleZone) {
        this.simpleZone = simpleZone;
    }

    @Override
    public void setSpy(String spy) {
        simpleLoyer.setSpy(spy);
    }

}
