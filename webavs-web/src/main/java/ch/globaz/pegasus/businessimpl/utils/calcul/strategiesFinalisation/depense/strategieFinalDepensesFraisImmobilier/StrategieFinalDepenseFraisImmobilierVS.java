package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategieFinalDepensesFraisImmobilier;

import java.text.DecimalFormat;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalDepenseFraisImmobilierVS implements StrategieCalculFinalisation {

    // private static final float taux_frais_entretien = 1f / 5f;

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut)
            throws CalculBusinessException, NumberFormatException, CalculException {

        float somme = 0;
        float totalFraisInterets = 0f;
        // float revenusBruts = 0;
        float revenusActiviteAgricole = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE);

        // Si pas de revenu activit� lucrative ind�pendante agricole
        if ((revenusActiviteAgricole) == 0) {
            somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE);
            // revenusBruts = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE);

        }

        // float plafondRevenus = revenusBruts;
        // plafondRevenus += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES);

        // donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_REVENU,
        // plafondRevenus));
        String typeHabitationCS = donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);

        // Si type de propri�t� n'est pas droit d'habitation
        if ((typeHabitationCS == null)
                || ((donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE) != null) && !donnee
                        .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE).equals(
                                IPCBienImmoPrincipal.CS_TYPE_DROIT_HABITATION))) {

            // R�cup�ration du flag bool�en pour savoir si le bine � moins de 10ans
            boolean isConstructionMoinsDixAns = false;

            if (donnee.getEnfants().get(
                    IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_MOINS_DE_10_ANS) != null) {
                float floatAsBoolean = donnee.getEnfants()
                        .get(IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_MOINS_DE_10_ANS)
                        .getValeur();
                isConstructionMoinsDixAns = TupleDonneeRapport.readBoolean(floatAsBoolean);
            }

            Attribut attribut = null;

            if (isConstructionMoinsDixAns) {
                attribut = Attribut.FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS;
            } else {
                attribut = Attribut.FRAIS_ENTRETIEN_IMMEUBLE;
            }

            float tauxFraisEntretien = Float.parseFloat(((ControlleurVariablesMetier) context.get(attribut))
                    .getValeurCourante());

            float legendeTaux = tauxFraisEntretien * 100;

            DecimalFormat floatFormat = new DecimalFormat("#.##");
            String taux = Double.valueOf(floatFormat.format(legendeTaux)) + "%";

            // float taux = Float.parseFloat(((ControlleurVariablesMetier) context.get(attribut)).getValeurCourante());
            // revenusBruts *= taux;

            float fraisEntretien = donnee
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE);
            float montantInterets = donnee
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE);

            if (null != donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE)) {
                donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE)
                        .setLegende(taux);
            }

            // donnee.addEnfantTuple(new TupleDonneeRapport(
            // IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE, revenusBruts, legende));
            totalFraisInterets = fraisEntretien + montantInterets;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL, totalFraisInterets));

    }

}
