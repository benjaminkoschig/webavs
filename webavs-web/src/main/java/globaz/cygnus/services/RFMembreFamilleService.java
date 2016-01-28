package globaz.cygnus.services;

import globaz.cygnus.exceptions.RFMembreFamilleException;
import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.sql.SQLException;
import java.util.ArrayList;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.hera.common.SessionProvider;

/**
 * author jje
 */
public class RFMembreFamilleService {

    BTransaction transaction = null;

    public RFMembreFamilleService(BTransaction transaction) {
        this.transaction = transaction;
    }

    public MembreFamilleVO[] getMembreFamille(String idTiers, String date, boolean isFratrie)
            throws JadeApplicationException {

        boolean hasContext = true;

        try {

            JadeThreadContext ctx = JadeThread.currentContext();

            if (ctx == null) {
                hasContext = false;
                BJadeThreadActivator.startUsingContext(transaction);
                ctx = JadeThread.currentContext();
            }

            ctx.storeTemporaryObject(SessionProvider.OBJ_BSESSION, transaction.getSession());

            MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes = null;

            if (!isFratrie) {

                searchMembresFamilleRequerantDomaineRentes = HeraServiceLocator.getMembreFamilleService()
                        .searchMembresFamilleRequerantDomaineRentes(idTiers, date);

            } else {

                ArrayList<MembreFamilleVO> searchMembresFamilleRequerantDomaineRentesArray = HeraServiceLocator
                        .getMembreFamilleService().getFamilleByIDEnfant(idTiers, date, true);

                searchMembresFamilleRequerantDomaineRentes = searchMembresFamilleRequerantDomaineRentesArray
                        .toArray(new MembreFamilleVO[0]);
            }

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                throw new RFMembreFamilleException("RFMembreFamilleService#getMembreFamille Error(s) in context");
            }

            return searchMembresFamilleRequerantDomaineRentes;

        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new RFMembreFamilleException(
                    "RFMembreFamilleService#getMembreFamille (BJadeMultipleJdbcConnectionInSameThreadException)", e);
        } catch (SQLException e) {
            throw new RFMembreFamilleException("RFMembreFamilleService#getMembreFamille (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new RFMembreFamilleException("RFMembreFamilleService#getMembreFamille : unable to get membre", e);
        } catch (NullPointerException e) {
            return null;
        } catch (MembreFamilleException e) {
            throw new RFMembreFamilleException("RFMembreFamilleService#getMembreFamille : MembreFamilleException", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RFMembreFamilleException(
                    "RFMembreFamilleService#getMembreFamille : JadeApplicationServiceNotAvailableException", e);
        } catch (Exception e) {
            throw new RFMembreFamilleException("RFMembreFamilleService#getMembreFamille : ", e);
        } finally {
            if (!hasContext) {
                BJadeThreadActivator.stopUsingContext(transaction);
            }
        }
    }
}
