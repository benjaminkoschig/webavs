package globaz.amal.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * ViewBean gérant le modèle de recherche de tierse, utilisé dans une recherche ajax pour voir si un tiers correspond au
 * nss saisi
 * 
 * @author CBU
 * 
 */
public class AMTiersAjaxListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le modèle de recherche de tiers
     */
    private PersonneEtendueSearchComplexModel personneEtendueSearchComplexModel = null;

    /**
     * Constructeur du listViewBean
     */
    public AMTiersAjaxListViewBean() {
        super();
        personneEtendueSearchComplexModel = new PersonneEtendueSearchComplexModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        personneEtendueSearchComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().find(
                personneEtendueSearchComplexModel);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return personneEtendueSearchComplexModel;
    }

    /**
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle PersonneEtendueComplexModel voulu ou un modèle PersonneEtendueComplexModel vide si position non
     *         trouvée
     */
    public PersonneEtendueComplexModel getPersonneEtendueResult(int idx) {
        return idx < getCount() ? (PersonneEtendueComplexModel) getManagerModel().getSearchResults()[idx]
                : new PersonneEtendueComplexModel();
    }

    /**
     * @return personneEtendueSearchComplexModel Le modèle de recherche de tiers
     */
    public PersonneEtendueSearchComplexModel getPersonneEtendueSearchComplexModel() {
        return personneEtendueSearchComplexModel;
    }

    /**
     * @param personneEtendueSearchComplexModel
     *            Le modèle de recherche de tiers
     */
    public void setPersonneEtendueSearchComplexModel(PersonneEtendueSearchComplexModel personneEtendueSearchComplexModel) {
        this.personneEtendueSearchComplexModel = personneEtendueSearchComplexModel;
    }
}
