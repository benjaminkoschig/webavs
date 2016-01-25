package globaz.pegasus.vb.lot;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Arrays;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;

/**
 * Classe de gestion des listBeans Lot
 * 
 * @author BSC
 * 
 */
public class PCLotListViewBean extends BJadePersistentObjectListViewBean {

    // instance du modele de recherche
    private SimpleLotSearch lotSearch = null;
    private boolean mustSearchRestitution = false;

    /**
     * Constructeur
     */
    public PCLotListViewBean() {
        super();
        lotSearch = new SimpleLotSearch();
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
        lotSearch.setForCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        lotSearch.setWhereKey(SimpleLotSearch.LOT_FOR_PC);
        if (mustSearchRestitution) {
            lotSearch.setInCsType(Arrays.asList(IRELot.CS_TYP_LOT_DECISION, IRELot.CS_TYP_LOT_DEBLOCAGE_RA));
        } else {
            lotSearch.setInCsType(Arrays.asList(IRELot.CS_TYP_LOT_DECISION, IRELot.CS_TYP_LOT_DEBLOCAGE_RA,
                    IRELot.CS_TYP_LOT_DECISION_RESTITUTION));
        }
        lotSearch.setForCsType2(IRELot.CS_TYP_LOT_MENSUEL);

        lotSearch = CorvusServiceLocator.getLotService().search(lotSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < lotSearch.getSize() ? new PCLotViewBean((SimpleLot) lotSearch.getSearchResults()[idx])
                : new PCLotViewBean();
    }

    /**
     * Retourne l'instance du modele de recherche
     * 
     * @return the lotSearch
     */
    public SimpleLotSearch getLotSearch() {
        return lotSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return lotSearch;
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
     * @param lotSearch
     *            the lotSearch to set
     */
    public void setLotSearch(SimpleLotSearch lotSearch) {
        this.lotSearch = lotSearch;
    }

    public boolean isMustSearchRestitution() {
        return mustSearchRestitution;
    }

    public void setMustSearchRestitution(boolean mustSearchRestitution) {
        this.mustSearchRestitution = mustSearchRestitution;
    }

}