package globaz.pegasus.vb.habitat;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCTaxeJournaliereHomeHandler;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCTaxeJournaliereHomeAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean afficheTypeChambre = false;
    private Boolean doAddPeriode = null;
    private PCTaxeJournaliereHomeAjaxListViewBean listeTaxeJournaliereHome;
    private TaxeJournaliereHome taxeJournaliereHome = null;

    public PCTaxeJournaliereHomeAjaxViewBean() {
        super();
        taxeJournaliereHome = new TaxeJournaliereHome();
    }

    public PCTaxeJournaliereHomeAjaxViewBean(TaxeJournaliereHome taxeJournaliereHome) {
        super();
        this.taxeJournaliereHome = taxeJournaliereHome;
    }

    @Override
    public void add() throws Exception {
        taxeJournaliereHome = PegasusServiceLocator.getDroitService().createTaxeJournaliereHome(getDroit(),
                getInstanceDroitMembreFamille(), taxeJournaliereHome);

        this.updateListe();
    }

    @Override
    public void delete() throws Exception {
        String idDroitMembreFamille = taxeJournaliereHome.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        taxeJournaliereHome = PegasusServiceLocator.getDroitService().deleteTaxeJournaliereHome(getDroit(),
                taxeJournaliereHome);
        this.updateListe(idDroitMembreFamille);
    }

    public void find() throws Exception {
        listeTaxeJournaliereHome = new PCTaxeJournaliereHomeAjaxListViewBean();
    }

    /**
     * @return the afficheTypeChambre
     */
    public Boolean getAfficheTypeChambre() {
        return afficheTypeChambre;
    }

    /**
     * @return the doAddPeriode
     */
    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    @Override
    public String getId() {
        return taxeJournaliereHome.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeTaxeJournaliereHome;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return taxeJournaliereHome.getSimpleDonneeFinanciereHeader();
    }

    @Override
    public BSpy getSpy() {
        return (taxeJournaliereHome != null) && !taxeJournaliereHome.isNew() ? new BSpy(taxeJournaliereHome.getSpy())
                : new BSpy((BSession) getISession());
    }

    /**
     * @return the taxeJournaliereHome
     */
    public TaxeJournaliereHome getTaxeJournaliereHome() {
        return taxeJournaliereHome;
    }

    @Override
    public Iterator iterator() {
        return listeTaxeJournaliereHome.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        // this.updateListe();
        if (!JadeStringUtil.isEmpty(taxeJournaliereHome.getId())) {
            taxeJournaliereHome = PegasusServiceLocator.getDroitService().readTaxeJournaliereHome(
                    taxeJournaliereHome.getId());
        }

    }

    /**
     * @param afficheTypeChambre
     *            the afficheTypeChambre to set
     */
    public void setAfficheTypeChambre(Boolean afficheTypeChambre) {
        this.afficheTypeChambre = afficheTypeChambre;
    }

    private void setAllPrixInMap() throws JadeApplicationServiceNotAvailableException, PrixChambreException,
            HomeException, JadePersistenceException {
        Habitat habitat = new Habitat();

        for (JadeAbstractModel search : listeTaxeJournaliereHome.getSearchModel().getSearchResults()) {

            TaxeJournaliereHome tJournaliereHome = (TaxeJournaliereHome) search;
            habitat.setSimpleDonneeFinanciereHeader(tJournaliereHome.getSimpleDonneeFinanciereHeader());
            habitat.setTaxeJournaliereHome(tJournaliereHome);

            PCTaxeJournaliereHomeHandler.putPrix(habitat);
        }
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
        taxeJournaliereHome.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeTaxeJournaliereHome = (PCTaxeJournaliereHomeAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param taxeJournaliereHome
     *            the taxeJournaliereHome to set
     */
    public void setTaxeJournaliereHome(TaxeJournaliereHome taxeJournaliereHome) {
        this.taxeJournaliereHome = taxeJournaliereHome;
    }

    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            taxeJournaliereHome = (PegasusServiceLocator.getDroitService().createAndCloseTaxeJournaliereHome(
                    getDroit(), taxeJournaliereHome, false));
        } else if (isForceClorePeriode()) {
            taxeJournaliereHome = (PegasusServiceLocator.getDroitService().createAndCloseTaxeJournaliereHome(
                    getDroit(), taxeJournaliereHome, true));
        } else {
            taxeJournaliereHome = (PegasusServiceLocator.getDroitService().updateTaxeJournaliereHome(getDroit(),
                    getInstanceDroitMembreFamille(), taxeJournaliereHome));
        }
        this.updateListe();

    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        this.updateListe(taxeJournaliereHome.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    /**
     * Mise à jour de la liste avec paramètre
     * 
     * @param idDroitMembreFamille
     * @throws Exception
     */
    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeTaxeJournaliereHome = new PCTaxeJournaliereHomeAjaxListViewBean();
            TaxeJournaliereHomeSearch search = listeTaxeJournaliereHome.getSearchModel();
            // Pour recherche tout le monde...
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeTaxeJournaliereHome.find();
            setAllPrixInMap();
        }
    }

}
