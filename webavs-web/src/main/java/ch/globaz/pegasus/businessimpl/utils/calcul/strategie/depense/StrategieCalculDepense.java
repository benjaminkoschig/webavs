/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul;

/**
 * Classe abstraite de stratégie d'une catégorie de dépense pour un droit.
 * 
 * @author ECO
 * 
 */
public abstract class StrategieCalculDepense extends StrategieCalcul {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calcule(java.util.List,
     * java.util.Map, ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee, int)
     */
    @Override
    public final TupleDonneeRapport calcule(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        return calculeDepense(donnee, context, resultatExistant);
    }

    protected abstract TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException;

    protected float getTauxFraisEntretien(boolean isConstructionMoins10Ans, CalculContext context)
            throws CalculBusinessException, NumberFormatException, CalculException {

        if (isConstructionMoins10Ans) {
            return Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS)).getValeurCourante());
        } else {
            return Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.FRAIS_ENTRETIEN_IMMEUBLE))
                    .getValeurCourante());
        }

    }

}
