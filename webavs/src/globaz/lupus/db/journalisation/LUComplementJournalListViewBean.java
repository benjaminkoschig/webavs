/*
 * Créé le 5 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.globall.db.BEntity;
import globaz.journalisation.db.journalisation.access.JOComplementJournalManager;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
