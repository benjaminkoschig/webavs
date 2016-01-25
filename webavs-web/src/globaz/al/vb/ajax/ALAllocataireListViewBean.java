package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle de recherche d'allocataire, utilisé dans une recherche ajax pour voir si un allocataire
 * correspond au nss
 * 
 * @author GMO
 * 
 */
public class ALAllocataireListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Modèle de recherche d'allocataire
     */
    private AllocataireComplexSearchModel allocataireComplexSearchModel = null;
    /**
     * Liste indiquant si l'/les allocataire/s trouvé/s sont déjà actifs dans un autre dossier 1er élément de la liste
     * correspond au 1er résultat du modèle de recherche.
     */
    private List isActifListResults = null;

    /**
     * Constructeur du listViewBean
     */
    public ALAllocataireListViewBean() {
        super();
        allocataireComplexSearchModel = new AllocataireComplexSearchModel();
        isActifListResults = new ArrayList();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        allocataireComplexSearchModel = ALServiceLocator.getAllocataireComplexModelService().search(
                allocataireComplexSearchModel);
        // pour chaque résultat allocataire, on checke dans combien de dossiers
        // il est actif
        isActifListResults.clear();
        for (int i = 0; i < allocataireComplexSearchModel.getSize(); i++) {
            isActifListResults.add(new Integer(ALServiceLocator.getAllocataireBusinessService().isActif(
                    getAllocataireComplexResult(i).getId())));
        }
    }

    /**
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle AllocataireComplexModel voulu ou un modèle AllocataireComplexModel vide si position non trouvée
     */
    public AllocataireComplexModel getAllocataireComplexResult(int idx) {
        return idx < getCount() ? (AllocataireComplexModel) getManagerModel().getSearchResults()[idx]
                : new AllocataireComplexModel();
    }

    /**
     * @return allocataireComplexSearchModel Le modèle de recherche allocataire
     */
    public AllocataireComplexSearchModel getAllocataireComplexSearchModel() {
        return allocataireComplexSearchModel;
    }

    /**
     * @return isActifListResults La liste indiquant si les allocataires résultant sont actifs dans d'autres dossiers
     */
    public List getIsActifListResults() {
        return isActifListResults;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return allocataireComplexSearchModel;
    }

    /**
     * @param allocataireComplexSearchModel
     *            Le modèle de recherche allocataire
     */
    public void setAllocataireComplexSearchModel(AllocataireComplexSearchModel allocataireComplexSearchModel) {
        this.allocataireComplexSearchModel = allocataireComplexSearchModel;
    }

    /**
     * @param isActifListResults
     *            La liste indiquant si les allocataires résultant sont actifs dans d'autres dossiers
     */
    public void setIsActifListResults(List isActifListResults) {
        this.isActifListResults = isActifListResults;
    }

}
