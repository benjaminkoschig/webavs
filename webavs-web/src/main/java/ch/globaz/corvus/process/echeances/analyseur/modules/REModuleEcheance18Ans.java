package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module v�rifiant si le tiers de l'�ch�ance aura 18 ans de le mois courant, mais n'a pas de p�riode d'�tude lui
 * permettant de prolonger son droit � sa rente<br/>
 * <br/>
 * Le motif de retour en cas de r�ponse positive est : {@link REMotifEcheance#Echeance18ans}<br/>
 * <br/>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheance18AnsTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheance18Ans extends REModuleAnalyseEcheance {

    private REModuleEcheanceSelonAge module18Ans;
    private REModuleEcheanceEtude moduleEtude;
    private REModuleEcheanceRentePourEnfant moduleRentePourEnfant;

    public REModuleEcheance18Ans(BISession session, String moisTraitement) {
        super(session, moisTraitement);

        moduleRentePourEnfant = new REModuleEcheanceRentePourEnfant(session, moisTraitement);
        module18Ans = new REModuleEcheanceSelonAge(session, moisTraitement, 18);
        moduleEtude = new REModuleEcheanceEtude(session, JadeDateUtil.addMonths("01." + moisTraitement, 1).substring(3));
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {
        // si la date de naissance du tiers n'est pas d�finie, ou si le tiers est d�c�d�, aucune �ch�ance � retenir
        if (!JadeDateUtil.isGlobazDate(echeancesPourUnTiers.getDateNaissanceTiers())
                || JadeDateUtil.isGlobazDate(echeancesPourUnTiers.getDateDecesTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        // si le tiers aura 18 ans dans le mois courant et qu'il est au b�n�fice d'une rente pour enfant
        if (module18Ans.analyserEcheance(echeancesPourUnTiers).isListerEcheance()
                && moduleRentePourEnfant.analyserEcheance(echeancesPourUnTiers).isListerEcheance()) {
            // on v�rifie s'il a une p�riode d'�tude afin de prolonger son droit
            REReponseModuleAnalyseEcheance reponseModuleEtude = moduleEtude.analyserEcheance(echeancesPourUnTiers);
            if (reponseModuleEtude.isListerEcheance()) {
                switch (reponseModuleEtude.getMotif()) {
                    case EcheanceEtudesAucunePeriode:
                    case EcheanceFinEtudesDepassees:
                        return REReponseModuleAnalyseEcheance.Vrai(reponseModuleEtude.getRente(),
                                REMotifEcheance.Echeance18ans, echeancesPourUnTiers.getIdTiers());
                    default:
                        break;
                }
            }
            return REReponseModuleAnalyseEcheance.Faux;
        } else {
            // si le tiers n'a pas 18 ans dans le mois de traitement, ou n'est pas au b�n�fice d'une rente pour enfant,
            // aucune �ch�ance � retenir
            return REReponseModuleAnalyseEcheance.Faux;
        }
    }

    @Override
    public void setMoisTraitement(String moisTraitement) {
        super.setMoisTraitement(moisTraitement);

        moduleRentePourEnfant.setMoisTraitement(moisTraitement);
        module18Ans.setMoisTraitement(moisTraitement);
        moduleEtude.setMoisTraitement(JadeDateUtil.addMonths("01." + moisTraitement, 1).substring(3));
    }
}
