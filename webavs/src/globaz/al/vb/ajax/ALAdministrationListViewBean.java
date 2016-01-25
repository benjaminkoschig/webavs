package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * listViewBean pr�sentant une liste de r�sultats des administrations
 * 
 * @author GMO
 * 
 */
public class ALAdministrationListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le mod�le de recherche d'une administration
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
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le AdministrationComplexModel voulu ou un mod�le PersonneEtendueComplexModel vide si position non
     *         trouv�e
     */
    public AdministrationComplexModel getAdministrationComplexResult(int idx) {
        return idx < getCount() ? (AdministrationComplexModel) getManagerModel().getSearchResults()[idx]
                : new AdministrationComplexModel();
    }

    /**
     * @return null car pas de SearchModel utilis� dans ce listViewBean
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
