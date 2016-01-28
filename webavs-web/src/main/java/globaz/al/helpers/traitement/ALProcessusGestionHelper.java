package globaz.al.helpers.traitement;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.traitement.ALTraitementLancementProcess;
import globaz.al.vb.traitement.ALProcessusGestionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper dédié au viewBean ALProcessusGestionViewBean
 * 
 * @author GMO
 * 
 */
public class ALProcessusGestionHelper extends ALAbstractHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        super._retrieve(viewBean, action, session);
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        ALProcessusGestionViewBean vb = (ALProcessusGestionViewBean) viewBean;
        // le traitement sera d'abord mis dans la file d'attente (état en attente)
        // Dès que le process sera traité par le batch, le traitement sera mis à l'état en cours

        try {
            TraitementPeriodiqueModel traitementToUpdate = ALServiceLocator.getTraitementPeriodiqueModelService().read(
                    vb.getId());
            traitementToUpdate.setEtat(ALCSProcessus.ETAT_ATTENTE);
            ALServiceLocator.getTraitementPeriodiqueModelService().update(traitementToUpdate);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
        ALTraitementLancementProcess process = new ALTraitementLancementProcess();
        process.setCancelMode(false);
        process.setIdProcessusPeriodique(vb.getIdProcessusPeriodique());
        process.setIdTraitementPeriodique(vb.getId());
        process.setSession((BSession) session);
        vb.setISession(process.getSession());

        try {
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("annulerTraitement".equals(action.getActionPart()) && (viewBean instanceof ALProcessusGestionViewBean)) {

            ALProcessusGestionViewBean vb = (ALProcessusGestionViewBean) viewBean;
            ALTraitementLancementProcess process = new ALTraitementLancementProcess();
            process.setCancelMode(true);
            process.setIdProcessusPeriodique(vb.getIdProcessusPeriodique());
            process.setIdTraitementPeriodique(vb.getId());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            try {
                BProcessLauncher.start(process, false);
            } catch (Exception e) {
                JadeLogger.warn(this, e.toString());
            }

        }

        if ("creerPartiel".equals(action.getActionPart()) && (viewBean instanceof ALProcessusGestionViewBean)) {
            ALProcessusGestionViewBean vb = (ALProcessusGestionViewBean) viewBean;

            try {
                TemplateTraitementListComplexSearchModel processusPeriodiqueConfigSearch = new TemplateTraitementListComplexSearchModel();
                processusPeriodiqueConfigSearch.setForIdProcessusPeriodique(vb.getIdProcessusPeriodique());
                processusPeriodiqueConfigSearch = ALServiceLocator.getTemplateTraitementListComplexModelService()
                        .search(processusPeriodiqueConfigSearch);

                ALServiceLocator.getBusinessProcessusService().initBusinessProcessusForPeriode(
                        ((TemplateTraitementListComplexModel) processusPeriodiqueConfigSearch.getSearchResults()[0])
                                .getConfigProcessusModel().getTemplate(),
                        ((TemplateTraitementListComplexModel) processusPeriodiqueConfigSearch.getSearchResults()[0])
                                .getConfigProcessusModel().getBusinessProcessus(),
                        ((TemplateTraitementListComplexModel) processusPeriodiqueConfigSearch.getSearchResults()[0])
                                .getPeriodeAFModel().getDatePeriode(), true);
            } catch (JadeApplicationException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            } catch (JadePersistenceException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }

        }
        if ("supprimerPartiel".equals(action.getActionPart()) && (viewBean instanceof ALProcessusGestionViewBean)) {
            ALProcessusGestionViewBean vb = (ALProcessusGestionViewBean) viewBean;

            try {
                ALServiceLocator.getBusinessProcessusService().deleteProcessusPartiel(vb.getIdProcessusPeriodique());
            } catch (JadeApplicationException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            } catch (JadePersistenceException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }

        }
        return super.execute(viewBean, action, session);
    }
}
