package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuAutresCommun implements StrategieCalculFinalisation {
    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_CHOMAGE, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAA,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAM, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAMAL,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_APG, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_IJ,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_ALLOCATIONS_FAMILLIALES,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_PENSION_ALIM_RECUE,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        float somme = 0;
        for (String champ : StrategieFinalRevenuAutresCommun.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        somme += donnee.getSommeEnfants(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_REVENUS);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL, somme));

    }

}
