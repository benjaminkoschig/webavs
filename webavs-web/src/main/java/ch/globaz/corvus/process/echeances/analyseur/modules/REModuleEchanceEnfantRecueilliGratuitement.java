package ch.globaz.corvus.process.echeances.analyseur.modules;

import ch.globaz.corvus.business.models.echeances.*;
import ch.globaz.hera.business.constantes.ISFPeriode;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;

public class REModuleEchanceEnfantRecueilliGratuitement extends REModuleAnalyseEcheance {

    public REModuleEchanceEnfantRecueilliGratuitement(BSession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {

        for (IREPeriodeEcheances unePeriode : echeancesPourUnTiers.getPeriodes()) {

            // si la période n'est pas une période enfant ou enfant conjoint, on l'ignore
            if (ISFPeriode.CS_TYPE_PERIODE_ENFANT.equals(unePeriode.getCsTypePeriode())
                    || ISFPeriode.CS_TYPE_PERIODE_ENFANT_CONJOINT.equals(unePeriode.getCsTypePeriode())) {

                // si la date de fin de période est < à la date de du mois de traitement, on remonte l'échéance
                if (JadeDateUtil.isDateMonthYearBefore(JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()), getMoisTraitement())) {

                    IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                            .getRentePrincipale(echeancesPourUnTiers);

                    return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, REMotifEcheance.EcheanceEnfantRecueilliGratuitement,
                            echeancesPourUnTiers.getIdTiers());
                }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
