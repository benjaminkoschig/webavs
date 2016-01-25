/*
 * Créé le 9 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.releve;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.releve.AFGestionReleveViewBean;
import globaz.naos.process.AFGestionReleveProcess;

/**
 * Helper permettant de démarrer le process de suivi des relevés
 * 
 * @author sda
 */
public class AFGestionReleveHelper extends FWHelper {

    public AFGestionReleveHelper() {
        super();
    }

    /**
     * Start le process de suivi des relevés
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // On met les valeurs entrées à l'écran dans le viewBean
        AFGestionReleveViewBean gestionReleveViewBean = (AFGestionReleveViewBean) viewBean;
        AFGestionReleveProcess process = new AFGestionReleveProcess();
        // On passe toutes les données entrées à l'écran dans le process
        process.setISession(session);
        process.setPeriode(gestionReleveViewBean.getPeriode());
        process.setGenererRappel(gestionReleveViewBean.getGenererRappel());
        process.setGenererSommation(gestionReleveViewBean.getGenererSommation());
        process.setGenererTaxation(gestionReleveViewBean.getGenererTaxation());
        process.setEMailAddress(gestionReleveViewBean.getEmail());

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
