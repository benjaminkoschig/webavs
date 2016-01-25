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
import globaz.leo.process.LEGenererEtapesSuivantes;
import globaz.leo.vb.process.LEGenererEtapesSuivantesViewBean;
import java.util.ArrayList;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEtapesSuivantesHelper extends FWHelper {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_start(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    /*
     * 
     * listeEtapes.setForCategories(getCategorie());
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        LEGenererEtapesSuivantesViewBean donneesProcess = (LEGenererEtapesSuivantesViewBean) viewBean;
        ArrayList b = new ArrayList();
        LEGenererEtapesSuivantes etapes = new LEGenererEtapesSuivantes();

        etapes.setSession((BSession) session);
        etapes.setOrderBy1(donneesProcess.getOrder1());
        etapes.setOrderBy2(donneesProcess.getOrder2());
        etapes.setEMailAddress(donneesProcess.getForEmail());
        b.add(donneesProcess.getForCsFormule1());
        etapes.setCsFormule(b);
        etapes.setDatePriseEnCompte(donneesProcess.getForDateReference());
        etapes.setDateImpression(donneesProcess.getForDateImpression());
        etapes.setCategorie(donneesProcess.getForCategorie());
        etapes.setSimulation(donneesProcess.getForIsSimulation().equals(
                LEGenererEtapesSuivantesViewBean.SIMULATION_TRUE) ? true : false);
        etapes.setCommentaire(donneesProcess.getCommentaire());
        etapes.start();
    }

}
