package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFusion;

public class StrategieFusionDepenses implements StrategieCalculFusion {

    private static final String[] champs = new String[] {
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES,
            // IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_REVENU,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE,
            // IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_BRUT,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_LOYER_NET,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_CHARGES_FORFAITAIRES,
            // IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND, IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
            IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DIFF_PART_CANTONALE };

    @Override
    public void calcule(TupleDonneeRapport donneeCommun, TupleDonneeRapport donneeAvecEnfants,
            TupleDonneeRapport donneeSeul, TupleDonneeRapport donneeFusionne, CalculContext context, Date dateDebut)
            throws CalculException {

        for (String champ : StrategieFusionDepenses.champs) {

            TupleDonneeRapport tupleOriginal = donneeSeul.getEnfants().get(champ);
            if (tupleOriginal != null) {
                donneeFusionne.addEnfantTuple(tupleOriginal.getClone());
            } else {
                donneeFusionne.addEnfantTuple(new TupleDonneeRapport(champ, 0f));
            }

        }
    }

}
