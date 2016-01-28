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
public class StrategieMarchandiseStock extends StrategieCalculFortune implements IStrategieDessaisissable {

    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isUsufruit(donnee.getNumeraireCsTypePropriete())
                && !isNuProprietaire(donnee.getNumeraireCsTypePropriete())) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_MARCHANDISES_STOCK,
                    getMontant(donnee));
        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        float fraction = checkAmoutAndParseAsFloat(donnee.getMarchandiseStockFractionNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getMarchandiseStockFractionDenominateur());
        return getMontant(donnee) * fraction;
    }

    public float getMontant(CalculDonneesCC donnee) throws CalculException {
        return checkAmoutAndParseAsFloat(donnee.getMarchandiseStockMontant());
    }

}
