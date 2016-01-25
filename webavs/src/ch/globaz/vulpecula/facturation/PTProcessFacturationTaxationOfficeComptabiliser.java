package ch.globaz.vulpecula.facturation;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;

/***
 * Process de comptabilisation des taxation d'office
 * 
 */
public class PTProcessFacturationTaxationOfficeComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = -1019592250913241020L;

    @Override
    protected boolean launch() {
        List<TaxationOffice> taxationOffices = findTaxationForFacturation();
        for (TaxationOffice taxationOffice : taxationOffices) {
            taxationOffice.setEtat(EtatTaxation.COMPTABILISE);
            updateTaxationOffice(taxationOffice);
        }
        return true;
    }

    @Override
    protected void clean() {
    }

    protected void updateTaxationOffice(TaxationOffice taxationOffice) {
        VulpeculaRepositoryLocator.getTaxationOfficeRepository().update(taxationOffice);
    }

    protected List<TaxationOffice> findTaxationForFacturation() {
        return VulpeculaRepositoryLocator.getTaxationOfficeRepository().getTaxationForFacturation(getPassage().getId());
    }
}
