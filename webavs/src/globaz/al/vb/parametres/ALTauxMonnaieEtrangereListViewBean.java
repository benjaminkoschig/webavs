package globaz.al.vb.parametres;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant le mod�le permettant la recherche sur les taux de monnaies �trang�res (Ecran 40)
 * 
 * @author PTA
 * 
 */
public class ALTauxMonnaieEtrangereListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Mod�le de recherche pour le taux de la monnaie �trang�re
     */
    private TauxMonnaieEtrangereSearchModel tauxMonnaieEtrangereSearch = null;

    /**
     * Constructeur
     */

    public ALTauxMonnaieEtrangereListViewBean() {
        super();
        tauxMonnaieEtrangereSearch = new TauxMonnaieEtrangereSearchModel();
    }

    @Override
    public void find() throws Exception {
        tauxMonnaieEtrangereSearch = ALServiceLocator.getTauxMonnaieEtrangereModelService().search(
                tauxMonnaieEtrangereSearch);

    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return tauxMonnaieEtrangereSearch;
    }

    /**
     * Retourne le mod�le tauxMonnaieEtrang�re r�sultat se trouvant � la position indiqu�
     * 
     * @param idx
     *            La position du r�sultat souhait�
     * @return TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel getResult(int idx) {
        return idx < getCount() ? (TauxMonnaieEtrangereModel) getManagerModel().getSearchResults()[idx]
                : new TauxMonnaieEtrangereModel();
    }

    /**
     * @return the tauxMonnaieEtrangereSearch
     */
    public TauxMonnaieEtrangereSearchModel getTauxMonnaieEtrangereSearch() {
        return tauxMonnaieEtrangereSearch;
    }

    /**
     * @param tauxMonnaieEtrangereSearch
     *            the tauxMonnaieEtrangereSearch to set
     */
    public void setTauxMonnaieEtrangereSearch(TauxMonnaieEtrangereSearchModel tauxMonnaieEtrangereSearch) {
        this.tauxMonnaieEtrangereSearch = tauxMonnaieEtrangereSearch;
    }

}
