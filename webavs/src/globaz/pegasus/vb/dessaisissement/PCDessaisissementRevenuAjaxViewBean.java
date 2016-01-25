/**
 * 
 */
package globaz.pegasus.vb.dessaisissement;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCDessaisissementRevenuAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DessaisissementRevenu dessaisissementRevenu = null;
    private Boolean doAddPeriode = null;
    private PCDessaisissementRevenuAjaxListViewBean listeDessaisissementRevenu;

    /**
	 * 
	 */
    public PCDessaisissementRevenuAjaxViewBean() {
        super();
        dessaisissementRevenu = new DessaisissementRevenu();
    }

    /**
     * @param dessaisissementRevenu
     */
    public PCDessaisissementRevenuAjaxViewBean(DessaisissementRevenu dessaisissementRevenu) {
        super();
        this.dessaisissementRevenu = dessaisissementRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        dessaisissementRevenu = PegasusServiceLocator.getDroitService().createDessaisissementRevenu(getDroit(),
                getInstanceDroitMembreFamille(), dessaisissementRevenu);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = dessaisissementRevenu.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        dessaisissementRevenu = PegasusServiceLocator.getDroitService().deleteDessaisissementRevenu(getDroit(),
                getInstanceDroitMembreFamille(), dessaisissementRevenu);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the dessaisissementRevenu
     */
    public DessaisissementRevenu getDessaisissementRevenu() {
        return dessaisissementRevenu;
    }

    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return dessaisissementRevenu.getId();
    }

    /**
     * @return the listeDessaisissementRevenu
     */
    public PCDessaisissementRevenuAjaxListViewBean getListeDessaisissementRevenu() {
        return listeDessaisissementRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeDessaisissementRevenu;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return dessaisissementRevenu.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (dessaisissementRevenu != null) && !dessaisissementRevenu.isNew() ? new BSpy(
                dessaisissementRevenu.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeDessaisissementRevenu.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        dessaisissementRevenu = PegasusServiceLocator.getDroitService().readDessaisissementRevenu(
                dessaisissementRevenu.getId());
    }

    /**
     * @param dessaisissementRevenu
     *            the dessaisissementRevenu to set
     */
    public void setDessaisissementRevenu(DessaisissementRevenu dessaisissementRevenu) {
        this.dessaisissementRevenu = dessaisissementRevenu;
    }

    public void setDoAddPeriode(Boolean doAddPeriode) {
        this.doAddPeriode = doAddPeriode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        dessaisissementRevenu.setId(newId);
    }

    /**
     * @param listeDessaisissementRevenu
     *            the listeDessaisissementRevenu to set
     */
    public void setListeDessaisissementRevenu(PCDessaisissementRevenuAjaxListViewBean listeDessaisissementRevenu) {
        this.listeDessaisissementRevenu = listeDessaisissementRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeDessaisissementRevenu = (PCDessaisissementRevenuAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        if (doAddPeriode.booleanValue()) {
            dessaisissementRevenu = (PegasusServiceLocator.getDroitService().createAndCloseDessaisissementRevenu(
                    getDroit(), dessaisissementRevenu, false));
        } else if (isForceClorePeriode()) {
            dessaisissementRevenu = (PegasusServiceLocator.getDroitService().createAndCloseDessaisissementRevenu(
                    getDroit(), dessaisissementRevenu, true));
        } else {
            dessaisissementRevenu = (PegasusServiceLocator.getDroitService().updateDessaisissementRevenu(getDroit(),
                    getInstanceDroitMembreFamille(), dessaisissementRevenu));
        }

        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(dessaisissementRevenu.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeDessaisissementRevenu = new PCDessaisissementRevenuAjaxListViewBean();
            DessaisissementRevenuSearch search = listeDessaisissementRevenu.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeDessaisissementRevenu.find();
        }
    }

}
