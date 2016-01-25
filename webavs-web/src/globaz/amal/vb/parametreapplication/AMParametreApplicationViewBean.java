/**
 * 
 */
package globaz.amal.vb.parametreapplication;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMParametreApplicationViewBean extends BJadePersistentObjectViewBean {
    private SimpleParametreApplication simpleParametreApplication = null;

    /**
     * Default contructor
     */
    public AMParametreApplicationViewBean() {
        simpleParametreApplication = new SimpleParametreApplication();
    }

    /**
     * Default contructor with main parameter
     */
    public AMParametreApplicationViewBean(SimpleParametreApplication paramApplication) {
        this();
        simpleParametreApplication = paramApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().create(
                simpleParametreApplication);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().delete(
                simpleParametreApplication);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleParametreApplication.getId();
    }

    /**
     * @return the simpleParametreApplication
     */
    public SimpleParametreApplication getSimpleParametreApplication() {
        return simpleParametreApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleParametreApplication != null) {
            return new BSpy(simpleParametreApplication.getSpy());
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
        simpleParametreApplication.setId(newId);
    }

    /**
     * @param simpleParametreApplication
     *            the simpleParametreApplication to set
     */
    public void setSimpleParametreApplication(SimpleParametreApplication simpleParametreApplication) {
        this.simpleParametreApplication = simpleParametreApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().update(
                simpleParametreApplication);
    }

}
