/*
 * Cr�� le 6 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.journalisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.db.journalisation.access.JOReferenceProvenance;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LUReferenceProvenanceViewBean extends JOReferenceProvenance implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdReferenceProvenance())) {
            setIdReferenceProvenance(_incCounter(transaction, "0"));
        }
    }
}
