package globaz.vulpecula.helpers.decomptesalaire;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.decomptesalaire.PTAnnoncerMyProdisViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.process.myprodis.AnnoncerCPProcess;
import ch.globaz.vulpecula.process.myprodis.AnnoncerSalairesCotisantsProcess;
import ch.globaz.vulpecula.process.myprodis.AnnoncerSalairesTheoriquesProcessAnnuel;
import ch.globaz.vulpecula.process.myprodis.AnnoncerSalairesTheoriquesProcessMensuel;

public class PTAnnoncerMyProdisHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTAnnoncerMyProdisViewBean vb = (PTAnnoncerMyProdisViewBean) viewBean;
        BProcess process = null;
        switch (vb.getGenreAnnonce()) {
            case THEORIQUES_ANNUEL:
                process = new AnnoncerSalairesTheoriquesProcessAnnuel(vb.getAnneeAnnuel());
                break;
            case THEORIQUES_MENSUEL:
                process = new AnnoncerSalairesTheoriquesProcessMensuel(vb.getAnneeMensuel(), vb.getMoisMensuel());
                break;
            case COTISANTS:
                process = new AnnoncerSalairesCotisantsProcess();
                break;
            case CP:
                process = new AnnoncerCPProcess();
                break;
        }

        if (process != null) {
            try {
                BProcessLauncher.start(process);
            } catch (Exception e) {
                LOGGER.error(e.toString());
            }
        }
    }
}
