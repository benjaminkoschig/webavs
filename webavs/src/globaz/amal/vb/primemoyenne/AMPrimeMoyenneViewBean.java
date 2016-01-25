/**
 * 
 */
package globaz.amal.vb.primemoyenne;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMPrimeMoyenneViewBean extends BJadePersistentObjectViewBean {
    private SimplePrimeMoyenne simplePrimeMoyenne = null;

    public AMPrimeMoyenneViewBean() {
        super();
        simplePrimeMoyenne = new SimplePrimeMoyenne();
    }

    public AMPrimeMoyenneViewBean(SimplePrimeMoyenne simplePrimeMoyenne) {
        this();
        this.simplePrimeMoyenne = simplePrimeMoyenne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().create(simplePrimeMoyenne);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().delete(simplePrimeMoyenne);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simplePrimeMoyenne.getId();
    }

    public SimplePrimeMoyenne getSimplePrimeMoyenne() {
        return simplePrimeMoyenne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simplePrimeMoyenne != null) {
            return new BSpy(simplePrimeMoyenne.getSpy());
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
        // this.simplePrimeMoyenne =
        // AmalServiceLocator.getSimplePrimeMoyenneService().read(this.simplePrimeMoyenne.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        simplePrimeMoyenne.setId(newId);
    }

    public void setSimplePrimeMoyenne(SimplePrimeMoyenne simplePrimeMoyenne) {
        this.simplePrimeMoyenne = simplePrimeMoyenne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().update(simplePrimeMoyenne);
    }

}
