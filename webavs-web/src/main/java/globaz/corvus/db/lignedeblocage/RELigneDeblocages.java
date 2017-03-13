/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import java.util.ArrayList;

/**
 * Structure représentant une liste de ligne de déblocage
 * 
 * @author sco
 */
public class RELigneDeblocages extends ArrayList<RELigneDeblocage> {

    private static final long serialVersionUID = 1L;

    RELigneDeblocages getLigneDeblocageByEtat(RELigneDeblocageEtat byEtat) {

        RELigneDeblocages ld = new RELigneDeblocages();
        for (RELigneDeblocage l : this) {
            if (l.getEtat().equals(byEtat)) {
                ld.add(l);
            }
        }

        return ld;
    }

    public RELigneDeblocages getLigneDeblocageComptabilise() {
        return getLigneDeblocageByEtat(RELigneDeblocageEtat.COMPTABILISE);
    }

    public RELigneDeblocages getLigneDeblocageEnregistre() {
        return getLigneDeblocageByEtat(RELigneDeblocageEtat.ENREGISTRE);
    }

    public RELigneDeblocages getLigneDeblocageValide() {
        return getLigneDeblocageByEtat(RELigneDeblocageEtat.VALIDE);
    }

    RELigneDeblocages getLigneDeblocageByType(RELigneDeblocageType byType) {

        RELigneDeblocages ld = new RELigneDeblocages();
        for (RELigneDeblocage l : this) {
            if (l.getType().equals(byType)) {
                ld.add(l);
            }
        }

        return ld;
    }

    public RELigneDeblocages getLigneDeblocageCreancier() {
        return getLigneDeblocageByType(RELigneDeblocageType.CREANCIER);
    }

    public RELigneDeblocages getLigneDeblocageDetteEnCompta() {
        return getLigneDeblocageByType(RELigneDeblocageType.DETTE_EN_COMPTA);
    }

    public RELigneDeblocages getLigneDeblocageImpotsSource() {
        return getLigneDeblocageByType(RELigneDeblocageType.IMPOTS_SOURCE);
    }

    public RELigneDeblocages getLigneDeblocageVersementBeneficaire() {
        return getLigneDeblocageByType(RELigneDeblocageType.VERSEMENT_BENEFICIAIRE);
    }
}
