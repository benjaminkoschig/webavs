package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFusion;

public class StrategieFusionTotal implements StrategieCalculFusion {

    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        float somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL);
        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC, somme));

        float primeMoyenneAssMaladie = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL);

        float sommeDeduit = somme + primeMoyenneAssMaladie;
        float pcMensuel = 0;
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT, sommeDeduit));

        final String statusCalcul;

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
    }

    @Override
    public void calcule(TupleDonneeRapport donneeCommun, TupleDonneeRapport donneeAvecEnfants,
            TupleDonneeRapport donneeSeul, TupleDonneeRapport donneeFusionne, CalculContext context, Date dateDebut)
            throws CalculException {
        this.calcule(donneeFusionne, context, dateDebut);

    }

}
