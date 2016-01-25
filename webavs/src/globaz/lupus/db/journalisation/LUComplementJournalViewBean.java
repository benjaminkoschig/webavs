/*
 * Créé le 5 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.db.journalisation.access.JOComplementJournal;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUComplementJournalViewBean extends JOComplementJournal implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdComplementJournal())) {
            setIdComplementJournal(_incCounter(transaction, "0"));
        }
    }
}
