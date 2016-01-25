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
public class StrategieTitre extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return calculeMontantInterets(donnee, context, false, "0");
    }

    public float calculeMontantInterets(CalculDonneesCC donnee, CalculContext context, boolean isSansRendement,
            String montantRendement) throws CalculException {
        float fraction = checkAmoutAndParseAsFloat(donnee.getTitreFractionNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getTitreFractionDenominateur());

        float montantInitial = checkAmountAndParseAsFloat(donnee.getTitreMontant());

        float montantInteretCalcule = calculeInteretFictif(context, montantInitial, isSansRendement, montantRendement);

        String strDroitGarde = donnee.getTitreDroitGarde();
        float montantDroitGarde = 0;
        if (!JadeStringUtil.isEmpty(strDroitGarde)) {
            montantDroitGarde = Float.parseFloat(strDroitGarde);
        }
        montantInteretCalcule = Math.max(montantInteretCalcule - montantDroitGarde, 0);
        return montantInteretCalcule * fraction;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.
     * StrategieCalculRevenu#calculeRevenu(java.util.List, java.util.Map,
     * ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee, int)
     */
    @Override
    public TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getTitreCsTypePropriete())) {

            boolean isSansRendement = donnee.getTitreIsSansRendement();
            String montantRendement = donnee.getTitreRendement();
            float montantInteretCalcule = calculeMontantInterets(donnee, context, isSansRendement, montantRendement);

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_RENDEMENT_TITRES,
                    montantInteretCalcule);

        }
        return resultatExistant;
    }

}
