package ch.globaz.corvus.process.echeances.analyseur.modules;

import ch.globaz.corvus.business.models.echeances.*;
import ch.globaz.hera.business.constantes.ISFPeriode;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;

import java.util.Iterator;

public class REModuleEchanceEnfantRecueilliGratuitement extends REModuleAnalyseEcheance {

    public REModuleEchanceEnfantRecueilliGratuitement(BSession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {

        Iterator<IREPeriodeEcheances> iteratorDesPeriodes = echeancesPourUnTiers.getPeriodes().iterator();
        while (iteratorDesPeriodes.hasNext()) {
            IREPeriodeEcheances unePeriode = iteratorDesPeriodes.next();

            // si la période n'est pas une période enfant ou enfant conjoint, on l'ignore
            if (ISFPeriode.CS_TYPE_PERIODE_ENFANT.equals(unePeriode.getCsTypePeriode())
                    || ISFPeriode.CS_TYPE_PERIODE_ENFANT_CONJOINT.equals(unePeriode.getCsTypePeriode())) {

                IRERenteEcheances rentePrincipale = REModuleAnalyseEcheanceUtils
                            .getRentePrincipale(echeancesPourUnTiers);

                // si la date de fin de période est < à la date du mois de traitement, l'échéance est dépassée et on remonte le motif
                if (JadeDateUtil.isDateMonthYearBefore(JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()), getMoisTraitement())) {
                    return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, REMotifEcheance.EcheanceEnfantRecueilliGratuitementDepassee,
                            echeancesPourUnTiers.getIdTiers());

                // si la date de fin de période est = à la date du mois de traitement, l'échéance est à effectuer et on remonte le motif
                }  else if (JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()).equals(getMoisTraitement())) {
                    return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, REMotifEcheance.EcheanceEnfantRecueilliGratuitement,
                            echeancesPourUnTiers.getIdTiers());
                }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
