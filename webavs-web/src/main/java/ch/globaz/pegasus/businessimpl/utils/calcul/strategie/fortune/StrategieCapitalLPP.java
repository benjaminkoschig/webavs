/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

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
public class StrategieCapitalLPP extends StrategieCalculFortune implements IStrategieDessaisissable {

    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_CAPITAL_LPP, getMontant(donnee));
        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    public Float getMontant(CalculDonneesCC donnee) {
        float fraction = Float.parseFloat(donnee.getCapitalLPPFractionNumerateur())
                / Float.parseFloat(donnee.getCapitalLPPFractionDenominateur());
        return Float.parseFloat(donnee.getCapitalLPPMontant()) * fraction;
    }
}
