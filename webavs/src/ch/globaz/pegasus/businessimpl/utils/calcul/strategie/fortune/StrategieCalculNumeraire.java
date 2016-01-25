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
public class StrategieCalculNumeraire extends StrategieCalculFortune implements IStrategieDessaisissable {

    @Override
    public TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException, NumberFormatException {

        if (!isUsufruit(donnee.getNumeraireCsTypePropriete())
                && !isNuProprietaire(donnee.getNumeraireCsTypePropriete())) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_NUMERAIRES,
                    getMontant(donnee));

        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    private float getMontant(CalculDonneesCC donnee) throws CalculException, NumberFormatException {

        float fraction = checkAmoutAndParseAsFloat(donnee.getNumeraireFractionNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getNumeraireFractionDenominateur());

        float montant = checkAmoutAndParseAsFloat(donnee.getNumeraireMontant());

        return montant * fraction;
    }

}
