/**
 * 
 */
package globaz.pegasus.vb.dessaisissement;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.utils.PCDessaisissementHandler;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContrePresation;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCDessaisissementFortuneAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    transient private CalculContrePresation calculContrePresation = null;
    private DessaisissementFortune dessaisissementFortune = null;
    private PCDessaisissementFortuneAjaxListViewBean listeDessaisissementFortune;

    /**
	 * 
	 */
    public PCDessaisissementFortuneAjaxViewBean() {
        super();
        dessaisissementFortune = new DessaisissementFortune();
    }

    /**
     * @param dessaisissementFortune
     */
    public PCDessaisissementFortuneAjaxViewBean(DessaisissementFortune dessaisissementFortune) {
        super();
        this.dessaisissementFortune = dessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        dessaisissementFortune = PegasusServiceLocator.getDroitService().createDessaisissementFortune(getDroit(),
                getInstanceDroitMembreFamille(), dessaisissementFortune);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = dessaisissementFortune.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();

        dessaisissementFortune = PegasusServiceLocator.getDroitService().deleteDessaisissementFortune(getDroit(),
                getInstanceDroitMembreFamille(), dessaisissementFortune);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the calculContrePresation
     */
    public CalculContrePresation getCalculContrePresation() {
        return calculContrePresation;
    }

    /**
     * @return the dessaisissementFortune
     */
    public DessaisissementFortune getDessaisissementFortune() {
        return dessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return dessaisissementFortune.getId();
    }

    /**
     * @return the listeDessaisissementFortune
     */
    public PCDessaisissementFortuneAjaxListViewBean getListeDessaisissementFortune() {
        return listeDessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeDessaisissementFortune;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return dessaisissementFortune.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (dessaisissementFortune != null) && !dessaisissementFortune.isNew() ? new BSpy(
                dessaisissementFortune.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeDessaisissementFortune.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        dessaisissementFortune = PegasusServiceLocator.getDroitService().readDessaisissementFortune(
                dessaisissementFortune.getId());

        if (PCDessaisissementHandler.hasCalculToDo(dessaisissementFortune)) {
            calculContrePresation = PegasusServiceLocator.getDessaisissementFortuneService().calculContrePresation(
                    dessaisissementFortune);
        }

    }

    /**
     * @param calculContrePresation
     *            the calculContrePresation to set
     */
    public void setCalculContrePresation(CalculContrePresation calculContrePresation) {
        this.calculContrePresation = calculContrePresation;
    }

    /**
     * @param dessaisissementFortune
     *            the dessaisissementFortune to set
     */
    public void setDessaisissementFortune(DessaisissementFortune dessaisissementFortune) {
        this.dessaisissementFortune = dessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        dessaisissementFortune.setId(newId);
    }

    /**
     * @param listeDessaisissementFortune
     *            the listeDessaisissementFortune to set
     */
    public void setListeDessaisissementFortune(PCDessaisissementFortuneAjaxListViewBean listeDessaisissementFortune) {
        this.listeDessaisissementFortune = listeDessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeDessaisissementFortune = (PCDessaisissementFortuneAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        dessaisissementFortune = (PegasusServiceLocator.getDroitService().updateDessaisissementFortune(getDroit(),
                getInstanceDroitMembreFamille(), dessaisissementFortune));
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(dessaisissementFortune.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeDessaisissementFortune = new PCDessaisissementFortuneAjaxListViewBean();
            DessaisissementFortuneSearch search = listeDessaisissementFortune.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeDessaisissementFortune.find();
        }
    }

}
