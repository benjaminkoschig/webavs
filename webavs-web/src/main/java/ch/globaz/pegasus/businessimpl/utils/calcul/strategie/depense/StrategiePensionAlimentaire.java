/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCPensionAlimentaire;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 * 
 */
public class StrategiePensionAlimentaire extends StrategieCalculDepense {

    private static final float NB_MOIS = 12;

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (IPCPensionAlimentaire.CS_TYPE_PENSION_ALIMENTAIRE_VERSEE
                .equals(donnee.getPensionAlimentaireCsTypePension())) {

            Boolean isDeductionEnfant = donnee.getPensionAlimentaireIsDeductionsRenteEnfant();

            float pensionAlimentaireMontant = Float.parseFloat(donnee.getPensionAlimentaireMontant())
                    * StrategiePensionAlimentaire.NB_MOIS;

            if (isDeductionEnfant) {
                pensionAlimentaireMontant = Math
                        .max(pensionAlimentaireMontant
                                - (checkAmountAndParseAsFloat(donnee.getPensionAlimentaireMontantRenteEnfant()) * StrategiePensionAlimentaire.NB_MOIS),
                                0.0f);
            }

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
                    pensionAlimentaireMontant);
        }

        return resultatExistant;
    }

}
