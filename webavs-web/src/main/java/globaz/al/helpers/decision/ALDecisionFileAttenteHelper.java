package globaz.al.helpers.decision;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.exception.ALGestionnaireException;
import ch.globaz.al.utils.ALGestionnaireUtils;
import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.decision.ALDecisionsFileAttenteProcess;
import globaz.al.vb.decision.ALDecisionFileAttenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.fx.user.business.enums.TypeDroit;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;

/**
 * Helper dédié au action ALDecisionFileAttente*
 *
 * @author LGA
 *
 */
public class ALDecisionFileAttenteHelper extends ALAbstractHelper {

    private static final Logger logger = LoggerFactory.getLogger(ALDecisionFileAttenteHelper.class);

    /**
     * Affichage de la page 'Décision en file d'attente'
     */
    @Override
    protected void _init(FWViewBeanInterface vb, FWAction action, globaz.globall.api.BISession iSession) {
        if (vb == null) {
            throw new IllegalArgumentException("ViewBean van not be null");
        }
        if (!(vb instanceof ALDecisionFileAttenteViewBean)) {
            throw new IllegalArgumentException("Wrong viewBean type [" + vb.getClass().getName()
                    + "]. ViewBean must be an instance of " + ALDecisionFileAttenteViewBean.class.getName());
        }
        BSession session = (BSession) iSession;
        ALDecisionFileAttenteViewBean viewBean = (ALDecisionFileAttenteViewBean) vb;

        try {
            viewBean.setAlUsers(ALGestionnaireUtils.getGestionnaires(session, TypeDroit.UPDATE));
            viewBean.setUtilisateurSelectionne(session.getUserId());
            viewBean.setEmail(session.getUserEMail());
            viewBean.setInsertionGed(true);
            viewBean.setDateImpression(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        } catch (ALGestionnaireException e) {
            logger.error("Unable to find ALGestionnaires list : " + e.toString(), e);
            throw new RuntimeException("Exception thrown when retrieving AL users : " + e.toString(), e);
        }

    }

    /**
     * Lancement du processus depuis la page 'Décision en file d'attente'
     */
    @Override
    protected void _start(FWViewBeanInterface vb, FWAction action, BISession session) {
        ALDecisionFileAttenteViewBean viewBean = (ALDecisionFileAttenteViewBean) vb;
        ALDecisionsFileAttenteProcess process = new ALDecisionsFileAttenteProcess();

        process.setSession((BSession) session);
        process.setDateImpression(viewBean.getDateImpression());
        process.setInsertionGED(viewBean.isInsertionGed());
        process.setEmail(viewBean.getEmail());
        process.setIdGestionnaireSelectionne(viewBean.getIdGestionnaireSelectionne());

        try {
            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            logger.error("Exception thrown in process [" + ALDecisionsFileAttenteProcess.class.getName() + "] : "
                    + e.toString(), e);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

    }
}
