package ch.globaz.corvus.process.echeances.analyseur.modules;

import ch.globaz.corvus.business.models.echeances.*;
import ch.globaz.hera.business.constantes.ISFPeriode;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

import java.util.*;

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
            if ((ISFPeriode.CS_TYPE_PERIODE_ENFANT.equals(unePeriode.getCsTypePeriode())
                    || ISFPeriode.CS_TYPE_PERIODE_ENFANT_CONJOINT.equals(unePeriode.getCsTypePeriode()))
                    && !JadeStringUtil.isEmpty(unePeriode.getDateFin())) {

                // pour chaque rentes de l'échéance
                for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {

                    // si la date de fin de période est < à la date du mois de traitement, l'échéance est dépassée
                    if (JadeDateUtil.isDateMonthYearBefore(JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()), getMoisTraitement())) {
                        // si l'id du tiers complémentaire de la rente accordée correspond à l'id du tiers recueillant de la période, on remonte le motif
                        if (!JadeStringUtil.isEmpty(unePeriode.getIdRecueillant()) && unePeriode.getIdRecueillant().equals(uneRenteDuTiers.getIdTiersComplementaire1())) {
                            return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, REMotifEcheance.EcheanceEnfantRecueilliGratuitementDepassee,
                                    echeancesPourUnTiers.getIdTiers());
                        }
                    // si la date de fin de période est = à la date du mois de traitement, l'échéance est à effectuer et on remonte le motif
                    } else if (JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()).equals(getMoisTraitement())) {
                        // si l'id du tiers complémentaire de la rente accordée correspond à l'id du tiers recueillant de la période, on remonte le motif
                        if (!JadeStringUtil.isEmpty(unePeriode.getIdRecueillant()) && unePeriode.getIdRecueillant().equals(uneRenteDuTiers.getIdTiersComplementaire1())) {
                            return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, REMotifEcheance.EcheanceEnfantRecueilliGratuitement,
                                    echeancesPourUnTiers.getIdTiers());
                        }
                    }
                }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
