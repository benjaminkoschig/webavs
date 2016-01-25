/*
 * Créé le 18 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.jade.log.JadeLogger;
import java.util.Properties;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_LEO_REP = "leoRoot";
    public final static String APPLICATION_PREFIX = "LE";
    public final static String DEFAULT_APPLICATION_LEO = "LEO";

    public LEApplication() throws Exception {
        super(DEFAULT_APPLICATION_LEO);
    }

    @Override
    protected void _declareAPI() {
    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        try {
            cache.addFile("LEOMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "LEOMenu.xml non résolu : " + e.toString());
        }
    }

    @Override
    protected void _initializeCustomActions() {
        // actions depuis le RC_LIST (menu popup)
        FWAction.registerActionCustom("leo.envoi.envoi.etapeSuivante", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("leo.envoi.envoi.annulerEtape", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("leo.envoi.envoi.updateDate", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("leo.envoi.envoi.reception", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("leo.envoi.envoi.editerFormuleOk", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("leo.envoi.envoi.afficherRecu", FWSecureConstants.UPDATE);

        // action pour le select formule dans le viewbean des process
        // etapesuivante et formulesenattente
        FWAction.registerActionCustom("leo.process.etapesSuivantes.updateFormuleEtape", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("leo.process.listeFormulesEnAttente.updateFormule", FWSecureConstants.READ);

        // action afficher pour le process etapesuivante
        FWAction.registerActionCustom("leo.process.etapesSuivantes.afficherEtape", FWSecureConstants.UPDATE);

        // action par défaut dont on veut changer les droits (par défaut
        // l'action "executer" demande les droit "READ")
        FWAction.registerActionCustom("leo.process.etapesSuivantes.executer", FWSecureConstants.UPDATE);
    }

    protected void _readProperties(Properties properties) {
    }
}
