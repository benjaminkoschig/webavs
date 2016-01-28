/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul;

/**
 * Classe abstraite de stratégie d'une catégorie de fortune pour un droit.
 * 
 * @author ECO
 * 
 */
public abstract class StrategieCalculFortune extends StrategieCalcul {

    @Override
    public final TupleDonneeRapport calcule(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        return calculeFortune(donnee, context, resultatExistant);
    }

    protected abstract TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException;

}
