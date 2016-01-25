/**
 * 
 */
package globaz.amal.vb.parametreannuel;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.services.AmalServiceLocator;

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
        // cherche les données financières
        // SimpleParametreAnnuel search = new SimpleParametreAnnuel();
        //
        // search.setIdParametreAnnuel(this.getId());
        //
        // this.simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().read(this.getId());
        //
        // for (Iterator<JadeAbstractModel> it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
        // SimpleSubsideAnnee simpleSubsideAnnee = (SimpleSubsideAnnee) it.next();
        // this.donnees.add(simpleSubsideAnnee);
        // }
        // this.simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().read(
        // this.simpleParametreAnnuel.getId());
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
