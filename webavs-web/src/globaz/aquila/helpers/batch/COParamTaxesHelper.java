package globaz.aquila.helpers.batch;

import globaz.aquila.db.access.batch.COCalculTaxe;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeCalculTaxe;
import globaz.aquila.db.access.batch.COTrancheTaxeManager;
import globaz.aquila.db.batch.COParamTaxesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

public class COParamTaxesHelper extends FWHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof COParamTaxesViewBean) {
            try {

                COEtape etape = new COEtape();
                etape.setSession((BSession) session);
                etape.setAlternateKey(COEtape.ALT_KEY_LIB_ETAPE);
                etape.setIdSequence(((COParamTaxesViewBean) viewBean).getIdSequence());
                etape.setLibEtape(((COParamTaxesViewBean) viewBean).getEtape());
                etape.retrieve();

                if (etape != null && !etape.isNew()) {

                    COCalculTaxe ct = new COCalculTaxe();
                    ct.setSession((BSession) session);
                    ct.setIdCalculTaxe(((COParamTaxesViewBean) viewBean).getIdTaxe());
                    ct.setIdRubrique(((COParamTaxesViewBean) viewBean).getIdRubrique());
                    ct.setTypeTaxe(((COParamTaxesViewBean) viewBean).getTypeTaxe());
                    ct.setMontantFixe(((COParamTaxesViewBean) viewBean).getMontantFixe());
                    ct.setBaseTaxe(((COParamTaxesViewBean) viewBean).getBaseTaxe());
                    ct.setDescriptionFr(((COParamTaxesViewBean) viewBean).getLibelleFR());
                    ct.setDescriptionDe(((COParamTaxesViewBean) viewBean).getLibelleDE());
                    ct.setDescriptionIt(((COParamTaxesViewBean) viewBean).getLibelleIT());
                    ct.setTypeTaxeEtape(((COParamTaxesViewBean) viewBean).getTypeTaxeEtape());
                    ct.add();

                    COEtapeCalculTaxe ect = new COEtapeCalculTaxe();
                    ect.setSession((BSession) session);
                    ect.setIdEtape(etape.getIdEtape());
                    ect.setIdCalculTaxe(ct.getIdCalculTaxe());
                    ect.setImputerTaxe(((COParamTaxesViewBean) viewBean).getImputerTaxes());
                    ect.add();
                } else {
                    viewBean.setMessage(((BSession) session).getLabel("PARAM_TAXE_ETAPE_INEXISTANTE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._add(viewBean, action, session);
        }
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof COParamTaxesViewBean) {

            String idEtape = ((COParamTaxesViewBean) viewBean).getIdEtape();
            String idTaxe = ((COParamTaxesViewBean) viewBean).getIdTaxe();

            COTrancheTaxeManager mgr = new COTrancheTaxeManager();
            mgr.setSession((BSession) session);
            mgr.setForIdCalculTaxe(idTaxe);
            mgr.find();
            if (mgr.isEmpty()) {
                COEtapeCalculTaxe calc = new COEtapeCalculTaxe();
                calc.setSession((BSession) session);
                calc.setIdCalculTaxe(idTaxe);
                calc.setIdEtape(idEtape);
                calc.retrieve();

                if (!calc.isNew()) {
                    calc.delete();
                } else {
                    viewBean.setMessage(((BSession) session).getLabel("PARAM_TAXE_ENTITY_INEXISTANTE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                COCalculTaxe ct = new COCalculTaxe();
                ct.setSession((BSession) session);
                ct.setIdCalculTaxe(idTaxe);
                ct.retrieve();

                if (!ct.isNew()) {
                    ct.delete();
                } else {
                    viewBean.setMessage(((BSession) session).getLabel("PARAM_TAXE_ENTITY_INEXISTANTE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            } else {
                viewBean.setMessage(((BSession) session).getLabel("PARAM_TAXE_SUPPRESSION_IMPOSSIBLE"));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._delete(viewBean, action, session);
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // on ne fait rien, car viewBean bidon (COParamTaxesViewBean)
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof COParamTaxesViewBean) {
            String idEtape = ((COParamTaxesViewBean) viewBean).getIdEtape();
            String idTaxe = ((COParamTaxesViewBean) viewBean).getIdTaxe();
            try {
                COCalculTaxe ct = new COCalculTaxe();
                ct.setSession((BSession) session);
                ct.setIdCalculTaxe(idTaxe);
                ct.retrieve();
                ct.setIdRubrique(((COParamTaxesViewBean) viewBean).getIdRubrique());
                ct.setTypeTaxe(((COParamTaxesViewBean) viewBean).getTypeTaxe());
                ct.setMontantFixe(((COParamTaxesViewBean) viewBean).getMontantFixe());
                ct.setBaseTaxe(((COParamTaxesViewBean) viewBean).getBaseTaxe());
                ct.setDescriptionFr(((COParamTaxesViewBean) viewBean).getLibelleFR());
                ct.setDescriptionDe(((COParamTaxesViewBean) viewBean).getLibelleDE());
                ct.setDescriptionIt(((COParamTaxesViewBean) viewBean).getLibelleIT());
                ct.setTypeTaxeEtape(((COParamTaxesViewBean) viewBean).getTypeTaxeEtape());
                ct.update();

                COEtapeCalculTaxe calc = new COEtapeCalculTaxe();
                calc.setSession((BSession) session);
                calc.setIdCalculTaxe(idTaxe);
                calc.setIdEtape(idEtape);
                calc.retrieve();

                calc.setImputerTaxe(((COParamTaxesViewBean) viewBean).getImputerTaxes());
                calc.update();

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._update(viewBean, action, session);
        }
    }
}
