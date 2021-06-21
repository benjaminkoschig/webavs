package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRevenuActiviteIndependante;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

public class StrategieRevenuActiviteIndependanteConjoint extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getRevenuActiviteLucrativeIndependanteMontant());
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
                                               TupleDonneeRapport resultatExistant) throws CalculException {
        String cleRevenuActiviteFraisObtention = IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_CONJOINT;
        String csGenre = donnee.getRevenuActiviteLucrativeIndependanteCSGenreRevenu();
        resultatExistant.setLegende(csGenre);
        // aiguillage selon le genre de revenu, standard ou agricole/forestier
        if (csGenre.equals(IPCRevenuActiviteIndependante.CS_GENRE_ACTIVITE_AGRICOLE_FORESTIERE)) {
            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_CONJOINT,
                    donnee.getRevenuActiviteLucrativeIndependanteMontant());
        } else {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_CONJOINT,
                    donnee.getRevenuActiviteLucrativeIndependanteMontant());
        }
            this.getOrCreateChild(resultatExistant, cleRevenuActiviteFraisObtention,
                    donnee.getRevenuActiviteLucrativeIndependanteMontantFraisDeGarde());
        return resultatExistant;
    }
}
