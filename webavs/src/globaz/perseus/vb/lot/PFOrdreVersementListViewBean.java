package globaz.perseus.vb.lot;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * Classe de gestion des listBeans ordreVersement
 * 
 * @author MBO
 * 
 */
public class PFOrdreVersementListViewBean extends BJadePersistentObjectListViewBean {

    // instance du modele de recherche
    private OrdreVersementSearchModel ordreVersementSearch = null;

    /**
     * Constructeur
     */
    public PFOrdreVersementListViewBean() {
        super();
        ordreVersementSearch = new OrdreVersementSearchModel();
    }

    /**
     * Méthode de recherche avec paramètre de recherche
     * 
     * @throws LotException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws LotException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        ordreVersementSearch = PerseusServiceLocator.getOrdreVersementService().search(ordreVersementSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < ordreVersementSearch.getSize() ? new PFOrdreVersementViewBean(
                (OrdreVersement) ordreVersementSearch.getSearchResults()[idx]) : new PFOrdreVersementViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return ordreVersementSearch;
    }

    /**
     * Retourne l'instance du modele de recherche
     * 
     * @return the ordreVersementSearch
     */
    public OrdreVersementSearchModel getOrdreVersementSearch() {
        return ordreVersementSearch;
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
     * @param ordreVersementSearch
     *            the ordreVersementSearch to set
     */
    public void setOrdreVersementSearch(OrdreVersementSearchModel ordreVersementSearch) {
        this.ordreVersementSearch = ordreVersementSearch;
    }

}