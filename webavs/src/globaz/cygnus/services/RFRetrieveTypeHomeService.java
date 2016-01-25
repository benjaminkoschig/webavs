package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFRetrieveTypeHomeException;
import globaz.cygnus.utils.RFUtils;
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
import ch.globaz.pegasus.business.models.home.MembreFamilleHome;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * author jje
 */
public class RFRetrieveTypeHomeService {

    BTransaction transaction = null;

    public RFRetrieveTypeHomeService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public String getCsTypeHome(String idTiers, String date, String idVersionDroit) throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            MembreFamilleHome membreFamilleHome = PegasusServiceLocator.getHomeService().retrieveTypeHome(idTiers,
                    idVersionDroit, date);

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFRetrieveTypeHomeException("RFRetrieveTypeHomeService#retrieveTypeHome Error(s) in context");
            }

            return convertCSPCversCSRFM(membreFamilleHome);

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFRetrieveTypeHomeException(
                    "RFRetrieveTypeHomeService#retrieveTypeHome (BJadeMultipleJdbcConnectionInSameThreadException)", e);
        } catch (SQLException e) {
            throw new RFRetrieveTypeHomeException("RFRetrieveTypeHomeService#retrieveTypeHome (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFRetrieveTypeHomeException("RFRetrieveTypeHomeService#retrieveTypeHome : unable to get membre",
                    e);
        } catch (NullPointerException e) {
            return null;
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }

    }

    /**
     * Convertis le code système PC (typeDeHome) vers les codes système RFM typedeHome
     * 
     * @param membreFamilleHome
     * @throws RFRetrieveTypeHomeException
     */
    private String convertCSPCversCSRFM(MembreFamilleHome membreFamilleHome) throws RFRetrieveTypeHomeException {
        String value = "";
        if (membreFamilleHome != null) {
            return RFUtils.convertCSTypeHomePCversCSTypeHomeRFM(membreFamilleHome.getCsServiceEtat());
        }
        return value;
    }
}
