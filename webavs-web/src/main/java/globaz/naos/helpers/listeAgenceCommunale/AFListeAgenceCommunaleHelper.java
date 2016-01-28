package globaz.naos.helpers.listeAgenceCommunale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunaleViewBean;
import globaz.naos.process.AFListeExcelAgenceCommunaleProcess;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

public class AFListeAgenceCommunaleHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFListeAgenceCommunaleViewBean vb = (AFListeAgenceCommunaleViewBean) viewBean;

        try {
            AFListeExcelAgenceCommunaleProcess process = new AFListeExcelAgenceCommunaleProcess();
            process.setSession((BSession) session);
            process.setForDate(vb.getDate());
            process.setEMailAddress(vb.getEmail());
            process.setWantCsv(vb.getWantCsv());
            vb.setISession(process.getSession());
            if (!JadeStringUtil.isBlankOrZero(vb.getIdTiersAgence())) {
                process.setForIdTiersAgence(vb.getIdTiersAgence());
                BProcessLauncher.start(process);
            } else {
                TIAdministrationManager agenceManager = new TIAdministrationManager();
                agenceManager.setSession((BSession) session);
                agenceManager.setForGenreAdministration(CodeSystem.GENRE_ADMIN_AGENCE_COMMUNALE);
                agenceManager.orderByCodeAdministration();
                agenceManager.find(BManager.SIZE_NOLIMIT);
                if (agenceManager.size() > 0) {
                    for (int i = 0; i < agenceManager.size(); i++) {
                        process.setForIdTiersAgence(((TIAdministrationViewBean) agenceManager.getEntity(i))
                                .getIdTiersAdministration());
                        BProcessLauncher.start(process);
                    }
                }
                process.setForIdTiersAgenceVide(new Boolean(true));
                process.setForIdTiersAgence("");
                BProcessLauncher.start(process);

            }

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
