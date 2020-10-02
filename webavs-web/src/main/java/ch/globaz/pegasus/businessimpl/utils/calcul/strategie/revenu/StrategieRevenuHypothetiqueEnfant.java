package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class StrategieRevenuHypothetiqueEnfant extends StrategieCalculRevenu {

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
                                               TupleDonneeRapport resultatExistant) throws CalculException {

        Float revenuNet = checkAmoutAndParseAsFloat(donnee.getRevenuHypothetiqueMontantRevenuNet());

        if (revenuNet == 0) {
            revenuNet = checkAmoutAndParseAsFloat(donnee.getRevenuHypothetiqueMontantRevenuBrut());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_ENFANT,
                    checkAmountAndParseAsFloat(donnee.getRevenuHypothetiqueMontantFraisGarde()));

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_ENFANT,
                    donnee.getRevenuHypothetiqueMontantDeductionsSociales());
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_ENFANT,
                    donnee.getRevenuHypothetiqueMontantDeductionsLPP());
        }
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_ENFANT, revenuNet);

        return resultatExistant;
    }

}
