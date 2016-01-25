package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class TypeChambreChecker extends PegasusAbstractChecker {
    /**
     * @param typeChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws TypeChambreException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(TypeChambre typeChambre) throws TypeChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        TypeChambreChecker.checkMandatory(typeChambre);
        if (!PegasusAbstractChecker.threadOnError()) {
            TypeChambreChecker.checkIntegrity(typeChambre);
        }
    }

    /**
     * @param typeChambre
     */
    public static void checkForDelete(TypeChambre typeChambre) {
    }

    /**
     * @param typeChambre
     */
    public static void checkForUpdate(TypeChambre typeChambre) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>Il ne peut exister qu'un seul home pour un tiersHome et un nom de batiment donne</li>
     * 
     * @param typeChambre
     * @throws TypeChambreException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(TypeChambre typeChambre) throws TypeChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // Un type de chambre (sans tenir compte des particularites) doit etre
        // unique pour un home
        TypeChambreSearch tchSearch = new TypeChambreSearch();
        tchSearch.setForIdHome(typeChambre.getSimpleTypeChambre().getIdHome());
        tchSearch.setForDesignation(typeChambre.getSimpleTypeChambre().getDesignation());
        tchSearch.setForCsCategorie(typeChambre.getSimpleTypeChambre().getCsCategorie());
        tchSearch.setForIsApiFacturee(typeChambre.getSimpleTypeChambre().getIsApiFacturee());
        tchSearch.setNotForIdTypeChambre(typeChambre.getSimpleTypeChambre().getIdTypeChambre());
        tchSearch.setForIsParticularite(Boolean.FALSE);

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getTypeChambreService().count(tchSearch) > 0) {
                    JadeThread.logError(typeChambre.getClass().getName(),
                            "pegasus.typeChambre.typeChambreUniqueParHome.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeChambreException("Unable to check typeChambre", e);
        }
    }

    /**
     * @param typeChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws TypeChambreException
     */
    private static void checkMandatory(TypeChambre typeChambre) throws TypeChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
    }
}
