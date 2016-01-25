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
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCPretEnversTiersAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private PCPretEnversTiersAjaxListViewBean listePretsEnversTiers;

    private PretEnversTiers pretEnversTiers = null;

    /**
	 * 
	 */
    public PCPretEnversTiersAjaxViewBean() {
        super();
        pretEnversTiers = new PretEnversTiers();
    }

    /**
     * @param pretEnversTiers
     */
    public PCPretEnversTiersAjaxViewBean(PretEnversTiers pretEnversTiers) {
        super();
        this.pretEnversTiers = pretEnversTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        pretEnversTiers = PegasusServiceLocator.getDroitService().createPretEnversTiers(getDroit(),
                getInstanceDroitMembreFamille(), pretEnversTiers);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = pretEnversTiers.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        pretEnversTiers = PegasusServiceLocator.getDroitService().deletePretEnversTiers(getDroit(), pretEnversTiers);
        this.updateListe(idDroitMbrFam);
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
        return pretEnversTiers.getId();
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

    /**
     * @return the pretEnversTiers
     */
    public PretEnversTiers getPretEnversTiers() {
        return pretEnversTiers;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return pretEnversTiers.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (pretEnversTiers != null) && !pretEnversTiers.isNew() ? new BSpy(pretEnversTiers.getSpy()) : new BSpy(
                (BSession) getISession());
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
        pretEnversTiers = PegasusServiceLocator.getDroitService().readPretEnversTiers(pretEnversTiers.getId());
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
        pretEnversTiers.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCPretEnversTiersAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        pretEnversTiers.getSimplePretEnversTiers().setPartProprieteNumerateur(parts[0]);
        pretEnversTiers.getSimplePretEnversTiers().setPartProprieteDenominateur(parts[1]);
    }

    /**
     * @param pretEnversTiers
     *            the pretEnversTiers to set
     */
    public void setPretEnversTiers(PretEnversTiers pretEnversTiers) {
        this.pretEnversTiers = pretEnversTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            pretEnversTiers = (PegasusServiceLocator.getDroitService().createAndClosePretEnversTiers(getDroit(),
                    pretEnversTiers, false));
        } else if (isForceClorePeriode()) {
            pretEnversTiers = (PegasusServiceLocator.getDroitService().createAndClosePretEnversTiers(getDroit(),
                    pretEnversTiers, true));
        } else {
            pretEnversTiers = (PegasusServiceLocator.getDroitService().updatePretEnversTiers(getDroit(),
                    getInstanceDroitMembreFamille(), pretEnversTiers));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(pretEnversTiers.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCPretEnversTiersAjaxListViewBean();
            PretEnversTiersSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }

}
