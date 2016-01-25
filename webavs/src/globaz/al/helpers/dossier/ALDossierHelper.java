package globaz.al.helpers.dossier;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.dossier.ALDossierViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Helper dédié au viewBean ALDossierViewBean
 * 
 * @author GMO
 * 
 */
public class ALDossierHelper extends ALAbstractHelper {

    /**
     * Initialise le modèle complex (dossierPlusComplexModel) du viewBean, c'est-à-dire qu'il le charge si il existe ou
     * définit les valeurs par défaut sinon.
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (viewBean instanceof ALDossierViewBean) {

            ((ALDossierViewBean) viewBean).setDossierComplexModel(ALServiceLocator.getDossierComplexModelService()
                    .initModel(((ALDossierViewBean) viewBean).getDossierComplexModel()));

        }
        super._init(viewBean, action, session);

    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("lierDossierLibra".equals(action.getActionPart()) && (viewBean instanceof ALDossierViewBean)) {
            try {

                if (((ALDossierViewBean) viewBean).getDossierComplexModel().isNew()) {
                    ((ALDossierViewBean) viewBean).retrieve();
                }

                DossierComplexModel dossier = ((ALDossierViewBean) viewBean).getDossierComplexModel();
                // idExterne = iddossier
                // libelle de la journalisation
                // tiers alloc
                // ILIConstantesExternes.CS_DOMAINE_AF, true

                LibraServiceLocator.getJournalisationService().createJournalisationWithTestDossier(dossier.getId(),
                        ALConstJournalisation.DOSSIER_MOTIF_JOURNALISATION_OUVERTURE,
                        dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                        ILIConstantesExternes.CS_DOMAINE_AF, true);

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

}
