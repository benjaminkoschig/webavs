/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieRevenuActiviteDependante extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getRevenuActiviteLucrativeDependanteMontant())
                - (checkAmountAndParseAsFloat(donnee.getRevenuActiviteLucrativeDependanteDeductionsSociales())
                        + checkAmountAndParseAsFloat(donnee.getRevenuActiviteLucrativeDependanteDeductionsLPP()) + checkAmountAndParseAsFloat(donnee
                            .getRevenuActiviteLucrativeDependanteMontantFraisEffectifs()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        String cleActiviteDependante = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE;
        String cleRevenuActiviteDeductionsSociales = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES;
        String cleRevenuActiviteDeductionsLpp = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP;
        String cleRevenuActiviteFraisObtention = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU;

        this.getOrCreateChild(resultatExistant, cleActiviteDependante,
                donnee.getRevenuActiviteLucrativeDependanteMontant());

        this.getOrCreateChild(resultatExistant, cleRevenuActiviteDeductionsSociales,
                donnee.getRevenuActiviteLucrativeDependanteDeductionsSociales());

        this.getOrCreateChild(resultatExistant, cleRevenuActiviteDeductionsLpp,
                donnee.getRevenuActiviteLucrativeDependanteDeductionsLPP());

        this.getOrCreateChild(resultatExistant, cleRevenuActiviteFraisObtention,
                donnee.getRevenuActiviteLucrativeDependanteMontantFraisEffectifs());

        if(context.contains(CalculContext.Attribut.REFORME)){
            this.getOrCreateChild(resultatExistant, cleRevenuActiviteFraisObtention,
                    donnee.getRevenuActiviteLucrativeDependanteMontantFraisDeGarde());
        }

        return resultatExistant;
    }
}
