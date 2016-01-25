package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * <p>
 * Module vérifiant si une date d'échéance, rentrée à la main par les utilisateurs, échoit dans le mois courant.
 * </p>
 * <p>
 * Motifs de retour positif possibles : {@link REMotifEcheance#EcheanceForcee}
 * </p>
 * <p>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheancesForceesTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * </p>
 * 
 * @author PBA
 */
public class REModuleEcheancesForcees extends REModuleAnalyseEcheance {

    public REModuleEcheancesForcees(BISession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {

        for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {

            if (JadeStringUtil.isBlankOrZero(uneRenteDuTiers.getDateEcheance())) {
                continue;
            }

            String moisEcheance;
            if (JadeDateUtil.isGlobazDate(uneRenteDuTiers.getDateEcheance())) {
                moisEcheance = JadeDateUtil.convertDateMonthYear(uneRenteDuTiers.getDateEcheance());
            } else if (JadeDateUtil.isGlobazDateMonthYear(uneRenteDuTiers.getDateEcheance())) {
                moisEcheance = uneRenteDuTiers.getDateEcheance();
            } else {
                return REReponseModuleAnalyseEcheance.Faux;
            }

            if (moisEcheance.equals(getMoisTraitement()) && JadeStringUtil.isBlank(uneRenteDuTiers.getDateFinDroit())) {
                return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, REMotifEcheance.EcheanceForcee,
                        echeancesPourUnTiers.getIdTiers());
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
