/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.leo.process.LEGenererListeFormulesEnAttente;
import globaz.leo.vb.process.LEListeFormulesEnAttenteViewBean;
import java.util.ArrayList;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEListeFormulesEnAttenteHelper extends FWHelper {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_start(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        ArrayList csFormule = new ArrayList();

        LEListeFormulesEnAttenteViewBean donneesProcess = (LEListeFormulesEnAttenteViewBean) viewBean;
        csFormule.add(donneesProcess.getForCsFormule1());
        csFormule.add(donneesProcess.getForCsFormule2());
        // a.add(donneesProcess.getForOrderBy());
        LEGenererListeFormulesEnAttente etapes = new LEGenererListeFormulesEnAttente();
        etapes.setSession((BSession) session);
        etapes.setEMailAddress(donneesProcess.getForEmail());
        // etapes.setCsFormule(donneesProcess.getForCsFormule());
        etapes.setDateReference(donneesProcess.getForDateReference());
        etapes.setOrder1(donneesProcess.getOrder1());
        etapes.setOrder2(donneesProcess.getOrder2());
        etapes.setCsFormule(csFormule);
        etapes.setIsFormatExcel(donneesProcess.getIsFormatExcel());
        etapes.setIsFormatIText(donneesProcess.getIsFormatIText());
        // if(donneesProcess.getForCategorie().length()<=0){
        // etapes.setCategorie("("+ILEConstantes.CS_CATEGORIE_SUIVI_LAA+" OR "+ILEConstantes.CS_CATEGORIE_SUIVI_LPP+")");
        // }
        // else{
        etapes.setCategorie(donneesProcess.getForCategorie());
        // }
        // etapes.setOrderBy(a);
        // etapes.setSimulation(donneesProcess.getForIsSimulation().equals(LEEtapesSuivantesViewBean.SIMULATION_TRUE)?true:false);
        etapes.start();
    }

}
