package ch.globaz.pegasus.businessimpl.tests.retroAndOv;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.tests.util.LogTemplate;

public class UtilsLotForTestRetroAndOV {

    public static void comptabliserCurrentLot() throws Exception {
        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                SimpleLot lotDecisionPc = UtilsLotForTestRetroAndOV.findOpenLotDecision();
                SimpleLot lot = UtilsLotForTestRetroAndOV.findLotPaiment();
                String date = lot.getDateEnvoi();

                // Remet la date du jour pour ne pas avoir d'erreur lors de la comptabilisations
                UtilsLotForTestRetroAndOV.updateDateLot(lot, JACalendar.todayJJsMMsAAAA());

                PegasusServiceLocator.getLotService().comptabiliserLot(lotDecisionPc.getIdLot(), "3", "4",
                        "Test Journale Decision PC", UtilsLotForTestRetroAndOV.findLotPaiment().getDateEnvoi(),
                        JACalendar.todayJJsMMsAAAA());

                lot = UtilsLotForTestRetroAndOV.findLotPaiment();
                // JadeThread.commitSession();
                // UtilsLotForTestRetroAndOV.updateDateLot(lot, date);
                Thread.sleep(30000);
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

    public static void updateDateLot(SimpleLot simpleLotOrinigal, String date) throws JadePersistenceException,
            LotException, JadeApplicationServiceNotAvailableException {
        simpleLotOrinigal.setDateEnvoi(date);
        String sql = "update ccjuweb.RELOTS  set YTTETA = 52834002 WHERE YTTTYP = 52833002  and YTTOWN = 52858001 and YTILOT <> "
                + simpleLotOrinigal.getId();
        PersistenceUtil.executeUpdate(sql, UtilsLotForTestRetroAndOV.class);
        CorvusServiceLocator.getLotService().update(simpleLotOrinigal);

    }

}
