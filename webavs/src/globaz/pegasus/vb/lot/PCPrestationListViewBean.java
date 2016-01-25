package globaz.pegasus.vb.lot;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Classe de gestion des listBeans Prestation
 * 
 * @author BSC
 * 
 */
public class PCPrestationListViewBean extends BJadePersistentObjectListViewBean {

    // instance du modele de recherche
    private PrestationSearch prestationSearch = null;

    /**
     * Constructeur
     */
    public PCPrestationListViewBean() {
        super();
        prestationSearch = new PrestationSearch();
    }

    /**
     * Méthode de recherche avec paramètre de recherche
     * 
     * @throws PrestationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws PrestationException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        prestationSearch = PegasusServiceLocator.getPrestationService().search(prestationSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < prestationSearch.getSize() ? new PCPrestationViewBean(
                (Prestation) prestationSearch.getSearchResults()[idx]) : new PCPrestationViewBean();
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
    public PrestationSearch getPrestationSearch() {
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
     * @param prestationSearch
     *            the prestationSearch to set
     */
    public void setPrestationSearch(PrestationSearch prestationSearch) {
        this.prestationSearch = prestationSearch;
    }

}