package globaz.al.vb.rafam;

import globaz.al.vb.dossier.ALDossierResultViewBean;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle permettant la recherche d'annonces RAFAM
 * 
 * @author jts
 * 
 */
public class ALAnnonceRafamListViewBean extends BJadePersistentObjectListViewBean {
    private boolean filterLastAnnonce = false;
    /**
     * Modèle de recherche pour les annonces
     */
    private AnnonceRafamComplexSearchModel searchModel = null;

    /**
     * Constructeur du listViewBean
     */
    public ALAnnonceRafamListViewBean() {
        super();
        searchModel = new AnnonceRafamComplexSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel.setOrderKey("AL0030");
        searchModel = ALServiceLocator.getAnnonceRafamComplexModelService().search(searchModel);

        ArrayList<AnnonceRafamComplexModel> annoncesToDisplay = new ArrayList<AnnonceRafamComplexModel>();
        List<String> recordNumberToDisplay = new ArrayList<String>();
        // List<List<Annon>> annoncesToDisplay = new ArrayList<AnnonceRafamComplexModel>();
        // si la case à cocher pour afficher que la dernière du recordNumber est cochée, on "nettoie" les résultats de
        // recherche
        if (filterLastAnnonce) {
            for (int i = 0; i < searchModel.getSize(); i++) {
                AnnonceRafamComplexModel currentAnnonce = (AnnonceRafamComplexModel) searchModel.getSearchResults()[i];

                if (!recordNumberToDisplay.contains(currentAnnonce.getAnnonceRafamModel().getRecordNumber())) {
                    recordNumberToDisplay.add(currentAnnonce.getAnnonceRafamModel().getRecordNumber());
                    annoncesToDisplay.add(currentAnnonce);
                }
            }

            AnnonceRafamComplexModel[] listResultsLightFiltering = annoncesToDisplay
                    .toArray(new AnnonceRafamComplexModel[annoncesToDisplay.size()]);

            searchModel.setSearchResults(listResultsLightFiltering);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < getCount() ? new ALDossierResultViewBean((DossierListComplexModel) getManagerModel()
                .getSearchResults()[idx]) : new ALDossierResultViewBean();
    }

    /*
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
    public AnnonceRafamComplexSearchModel getSearchModel() {
        return searchModel;
    }

    /**
     * @return valeur du critère statut NP
     */
    public Boolean isFilterLastAnnonce() {
        return new Boolean(filterLastAnnonce);
    }

    public void setFilterLastAnnonce(Boolean isActif) {
        filterLastAnnonce = isActif.booleanValue();

    }

    /**
     * 
     * @param searchModel
     *            le modèle de recherche à utiliser
     */
    public void setSearchModel(AnnonceRafamComplexSearchModel searchModel) {
        this.searchModel = searchModel;
    }
}
