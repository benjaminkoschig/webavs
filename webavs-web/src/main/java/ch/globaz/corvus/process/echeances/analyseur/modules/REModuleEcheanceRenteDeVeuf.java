package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITIPersonne;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREEnfantEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Module v�rifiant si le tiers, de sexe masculin, est au b�n�fice d'une rente de veuf et si son dernier enfant (le plus
 * jeune), b�n�ficiant d'une compl�mentaire de la rente de veuf, aura 18 ans dans le mois courant. V�rifie aussi le le
 * tiers est au b�n�fice d'une rente de veuf sans enfant. <br/>
 * <br/>
 * Motifs de retour positif :
 * <ul>
 * <li>{@link REMotifEcheance#RenteDeVeuf}</li>
 * <li>{@link REMotifEcheance#RenteDeVeufSansEnfant}</li>
 * </ul>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheanceRenteDeVeufTest dans le projet __TestJUnit) si une
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
        // si le tiers est une femme, ou s'il est d�c�d�, aucune �ch�ance � retenir
        if (JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateDecesTiers())
                && !ITIPersonne.CS_HOMME.equals(echeancesPourUnTiers.getCsSexeTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }
        for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {
            Integer codePrestationRente = Integer.parseInt(uneRenteDuTiers.getCodePrestation());
            if (((REGenresPrestations.RENTE_PRINCIPALE_SURVIVANT_ORDINAIRE == codePrestationRente) || (REGenresPrestations.RENTE_PRINCIPALE_SURVIVANT_EXTRAORDINAIRE == codePrestationRente))
                    && IREPrestationAccordee.CS_ETAT_VALIDE.equals(uneRenteDuTiers.getCsEtat())
                    && JadeStringUtil.isBlankOrZero(uneRenteDuTiers.getDateFinDroit())) {

                if (echeancesPourUnTiers.getEnfantsDuTiers().isEmpty()) {
                    // si le tiers n'a pas d'enfant et une rente de veuf, motif : rente de veuf sans enfant
                    return REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers, REMotifEcheance.RenteDeVeufSansEnfant,
                            echeancesPourUnTiers.getIdTiers());
                }// s'il y a des enfants, on v�rifie la date de naissance du plus jeune
                else {
                    return resolveYoungerChildBirthDate(echeancesPourUnTiers, uneRenteDuTiers);
                }
            }
        }
        // Si pas de rente de veuf ou pas d'enfant, au b�n�fice d'une compl�mentaire de la rente de veuf,
        // atteignant leurs 18 ans dans le mois de traitement : aucune �ch�ance � retenir
        return REReponseModuleAnalyseEcheance.Faux;
    }

    private REReponseModuleAnalyseEcheance resolveYoungerChildBirthDate(final IREEcheances echeancesPourUnTiers,
            IRERenteEcheances uneRenteDuTiers) {
        Integer nbMoisVieEnfant = null;
        REReponseModuleAnalyseEcheance responseAnalyseModuleEcheance = REReponseModuleAnalyseEcheance.Vrai(
                uneRenteDuTiers, REMotifEcheance.RenteDeVeufSansEnfant, echeancesPourUnTiers.getIdTiers());
        // Instanciation et chargement d'une liste temporaire -> Permet de d�piler sans modifier le contenu de la liste
        // originale
        SortedSet<IREEnfantEcheances> tempChildList = new TreeSet<IREEnfantEcheances>();
        tempChildList.addAll(echeancesPourUnTiers.getEnfantsDuTiers());
        while (nbMoisVieEnfant == null && !tempChildList.isEmpty()) {
            IREEnfantEcheances enfantLePlusJeune = tempChildList.last();
            if (JadeStringUtil.isBlankOrZero(enfantLePlusJeune.getDateDeces())) {
                nbMoisVieEnfant = JadeDateUtil.getNbMonthsBetween(enfantLePlusJeune.getDateNaissance(), "01."
                        + getMoisTraitement());
            }
            tempChildList.remove(enfantLePlusJeune);
        }
        if (nbMoisVieEnfant != null && ((18 * 12) == nbMoisVieEnfant)) {
            // s'il a 18 ans dans le mois de traitement, motif : rente de veuf
            responseAnalyseModuleEcheance = REReponseModuleAnalyseEcheance.Vrai(uneRenteDuTiers,
                    REMotifEcheance.RenteDeVeuf, echeancesPourUnTiers.getIdTiers());
            // Si l'enfant a� moins de 18 ans, on ne le sort pas
        } else if (nbMoisVieEnfant != null && ((18 * 12) > nbMoisVieEnfant)) {

            responseAnalyseModuleEcheance = REReponseModuleAnalyseEcheance.Faux;
        }

        return responseAnalyseModuleEcheance;
    }
}
