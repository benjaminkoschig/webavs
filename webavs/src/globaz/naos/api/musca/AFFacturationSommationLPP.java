/*
 * Créé le 28 avr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFFacturationTaxeSommationLPP;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFFacturationSommationLPP extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**  
	 * 
	 */
    public AFFacturationSommationLPP() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.musca.external.IntModuleFacturation#generer(globaz.musca.api. IFAPassage, globaz.globall.db.BProcess)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        AFFacturationTaxeSommationLPP procFacturation = new AFFacturationTaxeSommationLPP();
        // copier le process parent
        procFacturation.setParentWithCopy(context);
        procFacturation.setContext(context);
        FAPassage myPassage = (FAPassage) passage;
        procFacturation.setPassage(myPassage);

        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setSendCompletionMail(false);
        procFacturation.executeProcess();
        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

}
