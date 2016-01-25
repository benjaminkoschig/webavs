package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFRetrieveRentesIjApiException;
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
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.donneeFinanciere.RenteAvsAiVO;

/**
 * author jje
 */
public class RFRetrieveRentesIjApiService {

    BTransaction transaction = null;

    public RFRetrieveRentesIjApiService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public List<RenteAvsAiVO> getRenteIjApi(String idPCAccodee, String date) throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            List<RenteAvsAiVO> renAvsAiVO = PegasusServiceLocator.getRenteIjApiService()
                    .searchRenteAvsAiByIdPCAccordee(idPCAccodee, date);

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFRetrieveRentesIjApiException(
                        "RFRetrieveRentesIjApiService#getRenteIjApi Error(s) in context");
            }

            return renAvsAiVO;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFRetrieveRentesIjApiException(
                    "RFRetrieveRentesIjApiService#getRenteIjApi (BJadeMultipleJdbcConnectionInSameThreadException)", e);
        } catch (SQLException e) {
            throw new RFRetrieveRentesIjApiException("RFRetrieveRentesIjApiService#getRenteIjApi (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFRetrieveRentesIjApiException(
                    "RFRetrieveRentesIjApiService#getRenteIjApi : unable to get membre", e);
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }

    }

}
