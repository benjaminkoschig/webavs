package globaz.amal.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * ViewBean g�rant le mod�le de recherche de tierse, utilis� dans une recherche ajax pour voir si un tiers correspond au
 * nss saisi
 * 
 * @author CBU
 * 
 */
public class AMTiersAjaxListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le mod�le de recherche de tiers
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
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le PersonneEtendueComplexModel voulu ou un mod�le PersonneEtendueComplexModel vide si position non
     *         trouv�e
     */
    public PersonneEtendueComplexModel getPersonneEtendueResult(int idx) {
        return idx < getCount() ? (PersonneEtendueComplexModel) getManagerModel().getSearchResults()[idx]
                : new PersonneEtendueComplexModel();
    }

    /**
     * @return personneEtendueSearchComplexModel Le mod�le de recherche de tiers
     */
    public PersonneEtendueSearchComplexModel getPersonneEtendueSearchComplexModel() {
        return personneEtendueSearchComplexModel;
    }

    /**
     * @param personneEtendueSearchComplexModel
     *            Le mod�le de recherche de tiers
     */
    public void setPersonneEtendueSearchComplexModel(PersonneEtendueSearchComplexModel personneEtendueSearchComplexModel) {
        this.personneEtendueSearchComplexModel = personneEtendueSearchComplexModel;
    }
}
