package globaz.pegasus.vb.renteijapi;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.IjApgSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCIjApgAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private IjApg ijApg = null;
    private PCIjApgAjaxListViewBean listeIjApg;

    /**
	 * 
	 */
    public PCIjApgAjaxViewBean() {
        super();
        ijApg = new IjApg();
    }

    /**
     * @param ijApg
     */
    public PCIjApgAjaxViewBean(IjApg ijApg) {
        super();
        this.ijApg = ijApg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        ijApg = PegasusServiceLocator.getDroitService().createIjApg(getDroit(), getInstanceDroitMembreFamille(), ijApg);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = ijApg.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        ijApg = PegasusServiceLocator.getDroitService().deleteIjApg(getDroit(), ijApg);
        this.updateListe(idDroitMbrFam);
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
        return ijApg.getId();
    }

    /**
     * @return the ijApg
     */
    public IjApg getIjApg() {
        return ijApg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeIjApg;
    }

    public String getNomFournisseurPrestation() {
        if (ijApg.getSimpleIjApg().getIdFournisseurPrestation() != null) {
            return ijApg.getTiersFournisseurPrestation().getDesignation1() + " "
                    + ijApg.getTiersFournisseurPrestation().getDesignation2();
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return ijApg.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (ijApg != null) && !ijApg.isNew() ? new BSpy(ijApg.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeIjApg.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        ijApg = PegasusServiceLocator.getDroitService().readIjApg(ijApg.getId());
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
        ijApg.setId(newId);
    }

    /**
     * @param ijApg
     *            the ijApg to set
     */
    public void setIjApg(IjApg ijApg) {
        this.ijApg = ijApg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeIjApg = (PCIjApgAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            ijApg = (PegasusServiceLocator.getDroitService().createAndCloseIjApg(getDroit(), ijApg, false));
        } else if (isForceClorePeriode()) {
            ijApg = (PegasusServiceLocator.getDroitService().createAndCloseIjApg(getDroit(), ijApg, true));
        } else {
            ijApg = (PegasusServiceLocator.getDroitService().updateIjApg(getDroit(), getInstanceDroitMembreFamille(),
                    ijApg));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(ijApg.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeIjApg = new PCIjApgAjaxListViewBean();
            IjApgSearch search = listeIjApg.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeIjApg.find();
        }
    }
}
