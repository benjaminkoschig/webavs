/*
 * Cr�� le 5 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LUGroupeJournalViewBean extends JOGroupeJournal implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public boolean checkDate(String date) {
        // on v�rifie que la date de r�ception est correcte !
        if (!JAUtil.isStringEmpty(date)) {
            if (!_checkDate(null, date, "La date de r�ception n'est pas valide")) {
                JadeLogger.error(this, "La date de r�ception n'est pas valide !");
                return false;
            }
            return true;
        }
        return false;
    }
}
