package globaz.pegasus.vb.monnaieetrangere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Classe de gestion des listBeans MonnaieEtrangere 6.2010
 * 
 * @author SCE
 * 
 */
public class PCMonnaieEtrangereListViewBean extends BJadePersistentObjectListViewBean {

    // instance du modele de recherche
    private MonnaieEtrangereSearch monnaieEtrangereSearch = null;

    /**
     * Constructeur
     */
    public PCMonnaieEtrangereListViewBean() {
        super();
        monnaieEtrangereSearch = new MonnaieEtrangereSearch();
    }

    /**
     * Méthode de recherche avec paramètre de recherche
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws MonnaieEtrangereException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        monnaieEtrangereSearch = PegasusServiceLocator.getMonnaieEtrangereService().search(monnaieEtrangereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < monnaieEtrangereSearch.getSize() ? new PCMonnaieEtrangereViewBean(
                (MonnaieEtrangere) monnaieEtrangereSearch.getSearchResults()[idx]) : new PCMonnaieEtrangereViewBean();
    }

    /**
     * retourne le jadeAbstractModel
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return monnaieEtrangereSearch;
    }

    /**
     * Retourne l'instance du modele de recherche
     * 
     * @return the monnaieEtrangereSearch
     */
    public MonnaieEtrangereSearch getMonnaieEtrangereSearch() {
        return monnaieEtrangereSearch;
    }

    /**
     * @param variableMetierSearch
     *            the variableMetierSearch to set
     */
    public void setMonnaieEtrangereSearch(MonnaieEtrangereSearch monnaieEtrangereSearch) {
        this.monnaieEtrangereSearch = monnaieEtrangereSearch;
    }

}
