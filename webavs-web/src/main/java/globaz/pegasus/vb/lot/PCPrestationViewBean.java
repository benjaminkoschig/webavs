/**
 * 
 */
package globaz.pegasus.vb.lot;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author BSC
 */
public class PCPrestationViewBean extends BJadePersistentObjectViewBean {

    // instance de la classe métier
    private Prestation prestation = null;

    /**
     * Constructeur simple
     */
    public PCPrestationViewBean() {
        super();
        prestation = new Prestation();
    }

    /**
     * Constructeur simple
     */
    public PCPrestationViewBean(Prestation prestation) {
        super();
        this.prestation = prestation;
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
        return prestation.getId();
    }

    public Prestation getPrestation() {
        return prestation;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (prestation != null) && !prestation.isNew() ? new BSpy(prestation.getSpy()) : new BSpy(getSession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        prestation = PegasusServiceLocator.getPrestationService().read(prestation.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        prestation.setId(newId);
    }

    public void setPrestation(Prestation prestation) {
        this.prestation = prestation;
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
