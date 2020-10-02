package ch.globaz.pegasus.businessimpl.checkers.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladieSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author DMA
 * @date 10 nov. 2010
 */
public class SimpleForfaitPrimesAssuranceMaladieChecker {
    /**
     * @param simpleForfaitPrimesAssuranceMaladie
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleForfaitPrimesAssuranceMaladieChecker.checkMandatory(simpleForfaitPrimesAssuranceMaladie);
        SimpleForfaitPrimesAssuranceMaladieChecker.checkIntegrity(simpleForfaitPrimesAssuranceMaladie);
    }

    /**
     * @param simpleLienZoneLocalite
     */
    public static void checkForDelete(SimpleForfaitPrimesAssuranceMaladie simpleLienZoneLocalite) {
    }

    /**
     * @param simpleForfaitPrimesAssuranceMaladie
     * @throws ForfaitsPrimesAssuranceMaladieException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForUpdate(SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleForfaitPrimesAssuranceMaladieChecker.checkMandatory(simpleForfaitPrimesAssuranceMaladie);
        SimpleForfaitPrimesAssuranceMaladieChecker.checkIntegrity(simpleForfaitPrimesAssuranceMaladie);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleForfaitPrimesAssuranceMaladie
     * @throws ForfaitsPrimesAssuranceMaladieException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (!SimpleForfaitPrimesAssuranceMaladieChecker.isUnique(simpleForfaitPrimesAssuranceMaladie)) {
            JadeThread.logError(simpleForfaitPrimesAssuranceMaladie.getClass().getName(),
                    "pegasus.simpleforfaitprimesassurancemaladie.unique.integrity");

        }
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * <li>Vérifie que le SimpleForfaitPrimesAssuranceMaladie ait un type</li> <li>Si il y a une désignation</li> <li>
     * <li>Veérifi si il y a un montant</li>
     * 
     * @param var
     */
    private static void checkMandatory(SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie) {
        if (JadeStringUtil.isEmpty(simpleForfaitPrimesAssuranceMaladie.getCsTypePrime())) {
            JadeThread.logError(simpleForfaitPrimesAssuranceMaladie.getClass().getName(),
                    "pegasus.simpleforfaitprimesassurancemaladie.cstype.mandatory");

        }
        if (JadeStringUtil.isEmpty(simpleForfaitPrimesAssuranceMaladie.getMontantPrimeMoy())) {
            JadeThread.logError(simpleForfaitPrimesAssuranceMaladie.getClass().getName(),
                    "pegasus.simpleforfaitprimesassurancemaladie.montantprimemoy.mandatory");

        }
        if (JadeStringUtil.isEmpty(simpleForfaitPrimesAssuranceMaladie.getDateDebut())) {
            JadeThread.logError(simpleForfaitPrimesAssuranceMaladie.getClass().getName(),
                    "pegasus.simpleforfaitprimesassurancemaladie.annee.mandatory");

        }
    }

    private static boolean isUnique(SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean isUnique = true;
        int count = 0;
        SimpleForfaitPrimesAssuranceMaladieSearch search = new SimpleForfaitPrimesAssuranceMaladieSearch();
        search.setForAnneeValidite(simpleForfaitPrimesAssuranceMaladie.getDateDebut());
        search.setForIdZoneForfaits(simpleForfaitPrimesAssuranceMaladie.getIdZoneForfait());
        search.setForCsTypePrime(simpleForfaitPrimesAssuranceMaladie.getCsTypePrime());

        search = PegasusImplServiceLocator.getSimpleForfaitPrimesAssuranceMaladieService().search(search);
        if (search.getSize() > 0) {
            // check s'il ne s'agit pas du même tuple.
            if ((search.getSize() == 1)
                    && (((SimpleForfaitPrimesAssuranceMaladie) search.getSearchResults()[0]).getId()
                            .equals(simpleForfaitPrimesAssuranceMaladie.getId()))) {
                isUnique = true;

            } else {
                isUnique = false;
            }
        }
        return isUnique;
    }
}
