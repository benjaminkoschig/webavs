package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
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

        REReponseModuleAnalyseEcheance reponseModule18Ans = module18Ans.analyserEcheance(echeancesPourUnTiers);
        REReponseModuleAnalyseEcheance reponseModuleRentePourEnfant = moduleRentePourEnfant
                .analyserEcheance(echeancesPourUnTiers);

        // si le tiers aura 18 ans dans le mois courant et qu'il est au b�n�fice d'une rente pour enfant
        if (reponseModule18Ans.isListerEcheance() && reponseModuleRentePourEnfant.isListerEcheance()) {
            return definiReponse18AnsRenteEnCours(echeancesPourUnTiers, reponseModuleRentePourEnfant);
        } else if (!reponseModule18Ans.isListerEcheance() && reponseModuleRentePourEnfant.isListerEcheance()) {
            // si le tiers a d�j� eu 18 ans avant le mois courant et qu'il est au b�n�fice d'une rente pour enfant
            return definiReponsePlus18AnsRenteEnCours(echeancesPourUnTiers, reponseModule18Ans,
                    reponseModuleRentePourEnfant);
        }

        return REReponseModuleAnalyseEcheance.Faux;
    }

    private REReponseModuleAnalyseEcheance definiReponsePlus18AnsRenteEnCours(IREEcheances echeancesPourUnTiers,
            REReponseModuleAnalyseEcheance reponseModule18Ans,
            REReponseModuleAnalyseEcheance reponseModuleRentePourEnfant) {

        REReponseModuleAnalyseEcheance reponseModuleEtude = moduleEtude.analyserEcheance(echeancesPourUnTiers);

        // si l'�ge de 18 ans est d�pass� et qu'il ne contient pas de p�riodes d'�tudes
        if (REMotifEcheance.Interne_AgeVouluDepasseDansMoisCourant.equals(reponseModule18Ans.getMotif())
                && REMotifEcheance.EcheanceEtudesAucunePeriode.equals(reponseModuleEtude.getMotif())) {

            IRERenteEcheances rentePrincipale = reponseModuleRentePourEnfant.getRente();
            // le programme doit prendre en compte un enfant avec une rente en cours qui a 18 ans d�pass� dans le mois
            // concern�, qui n'a pas de p�riodes d'�tudes, mais qui poss�de une date d'�ch�ance �gale au mois renseign�
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

        // on v�rifie s'il a une p�riode d'�tude afin de prolonger son droit
        REReponseModuleAnalyseEcheance reponseModuleEtude = moduleEtude.analyserEcheance(echeancesPourUnTiers);

        if (reponseModuleEtude.isListerEcheance()) {
            IRERenteEcheances rentePrincipale = reponseModuleRentePourEnfant.getRente();

            // le programme ne doit pas prendre en compte un enfant avec une rente en cours qui a 18 ans
            // dans le mois concern�, qui n'a pas de p�riodes d'�tudes, mais qui poss�de une date d'�ch�ance
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
