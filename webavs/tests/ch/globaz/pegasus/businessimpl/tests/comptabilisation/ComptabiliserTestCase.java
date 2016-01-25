package ch.globaz.pegasus.businessimpl.tests.comptabilisation;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JACalendar;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import junit.framework.Assert;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.UtilsLotForTestRetroAndOV;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.tests.util.BaseTestCase;
import ch.globaz.pegasus.tests.util.LogTemplate;

public class ComptabiliserTestCase extends BaseTestCase {
    public final static List<SimpleLot> findLotsPc() throws LotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleLotSearch searchLot = new SimpleLotSearch();
        searchLot.setForCsType(IRELot.CS_TYP_LOT_DECISION);
        // searchLot.setForCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
        searchLot.setForCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        // searchLot.setDefinedSearchSize(10);

        List<SimpleLot> list = PersistenceUtil.search(searchLot, searchLot.whichModelClass());

        return list;
    }

    public void testComptabilisation() throws Exception {
        JadeThread.commitSession();
        List<SimpleLot> list = ComptabiliserTestCase.findLotsPc();
        JadeThread.commitSession();

        for (SimpleLot simpleLot : list) {
            simpleLot.setCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
            CorvusServiceLocator.getLotService().update(simpleLot);
        }

        list = ComptabiliserTestCase.findLotsPc();

        final String date = UtilsLotForTestRetroAndOV.findLotPaiment().getDateEnvoi();

        for (final SimpleLot simpleLot : list) {
            new LogTemplate() {
                @Override
                protected void execute() throws Exception {
                    String desc = "T" + simpleLot.getDescription();
                    System.out.println(desc);
                    // Remet la date du jour pour ne pas avoir d'erreur lors de la comptabilisations
                    // UtilsLotForTestRetroAndOV.updateDateLot(simpleLot, JACalendar.todayJJsMMsAAAA());
                    PegasusServiceLocator.getLotService().comptabiliserLot(simpleLot.getIdLot(), "3", "4", desc, date,
                            JACalendar.todayJJsMMsAAAA());
                    Assert.assertTrue(desc, true);
                    // Thread.sleep(60000);

                }
            }.run();

        }

    }
}
