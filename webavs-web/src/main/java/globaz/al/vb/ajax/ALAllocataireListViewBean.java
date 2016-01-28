package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant le mod�le de recherche d'allocataire, utilis� dans une recherche ajax pour voir si un allocataire
 * correspond au nss
 * 
 * @author GMO
 * 
 */
public class ALAllocataireListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Mod�le de recherche d'allocataire
     */
    private AllocataireComplexSearchModel allocataireComplexSearchModel = null;
    /**
     * Liste indiquant si l'/les allocataire/s trouv�/s sont d�j� actifs dans un autre dossier 1er �l�ment de la liste
     * correspond au 1er r�sultat du mod�le de recherche.
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
        // pour chaque r�sultat allocataire, on checke dans combien de dossiers
        // il est actif
        isActifListResults.clear();
        for (int i = 0; i < allocataireComplexSearchModel.getSize(); i++) {
            isActifListResults.add(new Integer(ALServiceLocator.getAllocataireBusinessService().isActif(
                    getAllocataireComplexResult(i).getId())));
        }
    }

    /**
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le AllocataireComplexModel voulu ou un mod�le AllocataireComplexModel vide si position non trouv�e
     */
    public AllocataireComplexModel getAllocataireComplexResult(int idx) {
        return idx < getCount() ? (AllocataireComplexModel) getManagerModel().getSearchResults()[idx]
                : new AllocataireComplexModel();
    }

    /**
     * @return allocataireComplexSearchModel Le mod�le de recherche allocataire
     */
    public AllocataireComplexSearchModel getAllocataireComplexSearchModel() {
        return allocataireComplexSearchModel;
    }

    /**
     * @return isActifListResults La liste indiquant si les allocataires r�sultant sont actifs dans d'autres dossiers
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
     *            Le mod�le de recherche allocataire
     */
    public void setAllocataireComplexSearchModel(AllocataireComplexSearchModel allocataireComplexSearchModel) {
        this.allocataireComplexSearchModel = allocataireComplexSearchModel;
    }

    /**
     * @param isActifListResults
     *            La liste indiquant si les allocataires r�sultant sont actifs dans d'autres dossiers
     */
    public void setIsActifListResults(List isActifListResults) {
        this.isActifListResults = isActifListResults;
    }

}
