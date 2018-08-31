package ch.globaz.vulpecula.ws.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.lang.Validate;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.ws.bean.QualificationEbu;
import ch.globaz.vulpecula.ws.utils.UtilsService;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;

/**
 * Description de la classe
 *
 * @since WebBMS 1.0
 */
@WebService(endpointInterface = "ch.globaz.vulpecula.ws.services.QualificationEbuService")
public class QualificationEbuServiceImpl extends VulpeculaAbstractService implements QualificationEbuService {

    @Override
    public List<QualificationEbu> findByConvention(String conventionCode) {
        Validate.notEmpty(conventionCode.trim(), "ConventionCode must not be empty !");

        // Récupération d'une session
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        List<QualificationEbu> liste = new ArrayList<QualificationEbu>();
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            String code = JadeStringUtil.fillWithZeroes(conventionCode, 2);
            Administration adminConvention = VulpeculaRepositoryLocator.getAdministrationRepository()
                    .findByCodeAndGenre(code, "68900002");

            Validate.notNull(adminConvention, "Code convention doesn't exist !");

            List<ConventionQualification> listeConvQual = VulpeculaRepositoryLocator
                    .getConventionQualificationRepository().findByIdConvention(adminConvention.getId());

            Validate.notEmpty(listeConvQual, "Qualification for code convention " + conventionCode + " not found !!");

            for (ConventionQualification qualification : listeConvQual) {
                try {
                    if (VulpeculaServiceLocator.getQualificationService().isCCT(qualification)) {
                        liste.add(new QualificationEbu(session, qualification.getQualification(), true));
                    } else {
                        liste.add(new QualificationEbu(session, qualification.getQualification(), false));
                    }
                } catch (UnsatisfiedSpecificationException e) {
                    JadeLogger.error(e, e.getMessage());
                }

            }

        } catch (SQLException e) {
            session.addError(e.getMessage());
            e.printStackTrace();
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return liste;
    }

}
