/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieContratRenteViager extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getContratEntretienViagerMontant());
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

        Boolean isHome = false;

        List<CalculDonneesHome> home = (List<CalculDonneesHome>) context.get(Attribut.DONNEES_HOMES);
        String dateDebut = donnee.getDateDebutDonneeFinanciere();
        String idMembre = donnee.getIdDroitMembreFamille();

        for (CalculDonneesHome donneesHome : home) {
            if (donneesHome.getDateDebutDFH().equals(dateDebut) && donneesHome.getIdMembreFamille().equals(idMembre)
                    && donneesHome.getIsApiFactureParHome()) {
                isHome = true;
                break;
            }
        }
        if (!isHome) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER,
                    donnee.getContratEntretienViagerMontant());
        }

        return resultatExistant;
    }

}
