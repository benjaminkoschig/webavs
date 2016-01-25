/**
 * 
 */
package globaz.pegasus.vb.fortuneusuelle;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPPSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCCapitalLPPAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CapitalLPP capitalLPP = null;
    private Boolean doAddPeriode = null;
    private PCCapitalLPPAjaxListViewBean listeCapitalLPP;

    /**
	 * 
	 */
    public PCCapitalLPPAjaxViewBean() {
        super();
        capitalLPP = new CapitalLPP();
    }

    /**
     * @param capitalLPP
     */
    public PCCapitalLPPAjaxViewBean(CapitalLPP capitalLPP) {
        super();
        this.capitalLPP = capitalLPP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        capitalLPP = PegasusServiceLocator.getDroitService().createCapitalLPP(getDroit(),
                getInstanceDroitMembreFamille(), capitalLPP);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = capitalLPP.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        capitalLPP = PegasusServiceLocator.getDroitService().deleteCapitalLPP(getDroit(), capitalLPP);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the capitalLPP
     */
    public CapitalLPP getCapitalLPP() {
        return capitalLPP;
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
        return capitalLPP.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeCapitalLPP;
    }

    public String getNomCaisse() {
        if (capitalLPP.getCaisse() != null) {
            return capitalLPP.getCaisse().getTiers().getDesignation1();
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return capitalLPP.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (capitalLPP != null) && !capitalLPP.isNew() ? new BSpy(capitalLPP.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeCapitalLPP.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        capitalLPP = PegasusServiceLocator.getDroitService().readCapitalLPP(capitalLPP.getId());
    }

    /**
     * @param CapitalLPP
     *            the CapitalLPP to set
     */
    public void setCapitalLPP(CapitalLPP capitalLPP) {
        this.capitalLPP = capitalLPP;
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
        capitalLPP.setId(newId);
    }

    public void setIdInstitutionPrevoyance(String idCaisseCompensation) {
        capitalLPP.getSimpleCapitalLPP().setIdInstitutionPrevoyance(idCaisseCompensation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeCapitalLPP = (PCCapitalLPPAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        capitalLPP.getSimpleCapitalLPP().setPartProprieteNumerateur(parts[0]);
        capitalLPP.getSimpleCapitalLPP().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            capitalLPP = (PegasusServiceLocator.getDroitService().createAndCloseCapitalLPP(getDroit(), capitalLPP,
                    false));
        } else if (isForceClorePeriode()) {
            capitalLPP = (PegasusServiceLocator.getDroitService()
                    .createAndCloseCapitalLPP(getDroit(), capitalLPP, true));
        } else {
            capitalLPP = (PegasusServiceLocator.getDroitService().updateCapitalLPP(getDroit(),
                    getInstanceDroitMembreFamille(), capitalLPP));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(capitalLPP.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeCapitalLPP = new PCCapitalLPPAjaxListViewBean();
            CapitalLPPSearch search = listeCapitalLPP.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedCLPP");
            listeCapitalLPP.find();
        }
    }

}
