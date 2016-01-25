/*
 * Créé le 12 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pavo.db.inscriptions.CIJournal;

/**
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIEcrituresNonRAViewBean extends CIEcriture implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CIEcrituresNonRAViewBean() {
        super();
        // TODO Raccord de constructeur auto-généré

    }

    public String getIdTypeInscription() {
        CIJournal journal = getJournal(null, false);
        return journal.getIdTypeInscription();

    }

}
