package globaz.vulpecula.helpers.is;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.is.PTPrimeNaissanceAFViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.documents.af.DocumentPrimeNaissanceAFPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTPrimeNaissanceAFHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTPrimeNaissanceAFViewBean vb = (PTPrimeNaissanceAFViewBean) viewBean;
        DocumentPrimeNaissanceAFPrinter printer = new DocumentPrimeNaissanceAFPrinter();
        printer.setIdTravailleur(vb.getIdTravailleur());
        printer.setDateNaissance(new Date(vb.getDateNaissance()));
        printer.setNomEnfant(vb.getNomEnfant());
        printer.setEMailAddress(vb.getEmail());
        try {
            BProcessLauncher.start(printer);
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }
}
