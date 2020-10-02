/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author EBKO
 * 
 */
public class StrategieSejourMoisPartiel extends StrategieCalculDepense {

    private static final float NB_MOIS = 12;
    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieSejourMoisPartiel
     * #calculeDepense(ch.globaz.pegasus.business.models.calcul.CalculDonneesCC,
     * ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext,
     * ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport)
     */
    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        float prixJournalier = checkAmountAndParseAsFloat(donnee.getSejourMoisPartielPrixJournalier());
        float fraisNourriture = checkAmountAndParseAsFloat(donnee.getSejourMoisPartielFraisNourriture());
        float nbJours = checkAmountAndParseAsFloat(donnee.getSejourMoisPartielNombreJour());

        TupleDonneeRapport tupleSejourMoisPartiel = this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL,
                0f);

        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_PRIX_JOURNALIER, prixJournalier);
        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_FRAIS_NOURRITURE, fraisNourriture);
        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_NOMBRE_JOURS, nbJours);

        float montantSejourMoisPartiel = Float.max(prixJournalier - fraisNourriture, 0.0f) * nbJours * NB_MOIS;

        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL, montantSejourMoisPartiel);

        return resultatExistant;
    }

}
