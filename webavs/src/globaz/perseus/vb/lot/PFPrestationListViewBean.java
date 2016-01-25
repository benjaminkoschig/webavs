package globaz.perseus.vb.lot;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationRP;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * Classe de gestion des listBeans Prestation
 * 
 * @author BSC
 * 
 */
public class PFPrestationListViewBean extends BJadePersistentObjectListViewBean {

    private String csTypeLot = null;
    // instance du modele de recherche
    private PrestationSearchModel prestationSearch = null;

    /**
     * Constructeur
     */
    public PFPrestationListViewBean() {
        super();
        prestationSearch = new PrestationSearchModel();
    }

    /**
     * Méthode de recherche avec paramètre de recherche
     * 
     * @throws Exception
     */
    @Override
    public void find() throws Exception {
        if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(csTypeLot)
                || CSTypeLot.LOT_FACTURES.getCodeSystem().equals(csTypeLot)) {
            prestationSearch.setModelClass(Prestation.class);
            prestationSearch.getInTypeLot().add(csTypeLot);
        } else {
            prestationSearch.setModelClass(PrestationRP.class);
        }
        prestationSearch = PerseusServiceLocator.getPrestationService().search(prestationSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(csTypeLot)
                || CSTypeLot.LOT_FACTURES.getCodeSystem().equals(csTypeLot)) {
            return idx < prestationSearch.getSize() ? new PFPrestationViewBean(
                    (Prestation) prestationSearch.getSearchResults()[idx]) : new PFPrestationViewBean();
        } else {
            return idx < prestationSearch.getSize() ? new PFPrestationRPViewBean(
                    (PrestationRP) prestationSearch.getSearchResults()[idx]) : new PFPrestationRPViewBean();
        }
    }

    /**
     * @return the csTypeLot
     */
    public String getCsTypeLot() {
        return csTypeLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return prestationSearch;
    }

    /**
     * Retourne l'instance du modele de recherche
     * 
     * @return the prestationSearch
     */
    public PrestationSearchModel getPrestationSearch() {
        return prestationSearch;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * @param csTypeLot
     *            the csTypeLot to set
     */
    public void setCsTypeLot(String csTypeLot) {
        this.csTypeLot = csTypeLot;
    }

    /**
     * @param prestationSearch
     *            the prestationSearch to set
     */
    public void setPrestationSearch(PrestationSearchModel prestationSearch) {
        this.prestationSearch = prestationSearch;
    }

}