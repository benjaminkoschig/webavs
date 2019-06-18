package globaz.cygnus.services;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.exceptions.RFRetrieveApiException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.hera.common.SessionProvider;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.MembreFamilleAllocationImpotent;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * author jje
 */
public class RFRetrieveDegreApiService {

    private BTransaction transaction = null;
    private Set<String> typeApiFaible = new HashSet<String>();
    private Set<String> typeApiGrave = new HashSet<String>();
    private Set<String> typeApiMoyen = new HashSet<String>();

    public RFRetrieveDegreApiService(BTransaction transaction) {
        this.transaction = transaction;
    }

    private String getCsDegreApiRfm(String csTypeApi) throws Exception {

        initialisationTypeApiFaible();
        initialisationTypeApiGrave();
        initialisationTypeApiMoyen();

        if (typeApiGrave.contains(csTypeApi)) {
            return IRFQd.CS_DEGRE_API_GRAVE;
        } else if (typeApiMoyen.contains(csTypeApi)) {
            return IRFQd.CS_DEGRE_API_MOYEN;
        } else if (typeApiFaible.contains(csTypeApi)) {
            return IRFQd.CS_DEGRE_API_FAIBLE;
        } else {
            throw new Exception("RFRetrieveDegreApiService.getCsDegreApiRfm():Type API PC introuvable");
        }
    }

    private void initialisationTypeApiFaible() {
        typeApiFaible = new HashSet<String>();
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_81);
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_84);
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_85);
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_89);
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_91);
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_94);
        typeApiFaible.add(IPCApiAvsAi.CS_TYPE_API_95);
    }

    private void initialisationTypeApiGrave() {
        typeApiGrave = new HashSet<String>();
        typeApiGrave.add(IPCApiAvsAi.CS_TYPE_API_83);
        typeApiGrave.add(IPCApiAvsAi.CS_TYPE_API_87);
        typeApiGrave.add(IPCApiAvsAi.CS_TYPE_API_93);
        typeApiGrave.add(IPCApiAvsAi.CS_TYPE_API_97);
    }

    private void initialisationTypeApiMoyen() {
        typeApiMoyen = new HashSet<String>();
        typeApiMoyen.add(IPCApiAvsAi.CS_TYPE_API_82);
        typeApiMoyen.add(IPCApiAvsAi.CS_TYPE_API_86);
        typeApiMoyen.add(IPCApiAvsAi.CS_TYPE_API_88);
        typeApiMoyen.add(IPCApiAvsAi.CS_TYPE_API_92);
        typeApiMoyen.add(IPCApiAvsAi.CS_TYPE_API_96);
    }

    public String retrieveCsDegreApi(String idTiers, String date, String idVersionDroit)
            throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (null == ctx) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            MembreFamilleAllocationImpotent memFamAllImp = PegasusServiceLocator.getRenteIjApiService()
                    .retrieveDegreAllocationImpotent(idTiers, idVersionDroit, date);

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFRetrieveApiException("RFRetrieveDegreApiService#retrieveCsDegreApi Error(s) in context");
            }

            if (memFamAllImp != null) {
                return getCsDegreApiRfm(memFamAllImp.getCsTypeRente());
            } else {
                return "";
            }

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFRetrieveApiException(
                    "RFRetrieveDegreApiService#retrieveCsDegreApi (BJadeMultipleJdbcConnectionInSameThreadException)",
                    e);
        } catch (SQLException e) {
            throw new RFRetrieveApiException("RFRetrieveDegreApiService#retrieveCsDegreApi (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFRetrieveApiException("RFRetrieveDegreApiService#retrieveCsDegreApi : unable to get membre", e);
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            throw new RFRetrieveApiException(e.getMessage());
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }

    }

}
