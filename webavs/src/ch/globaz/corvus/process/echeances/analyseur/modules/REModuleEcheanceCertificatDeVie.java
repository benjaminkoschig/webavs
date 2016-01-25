package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

/**
 * Module v�rifiant si le tiers poss�de une p�riode de certificat de vie se terminant dans le mois courant.<br/>
 * <br/>
 * Le motif de retour positif est : {@link REMotifEcheance#CertificatDeVie}<br/>
 * <br/>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheanceCertificatDeVieTest dans le projet __TestJUnit) si
 * une modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheanceCertificatDeVie extends REModuleAnalyseEcheance {

    public REModuleEcheanceCertificatDeVie(BISession session, String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        // si le tiers n'a pas de p�riode, on ne va pas plus loin
        if (echeancesPourUnTiers.getPeriodes().size() == 0) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        for (IREPeriodeEcheances unePeriode : echeancesPourUnTiers.getPeriodes()) {
            // si la p�riode n'est pas de type certificat de vie (par exemple �tude) on ignore la p�riode
            if (!ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE.equals(unePeriode.getCsTypePeriode())) {
                continue;
            }
            // v�rifie si la p�riode de cetificat de vie se termine dans le mois de traitement
            if (getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                return REReponseModuleAnalyseEcheance.Vrai(
                        REModuleAnalyseEcheanceUtils.getRentePrincipale(echeancesPourUnTiers),
                        REMotifEcheance.CertificatDeVie, echeancesPourUnTiers.getIdTiers());
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
