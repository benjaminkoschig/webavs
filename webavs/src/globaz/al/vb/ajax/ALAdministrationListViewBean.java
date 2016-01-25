package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * listViewBean présentant une liste de résultats des administrations
 * 
 * @author GMO
 * 
 */
public class ALAdministrationListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le modèle de recherche d'une administration
     */
    private AdministrationSearchComplexModel searchModel = null;

    /**
     * Constructeur du viewBean
     */
    public ALAdministrationListViewBean() {
        super();
        searchModel = new AdministrationSearchComplexModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel.setForGenreAdministration(ALCSDossier.GENRE_CAISSE_AF);
        searchModel = TIBusinessServiceLocator.getAdministrationService().find(searchModel);

    }

    /**
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle AdministrationComplexModel voulu ou un modèle PersonneEtendueComplexModel vide si position non
     *         trouvée
     */
    public AdministrationComplexModel getAdministrationComplexResult(int idx) {
        return idx < getCount() ? (AdministrationComplexModel) getManagerModel().getSearchResults()[idx]
                : new AdministrationComplexModel();
    }

    /**
     * @return null car pas de SearchModel utilisé dans ce listViewBean
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public AdministrationSearchComplexModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(AdministrationSearchComplexModel searchModel) {
        this.searchModel = searchModel;
    }

}
