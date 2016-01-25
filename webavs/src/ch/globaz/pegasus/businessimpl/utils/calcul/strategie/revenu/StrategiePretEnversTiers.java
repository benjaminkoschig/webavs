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
public class StrategiePretEnversTiers extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return calculMontantInterets(donnee, context, false, "0");
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu. StrategieCalculRevenu
     * #calculeRevenu(ch.globaz.pegasus.business.models.calcul.CalculDonneesCC,
     * ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext,
     * ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getPretEnversTiersCsTypePropriete())) {
            boolean isSansInteret = donnee.getPretEnversTiersIsSansInteret();
            String montantInteret = donnee.getPretEnversTiersMontantInterets();

            float montantInteretCalcule = calculMontantInterets(donnee, context, isSansInteret, montantInteret);
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_PRETS,
                    montantInteretCalcule);

        }

        return resultatExistant;
    }

    private float calculMontantInterets(CalculDonneesCC donnee, CalculContext context, boolean isSansInteret,
            String montantInteret) throws CalculException {

        float montantInteretCalcule = 0f;

        float montantInitial = checkAmountAndParseAsFloat(donnee.getPretEnversTiersMontant());

        float fraction = checkAmoutAndParseAsFloat(donnee.getPretEnversTiersPartProprieteNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getPretEnversTiersPartProprieteDenominateur());

        montantInteretCalcule = calculeInteretFictif(context, montantInitial, isSansInteret, montantInteret) * fraction;

        return montantInteretCalcule;
    }

}
