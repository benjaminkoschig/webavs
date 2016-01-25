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
public class StrategieCalculNumeraire extends StrategieCalculRevenu implements IStrategieDessaisissable {
    private float calculeInterets(CalculDonneesCC donnee, CalculContext context, boolean isSansInteret,
            String montantInteret) throws CalculException {
        String csTypePropriete = donnee.getNumeraireCsTypePropriete();
        float montantInteretCalcule = 0f;

        if (!isNuProprietaire(donnee.getNumeraireCsTypePropriete())) {

            float montantInitial = checkAmountAndParseAsFloat(donnee.getNumeraireMontant());

            float fraction = Float.parseFloat(donnee.getNumeraireFractionNumerateur())
                    / Float.parseFloat(donnee.getNumeraireFractionDenominateur());

            montantInteretCalcule = calculeInteretFictif(context, montantInitial, isSansInteret, montantInteret)
                    * fraction;

        }
        return montantInteretCalcule;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return calculeInterets(donnee, context, false, "0");
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        boolean isSansInteret = donnee.getNumeraireIsSansInteret();
        String montantInteret = donnee.getNumeraireMontantInterets();
        float montantInteretCalcule = calculeInterets(donnee, context, isSansInteret, montantInteret);
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_NUMERAIRES,
                montantInteretCalcule);

        return resultatExistant;
    }

}
