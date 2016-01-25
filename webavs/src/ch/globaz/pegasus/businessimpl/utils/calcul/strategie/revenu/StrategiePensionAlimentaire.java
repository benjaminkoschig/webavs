/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCPensionAlimentaire;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategiePensionAlimentaire extends StrategieCalculRevenu implements IStrategieDessaisissable {

    private static final float NB_MOIS = 12;

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return calculeMontantPension(donnee);
    }

    /**
     * Calcul de la pension alimentaire Une pension alimentaire du est saisie sous un enfant, ce qui impacte son revenu
     * 
     * @param donnee
     * @return
     */
    private float calculeMontantPension(CalculDonneesCC donnee) {
        float pensionAlimentaireMontant = 0f;

        pensionAlimentaireMontant = checkAmountAndParseAsFloat(donnee.getPensionAlimentaireMontant());

        return pensionAlimentaireMontant;
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
        // Une revenu c'est une pensiona alimentzaire due
        if (IPCPensionAlimentaire.CS_TYPE_PENSION_ALIMENTAIRE_DUE.equals(donnee.getPensionAlimentaireCsTypePension())) {
            float pensionAlimentaireMontant = calculeMontantPension(donnee);

            pensionAlimentaireMontant = checkAmountAndParseAsFloat(donnee.getPensionAlimentaireMontant());
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_PENSION_ALIM_RECUE,
                    pensionAlimentaireMontant * StrategiePensionAlimentaire.NB_MOIS);
        }
        return resultatExistant;
    }

}
