package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.ci.business.models.CompteIndividuelModel;
import ch.globaz.ci.business.models.CompteIndividuelSearchModel;
import ch.globaz.ci.business.service.CIBusinessServiceLocator;

/**
 * listViewBean pr�sentant une liste de r�sultats des CI pour un n� NSS. Utilise les services
 * CIBusinessServiceLocator.getCompteIndividuelService()
 * 
 * @author GMO
 * 
 */
public class ALCiListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le mod�le de recherche de CompteIndividuelModel
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
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le EnfantComplexModel voulu ou un mod�le EnfantComplexModel vide si position non trouv�e
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
