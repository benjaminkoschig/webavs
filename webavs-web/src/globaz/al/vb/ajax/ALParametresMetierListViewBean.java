package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * ViewBean g�rant le mod�le de recherche des param�tres m�tier, utilis� dans une recherche ajax pour obtenir la listes
 * des param�tres depuis l'�cran de gestion
 * 
 * @author GMO
 * 
 */
public class ALParametresMetierListViewBean extends BJadePersistentObjectListViewBean {

    ParameterSearchModel parameterSearchModel = null;

    /**
     * Constructeur du listViewBean
     */
    public ALParametresMetierListViewBean() {
        super();
        parameterSearchModel = new ParameterSearchModel();

    }

    @Override
    public void find() throws Exception {
        parameterSearchModel.setWhereKey(ParameterSearchModel.SEARCH_DEFINITION_ECRAN);
        parameterSearchModel.setOrderKey(ParameterSearchModel.ORDER_DEFINITION_ECRAN);
        parameterSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        parameterSearchModel = ParamServiceLocator.getParameterModelService().search(parameterSearchModel);
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return parameterSearchModel;
    }

    /**
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le ParameterModel voulu ou un mod�le ParameterModel vide si position non trouv�e
     */
    public ParameterModel getParameterModelResult(int idx) {
        return idx < getCount() ? (ParameterModel) getManagerModel().getSearchResults()[idx] : new ParameterModel();
    }

    public ParameterSearchModel getParameterSearchModel() {
        return parameterSearchModel;
    }

    public void setParameterSearchModel(ParameterSearchModel parameterSearchModel) {
        this.parameterSearchModel = parameterSearchModel;
    }

}
