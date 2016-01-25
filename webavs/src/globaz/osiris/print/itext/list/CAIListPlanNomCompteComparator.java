/*
 * Créé le 18 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.print.itext.list;

import globaz.osiris.print.itext.list.CAIListPlanRecouvNonRespectes.CAIPlanEcheanceSection;
import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dostes Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAIListPlanNomCompteComparator implements Comparator, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CAIListPlanNomCompteComparator() {
        super();

    }

    /**
	 * 
	 */
    @Override
    public int compare(Object o1, Object o2) {

        CAIPlanEcheanceSection e1 = (CAIPlanEcheanceSection) o1;
        CAIPlanEcheanceSection e2 = (CAIPlanEcheanceSection) o2;

        if (e1.equals(e2)) { // si même objet
            return 0;
        }
        /** * */

        if (e1.getPlan().getCompteAnnexe().getTiers().getNom()
                .compareTo(e2.getPlan().getCompteAnnexe().getTiers().getNom()) == 0) {
            // e1 nom du compte == e2 nom du compte
            return e1.getPlan().getCompteAnnexe().getIdExterneRole()
                    .compareTo(e2.getPlan().getCompteAnnexe().getIdExterneRole());
        } else {
            // e1 nom du compte <> e2 nom du compte
            return e1.getPlan().getCompteAnnexe().getTiers().getNom()
                    .compareTo(e2.getPlan().getCompteAnnexe().getTiers().getNom());
        }

    }

}
