package globaz.pegasus.vb.droit;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDroitListViewBean extends BJadePersistentObjectListViewBean {

    private DroitSearch droitSearch = null;

    public PCDroitListViewBean() {
        super();
        droitSearch = new DroitSearch();
    }

    @Override
    public void find() throws Exception {
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < droitSearch.getSize() ? new PCDroitViewBean((Droit) droitSearch.getSearchResults()[idx])
                : new PCDroitViewBean();
    }

    /**
     * @return the droitSearch
     */
    public DroitSearch getDroitSearch() {
        return droitSearch;
    }

    /**
     * @return the forCsEtatDroit
     */
    public String getForCsEtatDroit() {
        return droitSearch.getForCsEtatDroit();
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return droitSearch.getForCsSexe();
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return droitSearch.getForDateNaissance();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return droitSearch;
    }

    /**
     * @param droitSearch
     *            the droitSearch to set
     */
    public void setDroitSearch(DroitSearch droitSearch) {
        this.droitSearch = droitSearch;
    }

    /**
     * @param forCsEtatDroit
     *            the forCsEtatDroit to set
     */
    public void setForCsEtatDroit(String forCsEtatDroit) {
        droitSearch.setForCsEtatDroit(forCsEtatDroit);
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        droitSearch.setForCsSexe(forCsSexe);
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        droitSearch.setForDateNaissance(forDateNaissance);
    }

}
