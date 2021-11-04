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
public class StrategieBienImmoAnnexe extends StrategieCalculFortune implements IStrategieDessaisissable {

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeFortune
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
                                                TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isUsufruit(donnee.getBienImmoAnnexeCsTypePropriete())
                && !isNuProprietaire(donnee.getBienImmoAnnexeCsTypePropriete())) {
            float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartDenominateur());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantValeurVenale()) * fraction);

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantDetteHypothecaire()) * fraction);

            if (context.contains(CalculContext.Attribut.REFORME)) {
                // Si la valeur du bien est inférieur à sa valeur hypothécaire, on ne tient pas compte de la valeur hypothécaire.
                // Exigence 4.15
                if (checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantDetteHypothecaire()) * fraction < checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantValeurVenale()) * fraction) {
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED, checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantDetteHypothecaire()) * fraction);
                } else {
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED,
                            checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantValeurVenale()) * fraction);
                    // Si le montant est plafonné, on ajoute un élément de légende
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_ANNEXE_PLAFONNEE, "1");
                }
            }
        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartDenominateur());

        return (checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantValeurVenale()) - checkAmoutAndParseAsFloat(donnee
                .getBienImmoAnnexeMontantDetteHypothecaire())) * fraction;
    }

}
