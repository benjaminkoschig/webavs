/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 */
public class StrategieAssuranceRenteViagere extends StrategieCalculRevenu implements IStrategieDessaisissable {

    private static final float facteurMontant = 0.8f;

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return (checkAmountAndParseAsFloat(donnee.getAssuranceRenteViagereMontant()) * StrategieAssuranceRenteViagere.facteurMontant)
                + checkAmountAndParseAsFloat(donnee.getAssuranceRenteViagereExcedant());
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

        // Calcul montant avec imputation 80%
        float montant = calculRevenu(donnee.getAssuranceRenteViagereMontant(),
                donnee.getAssuranceRenteViagereExcedant());
        // Creation de la cle
        TupleDonneeRapport tupleRente = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_RENTE_VIAGERE, montant);

        // this.getOrCreateChild(tupleRente, IPCValeursPlanCalcul.CLE_REVENU_ASSURANCE_RENTE_VIAGERE_EXCEDANT,
        // this.getFloatValue(donnee.getAssuranceRenteViagereExcedant()));

        return resultatExistant;
    }

    public float calculRevenu(String montantRente, String excedant) {
        float montantSansExcedant = checkAmountAndParseAsFloat(montantRente)
                * StrategieAssuranceRenteViagere.facteurMontant;

        // ajout execdant
        float montant = montantSansExcedant + checkAmountAndParseAsFloat(excedant);
        return montant;
    }

    public static Montant calculRevenu(Montant montantRente, Montant excedant) {
        Montant montantSansExcedant = montantRente.multiply(StrategieAssuranceRenteViagere.facteurMontant);
        // ajout execdant
        return montantSansExcedant.add(excedant);
    }

}
