package globaz.pegasus.vb.renteijapi;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.AutreApi;
import ch.globaz.pegasus.business.models.renteijapi.AutreApiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutreApiAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AutreApi autreApi = null;
    private Boolean doAddPeriode = null;
    private PCAutreApiAjaxListViewBean listeAutreApi;

    /**
	 * 
	 */
    public PCAutreApiAjaxViewBean() {
        super();
        autreApi = new AutreApi();
    }

    /**
     * @param autreApi
     */
    public PCAutreApiAjaxViewBean(AutreApi autreApi) {
        super();
        this.autreApi = autreApi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        autreApi = PegasusServiceLocator.getDroitService().createAutreApi(getDroit(), getInstanceDroitMembreFamille(),
                autreApi);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = autreApi.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        autreApi = PegasusServiceLocator.getDroitService().deleteAutreApi(getDroit(), autreApi);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the autreApi
     */
    public AutreApi getAutreApi() {
        return autreApi;
    }

    /**
     * @return the doAddPeriode
     */
    public String getDoAddPeriode() {
        return doAddPeriode.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return autreApi.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAutreApi;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return autreApi.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (autreApi != null) && !autreApi.isNew() ? new BSpy(autreApi.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAutreApi.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        autreApi = PegasusServiceLocator.getDroitService().readAutreApi(autreApi.getId());
    }

    /**
     * @param autreApi
     *            the autreApi to set
     */
    public void setAutreApi(AutreApi autreApi) {
        this.autreApi = autreApi;
    }

    /**
     * @param doAddPeriode
     *            the doAddPeriode to set
     */
    public void setDoAddPeriode(String doAddPeriode) {
        this.doAddPeriode = Boolean.valueOf(doAddPeriode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        autreApi.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAutreApi = (PCAutreApiAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            autreApi = (PegasusServiceLocator.getDroitService().createAndCloseAutreApi(getDroit(), autreApi, false));
        } else if (isForceClorePeriode()) {
            autreApi = (PegasusServiceLocator.getDroitService().createAndCloseAutreApi(getDroit(), autreApi, true));
        } else {
            autreApi = (PegasusServiceLocator.getDroitService().updateAutreApi(getDroit(),
                    getInstanceDroitMembreFamille(), autreApi));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(autreApi.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeAutreApi = new PCAutreApiAjaxListViewBean();
            AutreApiSearch search = listeAutreApi.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeAutreApi.find();
        }
    }
}
