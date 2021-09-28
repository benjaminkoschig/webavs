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
public class StrategieBienImmoPrincipal extends StrategieCalculFortune implements IStrategieDessaisissable {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeFortune
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // if (!this.isUsufruit(donnee.getBienImmoPrincipalCSPropriete())
        // && !this.isNuProprietaire(donnee.getBienImmoPrincipalCSPropriete())
        // && !this.isDroitHabitation(donnee.getBienImmoPrincipalCSPropriete())) {

        // fortune du bien prise en compte suelemtn si proprietaite
        if (isProprietaire(donnee.getBienImmoPrincipalCSPropriete())) {

            float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalPartNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalPartDenominateur());

            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurFiscale()) * fraction);
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantDetteHypothecaire()) * fraction);
            if(context.contains(CalculContext.Attribut.REFORME)){
                // Si la valeur du bien est inférieur à sa valeur hypothécaire, on ne tient pas compte de la valeur hypothécaire.
                // Exigence 4.15
                if (checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantDetteHypothecaire()) * fraction < checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurFiscale()) * fraction) {
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY, checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantDetteHypothecaire()) * fraction);
                } else {
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY,
                            checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurFiscale()) * fraction);
                }
            }

        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalPartNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalPartDenominateur());

        return (checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurFiscale()) - checkAmoutAndParseAsFloat(donnee
                .getBienImmoPrincipalMontantDetteHypothecaire())) * fraction;
    }

}
