package globaz.corvus.tools;

import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author SCR
 * 
 */
public class REArcFrenchValidator {

    public REAnnoncesAugmentationModification9Eme validateARC_41_02(REAnnoncesAugmentationModification9Eme arc4102) {

        // Si degré invalidité est vide, AgeDebutInvalidité doit être à blank.
        if (JadeStringUtil.isBlankOrZero(arc4102.getDegreInvalidite())
                && JadeStringUtil.isBlankOrZero(arc4102.getOfficeAICompetent())
                && JadeStringUtil.isBlankOrZero(arc4102.getSurvenanceEvenAssure())
                && JadeStringUtil.isBlankOrZero(arc4102.getCodeInfirmite())) {
            // Mise à blanc en lieu et place de 0
            arc4102.setAgeDebutInvalidite("");
        }

        if (JadeStringUtil.isBlankOrZero(arc4102.getBteMoyennePrisEnCompte())) {
            arc4102.setBteMoyennePrisEnCompte("");
        }
        // On supprime les '.'
        else if (arc4102.getBteMoyennePrisEnCompte() != null) {
            arc4102.setBteMoyennePrisEnCompte(JadeStringUtil.change(arc4102.getBteMoyennePrisEnCompte(), ".", ""));
        }

        if (JadeStringUtil.isBlank(arc4102.getRevenuPrisEnCompte())) {
            // rente extraordinaire. on ne pouvait pas récupérer le code prestation dans tous les cas où cette méthode
            // est utilisée, donc on regarde si le revenue pris en compte est vide (ce qui n'est le cas qu'avec les
            // rentes extraordinaires)
            if (JadeStringUtil.isBlankOrZero(arc4102.getDureeCotManquante48_72())) {
                arc4102.setDureeCotManquante48_72("");
            }
            if (JadeStringUtil.isBlankOrZero(arc4102.getDureeCotManquante73_78())) {
                arc4102.setDureeCotManquante73_78("");
            }
        } else {
            if (JadeStringUtil.isBlankOrZero(arc4102.getDureeCotManquante48_72())) {
                arc4102.setDureeCotManquante48_72("00");
            }
            if (JadeStringUtil.isBlankOrZero(arc4102.getDureeCotManquante73_78())) {
                arc4102.setDureeCotManquante73_78("00");
            }
        }

        return arc4102;
    }

}
