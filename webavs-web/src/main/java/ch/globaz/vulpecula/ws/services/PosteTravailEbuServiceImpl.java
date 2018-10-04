package ch.globaz.vulpecula.ws.services;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.lang.Validate;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.ws.bean.PosteTravailEbu;
import ch.globaz.vulpecula.ws.utils.UtilsService;

/**
 * @since eBMS 1.0
 */
@WebService(endpointInterface = "ch.globaz.vulpecula.ws.services.PosteTravailEbuService")
public class PosteTravailEbuServiceImpl extends VulpeculaAbstractService implements PosteTravailEbuService {

    @Override
    public List<PosteTravailEbu> listAllPosteTravail(String idEmployeur) {
        Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
        List<PosteTravailEbu> listeDecompteEbu = new ArrayList<PosteTravailEbu>();

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            List<PosteTravail> liste = VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdEmployeur(
                    idEmployeur);
            for (PosteTravail posteTravail : liste) {
                PosteTravailEbu pt = new PosteTravailEbu(posteTravail);
                listeDecompteEbu.add(pt);
            }
        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return listeDecompteEbu;
    }

    @Override
    public List<PosteTravailEbu> listPosteTravail(String idEmployeur, String yearsMonth) {
        Validate.notEmpty(idEmployeur.trim(), "idEmployeur must not be empty !");
        Validate.notEmpty(yearsMonth.trim(), "yearsMonth must not be empty !");

        String date = yearsMonth + "01";

        Validate.isTrue(Date.isValid(date), "yearsMonth must be with format YYYYMM !");

        List<PosteTravailEbu> listeDecompteEbu = new ArrayList<PosteTravailEbu>();

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            List<PosteTravail> liste = VulpeculaServiceLocator.getPosteTravailService().findPostesActifsByIdAffilie(
                    idEmployeur, new Date(date));
            for (PosteTravail posteTravail : liste) {
                PosteTravailEbu pt = new PosteTravailEbu(posteTravail);
                listeDecompteEbu.add(pt);
            }
        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return listeDecompteEbu;
    }

    @Override
    public PosteTravailEbu readPosteTravail(String idPosteTravail) {
        Validate.notEmpty(idPosteTravail.trim(), "idPosteTravail must not be empty !");

        PosteTravailEbu pt = null;

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            pt = new PosteTravailEbu(VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail));

        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return pt;
    }

    @Override
    public PosteTravailEbu readPosteTravailAnnonce(String correlationId) {
        Validate.notEmpty(correlationId.trim(), "correlationId must not be empty !");

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return null;
    }

}
