/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 */
public class StrategieTaxeJournaliere extends StrategieCalculRevenu {

    public static float calculeRevenu(String montantJournalier, String primeApyer, Integer nbJour) {
        Float montantJournalierLCA = Float.parseFloat(montantJournalier);
        float montantParticipationLCA = nbJour * montantJournalierLCA;
        float primeAnnuelle = 12 * Float.parseFloat(primeApyer);
        return montantParticipationLCA - primeAnnuelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu. StrategieCalculRevenu
     * #calculeRevenu(ch.globaz.pegasus.business.models.calcul.CalculDonneesCC,
     * ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext,
     * ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        Boolean isPartLCA = donnee.getTaxeJournaliereIsParticipationLCA();

        if (isPartLCA) {
            Float montant = StrategieTaxeJournaliere.calculeRevenu(donnee.getTaxeJournaliereMontantJournalierLCA(),
                    donnee.getTaxeJournalierePrimeAPayer(), (Integer) context.get(Attribut.DUREE_ANNEE));
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA, montant);
        }

        return resultatExistant;
    }

}
