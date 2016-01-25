package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFRetrieveNumeroDecisionServiceException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import java.sql.SQLException;
import ch.globaz.hera.common.SessionProvider;

public class RFRetrieveNumeroDecisionService {

    private final static String RFM_MODULE_PREFIX = "RF";

    /**
     * Retourne un numero de decision unique avec l'année passé en paramètre Par defaut le form est AAAA-######
     * 
     * @param annee
     * @return String, le numero de decision
     * @throws JadePersistenceException
     */
    private static String getNoDecision(String prefix, String annee) throws JadePersistenceException {

        final int nbreDigit = 6; // Nombre de chiffre apres annee
        final String separator = "-";
        final String counterKey = prefix + "_" + annee;

        // Seconde partie du numero
        String partieIncrement = new String();
        // Recuperation de l'increment
        String increment = JadePersistenceManager.incIndentifiant(counterKey);
        // Nombre de 'zero' apres increment
        int cptBeforeDigitIncr = nbreDigit - increment.length();
        for (int cpt = 0; cpt < cptBeforeDigitIncr; cpt++) {
            partieIncrement += "0";
        }
        // Ajout de l'increment
        partieIncrement += increment;
        // Construction de la chaine
        String noDecision = annee + separator + partieIncrement;
        return noDecision;
    }

    BTransaction transaction = null;

    public RFRetrieveNumeroDecisionService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public String getNumeroDecision(String annee) throws JadeApplicationException, JadePersistenceException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            String numeroDecision = RFRetrieveNumeroDecisionService.getNoDecision(
                    RFRetrieveNumeroDecisionService.RFM_MODULE_PREFIX, annee);

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFRetrieveNumeroDecisionServiceException(
                        "RFRetrieveNumeroDecisionServiceException#getNumeroDecision Error(s) in context");
            }

            return numeroDecision;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFRetrieveNumeroDecisionServiceException(
                    "RFRetrieveNumeroDecisionServiceException#getNumeroDecision (BJadeMultipleJdbcConnectionInSameThreadException)",
                    e);
        } catch (SQLException e) {
            throw new RFRetrieveNumeroDecisionServiceException(
                    "RFRetrieveNumeroDecisionServiceException#getNumeroDecision (SQLException) ", e);
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }
    }
}
