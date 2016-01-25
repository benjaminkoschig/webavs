package globaz.perseus.vb.parametres;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.models.parametres.LienLocaliteSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * 
 * @author MBO
 * 
 */

public class PFLienLocaliteListViewBean extends BJadePersistentObjectListViewBean {

    private LienLocaliteSearchModel lienLocaliteSearch = new LienLocaliteSearchModel();

    public PFLienLocaliteListViewBean() {
        super();
        lienLocaliteSearch = new LienLocaliteSearchModel();
    }

    @Override
    public void find() throws Exception {
        lienLocaliteSearch = PerseusServiceLocator.getLienLocaliteService().search(lienLocaliteSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < lienLocaliteSearch.getSize() ? new PFLienLocaliteViewBean(
                (LienLocalite) lienLocaliteSearch.getSearchResults()[idx]) : new PFLienLocaliteViewBean();
    }

    /**
     * @return the forDateValableConverter
     */
    public String getForDateValableConverter() {
        return lienLocaliteSearch.getForDateValable();
    }

    public LienLocaliteSearchModel getLienLocaliteSearch() {
        return lienLocaliteSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return lienLocaliteSearch;
    }

    /**
     * Recoit une date au format mm.yyyy et la converti en 01.mm.yyyy
     * 
     * @param forDateValableConverter
     *            the forDateValableConverter to set
     */
    public void setForDateValableConverter(String forDateValableConverter) {
        if (!JadeStringUtil.isEmpty(forDateValableConverter)) {
            forDateValableConverter = "01." + forDateValableConverter;
            lienLocaliteSearch.setForDateValable(forDateValableConverter);
        }
    }

    public void setLienLocaliteSearch(LienLocaliteSearchModel lienLocaliteSearch) {
        this.lienLocaliteSearch = lienLocaliteSearch;
    }

}
