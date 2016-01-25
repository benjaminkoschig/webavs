/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * @author MBO
 * 
 */
public class CreancierRentePont extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCreancierRentePont simpleCreancierRentePont = null;
    private SimpleRentePont simpleRentePont = null;
    private TiersSimpleModel simpleTiers = null;

    public CreancierRentePont() {
        super();
        simpleCreancierRentePont = new SimpleCreancierRentePont();
        simpleRentePont = new SimpleRentePont();
        simpleTiers = new TiersSimpleModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCreancierRentePont.getId();
    }

    public SimpleCreancierRentePont getSimpleCreancierRentePont() {
        return simpleCreancierRentePont;
    }

    /**
     * @return the simpleRentePont
     */
    public SimpleRentePont getSimpleRentePont() {
        return simpleRentePont;
    }

    public TiersSimpleModel getSimpleTiers() {
        return simpleTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCreancierRentePont.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCreancierRentePont.setId(id);

    }

    public void setSimpleCreancierRentePont(SimpleCreancierRentePont simpleCreancierRentePont) {
        this.simpleCreancierRentePont = simpleCreancierRentePont;
    }

    /**
     * @param simpleRentePont
     *            the simpleRentePont to set
     */
    public void setSimpleRentePont(SimpleRentePont simpleRentePont) {
        this.simpleRentePont = simpleRentePont;
    }

    public void setSimpleTiers(TiersSimpleModel simpleTiers) {
        this.simpleTiers = simpleTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCreancierRentePont.setSpy(spy);

    }

}
