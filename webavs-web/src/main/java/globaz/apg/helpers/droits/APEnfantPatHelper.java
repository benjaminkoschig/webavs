package globaz.apg.helpers.droits;

import ch.globaz.common.util.Instances;
import globaz.apg.businessimpl.service.APProcheAidantServiceHelper;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.vb.droits.APEnfantPatViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

import java.util.Optional;

public class APEnfantPatHelper extends PRAbstractHelper {

    protected void _init(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (action.getElement().equals("apg.droits.enfantPat.nouveau")) {
            Optional<Integer> id = new APProcheAidantServiceHelper((BSession) session)
                    .chercherEnfant(((APEnfantPatViewBean) viewBean).getIdDroitPaternite());
            if (id.isPresent()) {
                ((APEnfantPatViewBean) viewBean).setId(id.get().toString());
                action.changeActionPart("afficher");
                this._retrieve(viewBean, action, session);
            }
        }
    }

    @Override
    protected void _update(final FWViewBeanInterface viewBean, final FWAction action, final BISession session) throws Exception {
        super._update(viewBean, action, session);
        new CareLeaveEventIdHelper().updateCareLeveEventId(viewBean, session);
    }

    @Override
    protected void _add(final FWViewBeanInterface viewBean, final FWAction action, final BISession session) throws Exception {
        if (viewBean instanceof APEnfantPatViewBean) {
            if(JadeStringUtil.isBlankOrZero(((APEnfantPatViewBean)viewBean).getIdSitFamPaternite())){
                super._add(viewBean, action, session);
            }else {
                this._update(viewBean, action, session);
            }
        }
        new CareLeaveEventIdHelper().updateCareLeveEventId(viewBean, session);
    }

    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);
        Instances.of(viewBean).when(APEnfantPatViewBean.class, vb -> {
            if (vb.getTypeDemande().isProcheAidant()) {
                APDroitProcheAidant apDroitProcheAidant = APDroitProcheAidant.retrieve(vb.getDroitDTO().getIdDroit(), session);
                CareLeaveEventIdHelper careLeaveEventIdHelper = new CareLeaveEventIdHelper();
                Optional<APDroitProcheAidant.CareLeaveEventId> careLeaveEventId = careLeaveEventIdHelper.updateCareLeveEventId(viewBean, session);
                vb.setDelaiCadreModifie(careLeaveEventId.map(APDroitProcheAidant.CareLeaveEventId::isCreated).orElse(false));
                vb.setCopyDroit(apDroitProcheAidant.isACopy());
                vb.setNumeroDelaiCadre(apDroitProcheAidant.getCareLeaveEventID());
            }
        });
    }
}
