/*
 * Créé le 20 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.helpers.ventilation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ventilation.CAVPImprimerVentilation;
import globaz.osiris.db.ventilation.CAVPImprimerVentilationViewBean;

/**
 * @author jmc
 */
public class CAVPImprimerVentilationHelper extends FWHelper {
    /**
	 *
	 */
    public CAVPImprimerVentilationHelper() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_start(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CAVPImprimerVentilationViewBean vb = (CAVPImprimerVentilationViewBean) viewBean;
        try {
            CAVPImprimerVentilation process = new CAVPImprimerVentilation();
            process.setForCompteAnnexe(vb.getIdCompteAnnexe());
            process.setForIdSections(vb.getListIdSections());
            process.setEMailAddress(vb.getEMailAddress());
            process.setTypeDeProcedure(vb.getTypeDeProcedure());
            process.setVentilationGlobale(vb.getVentilationGlobale());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
