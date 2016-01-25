/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDessaisissementRevenu;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategiesFactory;

/**
 * @author ECO
 * 
 */
public class StrategieDessaisissementRevenu extends StrategieCalculRevenu {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String dateValidite = (String) context.get(Attribut.DATE_DEBUT_PERIODE);

        final String csTypeDF = donnee.getCsTypeDonneeFinanciere();

        float total;
        if (IPCDessaisissementRevenu.CS_TYPE_DONNEE_FINANCIERE.equals(csTypeDF)) {
            // cas de dessaisissement manuel
            total = checkAmountAndParseAsFloat(donnee.getDessaisissementRevenuMontant())
                    - checkAmountAndParseAsFloat(donnee.getDessaisissementRevenuDeductions());
        } else {
            // cas de dessaisissement automatique
            IStrategieDessaisissable strategie = (IStrategieDessaisissable) StrategiesFactory.getRevenuFactory()
                    .getStrategie(csTypeDF, dateValidite);
            total = strategie.calculeMontantDessaisi(donnee, context);
        }
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL, total);

        return resultatExistant;
    }

}