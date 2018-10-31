/**
 *
 */
package globaz.amal.vb.parametreannuel;

import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.services.AmalServiceLocator;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * @author CBU
 *
 */
public class AMParametreAnnuelViewBean extends BJadePersistentObjectViewBean {
    private SimpleParametreAnnuel simpleParametreAnnuel = null;

    public AMParametreAnnuelViewBean() {
        super();
        simpleParametreAnnuel = new SimpleParametreAnnuel();
    }

    public AMParametreAnnuelViewBean(SimpleParametreAnnuel simpleParametreAnnuel) {
        this();
        this.simpleParametreAnnuel = simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().create(simpleParametreAnnuel);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().delete(simpleParametreAnnuel);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleParametreAnnuel.getId();
    }

    public SimpleParametreAnnuel getSimpleParametreAnnuel() {
        return simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleParametreAnnuel != null) {
            return new BSpy(simpleParametreAnnuel.getSpy());
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
        simpleParametreAnnuel.setId(newId);
    }

    public void setSimpleParametreAnnuel(SimpleParametreAnnuel simpleParametreAnnuel) {
        this.simpleParametreAnnuel = simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().update(simpleParametreAnnuel);
    }

}
