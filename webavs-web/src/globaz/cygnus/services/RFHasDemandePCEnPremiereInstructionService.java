package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFHasDemandePCEnPremiereInstructionException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.sql.SQLException;
import ch.globaz.hera.common.SessionProvider;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * author jje
 */
public class RFHasDemandePCEnPremiereInstructionService {

    BTransaction transaction = null;

    public RFHasDemandePCEnPremiereInstructionService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public Boolean hasDemandePCEnPremiereInstruction(String idTiers) throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            Boolean hasDemandePCEnPremiereInstruction = PegasusServiceLocator.getDemandeService()
                    .hasDemandePCEnPremiereInstruction(idTiers);

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFHasDemandePCEnPremiereInstructionException(
                        "RFHasDemandePCEnPremiereInstructionService#hasDemandePCEnPremiereInstruction Errors in context");
            }

            return hasDemandePCEnPremiereInstruction;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFHasDemandePCEnPremiereInstructionException(
                    "RFHasDemandePCEnPremiereInstructionService#hasDemandePCEnPremiereInstruction (BJadeMultipleJdbcConnectionInSameThreadException)",
                    e);
        } catch (SQLException e) {
            throw new RFHasDemandePCEnPremiereInstructionException(
                    "RFHasDemandePCEnPremiereInstructionService#hasDemandePCEnPremiereInstruction (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFHasDemandePCEnPremiereInstructionException(
                    "RFHasDemandePCEnPremiereInstructionService#hasDemandePCEnPremiereInstruction : unable to get membre",
                    e);
        } catch (NullPointerException e) {
            return Boolean.FALSE;
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }

    }
}
