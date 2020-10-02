package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class StrategieRevenuHypothetiqueConjoint extends StrategieCalculRevenu {

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context, TupleDonneeRapport resultatExistant) throws CalculException {
        Float revenuNet = checkAmoutAndParseAsFloat(donnee.getRevenuHypothetiqueMontantRevenuNet());

        if (revenuNet == 0) {
            revenuNet = checkAmoutAndParseAsFloat(donnee.getRevenuHypothetiqueMontantRevenuBrut());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_CONJOINT,
                    donnee.getRevenuHypothetiqueMontantDeductionsSociales());
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_CONJOINT,
                    donnee.getRevenuHypothetiqueMontantDeductionsLPP());
        }
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_CONJOINT,
                checkAmountAndParseAsFloat(donnee.getRevenuHypothetiqueMontantFraisGarde()));
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_CONJOINT, revenuNet);

        return resultatExistant;
    }
}
