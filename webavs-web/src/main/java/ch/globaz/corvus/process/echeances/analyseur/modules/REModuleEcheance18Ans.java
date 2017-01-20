package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module vérifiant si le tiers de l'échéance aura 18 ans de le mois courant, mais n'a pas de période d'étude lui
 * permettant de prolonger son droit à sa rente<br/>
 * <br/>
 * Le motif de retour en cas de réponse positive est : {@link REMotifEcheance#Echeance18ans}<br/>
 * <br/>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheance18AnsTest dans le projet __TestJUnit) si une
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
        // si la date de naissance du tiers n'est pas définie, ou si le tiers est décédé, aucune échéance à retenir
        if (!JadeDateUtil.isGlobazDate(echeancesPourUnTiers.getDateNaissanceTiers())
                || JadeDateUtil.isGlobazDate(echeancesPourUnTiers.getDateDecesTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        REReponseModuleAnalyseEcheance reponseModule18Ans = module18Ans.analyserEcheance(echeancesPourUnTiers);
        REReponseModuleAnalyseEcheance reponseModuleRentePourEnfant = moduleRentePourEnfant
                .analyserEcheance(echeancesPourUnTiers);

        // si le tiers aura 18 ans dans le mois courant et qu'il est au bénéfice d'une rente pour enfant
        if (reponseModule18Ans.isListerEcheance() && reponseModuleRentePourEnfant.isListerEcheance()) {
            return definiReponse18AnsRenteEnCours(echeancesPourUnTiers, reponseModuleRentePourEnfant);
        } else if (!reponseModule18Ans.isListerEcheance() && reponseModuleRentePourEnfant.isListerEcheance()) {
            // si le tiers a déjà eu 18 ans avant le mois courant et qu'il est au bénéfice d'une rente pour enfant
            return definiReponsePlus18AnsRenteEnCours(echeancesPourUnTiers, reponseModule18Ans,
                    reponseModuleRentePourEnfant);
        }

        return REReponseModuleAnalyseEcheance.Faux;
    }

    private REReponseModuleAnalyseEcheance definiReponsePlus18AnsRenteEnCours(IREEcheances echeancesPourUnTiers,
            REReponseModuleAnalyseEcheance reponseModule18Ans,
            REReponseModuleAnalyseEcheance reponseModuleRentePourEnfant) {

        REReponseModuleAnalyseEcheance reponseModuleEtude = moduleEtude.analyserEcheance(echeancesPourUnTiers);

        // si l'âge de 18 ans est dépassé et qu'il ne contient pas de périodes d'études
        if (REMotifEcheance.Interne_AgeVouluDepasseDansMoisCourant.equals(reponseModule18Ans.getMotif())
                && REMotifEcheance.EcheanceEtudesAucunePeriode.equals(reponseModuleEtude.getMotif())) {

            IRERenteEcheances rentePrincipale = reponseModuleRentePourEnfant.getRente();
            // le programme doit prendre en compte un enfant avec une rente en cours qui a 18 ans dépassé dans le mois
            // concerné, qui n'a pas de périodes d'études, mais qui possède une date d'échéance égale au mois renseigné
            if (rentePrincipale != null
                    && (!JadeStringUtil.isEmpty(rentePrincipale.getDateEcheance()) && JadeDateUtil.areDatesEquals("01."
                            + rentePrincipale.getDateEcheance(), "01." + getMoisTraitement()))) {

                return REReponseModuleAnalyseEcheance.Vrai(rentePrincipale, REMotifEcheance.Echeance18ans,
                        echeancesPourUnTiers.getIdTiers());
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }

    private REReponseModuleAnalyseEcheance definiReponse18AnsRenteEnCours(IREEcheances echeancesPourUnTiers,
            REReponseModuleAnalyseEcheance reponseModuleRentePourEnfant) {

        // on vérifie s'il a une période d'étude afin de prolonger son droit
        REReponseModuleAnalyseEcheance reponseModuleEtude = moduleEtude.analyserEcheance(echeancesPourUnTiers);

        if (reponseModuleEtude.isListerEcheance()) {
            IRERenteEcheances rentePrincipale = reponseModuleRentePourEnfant.getRente();

            // le programme ne doit pas prendre en compte un enfant avec une rente en cours qui a 18 ans
            // dans le mois concerné, qui n'a pas de périodes d'études, mais qui possède une date d'échéance
            // dans le futur
            switch (reponseModuleEtude.getMotif()) {
                case EcheanceEtudesAucunePeriode:
                    if (rentePrincipale != null
                            && (!JadeStringUtil.isEmpty(rentePrincipale.getDateEcheance()) && JadeDateUtil
                                    .isDateMonthYearAfter(rentePrincipale.getDateEcheance(), getMoisTraitement()))) {
                        break;
                    }
                case EcheanceFinEtudesDepassees:
                    return REReponseModuleAnalyseEcheance.Vrai(reponseModuleEtude.getRente(),
                            REMotifEcheance.Echeance18ans, echeancesPourUnTiers.getIdTiers());
                default:
                    break;
            }
        }

        return REReponseModuleAnalyseEcheance.Faux;
    }

    @Override
    public void setMoisTraitement(String moisTraitement) {
        super.setMoisTraitement(moisTraitement);

        moduleRentePourEnfant.setMoisTraitement(moisTraitement);
        module18Ans.setMoisTraitement(moisTraitement);
        moduleEtude.setMoisTraitement(JadeDateUtil.addMonths("01." + moisTraitement, 1).substring(3));
    }
}
