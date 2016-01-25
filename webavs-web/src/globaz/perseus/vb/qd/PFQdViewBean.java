/**
 * 
 */
package globaz.perseus.vb.qd;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFQdViewBean extends BJadePersistentObjectViewBean {

    private QD qd = null;

    public PFQdViewBean() {
        super();
        qd = new QD();
    }

    public PFQdViewBean(QD qd) {
        super();
        this.qd = qd;
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
        return qd.getId();
    }

    /**
     * @return the qd
     */
    public QD getQd() {
        return qd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(getQd().getSpy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        qd = PerseusServiceLocator.getQDService().read(qd.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        qd.setId(newId);
    }

    /**
     * @param qd
     *            the qd to set
     */
    public void setQd(QD qd) {
        this.qd = qd;
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
