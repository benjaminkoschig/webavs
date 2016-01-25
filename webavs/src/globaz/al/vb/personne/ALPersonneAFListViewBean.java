package globaz.al.vb.personne;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.personne.PersonneAFComplexModel;
import ch.globaz.al.business.models.personne.PersonneAFComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle permettant la recherche sur les personnes AF (enfants,allocataires)
 * 
 * @author GMO
 * 
 */
public class ALPersonneAFListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le modèle de recherche personneAF
     */
    private PersonneAFComplexSearchModel searchModel = null;

    /**
     * Constructeur du listViewBean
     */
    public ALPersonneAFListViewBean() {
        super();
        searchModel = new PersonneAFComplexSearchModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = ALServiceLocator.getPersonneAFComplexModelService().search(searchModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * Retourne le modèle personneAF résultat se trouvant à la position indiqué
     * 
     * @param idx
     *            La position du résultat souhaité
     * @return Modèle PersonneAFComplexModel
     */
    public PersonneAFComplexModel getResult(int idx) {
        return idx < getCount() ? (PersonneAFComplexModel) getManagerModel().getSearchResults()[idx]
                : new PersonneAFComplexModel();
    }

    /**
     * @return searchModel Le modèle de recherche de personne AF
     */
    public PersonneAFComplexSearchModel getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            Le modèle de recherche de personne AF
     */
    public void setSearchModel(PersonneAFComplexSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
