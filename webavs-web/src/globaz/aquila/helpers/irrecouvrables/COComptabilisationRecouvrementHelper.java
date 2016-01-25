package globaz.aquila.helpers.irrecouvrables;

import globaz.aquila.db.irrecouvrables.COComptabilisationRecouvrementViewBean;
import globaz.aquila.process.COProcessComptabiliserRecouvrement;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;

public class COComptabilisationRecouvrementHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        COComptabilisationRecouvrementViewBean comptabilisationViewBean = (COComptabilisationRecouvrementViewBean) viewBean;
        COProcessComptabiliserRecouvrement process = new COProcessComptabiliserRecouvrement();

        try {
            BigDecimal montatARecouvrir = new BigDecimal(JANumberFormatter.deQuote(
                    comptabilisationViewBean.getMontantARecouvrir()).trim());
            process.setMontantARecouvrir(montatARecouvrir);
            process.setEMailAddress(comptabilisationViewBean.getEmail());
            process.setIdCompteAnnexe(comptabilisationViewBean.getIdCompteAnnexe());
            process.setIdCompteIndividuelAffilie(comptabilisationViewBean.getIdCompteIndividuelAffilie());
            process.setRecouvrementCiMap(comptabilisationViewBean.getCiContainer().getRecouvrementCiMap());
            process.setRecouvrementPostesMap(comptabilisationViewBean.getPosteContainer().getRecouvrementPostesMap());
            process.setWantTraiterRecouvrementCi(comptabilisationViewBean.getEffectuerRectificationCI());
            process.setIdSectionsList(comptabilisationViewBean.getIdSectionsList());
            process.setSession((BSession) session);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            comptabilisationViewBean.setMsgType(FWViewBeanInterface.ERROR);
            comptabilisationViewBean.setMessage(e.toString());
        }
    }
}
