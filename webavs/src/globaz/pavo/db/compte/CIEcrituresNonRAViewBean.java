/*
 * Cr�� le 12 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pavo.db.inscriptions.CIJournal;

/**
 * @author sda
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
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
        // TODO Raccord de constructeur auto-g�n�r�

    }

    public String getIdTypeInscription() {
        CIJournal journal = getJournal(null, false);
        return journal.getIdTypeInscription();

    }

}
