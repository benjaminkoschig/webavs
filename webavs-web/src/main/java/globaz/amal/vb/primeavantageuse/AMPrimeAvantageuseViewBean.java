/**
 * 
 */
package globaz.amal.vb.primeavantageuse;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMPrimeAvantageuseViewBean extends BJadePersistentObjectViewBean {
    private SimplePrimeAvantageuse simplePrimeAvantageuse = null;

    public AMPrimeAvantageuseViewBean() {
        super();
        simplePrimeAvantageuse = new SimplePrimeAvantageuse();
    }

    public AMPrimeAvantageuseViewBean(SimplePrimeAvantageuse simplePrimeAvantageuse) {
        this();
        this.simplePrimeAvantageuse = simplePrimeAvantageuse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().create(simplePrimeAvantageuse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().delete(simplePrimeAvantageuse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simplePrimeAvantageuse.getId();
    }

    public SimplePrimeAvantageuse getSimplePrimeAvantageuse() {
        return simplePrimeAvantageuse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simplePrimeAvantageuse != null) {
            return new BSpy(simplePrimeAvantageuse.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        simplePrimeAvantageuse.setId(newId);
    }

    public void setSimplePrimeAvantageuse(SimplePrimeAvantageuse simplePrimeAvantageuse) {
        this.simplePrimeAvantageuse = simplePrimeAvantageuse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().update(simplePrimeAvantageuse);
    }

}
