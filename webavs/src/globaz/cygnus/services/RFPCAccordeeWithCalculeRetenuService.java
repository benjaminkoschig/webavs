package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFPCAccordeeWithCalculeRetenuException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.prestation.tools.PRDateFormater;
import java.sql.SQLException;
import java.util.List;
import ch.globaz.hera.common.SessionProvider;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAAccordeePlanClaculeAndMembreFamilleVO;

/**
 * author jje
 */
public class RFPCAccordeeWithCalculeRetenuService {

    BTransaction transaction = null;

    public RFPCAccordeeWithCalculeRetenuService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public List<PCAAccordeePlanClaculeAndMembreFamilleVO> getPCAccordeeWithCalculeRetenuVO(String idTiers,
            String dateJJMMAAAA) throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            List<PCAAccordeePlanClaculeAndMembreFamilleVO> searchPCAccordeeWithCalculeRetenuVO = PegasusServiceLocator
                    .getPCAccordeeService().searchPCAccordeeWithCalculeRetenuVO(idTiers,
                            PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateJJMMAAAA));

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFPCAccordeeWithCalculeRetenuException(
                        "RFPCAccordeeService#getPCAccordeePlanCalculRetenuEnfantsDansCalcul Errors in context");
            }

            return searchPCAccordeeWithCalculeRetenuVO;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFPCAccordeeWithCalculeRetenuException(
                    "RFPCAccordeeService#getPCAccordeePlanCalculRetenuEnfantsDansCalcul (BJadeMultipleJdbcConnectionInSameThreadException)",
                    e);
        } catch (SQLException e) {
            throw new RFPCAccordeeWithCalculeRetenuException(
                    "RFPCAccordeeService#getPCAccordeePlanCalculRetenuEnfantsDansCalcul (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFPCAccordeeWithCalculeRetenuException(
                    "RFPCAccordeeService#getPCAccordeePlanCalculRetenuEnfantsDansCalcul : unable to get membre", e);
        } catch (NullPointerException e) {
            return null;
        } catch (JAException e) {
            throw new RFPCAccordeeWithCalculeRetenuException(
                    "RFPCAccordeeService#getPCAccordeePlanCalculRetenuEnfantsDansCalcul (JAException) ", e);
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }

    }
}
