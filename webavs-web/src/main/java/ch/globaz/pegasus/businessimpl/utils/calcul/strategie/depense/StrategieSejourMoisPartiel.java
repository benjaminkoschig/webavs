/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import globaz.jade.client.util.JadeStringUtil;

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
        float idTier = checkAmountAndParseAsFloat(donnee.getIdTierCourant());
        boolean versementDirect = donnee.getSejourMoisPartielVersementDirect();
        float home = checkAmountAndParseAsFloat(donnee.getSejourMoisPartielHome());

        TupleDonneeRapport tupleSejourMoisPartiel = this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL,
                0f);

        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_PRIX_JOURNALIER, prixJournalier);
        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_FRAIS_NOURRITURE, fraisNourriture);
        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_NOMBRE_JOURS, nbJours);
        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_DATE_DEBUT, donnee.getDateDebutDonneeFinanciere());
        this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_DATE_FIN, donnee.getDateFinDonneeFinanciere());
        float montantSejourMoisPartiel = Float.max(prixJournalier - fraisNourriture, 0.0f) * nbJours * NB_MOIS;

        if(donnee.isRequerant()) {
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_MONTANT_REQUERANT, montantSejourMoisPartiel);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME_REQUERANT, home);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_VERSEMENT_DIRECT_REQUERANT, versementDirect ? 1f : 0f);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_IDTIER_REQUERANT, idTier);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_NOMBRE_JOURS_REQUERANT, nbJours);
        } else {
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_MONTANT_CONJOINT, montantSejourMoisPartiel);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME_CONJOINT, home);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_VERSEMENT_DIRECT_CONJOINT, versementDirect ? 1f : 0f);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_IDTIER_CONJOINT, idTier);
            this.getOrCreateChild(tupleSejourMoisPartiel, IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_NOMBRE_JOURS_CONJOINT, nbJours);
        }

        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL, montantSejourMoisPartiel);

        return resultatExistant;
    }

}
