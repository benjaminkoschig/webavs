/*
 * Cr�� le 5 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.globall.db.BEntity;
import globaz.journalisation.db.journalisation.access.JOComplementJournalManager;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LUComplementJournalListViewBean extends JOComplementJournalManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LUComplementJournalViewBean();
    }
}
