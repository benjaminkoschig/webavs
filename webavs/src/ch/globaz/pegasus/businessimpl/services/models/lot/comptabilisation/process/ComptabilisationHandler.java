package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.common.listoutput.ExecutionTime;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.business.service.JournalService;

public class ComptabilisationHandler {

    private static final String NAME_OBJECT_TIME_IN_THE_THREAD = "NAME_OBJECT_TIME_IN_THE_THREAD_FOR_COMPTABILISATION_PROCESS";

    public static String displayTime() {
        ExecutionTime time = (ExecutionTime) JadeThread.currentContext().getTemporaryObject(
                ComptabilisationHandler.NAME_OBJECT_TIME_IN_THE_THREAD);
        String times = "";
        if (time != null) {
            times = "Loader: " + time.getLoad() + "ms\n\rChecker: " + time.getCheck() + "ms\n\rThreat: "
                    + time.getHandle() + "ms\n\rPersister: " + time.getOut() + "ms\n\rnbPresation: "
                    + time.getNombre() + "\n\rAverage: "
                    + ((time.getLoad() + time.getCheck() + time.getHandle() + time.getOut()) / time.getNombre())
                    + "ms";
        }
        return times;
    }

    public static ComptabilisationData execute(String idLot, String idOrganeExecution, String numeroOG,
            String libelleJournal, String dateValeur, String dateEchance) throws JadeApplicationException,
            JadePersistenceException, JAException {
        return ComptabilisationHandler.execute(idLot, idOrganeExecution, numeroOG, libelleJournal, dateValeur,
                dateEchance, CABusinessServiceLocator.getJournalService());

    }

    static ComptabilisationData execute(String idLot, String idOrganeExecution, String numeroOG, String libelleJournal,
            String dateValeur, String dateEchance, JournalService comptatService) throws JadeApplicationException,
            JadePersistenceException, JAException {

        ExecutionTime time = new ExecutionTime();

        ComptabilisationLoader loader = new ComptabilisationLoader(idLot, idOrganeExecution, numeroOG, libelleJournal,
                dateValeur, dateEchance);
        ComptabilisationData data = loader.loadForCheck();
        time.addCheck();

        new ComptabilisationChecker(loader.getData()).check();
        data = loader.load();
        time.addLoad();

        ComptabilisationTreat treat = new ComptabilisationTreat(data);
        data = treat.treat();
        time.addHandler();

        time.setNombre(data.getJournalConteneur().getOperations().size());
        ComptabilisationPersister persister = new ComptabilisationPersister(data.getJournalConteneur(),
                data.getSimpleLot(), data.getMapIdTierDescription());
        persister.persit(comptatService);
        time.addOut();
        time.stop();

        JadeThread.currentContext().storeTemporaryObject(ComptabilisationHandler.NAME_OBJECT_TIME_IN_THE_THREAD, time);
        return data;
    }

    public static ComptabilisationData generateEcritureForOnePresation(String idPrestation)
            throws JadePersistenceException, JadeApplicationException, JAException {
        ComptabilisationData data = null;

        ComptabilisationLoader loader;
        loader = new ComptabilisationLoader();

        data = loader.loadForPrestation(idPrestation);
        data = loader.loadForCheck();
        ComptabilisationTreat treat = new ComptabilisationTreat(data);
        data = treat.treat();

        return data;
    }

    public static ComptabilisationData generateJournal(String idLot) throws JadePersistenceException,
            JadeApplicationException, JAException {
        ComptabilisationData data = null;

        ComptabilisationLoader loader;
        loader = new ComptabilisationLoader(idLot, null, null, JACalendar.todayJJsMMsAAAA(),
                JACalendar.todayJJsMMsAAAA(), JACalendar.todayJJsMMsAAAA());

        data = loader.loadForCheck();
        data = loader.load();

        ComptabilisationTreat treat = new ComptabilisationTreat(data);
        data = treat.treat();

        return data;
    }
}
