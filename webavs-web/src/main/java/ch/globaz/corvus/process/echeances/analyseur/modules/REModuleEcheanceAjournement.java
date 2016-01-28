package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module vérifiant si l'ajournement arrive à son terme dans le mois courant ou est déjà dépassé.<br/>
 * <br/>
 * Les motifs de retour positif possible sont :
 * <ul>
 * <li>{@link REMotifEcheance#Ajournement}</li>
 * <li>{@link REMotifEcheance#AjournementDepasse}</li>
 * </ul>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheanceAjournementTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheanceAjournement extends REModuleAnalyseEcheance {

    public REModuleEcheanceAjournement(BISession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        // si la date de naissance du tiers n'est pas définie, ou si le tiers a une date de décès
        // aucune échéance à retenir
        if (!JadeDateUtil.isGlobazDate(echeancesPourUnTiers.getDateNaissanceTiers())
                || !JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateDecesTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        // parcours des rentes à la recherche des rentes ajournées
        for (IRERenteEcheances renteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {
            REReponseModuleAnalyseEcheance reponseIsAjournee = REModuleAnalyseEcheanceUtils.isRenteAjournee(
                    renteDuTiers, echeancesPourUnTiers.getDateNaissanceTiers(), echeancesPourUnTiers.getCsSexeTiers(),
                    getMoisTraitement());
            if (reponseIsAjournee.isListerEcheance()) {
                // si une rente est ajournée, motif selon la méthode isRenteAjournee
                return reponseIsAjournee;
            }
        }

        return REReponseModuleAnalyseEcheance.Faux;
    }
}
