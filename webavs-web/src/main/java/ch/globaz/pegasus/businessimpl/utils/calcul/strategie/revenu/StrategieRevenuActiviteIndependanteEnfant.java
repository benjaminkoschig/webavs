package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuActiviteIndependante;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

public class StrategieRevenuActiviteIndependanteEnfant extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getRevenuActiviteLucrativeIndependanteMontant());
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

        String csGenre = donnee.getRevenuActiviteLucrativeIndependanteCSGenreRevenu();
        resultatExistant.setLegende(csGenre);
        // aiguillage selon le genre de revenu, standard ou agricole/forestier
        if (csGenre.equals(IPCRevenuActiviteIndependante.CS_GENRE_ACTIVITE_AGRICOLE_FORESTIERE)) {
            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_ENFANT,
                    donnee.getRevenuActiviteLucrativeIndependanteMontant());
        } else {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_ENFANT,
                    donnee.getRevenuActiviteLucrativeIndependanteMontant());
        }

        return resultatExistant;
    }

}
