package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * <p>
 * Module regardant si le tiers est au bénéfice d'une rente pour enfant (en cours)<br/>
 * N'est pas un module "final", mais regroupe un comportement commun à plusieurs modules finaux
 * </p>
 * <p>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheanceRentePourEnfantTest dans le projet __TestJUnit) si
 * une modification est fait sur ce module.
 * </p>
 * 
 * @author PBA
 */
public class REModuleEcheanceRentePourEnfant extends REModuleAnalyseEcheance {

    public REModuleEcheanceRentePourEnfant(BISession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    /**
     * N'étant pas un module final, si vrai, ne retournera pas de motif (<code>null</code>)
     */
    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {
            // si le code de prestation de la rente principale n'est pas défini, on en vas pas plus loin
            // OU si la date de fin de droit est APRES la date du mois recherché.
            if (JadeStringUtil.isBlankOrZero(uneRenteDuTiers.getCodePrestation())
                    || (JadeDateUtil.isGlobazDateMonthYear(uneRenteDuTiers.getDateFinDroit()) && !JadeDateUtil
                            .isDateMonthYearAfter(uneRenteDuTiers.getDateFinDroit(), getMoisTraitement()))) {
                continue;
            }
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRenteDuTiers
                    .getCodePrestation()));

            if (codePrestation.isRenteComplementairePourEnfant()) {
                return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, null, "");
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
