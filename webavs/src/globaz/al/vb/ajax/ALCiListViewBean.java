package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.ci.business.models.CompteIndividuelModel;
import ch.globaz.ci.business.models.CompteIndividuelSearchModel;
import ch.globaz.ci.business.service.CIBusinessServiceLocator;

/**
 * listViewBean présentant une liste de résultats des CI pour un n° NSS. Utilise les services
 * CIBusinessServiceLocator.getCompteIndividuelService()
 * 
 * @author GMO
 * 
 */
public class ALCiListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le modèle de recherche de CompteIndividuelModel
     */
    private CompteIndividuelSearchModel ciSearchModel = null;

    /**
     * constructeur du viewBean
     */
    public ALCiListViewBean() {
        super();
        ciSearchModel = new CompteIndividuelSearchModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        ciSearchModel = CIBusinessServiceLocator.getCompteIndividuelService().search(ciSearchModel);

    }

    public CompteIndividuelSearchModel getCiSearchModel() {
        return ciSearchModel;
    }

    /**
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle EnfantComplexModel voulu ou un modèle EnfantComplexModel vide si position non trouvée
     */
    public CompteIndividuelModel getCompteIndividuelResult(int idx) {
        return idx < getCount() ? (CompteIndividuelModel) getManagerModel().getSearchResults()[idx]
                : new CompteIndividuelModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return ciSearchModel;
    }

    public void setCiSearchModel(CompteIndividuelSearchModel ciSearchModel) {
        this.ciSearchModel = ciSearchModel;
    }

}
