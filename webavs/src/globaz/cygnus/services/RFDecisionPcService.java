package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFDecisionsPcException;
import globaz.cygnus.exceptions.RFPCAccordeeWithCalculeRetenuException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.sql.SQLException;
import java.util.List;
import ch.globaz.hera.common.SessionProvider;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;

/**
 * author jje
 */
public class RFDecisionPcService {

    BTransaction transaction = null;

    public RFDecisionPcService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public List<DecisionPcVO> getDecisionPc(List<String> datesValidations) throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            List<DecisionPcVO> searchDecisionsByDateValidation = PegasusServiceLocator.getDecisionService()
                    .searchDecisionsByDateValidation(datesValidations);

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFPCAccordeeWithCalculeRetenuException(
                        "RRFDecisionPcService#getDecisionPc Error(s) in context");
            }

            return searchDecisionsByDateValidation;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFDecisionsPcException(
                    "RFDecisionPcService#getDecisionPc (BJadeMultipleJdbcConnectionInSameThreadException)", e);
        } catch (SQLException e) {
            throw new RFDecisionsPcException("RFDecisionPcService#getDecisionPc (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFDecisionsPcException("RFDecisionPcService#getDecisionPc : unable to get membre", e);
        } catch (NullPointerException e) {
            return null;
        } catch (DecisionException e) {
            throw new RFDecisionsPcException("RFDecisionPcService#getDecisionPc (DecisionException)", e);
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }
    }
}
