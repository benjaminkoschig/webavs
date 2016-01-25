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
public class StrategieBienImmoPrincipal extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontantValeurLocative(donnee);
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String csTypePropriete = donnee.getBienImmoPrincipalCSPropriete();

        // Si pas nu proprietaire
        if (!isNuProprietaire(csTypePropriete)) {
            float fraction = Float.parseFloat(donnee.getBienImmoPrincipalPartNumerateur())
                    / Float.parseFloat(donnee.getBienImmoPrincipalPartDenominateur());
            // on prend toujours en compte la valeur locative
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE,
                    getMontantValeurLocative(donnee));

            // Si ce n'est pas un droit d'habitation, on prend en compte les autres champs
            if (!isDroitHabitation(csTypePropriete)) {
                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
                        checkAmountAndParseAsFloat(donnee.getBienImmoPrincipalMontantLoyersEncaisses()) * fraction);
                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
                        checkAmountAndParseAsFloat(donnee.getBienImmoPrincipalMontantSousLocation()) * fraction);

            }

        }

        return resultatExistant;
    }

    public float getMontantValeurLocative(CalculDonneesCC donnee) {
        float fraction = Float.parseFloat(donnee.getBienImmoPrincipalPartNumerateur())
                / Float.parseFloat(donnee.getBienImmoPrincipalPartDenominateur());
        return checkAmountAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurLocative()) * fraction;
    }

}
