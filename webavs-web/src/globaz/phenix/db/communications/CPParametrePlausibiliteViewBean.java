/*
 * Cr�� le 16 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author mmu
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CPParametrePlausibiliteViewBean extends CPParametrePlausibilite implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String typeSelected(int i) {
        if (getType().equals("")) {
            setType("0");
        }
        if (Integer.parseInt(getType()) == i) {
            return "selected=\"selected\"";
        } else {
            return "";
        }
    }

}
