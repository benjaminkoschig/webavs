package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFusion;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere.UtilFortune;

public class StrategieFusionTotal implements StrategieCalculFusion {


    @Override
    public void calcule(TupleDonneeRapport donneeCommun, TupleDonneeRapport donneeAvecEnfants,
                        TupleDonneeRapport donneeSeul, TupleDonneeRapport donneeFusionne, CalculContext context, Date dateDebut)
            throws CalculException {
        if (context.contains(Attribut.REFORME)) {
            calculFinalReforme(donneeFusionne, context);
        } else {
            calculFinalAvantReforme(donneeFusionne, context);
        }
    }

    private void calculFinalReforme(TupleDonneeRapport donnee, CalculContext context) throws CalculException {
        float somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL);
        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC, somme));

        float pcMinimale = donnee.getValeurEnfant(IPCValeursPlanCalcul.PC_MINIMALE);
        float primeEffective = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL);

        if ((somme > 0) && (somme < pcMinimale)) {
            if (primeEffective < pcMinimale) {
                somme  = Math.max(primeEffective, somme);
            } else {
                somme = pcMinimale;
            }
        }

        float sommeDeduit;
        if (somme < primeEffective) {
            sommeDeduit = 0;
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.MONTANT_VERSE_CAISSE_MALADIE,
                    somme));
        } else {
            sommeDeduit = somme - primeEffective;
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.MONTANT_VERSE_CAISSE_MALADIE,
                    primeEffective));
        }

        float pcMensuel = 0;
        final String statusCalcul;
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT, sommeDeduit));


        float diffPartCantonale = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DIFF_PART_CANTONALE);

        // S160704_002 - enregistre le statut de la part fédérale, s'il y a une part cantonale
        if (diffPartCantonale > 0) {
            calcStatutPartFederal(donnee, somme - diffPartCantonale, primeEffective);
        }

        boolean refusForce = UtilFortune.isRefusFortune(donnee, context);

        if (somme <= 0 || refusForce) {
            // excedant de revenu - refus
            statusCalcul = IPCValeursPlanCalcul.STATUS_REFUS;
        } else {
            // excedant de dépense - droit complet
            pcMensuel = (float) Math.ceil(sommeDeduit / 12);

            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC, 0f,
                IPCValeursPlanCalcul.STATUS_CALCUL_SEPARE_MALADIE));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS, 0f, statusCalcul));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL, pcMensuel));

        float partCantonale = diffPartCantonale / 12;

        if (partCantonale > pcMensuel) {
            partCantonale = pcMensuel;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PART_CANTONALE,
                partCantonale));
    }

    private void calculFinalAvantReforme(TupleDonneeRapport donnee, CalculContext context) throws CalculException {
        float somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL);
        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC, somme));

        float primeMoyenneAssMaladie = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL);

        float sommeDeduit = somme + primeMoyenneAssMaladie;
        float pcMensuel = 0;
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT, sommeDeduit));

        final String statusCalcul;

        float diffPartCantonale = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DIFF_PART_CANTONALE);

        // S160704_002 - enregistre le statut de la part fédérale, s'il y a une part cantonale
        if (diffPartCantonale > 0) {
            calcStatutPartFederal(donnee, somme - diffPartCantonale, primeMoyenneAssMaladie);
        }

        if (sommeDeduit < 0) {
            // excedant de revenu - refus
            statusCalcul = IPCValeursPlanCalcul.STATUS_REFUS;
        } else if (somme < 0) {
            // excedant faible de revenu - droit partiel
            // Tous les champs sont déjà fournis. TOTAL_CC_DEDUIT = 0
            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL;
        } else {
            // excedant de dépense - droit complet
            pcMensuel = (float) Math.ceil(somme / 12);

            statusCalcul = IPCValeursPlanCalcul.STATUS_OCTROI;
        }
        float montantMinimale = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.MONTANT_MINIMALE_PC)).getValeurCourante());
        if ((pcMensuel > 0) && (pcMensuel < montantMinimale)) {
            pcMensuel = montantMinimale;
        }
        // TypeSeparationCC typeSeparation = (TypeSeparationCC) context.get(Attribut.TYPE_SEPARATION_CC);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC, 0f,
                IPCValeursPlanCalcul.STATUS_CALCUL_SEPARE_MALADIE));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS, 0f, statusCalcul));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL, pcMensuel));

        float partCantonale = diffPartCantonale / 12;

        if (partCantonale > pcMensuel) {
            partCantonale = pcMensuel;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PART_CANTONALE,
                partCantonale));
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
