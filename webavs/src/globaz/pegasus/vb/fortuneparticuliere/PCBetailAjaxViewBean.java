/**
 * 
 */
package globaz.pegasus.vb.fortuneparticuliere;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCBetailAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Betail betail = null;
    private Boolean doAddPeriode = null;
    private PCBetailAjaxListViewBean listePretsEnversTiers;

    /**
	 * 
	 */
    public PCBetailAjaxViewBean() {
        super();
        betail = new Betail();
    }

    /**
     * @param betail
     */
    public PCBetailAjaxViewBean(Betail betail) {
        super();
        this.betail = betail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        betail = PegasusServiceLocator.getDroitService().createBetail(getDroit(), getInstanceDroitMembreFamille(),
                betail);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = betail.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        betail = PegasusServiceLocator.getDroitService().deleteBetail(getDroit(), betail);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the betail
     */
    public Betail getBetail() {
        return betail;
    }

    /**
     * @return the doAddPeriode
     */
    public String getDoAddPeriode() {
        return doAddPeriode.toString();
    }

    public DroitMembreFamilleEtendu getDroitMembreFamille() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return betail.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePretsEnversTiers;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return betail.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (betail != null) && !betail.isNew() ? new BSpy(betail.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePretsEnversTiers.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        betail = PegasusServiceLocator.getDroitService().readBetail(betail.getId());
    }

    /**
     * @param Betail
     *            the Betail to set
     */
    public void setBetail(Betail betail) {
        this.betail = betail;
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
        betail.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCBetailAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        betail.getSimpleBetail().setPartProprieteNumerateur(parts[0]);
        betail.getSimpleBetail().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            betail = (PegasusServiceLocator.getDroitService().createAndCloseBetail(getDroit(), betail, false));
        } else if (isForceClorePeriode()) {
            betail = (PegasusServiceLocator.getDroitService().createAndCloseBetail(getDroit(), betail, true));
        } else {
            betail = (PegasusServiceLocator.getDroitService().updateBetail(getDroit(), getInstanceDroitMembreFamille(),
                    betail));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(betail.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCBetailAjaxListViewBean();
            BetailSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }

}
