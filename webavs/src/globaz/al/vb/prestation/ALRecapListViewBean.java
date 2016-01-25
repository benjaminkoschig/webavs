package globaz.al.vb.prestation;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant le mod�le permettant la recherche sur les r�capitulatifs entreprise
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
     * @return recapSearchModel le mod�le de recherche d'un r�capitulatif
     */
    public RecapitulatifEntrepriseListComplexSearchModel getRecapSearchModel() {
        return recapSearchModel;
    }

    /**
     * D�finit le mod�le de recherche d'un r�capitulatif
     * 
     * @param recapSearchModel
     *            mod�le de recherche d'un r�capitulatif
     */
    public void setRecapSearchModel(RecapitulatifEntrepriseListComplexSearchModel recapSearchModel) {
        this.recapSearchModel = recapSearchModel;
    }

}
