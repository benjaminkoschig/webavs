/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDonneeFinanciere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieTitre extends StrategieCalculFortune implements IStrategieDessaisissable {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeFortune
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isUsufruit(donnee.getTitreCsTypePropriete())) {

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TITRES, getMontant(donnee));
        }
        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    private float getMontant(CalculDonneesCC donnee) throws CalculException {
        if (IPCDonneeFinanciere.CS_TYPE_PROPRIETE_PROPRIETAIRE.equals(donnee.getTitreCsTypePropriete())) {
            float fraction = checkAmoutAndParseAsFloat(donnee.getTitreFractionNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getTitreFractionDenominateur());
            return checkAmountAndParseAsFloat(donnee.getTitreMontant()) * fraction;
        }
        return 0f;
    }

}
