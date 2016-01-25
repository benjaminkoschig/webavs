package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;

/**
 * ViewBean gérant le modèle de recherche des paramètres métier, utilisé dans une recherche ajax pour obtenir la listes
 * des paramètres depuis l'écran de gestion
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
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle ParameterModel voulu ou un modèle ParameterModel vide si position non trouvée
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
