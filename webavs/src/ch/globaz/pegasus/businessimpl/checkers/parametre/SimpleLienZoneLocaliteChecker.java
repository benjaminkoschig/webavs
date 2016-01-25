package ch.globaz.pegasus.businessimpl.checkers.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocaliteSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class SimpleLienZoneLocaliteChecker {
    /**
     * @param simpleLienZoneLocalite
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleLienZoneLocaliteChecker.checkMandatory(simpleLienZoneLocalite);
        SimpleLienZoneLocaliteChecker.checkIntegrity(simpleLienZoneLocalite);
    }

    /**
     * @param simpleLienZoneLocalite
     */
    public static void checkForDelete(SimpleLienZoneLocalite simpleLienZoneLocalite) {
    }

    /**
     * @param simpleLienZoneLocalite
     * @throws ForfaitsPrimesAssuranceMaladieException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForUpdate(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleLienZoneLocaliteChecker.checkMandatory(simpleLienZoneLocalite);
        SimpleLienZoneLocaliteChecker.checkIntegrity(simpleLienZoneLocalite);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param var
     * @throws ForfaitsPrimesAssuranceMaladieException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (SimpleLienZoneLocaliteChecker.estDejaLieeAuneZone(simpleLienZoneLocalite)) {
            JadeThread.logError(simpleLienZoneLocalite.getClass().getName(),
                    "pegasus.simplelienzonelocalite.lienexistant.integrity");
        }

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * @param simpleLienZoneLocalite
     */
    private static void checkMandatory(SimpleLienZoneLocalite simpleLienZoneLocalite) {
        if (JadeStringUtil.isEmpty(simpleLienZoneLocalite.getDateDebut())) {
            JadeThread.logError(simpleLienZoneLocalite.getClass().getName(),
                    "pegasus.simplelienzonelocalite.dateDebut.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLienZoneLocalite.getIdLocalite())) {
            JadeThread.logError(simpleLienZoneLocalite.getClass().getName(),
                    "pegasus.simplelienzonelocalite.idlocalite.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleLienZoneLocalite.getIdZoneForfait())) {
            JadeThread.logError(simpleLienZoneLocalite.getClass().getName(),
                    "pegasus.simplelienzonelocalite.idzoneforfait.mandatory");
        }
    }

    private static boolean estDejaLieeAuneZone(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean estLiee = false;
        SimpleLienZoneLocaliteSearch search = new SimpleLienZoneLocaliteSearch();
        search.setForDateValable(simpleLienZoneLocalite.getDateDebut());
        search.setForIdLocalite(simpleLienZoneLocalite.getIdLocalite());
        search.setForIdZoneForfait(simpleLienZoneLocalite.getIdZoneForfait());
        search.setWhereKey(SimpleLienZoneLocaliteSearch.WITH_DATE_VALABLE_LE);

        search = PegasusImplServiceLocator.getSimpleLienZoneLocaliteService().search(search);
        if (search.getSize() > 0) {
            if (((SimpleLienZoneLocalite) search.getSearchResults()[0]).getId().equals(simpleLienZoneLocalite.getId())) {
                estLiee = false;
            } else {
                estLiee = true;
            }
        }

        return estLiee;

    }

    private static boolean isLocaliteInZone(SimpleLienZoneLocalite simpleLienZoneLocalite) {
        boolean inZone = true;

        /*
         * LocaliteSearchSimpleModel searchLocalite = new LocaliteSearchSimpleModel(); searchLocalite.s
         * 
         * TIBusinessServiceLocator.fi
         * 
         * SimpleZoneForfaitsSearch searchZone = new SimpleZoneForfaitsSearch(); PegasusServiceLocator
         * .getParametreServicesLocator().getZoneLocaliteService ().read(simpleLienZoneLocalite .getId());
         * //searchLocalite.set
         */
        return false;
    }
}
