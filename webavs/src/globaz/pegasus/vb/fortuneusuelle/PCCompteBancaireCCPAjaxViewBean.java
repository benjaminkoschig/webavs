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
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCPSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCCompteBancaireCCPAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CompteBancaireCCP compteBancaireCCP = null;
    private Boolean doAddPeriode = null;
    private PCCompteBancaireCCPAjaxListViewBean listeCompteBancaireCCP;

    /**
	 * 
	 */
    public PCCompteBancaireCCPAjaxViewBean() {
        super();
        compteBancaireCCP = new CompteBancaireCCP();
    }

    /**
     * @param compteBancaireCCP
     */
    public PCCompteBancaireCCPAjaxViewBean(CompteBancaireCCP compteBancaireCCP) {
        super();
        this.compteBancaireCCP = compteBancaireCCP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        compteBancaireCCP = PegasusServiceLocator.getDroitService().createCompteBancaireCCP(getDroit(),
                getInstanceDroitMembreFamille(), compteBancaireCCP);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = compteBancaireCCP.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        compteBancaireCCP = PegasusServiceLocator.getDroitService().deleteCompteBancaireCCP(getDroit(),
                compteBancaireCCP);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the compteBancaireCCP
     */
    public CompteBancaireCCP getCompteBancaireCCP() {
        return compteBancaireCCP;
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
        return compteBancaireCCP.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeCompteBancaireCCP;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return compteBancaireCCP.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (compteBancaireCCP != null) && !compteBancaireCCP.isNew() ? new BSpy(compteBancaireCCP.getSpy())
                : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeCompteBancaireCCP.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        compteBancaireCCP = PegasusServiceLocator.getDroitService().readCompteBancaireCCP(compteBancaireCCP.getId());
    }

    /**
     * @param CompteBancaireCCP
     *            the CompteBancaireCCP to set
     */
    public void setCompteBancaireCCP(CompteBancaireCCP compteBancaireCCP) {
        this.compteBancaireCCP = compteBancaireCCP;
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
        compteBancaireCCP.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeCompteBancaireCCP = (PCCompteBancaireCCPAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        compteBancaireCCP.getSimpleCompteBancaireCCP().setPartProprieteNumerateur(parts[0]);
        compteBancaireCCP.getSimpleCompteBancaireCCP().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            compteBancaireCCP = (PegasusServiceLocator.getDroitService().createAndCloseCompteBancaireCCP(getDroit(),
                    compteBancaireCCP, false));
        } else if (isForceClorePeriode()) {
            compteBancaireCCP = (PegasusServiceLocator.getDroitService().createAndCloseCompteBancaireCCP(getDroit(),
                    compteBancaireCCP, true));
        } else {
            compteBancaireCCP = (PegasusServiceLocator.getDroitService().updateCompteBancaireCCP(getDroit(),
                    getInstanceDroitMembreFamille(), compteBancaireCCP));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(compteBancaireCCP.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeCompteBancaireCCP = new PCCompteBancaireCCPAjaxListViewBean();
            CompteBancaireCCPSearch search = listeCompteBancaireCCP.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedCBCCP");
            listeCompteBancaireCCP.find();
        }
    }

}
