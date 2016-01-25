package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.tests.util.LogTemplate;

public class UtilsLotForTestRetroAndOV {

    public static void comptabliserCurrentLot() throws Exception {
        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                SimpleLot lotDecisionPc = UtilsLotForTestRetroAndOV.findOpenLotDecision();
                SimpleLot lot = UtilsLotForTestRetroAndOV.findLotPaiment();
                PegasusServiceLocator.getLotService().comptabiliserLot(lotDecisionPc.getIdLot(), "3", "4",
                        "Test Journale Decision PC", lot.getDateEnvoi(), JACalendar.todayJJsMMsAAAA());
                Thread.sleep(1000);
            }
        }.run();
    }

    public final static SimpleLot findLotPaiment() throws LotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleLotSearch searchLot = new SimpleLotSearch();
        searchLot.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
        searchLot.setForCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
        searchLot.setForCsProprietaire(IRELot.CS_LOT_OWNER_RENTES);
        searchLot = CorvusServiceLocator.getLotService().search(searchLot);
        SimpleLot simpleLotOrinigal = (SimpleLot) searchLot.getSearchResults()[0];
        return simpleLotOrinigal;
    }

    public final static SimpleLot findOpenLotDecision() throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleLotSearch searchLot = new SimpleLotSearch();
        searchLot.setForCsType(IRELot.CS_TYP_LOT_DECISION);
        searchLot.setForCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
        searchLot.setForCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        searchLot = CorvusServiceLocator.getLotService().search(searchLot);
        SimpleLot simpleLotOrinigal = (SimpleLot) searchLot.getSearchResults()[0];
        return simpleLotOrinigal;
    }
}
