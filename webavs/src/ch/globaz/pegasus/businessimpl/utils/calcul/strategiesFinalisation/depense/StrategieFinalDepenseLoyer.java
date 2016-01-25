package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense;

import globaz.jade.context.JadeThread;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.UtilStrategieBienImmobillier;

public class StrategieFinalDepenseLoyer extends UtilStrategieBienImmobillier implements StrategieCalculFinalisation {

    private final static String[] champs = { IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        final int nbPersonnesCalcul = (Integer) context.get(Attribut.NB_PERSONNES);

        final float plafondCouple = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.DEPENSE_LOYER_PLAFOND_COUPLE)).getValeurCourante());
        final float plafondCelibataire = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.DEPENSE_LOYER_PLAFOND_CELIBATAIRE)).getValeurCourante());
        final float plafondFauteuil = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT)).getValeurCourante());
        float plafond = 0f;
        boolean isFauteuilRoulant = false;

        TupleDonneeRapport tupleLoyers = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_LOYERS);
        if (tupleLoyers != null) {
            for (TupleDonneeRapport tupleLoyer : tupleLoyers.getEnfants().values()) {
                float montantBrut = tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_BRUT);
                float montantNet = tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_MONTANT_NET);
                float charges = tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES);
                float forfait = tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_FORFAIT);
                float forfaitFraisChauffage = tupleLoyer
                        .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE);

                // donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPENSE_FORFAIT_CHARGES).addValeur(charges);

                String roleHabitant = tupleLoyer
                        .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_ROLE_PROPRIETAIRE);

                isFauteuilRoulant = isFauteuilRoulant
                        || (tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_IS_FAUTEUIL_ROULANT) == 1);

                float nbHabitants = tupleLoyer.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_NB_PERSONNES);

                float taxeJournalierePNReconnue = tupleLoyer
                        .getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_LOYER_TAXE_JOURNALIERE_PENSION_NON_RECONNUE);
                donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE).addValeur(
                        taxeJournalierePNReconnue);

                float prorata = checkAndCreateProrataForPersonns(nbHabitants, nbPersonnesCalcul, true, roleHabitant);

                donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT).addValeur(
                        montantBrut * prorata);
                donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET).addValeur(
                        montantNet * prorata);

                donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES).addValeur(
                        forfait * prorata);
                donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES).addValeur(
                        charges * prorata);
                donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE).addValeur(
                        forfaitFraisChauffage * prorata);

            }
        }

        // Si cas habitation principale
        TupleDonneeRapport tupleHabitatPrincipal = donnee.getEnfants().get(
                IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);
        Float sommeHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);

        // Si tupleHabitatPrincipal
        if ((tupleHabitatPrincipal != null) && !isHomeEtDroitHabitation(sommeHomes, tupleHabitatPrincipal)) {
            String roleHabitant = tupleHabitatPrincipal
                    .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_BISHP_ROLE_PROPRIETAIRE);
            float montantValLocative = tupleHabitatPrincipal
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE);
            float nbPersonnes = tupleHabitatPrincipal
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE_NBPERSONNES);
            float prorata = checkAndCreateProrataForPersonns(nbPersonnes, nbPersonnesCalcul, false, roleHabitant);

            float forfait = Float.parseFloat(((ControlleurVariablesMetier) (context.get(Attribut.CS_FORFAIT_CHARGES)))
                    .getValeurCourante()) * prorata;
            // Cle valeur locative au prorata nbre personnes
            donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE).addValeur(
                    montantValLocative * prorata);
            // charges forfaitaires
            donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES).addValeur(forfait);

        }
        // calcul du plafond max

        int nbPersonnes = (Integer) context.get(Attribut.NB_PERSONNES);
        if (nbPersonnes > 1) {
            plafond = plafondCouple;
        } else {
            plafond = plafondCelibataire;
        }

        if (isFauteuilRoulant) {
            plafond += plafondFauteuil;
        }
        TupleDonneeRapport tuplePlafond = donnee.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND);
        tuplePlafond.addValeur(plafond);
        tuplePlafond.setLegende(Integer.toString((int) plafond));

        float somme = 0;

        for (String champ : StrategieFinalDepenseLoyer.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE, somme));

        somme = Math.min(somme, plafond);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL, somme));

    }

    private float checkAndCreateProrataForPersonns(float nbHabitants, int nbPersonnesCalcul, boolean forLoyer,
            String roleHabitant) throws CalculException {

        float prorata = 1;
        // Si nombre pas cohérent
        if (nbHabitants < 1) {
            // ":"
            // + nbHabitants;
            // JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), ""));

            throw new CalculException("pegasus.calcul.habit.nombrePersonneLoyer");
        }

        if (nbPersonnesCalcul < 1) {
            // "Le nombre de personne de personne détérminé par le calcul est inférieu, vlaleur trouvé:"
            // + nbPersonnesCalcul
            throw new CalculException("pegasus.calcul.habit.nombrePeronnesCalcul");
        }

        // ON gere le prorata uniquement si c'est le requérant ou le conjoint
        if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(roleHabitant)
                || IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(roleHabitant)) {

            if (nbPersonnesCalcul > nbHabitants) {
                if (forLoyer) {
                    JadeThread.logWarn("", "pegasus.calcul.habitat.loyer.prorata.integrity");
                } else {
                    JadeThread.logWarn("", "pegasus.calcul.habitat.bishp.prorata.integrity");
                }
                prorata = 1;
            } else {
                prorata = nbPersonnesCalcul / nbHabitants;
            }
        }
        return prorata;
    }
}
