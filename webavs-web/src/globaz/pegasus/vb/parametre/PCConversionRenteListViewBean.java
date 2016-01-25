package globaz.pegasus.vb.parametre;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.ConversionRente;
import ch.globaz.pegasus.business.models.parametre.ConversionRenteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * 
 */
public class PCConversionRenteListViewBean extends BJadePersistentObjectListViewBean {

    private ConversionRenteSearch conversionRenteSearch;

    public PCConversionRenteListViewBean() {
        super();
        conversionRenteSearch = new ConversionRenteSearch();
    }

    /**
     * @throws ConversionRenteException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws ConversionRenteException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        if (!JadeStringUtil.isEmpty(conversionRenteSearch.getForAnnee())) {
            conversionRenteSearch.setWhereKey(ConversionRenteSearch.WITH_DATE_VALABLE);
            conversionRenteSearch.setForDateDebut("01." + conversionRenteSearch.getForAnnee());
        }

        conversionRenteSearch = PegasusServiceLocator.getConversionRenteService().search(conversionRenteSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < conversionRenteSearch.getSize() ? new PCConversionRenteViewBean(
                (ConversionRente) conversionRenteSearch.getSearchResults()[idx]) : new PCConversionRenteViewBean();
    }

    /**
     * @return the conversionRenteSearch
     */
    public ConversionRenteSearch getConversionRenteSearch() {
        return conversionRenteSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return conversionRenteSearch;
    }

    /**
     * @param conversionRenteSearch
     *            the conversionRenteSearch to set
     */
    public void setConversionRenteSearch(ConversionRenteSearch conversionRenteSearch) {
        this.conversionRenteSearch = conversionRenteSearch;
    }
}
