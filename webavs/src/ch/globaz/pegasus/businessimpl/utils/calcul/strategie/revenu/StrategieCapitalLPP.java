/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import globaz.jade.client.util.JadeStringUtil;
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
public class StrategieCapitalLPP extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return calculMontantInterets(donnee, context, false, "0");
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

        if (!isNuProprietaire(donnee.getCapitalLPPCsTypePropriete())) {
            boolean isSansInteret = donnee.getCapitalLPPIsSansInteret();
            String montantInteret = donnee.getCapitalLPPMontantInterets();
            float montantInteretCalcule = calculMontantInterets(donnee, context, isSansInteret, montantInteret);

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_CAPITAL_LPP,
                    montantInteretCalcule);
        }

        return resultatExistant;
    }

    private float calculMontantInterets(CalculDonneesCC donnee, CalculContext context, boolean isSansInteret,
            String montantInteret) throws CalculException {
        float montantInitial = checkAmountAndParseAsFloat(donnee.getCapitalLPPMontant());

        String strMontantFrais = donnee.getCapitalLPPMontantFrais();
        float montantFrais = 0f;
        if (!JadeStringUtil.isEmpty(strMontantFrais)) {
            montantFrais = Float.parseFloat(strMontantFrais);
        }

        float montantInteretCalcule = calculeInteretFictif(context, montantInitial, isSansInteret, montantInteret);

        montantInteretCalcule = Math.max(montantInteretCalcule - montantFrais, 0);
        return montantInteretCalcule;
    }
}
