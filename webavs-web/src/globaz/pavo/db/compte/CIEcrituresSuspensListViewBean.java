/*
 * Cr�� le 17 janv. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * @author jmc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CIEcrituresSuspensListViewBean extends CIEcritureManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIEcrituresSuspensListViewBean() {
        super();
        // on active le control pour la s�curit� CI par rapport � une
        // affiliation
        setCacherEcritureEnSuspensProtege(1);// 1=true, 0=false
    }

}
