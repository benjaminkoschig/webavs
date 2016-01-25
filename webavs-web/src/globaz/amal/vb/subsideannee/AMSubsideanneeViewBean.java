/**
 * 
 */
package globaz.amal.vb.subsideannee;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMSubsideanneeViewBean extends BJadePersistentObjectViewBean {
    private String anneeSubside = null;
    // protected List<SimpleSubsideAnnee> donnees = new ArrayList<SimpleSubsideAnnee>();
    private SimpleSubsideAnnee simpleSubsideAnnee = null;

    public AMSubsideanneeViewBean() {
        super();
        simpleSubsideAnnee = new SimpleSubsideAnnee();
    }

    public AMSubsideanneeViewBean(SimpleSubsideAnnee simpleSubsideAnnee) {
        this();
        this.simpleSubsideAnnee = simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().create(simpleSubsideAnnee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().delete(simpleSubsideAnnee);
    }

    public String getAnneeSubside() {
        return anneeSubside;
    }

    // public List<SimpleSubsideAnnee> getDonnees() {
    // return this.donnees;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleSubsideAnnee.getId();
    }

    public SimpleSubsideAnnee getSimpleSubsideAnnee() {
        return simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleSubsideAnnee != null) {
            return new BSpy(simpleSubsideAnnee.getSpy());
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
        // SimpleSubsideAnneeSearch search = new SimpleSubsideAnneeSearch();
        //
        // search.setForAnneeSubside(this.getAnneeSubside());
        //
        // search = AmalServiceLocator.getSimpleSubsideAnneeService().search(search);
        //
        // for (Iterator<JadeAbstractModel> it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
        // SimpleSubsideAnnee simpleSubsideAnnee = (SimpleSubsideAnnee) it.next();
        // this.donnees.add(simpleSubsideAnnee);
        // }
        // this.simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().read(
        // this.simpleSubsideAnnee.getId());
    }

    public void setAnneeSubside(String anneeSubside) {
        this.anneeSubside = anneeSubside;
    }

    // public void setDonnees(List<SimpleSubsideAnnee> donnees) {
    // this.donnees = donnees;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        simpleSubsideAnnee.setId(newId);
    }

    public void setSimpleSubsideAnnee(SimpleSubsideAnnee simpleSubsideAnnee) {
        this.simpleSubsideAnnee = simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().update(simpleSubsideAnnee);
    }

}
