package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuSuppressionSeul implements StrategieCalculFinalisation {
    private final static String[] champs = {
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_TOTAL,
            // IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAA,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAM,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AUTRE_API,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAA,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_PRIVEE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_3EME_PILLIER,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAM,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_AUTRE_RENTE,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_CHOMAGE,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAA,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAM,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAMAL,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_APG,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_IJ,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_ALLOCATIONS_FAMILLIALES,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_PENSION_ALIM_RECUE,
            // IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_REVENUS,
            IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL, // IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL,
            // IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_NUMERAIRES,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_RENDEMENT_TITRES,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_PRETS,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_CAPITAL_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL, // IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES,
            IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        for (String champ : StrategieFinalRevenuSuppressionSeul.champs) {
            TupleDonneeRapport tuple = donnee.getOrCreateEnfant(champ);
            tuple.setValeur(new Float(0f));
        }

    }

}
