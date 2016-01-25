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
public class StrategieBienImmoAnnexe extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontantValeurLocative(donnee);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.
     * StrategieCalculRevenu#calculeRevenu(java.util.List, java.util.Map,
     * ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee, int)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getBienImmoAnnexeCsTypePropriete())) {
            float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartDenominateur());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE,
                    checkAmountAndParseAsFloat(donnee.getBienImmoAnnexeMontantValeurLocative()) * fraction);
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
                    checkAmountAndParseAsFloat(donnee.getBienImmoAnnexeMontantLoyersEncaisses()) * fraction);
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
                    checkAmountAndParseAsFloat(donnee.getBienImmoAnnexeMontantSousLocation()) * fraction);

        }

        return resultatExistant;
    }

    public float getMontantValeurLocative(CalculDonneesCC donnee) {
        float fraction = Float.parseFloat(donnee.getBienImmoAnnexePartNumerateur())
                / Float.parseFloat(donnee.getBienImmoAnnexePartDenominateur());
        return checkAmountAndParseAsFloat(donnee.getBienImmoAnnexeMontantValeurLocative()) * fraction;
    }
}
