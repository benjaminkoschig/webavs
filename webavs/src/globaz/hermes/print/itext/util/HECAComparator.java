/*
 * Créé le 28 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.print.itext.util;

import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import java.util.Comparator;

/**
 * @author dostes Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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

        if (e1.equals(e2)) { // si même objet
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
