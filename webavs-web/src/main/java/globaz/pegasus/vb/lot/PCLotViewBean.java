/**
 * 
 */
package globaz.pegasus.vb.lot;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;

/**
 * @author BSC
 */
public class PCLotViewBean extends BJadePersistentObjectViewBean {

    // instance de la classe métier
    private SimpleLot simpleLot = null;

    /**
     * Constructeur simple
     */
    public PCLotViewBean() {
        super();
        simpleLot = new SimpleLot();
    }

    /**
     * Constructeur simple
     */
    public PCLotViewBean(SimpleLot simpleLot) {
        super();
        this.simpleLot = simpleLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleLot.getId();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * @return
     */
    public SimpleLot getSimpleLot() {
        return simpleLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (simpleLot != null) && !simpleLot.isNew() ? new BSpy(simpleLot.getSpy()) : new BSpy(getSession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simpleLot = CorvusServiceLocator.getLotService().read(simpleLot.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        simpleLot.setIdLot(newId);
    }

    /**
     * @param simpleLot
     */
    public void setSimpleLot(SimpleLot simpleLot) {
        this.simpleLot = simpleLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
