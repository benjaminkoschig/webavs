package globaz.pegasus.vb.habitat;

import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;

import java.util.Iterator;

public class PCSejourMoisPartielHomeAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Boolean afficheTypeChambre = false;
    private Boolean doAddPeriode = null;
    private PCSejourMoisPartielHomeAjaxListViewBean listeSejourMoisPartielHome;
    private SejourMoisPartielHome sejourMoisPartielHome = null;

    public PCSejourMoisPartielHomeAjaxViewBean() {
        super();
        sejourMoisPartielHome = new SejourMoisPartielHome();
    }

    public PCSejourMoisPartielHomeAjaxViewBean(SejourMoisPartielHome sejourMoisPartielHome) {
        super();
        this.sejourMoisPartielHome = sejourMoisPartielHome;
    }

    @Override
    public void add() throws Exception {
        sejourMoisPartielHome = PegasusServiceLocator.getDroitService().createSejourMoisPartielHome(getDroit(),
                getInstanceDroitMembreFamille(), sejourMoisPartielHome);

        this.updateListe();
    }

    @Override
    public void delete() throws Exception {
        String idDroitMembreFamille = sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        sejourMoisPartielHome = PegasusServiceLocator.getDroitService().deleteSejourMoisPartielHome(getDroit(),
                sejourMoisPartielHome);
        this.updateListe(idDroitMembreFamille);
    }

    public void find() throws Exception {
        listeSejourMoisPartielHome = new PCSejourMoisPartielHomeAjaxListViewBean();
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
        return sejourMoisPartielHome.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeSejourMoisPartielHome;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return sejourMoisPartielHome.getSimpleDonneeFinanciereHeader();
    }

    @Override
    public BSpy getSpy() {
        return (sejourMoisPartielHome != null) && !sejourMoisPartielHome.isNew() ? new BSpy(sejourMoisPartielHome.getSpy())
                : new BSpy((BSession) getISession());
    }

    /**
     * @return the sejourMoisPartielHome
     */
    public SejourMoisPartielHome getSejourMoisPartielHome() {
        return sejourMoisPartielHome;
    }

    @Override
    public Iterator iterator() {
        return listeSejourMoisPartielHome.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        // this.updateListe();
        if (!JadeStringUtil.isEmpty(sejourMoisPartielHome.getId())) {
            sejourMoisPartielHome = PegasusServiceLocator.getDroitService().readSejourMoisPartielHome(
                    sejourMoisPartielHome.getId());
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
        sejourMoisPartielHome.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeSejourMoisPartielHome = (PCSejourMoisPartielHomeAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param sejourMoisPartielHome
     *            the sejourMoisPartielHome to set
     */
    public void setSejourMoisPartielHome(SejourMoisPartielHome sejourMoisPartielHome) {
        this.sejourMoisPartielHome = sejourMoisPartielHome;
    }

    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            sejourMoisPartielHome = (PegasusServiceLocator.getDroitService().createAndCloseSejourMoisPartielHome(
                    getDroit(), sejourMoisPartielHome, false));
        } else if (isForceClorePeriode()) {
            sejourMoisPartielHome = (PegasusServiceLocator.getDroitService().createAndCloseSejourMoisPartielHome(
                    getDroit(), sejourMoisPartielHome, true));
        } else {
            sejourMoisPartielHome = (PegasusServiceLocator.getDroitService().updateSejourMoisPartielHome(getDroit(),
                    getInstanceDroitMembreFamille(), sejourMoisPartielHome));
        }
        this.updateListe();

    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        this.updateListe(sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    /**
     * Mise à jour de la liste avec paramètre
     * 
     * @param idDroitMembreFamille
     * @throws Exception
     */
    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeSejourMoisPartielHome = new PCSejourMoisPartielHomeAjaxListViewBean();
            SejourMoisPartielHomeSearch search = listeSejourMoisPartielHome.getSearchModel();
            // Pour recherche tout le monde...
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeSejourMoisPartielHome.find();
        }
    }

}
