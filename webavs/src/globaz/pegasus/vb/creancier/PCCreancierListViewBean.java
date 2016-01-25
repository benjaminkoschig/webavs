package globaz.pegasus.vb.creancier;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * 
 */
public class PCCreancierListViewBean extends BJadePersistentObjectListViewBean {

    private CreancierSearch creancierSearch;

    public PCCreancierListViewBean() {
        super();
        creancierSearch = new CreancierSearch();
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws CreancierException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        creancierSearch = PegasusServiceLocator.getCreancierService().search(creancierSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < creancierSearch.getSize() ? new PCCreancierViewBean(
                (Creancier) creancierSearch.getSearchResults()[idx]) : new PCCreancierViewBean();
    }

    /**
     * @return the creancierSearch
     */
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

    /**
     * @param creancierSearch
     *            the creancierSearch to set
     */
    public void setCreancierSearch(CreancierSearch creancierSearch) {
        this.creancierSearch = creancierSearch;
    }

}
