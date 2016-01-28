package globaz.perseus.vb.rentepont;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFEnfantAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private EnfantFamilleSearchModel enfantFamilleSearch = null;
    private List listeEnfantFamille = new ArrayList();

    /**
	 * 
	 */
    public PFEnfantAjaxListViewBean() {
        super();
        enfantFamilleSearch = new EnfantFamilleSearchModel();
    }

    @Override
    public void find() throws Exception {
        enfantFamilleSearch = PerseusImplServiceLocator.getEnfantFamilleService().search(enfantFamilleSearch);

        // Reparcourir la liste pour ajouter les données financières
        for (JadeAbstractModel abstractModel : enfantFamilleSearch.getSearchResults()) {
            EnfantFamille ef = (EnfantFamille) abstractModel;
            listeEnfantFamille.add(ef);
        }

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < enfantFamilleSearch.getSize() ? new PFEnfantAjaxViewBean(
                (EnfantFamille) enfantFamilleSearch.getSearchResults()[idx]) : new PFEnfantAjaxViewBean();
    }

    /**
     * @return the enfantFamilleSearch
     */
    public EnfantFamilleSearchModel getEnfantFamilleSearch() {
        return enfantFamilleSearch;
    }

    /**
     * @return the listeEnfantFamille
     */
    public List getListeEnfantFamille() {
        return listeEnfantFamille;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return enfantFamilleSearch;
    }

    /**
     * @param enfantFamilleSearch
     *            the enfantFamilleSearch to set
     */
    public void setEnfantFamilleSearch(EnfantFamilleSearchModel enfantFamilleSearch) {
        this.enfantFamilleSearch = enfantFamilleSearch;
    }

    /**
     * @param listeEnfantFamille
     *            the listeEnfantFamille to set
     */
    public void setListeEnfantFamille(List listeEnfantFamille) {
        this.listeEnfantFamille = listeEnfantFamille;
    }

}
