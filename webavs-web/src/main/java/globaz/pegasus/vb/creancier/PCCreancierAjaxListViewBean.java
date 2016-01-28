package globaz.pegasus.vb.creancier;

import globaz.framework.util.FWCurrency;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * 
 */
public class PCCreancierAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private CreancierSearch creancierSearch;

    public PCCreancierAjaxListViewBean() {
        super();
        creancierSearch = new CreancierSearch();
    }

    @Override
    public void find() throws Exception {
        creancierSearch = PegasusServiceLocator.getCreancierService().search(creancierSearch);
    }

    public CreancierSearch getCreancierSearch() {
        return creancierSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return creancierSearch;
    }

    public String getMontantRepartiByCreancier(String idCreancier) throws CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CreanceAccordeeSearch creanceAccordeeSearch = new CreanceAccordeeSearch();
        creanceAccordeeSearch.setForIdCreancier(idCreancier);
        creanceAccordeeSearch = PegasusServiceLocator.getCreanceAccordeeService().search(creanceAccordeeSearch);
        FWCurrency montant = new FWCurrency(0);
        for (JadeAbstractModel model : creanceAccordeeSearch.getSearchResults()) {
            CreanceAccordee creanceAccordee = (CreanceAccordee) model;
            // FWCurrency mnt = new FWCurrency(montant);
            montant.add(creanceAccordee.getSimpleCreanceAccordee().getMontant());
            // montant = mnt;
        }
        return montant.toStringFormat();
    }

    public void setCreancierSearch(CreancierSearch creancierSearch) {
        this.creancierSearch = creancierSearch;
    }

}
