package ch.globaz.pegasus.businessimpl.checkers.monnaieetrangere;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangereSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class SimpleMonnaieEtrangereChecker extends PegasusAbstractChecker {

    /**
     * Vérifiaction lors de la création d'une monnaie étrangère
     * 
     * 
     * @param simpleMonnaieEtrangere
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws MonnaieEtrangereException
     * @throws PCAccordeeException
     */
    public static void checkForCreate(SimpleMonnaieEtrangere simpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleMonnaieEtrangereChecker.checkMandatory(simpleMonnaieEtrangere);
        // check si période supérieur
        SimpleMonnaieEtrangereChecker.checkPeriodeSuperior(simpleMonnaieEtrangere);
        // check Integrity
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleMonnaieEtrangereChecker.checkIntegrity(simpleMonnaieEtrangere);
        }
    }

    /**
     * Validation lors de l'effacement d'une monnaie
     * 
     * 
     * @param simpleMonnaieEtrangere
     * @throws TypeChambreException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     */
    public static void checkForDelete(SimpleMonnaieEtrangere simpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Validation lors de la mise à jour
     * 
     * @param simpleMonnaieEtrangere
     * @throws MonnaieEtrangereException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimpleMonnaieEtrangere simpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException {
        // check mandatory
        SimpleMonnaieEtrangereChecker.checkMandatory(simpleMonnaieEtrangere);
        // check Periode supérieur
        SimpleMonnaieEtrangereChecker.checkPeriodeSuperior(simpleMonnaieEtrangere);
        // check integrity
        SimpleMonnaieEtrangereChecker.checkIntegrity(simpleMonnaieEtrangere);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleMonnaieEtrangere
     * @throws MonnaieEtrangereException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     * @throws PCAccordeeException
     */
    private static void checkIntegrity(SimpleMonnaieEtrangere simpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Pour une période de taux pas de superposition possible
        SimpleMonnaieEtrangereSearch smeSearch = new SimpleMonnaieEtrangereSearch();
        smeSearch.setForCsTypeMonnaie(simpleMonnaieEtrangere.getCsTypeMonnaie());
        smeSearch.setForDateDebutCheckPeriode(simpleMonnaieEtrangere.getDateDebut());
        smeSearch.setForDateFinCheckPeriode(simpleMonnaieEtrangere.getDateFin());

        smeSearch.setForIdMonnaieEtrangere(simpleMonnaieEtrangere.getIdMonnaieEtrangere());
        smeSearch.setWhereKey("checkSuperpositionPeriodes");

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                // si results periode superposée
                if (PegasusImplServiceLocator.getSimpleMonnaieEtrangereService().count(smeSearch) > 0) {
                    JadeThread.logError(simpleMonnaieEtrangere.getClass().getName(),
                            "pegasus.simpleMonnaieEtrangere.superpositionPeriodes.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MonnaieEtrangereException("Unable to check simpleMonnaieEtrangere", e);
        }

        // la date de début ne doit pas être antérieur à la date de fin,
        // et la date de fin non null (periode ouverte)
        if (JadeDateUtil.isDateMonthYearAfter(simpleMonnaieEtrangere.getDateDebut(),
                simpleMonnaieEtrangere.getDateFin())
                && (simpleMonnaieEtrangere.getDateFin() != null)) {
            JadeThread.logError(simpleMonnaieEtrangere.getClass().getName(),
                    "pegasus.simpleMonnaieEtrangere.anteriorDate.mandatory");
        }
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Une monnaie etrangere doit obligatoirement avoir: - un taux - une monnaie - une date de debut
     * 
     * La date de debut ne doit pas etre antérieur a la date de fin</li>
     * 
     * @param simpleMonnaieEtrangere
     */
    private static void checkMandatory(SimpleMonnaieEtrangere simpleMonnaieEtrangere) {

        // Une monnaie etrangere doit avoir un taux et une monnaie (cs) définie
        if (JadeStringUtil.isEmpty(simpleMonnaieEtrangere.getTaux())) {
            JadeThread.logError(simpleMonnaieEtrangere.getClass().getName(),
                    "pegasus.simpleMonnaieEtrangere.taux.mandatory.");
        }
        // Le type de monnaie doit être spécifié
        if (JadeStringUtil.isEmpty(simpleMonnaieEtrangere.getCsTypeMonnaie())) {
            JadeThread.logError(simpleMonnaieEtrangere.getClass().getName(),
                    "pegasus.simpleMonnaieEtrangere.typeMonnaie.mandatory.");
        }
        // La date de debut doit etre obligatoire
        if (JadeStringUtil.isEmpty(simpleMonnaieEtrangere.getDateDebut())) {
            JadeThread.logError(simpleMonnaieEtrangere.getClass().getName(),
                    "pegasus.simpleMonnaieEtrangere.datedeb.mandatory");
        }

    }

    /**
     * Recherche si une période supérieur existe. La période, dans ce cas doit être fermé
     * 
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws MonnaieEtrangereException
     */
    private static void checkPeriodeSuperior(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // si date de fin vide... on traite
        if (JadeStringUtil.isEmpty(simpleMonnaieEtrangere.getDateFin())) {
            SimpleMonnaieEtrangereSearch smeSearch = new SimpleMonnaieEtrangereSearch();
            smeSearch.setForCsTypeMonnaie(simpleMonnaieEtrangere.getCsTypeMonnaie());
            smeSearch.setForDateDebutCheckPeriode(simpleMonnaieEtrangere.getDateDebut());
            smeSearch.setWhereKey("withPeriodeSuperior");

            try {
                if (!PegasusAbstractChecker.threadOnError()) {
                    if (PegasusImplServiceLocator.getSimpleMonnaieEtrangereService().count(smeSearch) > 0) {
                        JadeThread.logError(simpleMonnaieEtrangere.getClass().getName(),
                                "pegasus.simpleMonnaieEtrangere.withPeriodeSuperior");
                    }
                }
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new MonnaieEtrangereException("Unable to check simpleMonnaieEtrangere withPeriodeSuperior", e);
            }
        }

    }

}
