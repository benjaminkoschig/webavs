package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITIPersonne;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREEnfantEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module vérifiant si le tiers, de sexe masculin, est au bénéfice d'une rente de veuf et si son dernier enfant (le plus
 * jeune), bénéficiant d'une complémentaire de la rente de veuf, aura 18 ans dans le mois courant. Vérifie aussi le le
 * tiers est au bénéfice d'une rente de veuf sans enfant. <br/>
 * <br/>
 * Motifs de retour positif :
 * <ul>
 * <li>{@link REMotifEcheance#RenteDeVeuf}</li>
 * <li>{@link REMotifEcheance#RenteDeVeufSansEnfant}</li>
 * </ul>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheanceRenteDeVeufTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheanceRenteDeVeuf extends REModuleAnalyseEcheance {

    public REModuleEcheanceRenteDeVeuf(final BISession session, final String moisTraitement) {
        super(session, moisTraitement);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(final IREEcheances echeancesPourUnTiers) {
        // si le tiers est une femme, ou s'il est décédé, aucune échéance à retenir
        if (JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateDecesTiers())
                && !ITIPersonne.CS_HOMME.equals(echeancesPourUnTiers.getCsSexeTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {
            Integer codePrestationRente = Integer.parseInt(uneRenteDuTiers.getCodePrestation());
            if (((REGenresPrestations.RENTE_PRINCIPALE_SURVIVANT_ORDINAIRE == codePrestationRente) || (REGenresPrestations.RENTE_PRINCIPALE_SURVIVANT_EXTRAORDINAIRE == codePrestationRente))
                    && IREPrestationAccordee.CS_ETAT_VALIDE.equals(uneRenteDuTiers.getCsEtat())
                    && JadeStringUtil.isBlankOrZero(uneRenteDuTiers.getDateFinDroit())) {

                if (echeancesPourUnTiers.getEnfantsDuTiers().size() == 0) {
                    // si le tiers n'a pas d'enfant et une rente de veuf, motif : rente de veuf sans enfant
                    return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, REMotifEcheance.RenteDeVeufSansEnfant,
                            echeancesPourUnTiers.getIdTiers());
                }

                // s'il y a des enfants, on vérifie la date de naissance du plus jeune
                if (echeancesPourUnTiers.getEnfantsDuTiers().size() > 0) {
                    IREEnfantEcheances enfantLePlusJeune = echeancesPourUnTiers.getEnfantsDuTiers().last();
                    Integer nbMoisVieEnfant = JadeDateUtil.getNbMonthsBetween(enfantLePlusJeune.getDateNaissance(),
                            "01." + getMoisTraitement());
                    if ((18 * 12) == nbMoisVieEnfant) {
                        // s'il a 18 ans ou plus dans le mois de traitement, motif : rente de veuf
                        return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, REMotifEcheance.RenteDeVeuf,
                                echeancesPourUnTiers.getIdTiers());
                        // Si l'enfant à  plus de 18 ans, on le sort mais sous un autre motif
                    } else if ((18 * 12) < nbMoisVieEnfant) {
                        return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers,
                                REMotifEcheance.RenteDeVeufSansEnfant, echeancesPourUnTiers.getIdTiers());
                    }
                } else {
                    // Il n'y a pas d'enfant
                    if (ITIPersonne.CS_HOMME.equals(echeancesPourUnTiers.getCsSexeTiers())) {
                        return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers,
                                REMotifEcheance.RenteDeVeufSansEnfant, echeancesPourUnTiers.getIdTiers());
                    }
                }
            }
        }
        // si pas de rente de veuf ou pas d'enfant, au bénéfice d'une complémentaire de la rente de veuf,
        // atteignant leurs 18 ans dans le mois de traitement : aucune échéance à retenir
        return REReponseModuleAnalyseEcheance.Faux;
    }
}
