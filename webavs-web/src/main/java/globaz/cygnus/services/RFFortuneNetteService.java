package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFPCAccordeeWithCalculeRetenuException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.math.BigDecimal;
import java.sql.SQLException;
import ch.globaz.hera.common.SessionProvider;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * author jje
 */
public class RFFortuneNetteService {

    BTransaction transaction = null;

    public RFFortuneNetteService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public BigDecimal getFortuneNette(String idDecision, String idPcAccordee) throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            BigDecimal fortuneMoinsDettes = null;

            if (JadeStringUtil.isBlankOrZero(idDecision)) {
                fortuneMoinsDettes = PegasusServiceLocator.getFortuneService().calculeFortuneFromPcAccordee(
                        idPcAccordee);
            } else {
                fortuneMoinsDettes = PegasusServiceLocator.getFortuneService().calculeFortuneFromDecision(idDecision);
            }

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFPCAccordeeWithCalculeRetenuException(
                        "RFFortuneNetteService#getFortuneNette Error(s) in context");
            }

            return fortuneMoinsDettes;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFPCAccordeeWithCalculeRetenuException(
                    "RFFortuneNetteService#getFortuneNette (BJadeMultipleJdbcConnectionInSameThreadException)", e);
        } catch (SQLException e) {
            throw new RFPCAccordeeWithCalculeRetenuException("RFFortuneNetteService#getFortuneNette (SQLException) ", e);
        } catch (NullPointerException e) {
            return null;
        } catch (DecisionException e) {
            throw new RFPCAccordeeWithCalculeRetenuException(
                    "RFFortuneNetteService#getFortuneNette (DecisionException) ", e);
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }

    }

}
