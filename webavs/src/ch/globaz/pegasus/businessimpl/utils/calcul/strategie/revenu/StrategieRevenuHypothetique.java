/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 * 
 */
public class StrategieRevenuHypothetique extends StrategieCalculRevenu {

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

            // TODO créer une clé pour montant frais de garde
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU,
                    checkAmountAndParseAsFloat(donnee.getRevenuHypothetiqueMontantFraisGarde()));

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES,
                    donnee.getRevenuHypothetiqueMontantDeductionsSociales());
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP,
                    donnee.getRevenuHypothetiqueMontantDeductionsLPP());
        }
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE, revenuNet);

        return resultatExistant;
    }
}
