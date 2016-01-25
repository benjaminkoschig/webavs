package ch.globaz.pegasus.businessimpl.checkers.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.constantes.IPCRenteijapi;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAutreRenteChecker extends PegasusAbstractChecker {

    public static void checkForCreate(SimpleAutreRente autreRente) {
        SimpleAutreRenteChecker.checkMandatory(autreRente);
    }

    /**
     * @param autreRente
     */
    public static void checkForDelete(SimpleAutreRente autreRente) {
    }

    /**
     * @param autreRente
     */
    public static void checkForUpdate(SimpleAutreRente autreRente) {
        SimpleAutreRenteChecker.checkMandatory(autreRente);
        // SimpleAutreRenteChecker.checkIntegrity(autreRente);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param autreRente
     * @throws AutreRenteException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    /*
     * private static void checkIntegrity(SimpleAutreRente autreRente) throws AutreRenteException,
     * JadePersistenceException, JadeNoBusinessLogSessionError {
     * 
     * }
     */

    /**
     * Verification des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleTitre ait un type</li> <li>
     * Vérifie que le simpleTitre ait un genre</li>
     * 
     * @param autreRente
     */
    private static void checkMandatory(SimpleAutreRente autreRente) {
        if (JadeStringUtil.isEmpty(autreRente.getCsGenre())) {
            JadeThread.logError(autreRente.getClass().getName(), "pegasus.autreRente.csGentre.mandatory");
        }
        if (JadeStringUtil.isEmpty(autreRente.getMontant())) {
            JadeThread.logError(autreRente.getClass().getName(), "pegasus.autreRente.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(autreRente.getCsType())) {
            JadeThread.logError(autreRente.getClass().getName(), "pegasus.autreRente.csType.mandatory");
        }
        // si le genre de rente vaut "Autres" le champs autreGenre est
        // obligatoire
        if ((IPCRenteijapi.CS_AUTRES_RENTES_AUTRE.equals(autreRente.getCsGenre()))
                && JadeStringUtil.isEmpty(autreRente.getAutreGenre())) {
            JadeThread.logError(autreRente.getClass().getName(), "pegasus.autreRente.autreGenre.mandatory");
        }
        // Si le genre de rente vaut "Rente érangpre" le champs monnaie est
        // obligatoire
        if ((IPCRenteijapi.CS_RENTE_ETRENGERE.equals(autreRente.getCsGenre()))
                && JadeStringUtil.isEmpty(autreRente.getCsMonnaie())) {
            JadeThread.logError(autreRente.getClass().getName(), "pegasus.autreRente.csMonnaie.mandatory");
        }

    }
}
