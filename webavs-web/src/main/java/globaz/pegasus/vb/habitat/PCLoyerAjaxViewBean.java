package globaz.pegasus.vb.habitat;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.ArrayList;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.models.habitat.LoyerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCLoyerAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private PCLoyerAjaxListViewBean listeLoyer;
    private Loyer loyer = null;

    public PCLoyerAjaxViewBean() {
        super();
        loyer = new Loyer();
    }

    public PCLoyerAjaxViewBean(Loyer loyer) {
        super();
        this.loyer = loyer;
    }

    @Override
    public void add() throws Exception {

        loyer = PegasusServiceLocator.getDroitService().createLoyer(getDroit(), getInstanceDroitMembreFamille(), loyer);

        this.updateListe();
    }

    @Override
    public void delete() throws Exception {
        String idDroitMembreFamille = loyer.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        loyer = PegasusServiceLocator.getDroitService().deleteLoyer(getDroit(), loyer);
        this.updateListe(idDroitMembreFamille);
    }

    /**
     * @return the doAddPeriode
     */
    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    @Override
    public String getId() {
        return loyer.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeLoyer;
    }

    /**
     * @return the loyer
     */
    public Loyer getLoyer() {
        return loyer;
    }

    public String getNomBailleurRegie() {
        if (loyer.getSimpleLoyer().getIdBailleurRegie() != null) {
            return loyer.getTiersBailleurRegie().getDesignation1() + " "
                    + loyer.getTiersBailleurRegie().getDesignation2();
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return loyer.getSimpleDonneeFinanciereHeader();
    }

    @Override
    public BSpy getSpy() {
        return (loyer != null) && !loyer.isNew() ? new BSpy(loyer.getSpy()) : new BSpy((BSession) getISession());
    }

    @Override
    public Iterator iterator() {
        return listeLoyer == null ? new ArrayList().iterator() : listeLoyer.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        loyer = PegasusServiceLocator.getDroitService().readLoyer(loyer.getId());
    }

    /**
     * @param doAddPeriode
     *            the doAddPeriode to set
     */
    public void setDoAddPeriode(Boolean doAddPeriode) {
        this.doAddPeriode = doAddPeriode;
    }

    @Override
    public void setId(String newId) {
        loyer.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeLoyer = (PCLoyerAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param loyer
     *            the loyer to set
     */
    public void setLoyer(Loyer loyer) {
        this.loyer = loyer;
    }

    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            loyer = (PegasusServiceLocator.getDroitService().createAndCloseLoyer(getDroit(), loyer, false));
        } else if (isForceClorePeriode()) {
            loyer = (PegasusServiceLocator.getDroitService().createAndCloseLoyer(getDroit(), loyer, true));
        } else {
            loyer = (PegasusServiceLocator.getDroitService().updateLoyer(getDroit(), getInstanceDroitMembreFamille(),
                    loyer));
        }
        this.updateListe();

    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        this.updateListe(loyer.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    /**
     * Mise à jour de la liste avec paramètre
     * 
     * @param idDroitMembreFamille
     * @throws Exception
     */
    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeLoyer = new PCLoyerAjaxListViewBean();
            LoyerSearch search = listeLoyer.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeLoyer.find();
        }
    }

}
