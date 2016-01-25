package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimplePrixChambre;
import ch.globaz.pegasus.business.models.home.SimplePrixChambreSearch;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambreSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class SimplePrixChambreChecker extends PegasusAbstractChecker {
    /**
     * @param simplePrixChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PrixChambreException
     */
    public static void checkForCreate(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePrixChambreChecker.checkMandatory(simplePrixChambre);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePrixChambreChecker.checkIntegrity(simplePrixChambre);
        }
    }

    /**
     * 
     * @param simplePrixChambre
     */
    public static void checkForDelete(SimplePrixChambre simplePrixChambre) {
    }

    /**
     * @param simplePrixChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PrixChambreException
     */
    public static void checkForUpdate(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        SimplePrixChambreChecker.checkMandatory(simplePrixChambre);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePrixChambreChecker.checkIntegrity(simplePrixChambre);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>le type de chambre doit exister</li> <li>pour un type de chambre, il ne doit pas y avoir de superposition de
     * periode de prix</li>
     * 
     * @param simplePrixChambre
     * @throws TypeChambreException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePrixChambre simplePrixChambre) throws PrixChambreException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // le type de chambre doit exister
        SimpleTypeChambreSearch tchSearch = new SimpleTypeChambreSearch();
        tchSearch.setForIdTypeChambre(simplePrixChambre.getIdTypeChambre());

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getSimpleTypeChambreService().count(tchSearch) < 1) {
                    JadeThread.logError(simplePrixChambre.getClass().getName(),
                            "pegasus.prixChambre.typeChambre.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrixChambreException("Unable to check typeChambre", e);
        } catch (TypeChambreException e) {
            throw new PrixChambreException("Unable to check typeChambre", e);
        }

        // pour un type de chambre, il ne doit pas y avoir de superposition de
        // periode de prix
        SimplePrixChambreSearch pchSearch = new SimplePrixChambreSearch();
        pchSearch.setForIdPrixChambre(simplePrixChambre.getIdPrixChambre());
        pchSearch.setForIdTypeChambre(simplePrixChambre.getIdTypeChambre());
        pchSearch.setForDateDebutCheckPeriode(simplePrixChambre.getDateDebut());
        pchSearch.setForDateFinCheckPeriode(simplePrixChambre.getDateFin());
        pchSearch.setWhereKey("checkSuperpositionPeriode");

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getSimplePrixChambreService().count(pchSearch) > 0) {
                    JadeThread.logError(simplePrixChambre.getClass().getName(),
                            "pegasus.prixChambre.superpositionPeriodePourTypeChambre.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrixChambreException("Unable to check typeChambre", e);
        }

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>un prix de chambre doit avoir une reference sur un type de chambre</li> <li>la date de debut d'un type de
     * chambre est obligatoire</li>
     * 
     * @param simplePrixChambre
     */
    private static void checkMandatory(SimplePrixChambre simplePrixChambre) {

        // un prix de chambre doit avoir une reference sur un type de chambre
        if (JadeStringUtil.isIntegerEmpty(simplePrixChambre.getIdTypeChambre())) {
            JadeThread.logError(simplePrixChambre.getClass().getName(), "pegasus.prixChambre.idTypeChambre.mandatory");
        }
        // la date de debut d'un type de chambre est obligatoire
        if (JadeStringUtil.isEmpty(simplePrixChambre.getDateDebut())) {
            JadeThread.logError(simplePrixChambre.getClass().getName(), "pegasus.prixChambre.dateDebut.mandatory");
        }

    }
}
