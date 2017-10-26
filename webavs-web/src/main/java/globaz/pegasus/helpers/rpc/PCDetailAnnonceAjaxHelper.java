package globaz.pegasus.helpers.rpc;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.pegasus.vb.rpc.PCDetailAnnonceAjaxViewBean;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceRepositoryJade;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.RetourAnnonceRepository;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.StatusRetourAnnonce;

public class PCDetailAnnonceAjaxHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        final PCDetailAnnonceAjaxViewBean vb = (PCDetailAnnonceAjaxViewBean) viewBean;

        if (vb.getUpdateCodeTraitement()) {
            new AnnonceRepositoryJade().updateCodeTraitement(vb.getAnnonceId(), vb.getCodeTraitementValue());
            vb.setUpdateCodeTraitement(false);
        } else if (vb.getUpdateRemarque() && vb.getRemarque() != null) {
            vb.setUpdateRemarque(false);
            new RetourAnnonceRepository().updateRemarqueDetailAnnonce(vb.getSelectedItemId(), vb.getRemarque());
        } else if (vb.getUpdateItemStatus()) {
            vb.setUpdateItemStatus(false);
            if (vb.getChangeToTraite()) {
                new RetourAnnonceRepository().updateEtatDetailAnnonce(vb.getSelectedItemId(),
                        StatusRetourAnnonce.TRAITE);
            } else {
                new RetourAnnonceRepository().updateEtatDetailAnnonce(vb.getSelectedItemId(),
                        StatusRetourAnnonce.ACCEPTE);
            }
        } else {
            new AnnonceRepositoryJade().updateEtatAnnoncePourRenvoi(vb.getAnnonceId());
            vb.setEtatAnnonceRpc(EtatAnnonce.POUR_ENVOI);
            vb.setCodeTraitementAnnonceRpc(CodeTraitement.RETOUR_CORRIGE);
        }
    }
}
