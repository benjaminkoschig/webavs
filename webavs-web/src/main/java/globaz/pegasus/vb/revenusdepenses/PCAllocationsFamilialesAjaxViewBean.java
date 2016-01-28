package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAllocationsFamilialesAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AllocationsFamiliales allocationsFamiliales = null;

    private Boolean doAddPeriode = null;
    private PCAllocationsFamilialesAjaxListViewBean listeAllocationsFamiliales;

    /**
	 * 
	 */
    public PCAllocationsFamilialesAjaxViewBean() {
        super();
        allocationsFamiliales = new AllocationsFamiliales();
    }

    /**
     * @param AllocationsFamiliales
     */
    public PCAllocationsFamilialesAjaxViewBean(AllocationsFamiliales allocationsFamiliales) {
        super();
        this.allocationsFamiliales = allocationsFamiliales;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        allocationsFamiliales = PegasusServiceLocator.getDroitService().createAllocationsFamiliales(getDroit(),
                getInstanceDroitMembreFamille(), allocationsFamiliales);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = allocationsFamiliales.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        allocationsFamiliales = PegasusServiceLocator.getDroitService().deleteAllocationsFamiliales(getDroit(),
                allocationsFamiliales);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the allocationsFamiliales
     */
    public AllocationsFamiliales getAllocationsFamiliales() {
        return allocationsFamiliales;
    }

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
        return allocationsFamiliales.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAllocationsFamiliales;
    }

    public String getNomCaisse() {
        if (allocationsFamiliales.getCaisse() != null) {
            return allocationsFamiliales.getCaisse().getTiers().getDesignation1();
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return allocationsFamiliales.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (allocationsFamiliales != null) && !allocationsFamiliales.isNew() ? new BSpy(
                allocationsFamiliales.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAllocationsFamiliales.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        allocationsFamiliales = PegasusServiceLocator.getDroitService().readAllocationsFamiliales(
                allocationsFamiliales.getId());
    }

    /**
     * @param AllocationsFamiliales
     *            the AllocationsFamiliales to set
     */
    public void setAllocationsFamiliales(AllocationsFamiliales allocationsFamiliales) {
        this.allocationsFamiliales = allocationsFamiliales;
    }

    /*
     * public void setMontantActivite(String montantActiviteLucrative) { this.allocationsFamiliales
     * .getSimpleAllocationsFamiliales() .setMontantActiviteLucrative(montantActiviteLucrative); }
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
        allocationsFamiliales.setId(newId);
    }

    public void setIdCaisseCompensation(String idCaisseCompensation) {
        allocationsFamiliales.getSimpleAllocationsFamiliales().setIdCaisseAf(idCaisseCompensation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAllocationsFamiliales = (PCAllocationsFamilialesAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            allocationsFamiliales = (PegasusServiceLocator.getDroitService().createAndCloseAllocationsFamiliales(
                    getDroit(), allocationsFamiliales, false));
        } else if (isForceClorePeriode()) {
            allocationsFamiliales = (PegasusServiceLocator.getDroitService().createAndCloseAllocationsFamiliales(
                    getDroit(), allocationsFamiliales, true));
        } else {
            allocationsFamiliales = (PegasusServiceLocator.getDroitService().updateAllocationsFamiliales(getDroit(),
                    getInstanceDroitMembreFamille(), allocationsFamiliales));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(allocationsFamiliales.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeAllocationsFamiliales = new PCAllocationsFamilialesAjaxListViewBean();
            AllocationsFamilialesSearch search = listeAllocationsFamiliales.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedAF");
            listeAllocationsFamiliales.find();
        }

    }

}
