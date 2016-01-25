package ch.globaz.pegasus.businessimpl.checkers.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRente;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRenteSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class SimpleConversionRenteChecker extends PegasusAbstractChecker {
    public static void checkForCreate(SimpleConversionRente simpleConversionRente) throws ConversionRenteException {

        SimpleConversionRenteChecker.checkMandatory(simpleConversionRente);
        SimpleConversionRenteChecker.checkIntegrity(simpleConversionRente);
    }

    /**
     * @param simpleConversionRente
     */
    public static void checkForDelete(SimpleConversionRente simpleConversionRente) {
    }

    /**
     * @param simpleConversionRente
     * @throws ConversionRenteException
     */
    public static void checkForUpdate(SimpleConversionRente simpleConversionRente) throws ConversionRenteException {

        SimpleConversionRenteChecker.checkMandatory(simpleConversionRente);
        SimpleConversionRenteChecker.checkIntegrity(simpleConversionRente);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleConversionRente
     * @throws ConversionRenteException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleConversionRente simpleConversionRente) throws ConversionRenteException {
        SimpleConversionRenteSearch search = new SimpleConversionRenteSearch();

        search.setForAge(simpleConversionRente.getAge());
        search.setForDateDebut(simpleConversionRente.getDateDebut());

        try {
            int count = PegasusImplServiceLocator.getSimpleConversionRenteService().count(search);
            if ((simpleConversionRente.isNew() && (count > 0)) || (!simpleConversionRente.isNew() && (count > 1))) {
                JadeThread.logError(simpleConversionRente.getClass().getName(),
                        "pegasus.conversionrente.donneeExistante.integrity");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ConversionRenteException("Unable to check simpleConversionRente", e);
        } catch (JadePersistenceException e) {
            throw new ConversionRenteException("Unable to check simpleConversionRente", e);
        }
    }

    /**
     * Verification des donnees obligatoires: <li>une conversion de rente viagère doit avoir un age</li> <li>une
     * conversion de rente viagère doit avoir une anne</li>
     * 
     * @param simpleConversionRente
     * @throws ConversionRenteException
     */
    private static void checkMandatory(SimpleConversionRente simpleConversionRente) {
        if (JadeStringUtil.isEmpty(simpleConversionRente.getAge())) {
            JadeThread.logError(simpleConversionRente.getClass().getName(), "pegasus.conversionrente.age.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleConversionRente.getDateDebut())
                || (simpleConversionRente.getDateDebut() == "01.01.null")) {
            JadeThread.logError(simpleConversionRente.getClass().getName(), "pegasus.conversionrente.Annee.mandatory");
        }
    }
}
