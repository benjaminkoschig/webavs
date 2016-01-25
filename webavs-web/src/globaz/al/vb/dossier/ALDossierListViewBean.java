package globaz.al.vb.dossier;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.HashSet;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle permettant la recherche sur les dossierListComplexModel qui incluent : - allocataire, droit
 * et affiliation
 * 
 * @author GMO
 * 
 */
public class ALDossierListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Collection contenant les activités de dossier à prendre en compte
     */
    private HashSet<String> inActiviteFilter = new HashSet<String>();
    /**
     * Collection contenant les états de dossier à prendre en compte
     */
    private HashSet<String> inEtatFilter = new HashSet<String>();
    /**
     * Collection contenant les statuts de dossier à prendre en compte
     */
    private HashSet<String> inStatutFilter = new HashSet<String>();

    /**
     * Modèle de recherche pour les dossiers complets
     */
    private DossierListComplexSearchModel searchModel = null;

    /**
     * Constructeur du listViewBean
     */
    public ALDossierListViewBean() {
        super();
        searchModel = new DossierListComplexSearchModel();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        // selon etats des checkbox
        if (!inEtatFilter.isEmpty()) {
            searchModel.setInEtatsDossier(inEtatFilter);
        }
        // selon statuts des checkbox
        if (!inStatutFilter.isEmpty()) {
            searchModel.setInStatutsDossier(inStatutFilter);
        }

        // selon activite des checkbox
        if (!inActiviteFilter.isEmpty()) {
            searchModel.setInActivitesDossier(inActiviteFilter);
        }
        searchModel.setWhereKey("AL0002");
        searchModel.setOrderKey("AL0002");
        // si id dossier est spécifié, on utilise un searchModel contenant que
        // ce critère
        if (!JadeStringUtil.isEmpty(searchModel.getForIdDossier())) {

            DossierListComplexSearchModel searchDossier = new DossierListComplexSearchModel();
            searchDossier.setForIdDossier(searchModel.getForIdDossier());
            searchDossier.setWhereKey("AL0002");
            searchDossier.setOrderKey("AL0002");
            searchModel = ALServiceLocator.getDossierListComplexModelService().search(searchDossier);
        } else {
            searchModel = ALServiceLocator.getDossierListComplexModelService().search(searchModel);
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < getCount() ? new ALDossierResultViewBean((DossierListComplexModel) getManagerModel()
                .getSearchResults()[idx]) : new ALDossierResultViewBean();
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
     * 
     * @return the searchModel Modèle de recherche des dossiers
     */
    public DossierListComplexSearchModel getSearchModel() {
        return searchModel;
    }

    public void setFilterActivite(String activites) {
        String[] activitesTabs = activites.split(",");
        for (int i = 0; i < activitesTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(activitesTabs[i])) {
                inActiviteFilter.add(activitesTabs[i]);
            }
        }
    }

    public void setFilterEtat(String etats) {

        String[] etatsTabs = etats.split(",");
        for (int i = 0; i < etatsTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(etatsTabs[i])) {
                inEtatFilter.add(etatsTabs[i]);
            }
        }
    }

    public void setFilterStatut(String statuts) {

        String[] statutsTabs = statuts.split(",");
        for (int i = 0; i < statutsTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(statutsTabs[i])) {
                inStatutFilter.add(statutsTabs[i]);
            }
        }

    }

    /**
     * 
     * @param searchModel
     *            le modèle de recherche à utiliser
     */
    public void setSearchModel(DossierListComplexSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
