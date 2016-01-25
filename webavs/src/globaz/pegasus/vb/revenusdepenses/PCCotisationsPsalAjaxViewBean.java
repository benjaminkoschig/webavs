package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsalSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCCotisationsPsalAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CotisationsPsal cotisationsPsal = null;

    private Boolean doAddPeriode = null;
    private PCCotisationsPsalAjaxListViewBean listeCotisationsPsal;

    /**
	 * 
	 */
    public PCCotisationsPsalAjaxViewBean() {
        super();
        cotisationsPsal = new CotisationsPsal();
    }

    /**
     * @param CotisationsPsal
     */
    public PCCotisationsPsalAjaxViewBean(CotisationsPsal cotisationsPsal) {
        super();
        this.cotisationsPsal = cotisationsPsal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        cotisationsPsal = PegasusServiceLocator.getDroitService().createCotisationsPsal(getDroit(),
                getInstanceDroitMembreFamille(), cotisationsPsal);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = cotisationsPsal.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        cotisationsPsal = PegasusServiceLocator.getDroitService().deleteCotisationsPsal(getDroit(), cotisationsPsal);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the cotisationsPsal
     */
    public CotisationsPsal getCotisationsPsal() {
        return cotisationsPsal;
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
        return cotisationsPsal.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeCotisationsPsal;
    }

    public String getNomCaisse() {
        if (cotisationsPsal.getCaisse() != null) {
            return cotisationsPsal.getCaisse().getTiers().getDesignation1();
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return cotisationsPsal.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (cotisationsPsal != null) && !cotisationsPsal.isNew() ? new BSpy(cotisationsPsal.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeCotisationsPsal.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        cotisationsPsal = PegasusServiceLocator.getDroitService().readCotisationsPsal(cotisationsPsal.getId());
    }

    /**
     * @param CotisationsPsal
     *            the CotisationsPsal to set
     */
    public void setCotisationsPsal(CotisationsPsal cotisationsPsal) {
        this.cotisationsPsal = cotisationsPsal;
    }

    /*
     * public void setMontantActivite(String montantActiviteLucrative) { this.cotisationsPsal
     * .getSimpleCotisationsPsal() .setMontantActiviteLucrative(montantActiviteLucrative); }
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
        cotisationsPsal.setId(newId);
    }

    public void setIdCaisseCompensation(String idCaisseCompensation) {
        cotisationsPsal.getSimpleCotisationsPsal().setIdCaisseCompensation(idCaisseCompensation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeCotisationsPsal = (PCCotisationsPsalAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            cotisationsPsal = (PegasusServiceLocator.getDroitService().createAndCloseCotisationsPsal(getDroit(),
                    cotisationsPsal, false));
        } else if (isForceClorePeriode()) {
            cotisationsPsal = (PegasusServiceLocator.getDroitService().createAndCloseCotisationsPsal(getDroit(),
                    cotisationsPsal, true));
        } else {
            cotisationsPsal = (PegasusServiceLocator.getDroitService().updateCotisationsPsal(getDroit(),
                    getInstanceDroitMembreFamille(), cotisationsPsal));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(cotisationsPsal.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeCotisationsPsal = new PCCotisationsPsalAjaxListViewBean();
            CotisationsPsalSearch search = listeCotisationsPsal.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedCPSAL");
            listeCotisationsPsal.find();
        }

    }

}
