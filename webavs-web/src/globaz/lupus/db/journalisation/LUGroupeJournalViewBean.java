/*
 * Créé le 5 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JAUtil;
import globaz.jade.log.JadeLogger;
import globaz.journalisation.db.journalisation.access.JOGroupeJournal;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUGroupeJournalViewBean extends JOGroupeJournal implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public boolean checkDate(String date) {
        // on vérifie que la date de réception est correcte !
        if (!JAUtil.isStringEmpty(date)) {
            if (!_checkDate(null, date, "La date de réception n'est pas valide")) {
                JadeLogger.error(this, "La date de réception n'est pas valide !");
                return false;
            }
            return true;
        }
        return false;
    }
}
