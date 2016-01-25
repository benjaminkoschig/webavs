/*
 * Cr�� le 28 f�vr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hermes.print.itext.util;

import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import java.util.Comparator;

/**
 * @author dostes Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class HECAComparator implements Comparator {

    /**
	 * 
	 */
    public HECAComparator() {
        super();

    }

    /**
	 * 
	 */
    @Override
    public int compare(Object o1, Object o2) {

        HEOutputAnnonceViewBean e1 = (HEOutputAnnonceViewBean) o1;
        HEOutputAnnonceViewBean e2 = (HEOutputAnnonceViewBean) o2;

        if (e1.equals(e2)) { // si m�me objet
            return 0;
        }
        /** * */
        if (e1.getNumeroAffilie().compareTo(e2.getNumeroAffilie()) == 0) {
            // e1.numAFF > e2.numAFF
            if (e1.getNumeroAVS().compareTo(e2.getNumeroAVS()) == 0) {
                return e1.getIdAnnonce().compareTo(e2.getIdAnnonce());
            } else {
                return e1.getNumeroAVS().compareTo(e2.getNumeroAVS());
            }
        } else {
            // e1.numAFF < e2.numAFF
            return e1.getNumeroAffilie().compareTo(e2.getNumeroAffilie());
        }

    }

}
