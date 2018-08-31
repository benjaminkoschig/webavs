package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.listes.PTCaissemaladieViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.caissemaladie.GenreListe;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.process.caissemaladie.CaisseMaladieAdmissionProcess;
import ch.globaz.vulpecula.process.caissemaladie.CaisseMaladieDemissionProcess;
import ch.globaz.vulpecula.process.caissemaladie.CaisseMaladieProcess;

public class PTCaissemaladieHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            AffiliationCaisseMaladieService cmService = VulpeculaServiceLocator.getAffiliationCaisseMaladieService();

            PTCaissemaladieViewBean vb = (PTCaissemaladieViewBean) viewBean;
            CaisseMaladieProcess process = getProcess(vb.getListe());
            process.setEMailAddress(vb.getEmail());
            if (!JadeStringUtil.isEmpty(vb.getDate())) {
                process.setDateAnnonce(new Date(vb.getDate()));
            }

            if ("-1".equals(vb.getIdCaisseMaladie())) {
                cmService.checkPeriodValidty(vb.getDateFrom(), vb.getDateTo(), process.getSession());
            }

            if (!JadeStringUtil.isEmpty(vb.getDateFrom())) {
                process.setDateAnnonceFrom(new Date(vb.getDateFrom()).getValue());
            }

            if (JadeStringUtil.isEmpty(vb.getDateTo())) {
                process.setDateAnnonceTo("0");
            } else {
                process.setDateAnnonceTo(new Date(vb.getDateTo()).getValue());
            }

            process.setIdCaisseMaladie(vb.getIdCaisseMaladie());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

    private CaisseMaladieProcess getProcess(String genre) {
        GenreListe genreListe = GenreListe.fromValue(genre);
        switch (genreListe) {
            case ADMISSION:
                return new CaisseMaladieAdmissionProcess();
            case DEMISSION:
                return new CaisseMaladieDemissionProcess();

        }
        throw new IllegalArgumentException("Le type de liste n'est pas valide");
    }
}