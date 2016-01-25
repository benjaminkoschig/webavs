package ch.globaz.prestation.businessimpl.services.models.recap;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;
import ch.globaz.prestation.business.models.recap.SimpleRecapitulationPcRfm;
import ch.globaz.prestation.business.models.recap.SimpleRecapitulationPcRfmSearch;
import ch.globaz.prestation.business.services.models.recap.RecapitulationPcRfmService;
import ch.globaz.prestation.businessimpl.services.PrestationCommonAbstractServiceImpl;

public class RecapitulationPcRfmServiceImpl extends PrestationCommonAbstractServiceImpl implements
        RecapitulationPcRfmService {

    public RecapitulationPcRfmServiceImpl() {
        super();
    }

    private RecapitulationPcRfm buildMutationInfoRecap(List<SimpleRecapitulationPcRfm> searchResults) {

        RecapitulationPcRfm recap = new RecapitulationPcRfm();

        for (SimpleRecapitulationPcRfm unCodeRecap : searchResults) {
            int amount = 0;
            FWCurrency currency = new FWCurrency();

            switch (unCodeRecap.getCode()) {
                case MontantTotalPC_AI:
                case MontantTotalPC_AVS:
                case MontantTotalRFM_AI:
                case MontantTotalRFM_AVS:
                    currency = new FWCurrency(unCodeRecap.getValeur());
                    break;
                case NombrePrestationsPC_AI:
                case NombrePrestationsPC_AVS:
                case NombrePrestationsRFM_AI:
                case NombrePrestationsRFM_AVS:
                    if (!JadeStringUtil.isBlankOrZero(unCodeRecap.getValeur())) {
                        amount = Double.valueOf(unCodeRecap.getValeur()).intValue();
                    }
                    break;
            }

            switch (unCodeRecap.getCode()) {
                case MontantTotalPC_AI:
                    recap.setMontantTotalPCAI(currency);
                    break;
                case MontantTotalPC_AVS:
                    recap.setMontantTotalPCAVS(currency);
                    break;
                case NombrePrestationsPC_AI:
                    recap.setNbPrestationPCAI(amount);
                    break;
                case NombrePrestationsPC_AVS:
                    recap.setNbPrestationPCAVS(amount);
                    break;
                case MontantTotalRFM_AI:
                    recap.setMontantTotalRFMAI(currency);
                    break;
                case MontantTotalRFM_AVS:
                    recap.setMontantTotalRFMAVS(currency);
                    break;
                case NombrePrestationsRFM_AI:
                    recap.setNbPrestationRFMAI(amount);
                    break;
                case NombrePrestationsRFM_AVS:
                    recap.setNbPrestationRFMAVS(amount);
                    break;
            }
        }

        return recap;
    }

    @Override
    public RecapitulationPcRfm findInfoRecapByDate(String date) throws JadePersistenceException,
            PrestationCommonException {

        if (!JadeDateUtil.isGlobazDateMonthYear(date)) {
            throw new PrestationCommonException("Date must be at format MM.YYYY");
        }

        SimpleRecapitulationPcRfmSearch searchModel = new SimpleRecapitulationPcRfmSearch();
        searchModel.setForMois(date);
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        searchModel = (SimpleRecapitulationPcRfmSearch) JadePersistenceManager.search(searchModel);

        if (searchModel.getSize() == 0) {
            return null;
        }

        List<SimpleRecapitulationPcRfm> resultats = new ArrayList<SimpleRecapitulationPcRfm>();
        for (JadeAbstractModel unResultat : searchModel.getSearchResults()) {
            resultats.add((SimpleRecapitulationPcRfm) unResultat);
        }

        return buildMutationInfoRecap(resultats);
    }
}
