package globaz.al.vb.prestation;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle permettant la recherche sur les récapitulatifs entreprise
 * 
 * @author GMO
 * 
 */
public class ALRecapListViewBean extends BJadePersistentObjectListViewBean {

    private RecapitulatifEntrepriseListComplexSearchModel recapSearchModel = null;

    /**
     * Constructeur de la classe
     */
    public ALRecapListViewBean() {
        super();
        recapSearchModel = new RecapitulatifEntrepriseListComplexSearchModel();
    }

    @Override
    public void find() throws Exception {
        recapSearchModel.setDefinedSearchSize(100);
        recapSearchModel = ALServiceLocator.getRecapitulatifEntrepriseListComplexModelService()
                .search(recapSearchModel);

    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return recapSearchModel;
    }

    /**
     * 
     * @return recapSearchModel le modèle de recherche d'un récapitulatif
     */
    public RecapitulatifEntrepriseListComplexSearchModel getRecapSearchModel() {
        return recapSearchModel;
    }

    /**
     * Définit le modèle de recherche d'un récapitulatif
     * 
     * @param recapSearchModel
     *            modèle de recherche d'un récapitulatif
     */
    public void setRecapSearchModel(RecapitulatifEntrepriseListComplexSearchModel recapSearchModel) {
        this.recapSearchModel = recapSearchModel;
    }

}
