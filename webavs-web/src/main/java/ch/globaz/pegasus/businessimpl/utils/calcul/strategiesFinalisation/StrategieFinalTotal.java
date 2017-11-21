package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.PegasusCalculUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee.TypeSeparationCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;

public class StrategieFinalTotal implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        float somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL);
        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC, somme));

        float primeMoyenneAssMaladie = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL);

        // Somme déduite de la prime moyenne d'assurance maladie
        float sommeDeduit = somme + primeMoyenneAssMaladie;
        float pcMensuel = 0;

        final String statusCalcul;

        float diffPartCantonale = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DIFF_PART_CANTONALE);

        // S160704_002 - enregistre le statut de la part fédérale, s'il y a une part cantonale
        if (diffPartCantonale > 0) {
            calcStatutPartFederal(donnee, somme - diffPartCantonale, primeMoyenneAssMaladie);
        }

        // ******************** Traitement du status du calcul
        // excedant de revenu - amal < 0 --> REFUS
        if (sommeDeduit < 0) {

            statusCalcul = IPCValeursPlanCalcul.STATUS_REFUS;
        }
        // Octroi partiel
        else if (somme < 0) {
            // excedant faible de revenu - droit partiel
            // Tous les champs sont déjà fournis. TOTAL_CC_DEDUIT = 0
            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL;
        } else {
            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI;

            // ************* Calcul du montant de la pc mensuelle
            // on divise l'excedent de dépenses par 12
            pcMensuel = (float) Math.ceil(somme / 12);

            // récupération du montant minimale de la pc mensuelle
            float montantMinimale = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.MONTANT_MINIMALE_PC)).getValeurCourante());
            // Si la pc mensuelle plus petite, on plafonne
            if ((pcMensuel > 0) && (pcMensuel < montantMinimale)) {
                pcMensuel = montantMinimale;
            }
            // on ajoute la clé
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL, pcMensuel));

            float partCantonale = diffPartCantonale / 12;

            if (partCantonale > pcMensuel) {
                partCantonale = pcMensuel;
            }
            // RPC (valeur purement fédérales)
            float pcMensuelFederal = pcMensuel - partCantonale;
            if ((pcMensuelFederal > 0) && (pcMensuelFederal < montantMinimale)) {
                pcMensuelFederal = montantMinimale;
                donnee.addEnfantTuple(new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_TOTAL_CC_MENSUEL_MINIMAL_APPLIQUE_FEDERAL, 1));
            }
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_MENSUEL_CALCULE_FEDERAL,
                    pcMensuelFederal));

            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PART_CANTONALE,
                    partCantonale));

            // cas de couple à domicile avec 2 rentes principales
            if (PegasusCalculUtil.isRentesPrincipalesCoupleADom(context)) {

                float partRequerant = (float) Math.ceil(pcMensuel / 2f);
                float partConjoint = pcMensuel - partRequerant;

                donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_DEDUIT_MENSUEL_REQ,
                        partRequerant));
                donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_DEDUIT_MENSUEL_CONJOINT,
                        partConjoint));
            }

        }
        TypeSeparationCC typeSeparation = (TypeSeparationCC) context.get(Attribut.TYPE_SEPARATION_CC);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC, 0f,
                typeSeparation.toString()));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS, 0f, statusCalcul));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT, sommeDeduit));
    }

    // S160704_002 - enregistre le statut de la part fédérale, s'il y a une part cantonale
    private void calcStatutPartFederal(TupleDonneeRapport donnee, float somme, float prime) {
        float sommeDeduit = somme + prime;
        final String statusCalcul;
        if (sommeDeduit < 0) {
            statusCalcul = IPCValeursPlanCalcul.STATUS_REFUS;
        }
        // Octroi partiel
        else if (somme < 0) {
            // excedant faible de revenu - droit partiel
            // Tous les champs sont déjà fournis. TOTAL_CC_DEDUIT = 0
            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL;
        } else {
            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS_FEDERAL, 0f, statusCalcul));

    }

}
