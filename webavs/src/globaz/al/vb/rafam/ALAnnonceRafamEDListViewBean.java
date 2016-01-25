package globaz.al.vb.rafam;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

public class ALAnnonceRafamEDListViewBean extends BJadePersistentObjectListViewBean {
    private boolean filterLastAnnonce = false;
    private String forIdEmployeur = null;
    /**
     * Modèle de recherche pour les annonces
     */
    private AnnonceRafamSearchModel searchModel = null;

    /**
     * Constructeur du listViewBean
     */
    public ALAnnonceRafamEDListViewBean() {
        super();
        searchModel = new AnnonceRafamSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel.setOrderKey("AL0030");
        searchModel.setWhereKey("afDelegue");
        searchModel.setForDelegated(new Boolean(true));
        searchModel = ALServiceLocator.getAnnonceRafamModelService().search(searchModel);

        ArrayList<AnnonceRafamModel> annoncesToDisplay = new ArrayList<AnnonceRafamModel>();
        List<String> recordNumberToDisplay = new ArrayList<String>();

        // si la case à cocher pour afficher que la dernière du recordNumber est cochée, on "nettoie" les résultats de
        // recherche
        if (filterLastAnnonce) {
            for (int i = 0; i < searchModel.getSize(); i++) {
                AnnonceRafamModel currentAnnonce = (AnnonceRafamModel) searchModel.getSearchResults()[i];

                if (!recordNumberToDisplay.contains(currentAnnonce.getRecordNumber())) {
                    recordNumberToDisplay.add(currentAnnonce.getRecordNumber());
                    annoncesToDisplay.add(currentAnnonce);
                }
            }

            AnnonceRafamModel[] listResultsLightFiltering = annoncesToDisplay
                    .toArray(new AnnonceRafamModel[annoncesToDisplay.size()]);

            searchModel.setSearchResults(listResultsLightFiltering);
        }

    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public AnnonceRafamSearchModel getSearchModel() {
        return searchModel;
    }

    public boolean isFilterLastAnnonce() {
        return filterLastAnnonce;
    }

    public void setFilterLastAnnonce(boolean filterLastAnnonce) {
        this.filterLastAnnonce = filterLastAnnonce;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public void setSearchModel(AnnonceRafamSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
