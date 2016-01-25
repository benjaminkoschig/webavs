package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesManager;
import globaz.corvus.utils.REEcheancesUtils;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.api.ITIPersonne;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Regroupement des m�thodes stateless utilitaires utilis�es par les modules d'analyse de l'�ch�ancier des rentes
 * 
 * @author PBA
 */
public class REModuleAnalyseEcheanceUtils {

    /**
     * Calcul le nombre de mois de vie de la personne pendant le mois de traitement et compare cet entier avec le nombre
     * de mois repr�sentant l'�ge AVS de cette personne.
     * 
     * @param csSexe
     *            le code syst�me repr�sentant le sexe de la personne ({@link ITIPersonne#CS_FEMME} ou
     *            {@link ITIPersonne#CS_HOMME})
     * @param dateNaissance
     *            une date au format JJ.MM.AAAA repr�sentant la date de naissance de la personne
     * @param moisTraitement
     *            une date au format MM.AAAA repr�sentant le mois dans lequel la liste d'�ch�ances est voulue
     * @return <ul>
     *         <li>1 si la personne a d�j� atteint l'�ge AVS dans le mois de traitement</li>
     *         <li>0 si la personne atteint l'�ge AVS dans le mois de traitement</li>
     *         <li>-1 si la personne n'est pas encore arriv� � l'�ge AVS dans le mois de traitement</li>
     *         </ul>
     */
    public static int compareAgeAvsAgeMoisCourant(String csSexe, String dateNaissance, String moisTraitement) {
        Integer ageAvs = PRTiersHelper.getAgeAvs(csSexe, dateNaissance);
        Integer nbMoisVie = REModuleAnalyseEcheanceUtils.getNbMoisVie(dateNaissance, moisTraitement);
        return nbMoisVie.compareTo(ageAvs * 12);
    }

    private static CodePrestation getCodePrestation(IRERenteEcheances rente) {
        return CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));
    }

    /**
     * Retourne le nombre de mois s'�tant �coul�s entre la date de naissance et le mois de traitement
     * 
     * @param dateNaissance
     *            un date au format JJ.MM.AAAA
     * @param moisTraitement
     *            un date au format MM.AAAA
     * @return un entier repr�sentant le nombre de mois entre les deux dates (peut �tre n�gatif)
     */
    public static Integer getNbMoisVie(String dateNaissance, String moisTraitement) {
        return JadeDateUtil.getNbMonthsBetween("01." + JadeDateUtil.convertDateMonthYear(dateNaissance), "01."
                + moisTraitement);
    }

    /**
     * Recherche et retourne la rente dont le droit a commenc� apr�s la p�riode d'anticipation (la rente dont le d�but
     * du droit commence � l'�ge AVS et dont le champ ann�e d'anticipation n'est pas vide)
     * 
     * @param echeancesPourUnTiers
     * @return
     */
    public static IRERenteEcheances getRenteAnticipeeRecalculee(IREEcheances echeancesPourUnTiers) {

        String dateDebutDroitAvs = JadeDateUtil.convertDateMonthYear(PRTiersHelper.getDateDebutDroitAVS(
                echeancesPourUnTiers.getDateNaissanceTiers(), echeancesPourUnTiers.getCsSexeTiers()));

        // recherche des rentes avec anticipation du tiers
        for (IRERenteEcheances uneRente : echeancesPourUnTiers.getRentesDuTiers()) {
            if (!JadeStringUtil.isBlankOrZero(uneRente.getAnneeAnticipation())) {
                // on recherche la rente qui �tait a �t� recalcul� apr�s la p�riode d'anticipation
                // (la rente qui commence avec le droit AVS du tiers)
                if (dateDebutDroitAvs.equals(uneRente.getDateDebutDroit())) {
                    return uneRente;
                }
            }
        }

        return null;
    }

    public static IRERenteEcheances getRenteAPIAIEnCoursDurantLeMois(IREEcheances echeances, String moisTraitement) {
        Periode periodeTraitement = new Periode(moisTraitement, moisTraitement);

        for (IRERenteEcheances rente : echeances.getRentesDuTiers()) {
            CodePrestation codePrestation = REModuleAnalyseEcheanceUtils.getCodePrestation(rente);

            if (codePrestation.isAPIAI()) {
                Periode periodeRente = new Periode(rente.getDateDebutDroit(), rente.getDateFinDroit());

                if (periodeRente.comparerChevauchement(periodeTraitement) != ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES) {
                    return rente;
                }
            }
        }
        return null;
    }

    public static IRERenteEcheances getRenteAPIAVSEnCoursDurantLeMois(IREEcheances echeancesPourUnTiers,
            String moisTraitement) {

        Periode periodeTraitement = new Periode(moisTraitement, moisTraitement);

        for (IRERenteEcheances rente : echeancesPourUnTiers.getRentesDuTiers()) {
            CodePrestation code = REModuleAnalyseEcheanceUtils.getCodePrestation(rente);

            if (code.isAPIAVS()) {
                Periode periodeRente = new Periode(rente.getDateDebutDroit(), rente.getDateFinDroit());

                if (periodeRente.comparerChevauchement(periodeTraitement) != ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES) {
                    return rente;
                }
            }
        }

        return null;
    }

    /**
     * Cette m�thode s'appuye sur @see RECodePrestationUtils.getRentePrincipale(IREEcheances echeances) Cette
     * indirection est voulue dans le sens ou il est fort probable que cette m�thode soit sujet � des variations
     * d'impl�mentation dans le temps. Par contre la m�thode appel�e est immuable dans le sens ou son implementation ne
     * changera jamais � l'inverse dela composition des groupes qui sont sujets aux �volutions l�gales.
     * 
     * @param echeances
     *            Une IREEcheances contenant les rentes du tiers
     * @return Le code prestation de plus haut niveau ou <code>null</code> si 'echeances' est null ou si aucune rente
     *         trouv�e dans IREEcheance.getRentesDuTiers()
     */
    public static IRERenteEcheances getRentePrincipale(IREEcheances echeances) {
        return REEcheancesUtils.getRentePrincipale(echeances);
    }

    public static IRERenteEcheances getRenteSurvivantPrincipale(IREEcheances echeances) {

        for (IRERenteEcheances uneRente : echeances.getRentesDuTiers()) {

            // CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
            // .getCodePrestation()));

            CodePrestation codePrestation = REModuleAnalyseEcheanceUtils.getCodePrestation(uneRente);

            if (codePrestation.isSurvivant() && codePrestation.isRentePrincipale()) {
                return uneRente;
            }
        }
        return null;
    }

    public static IRERenteEcheances getRenteVieillesseComplementaire(IREEcheances echeances) {
        for (IRERenteEcheances rente : echeances.getRentesDuTiers()) {
            int codePrestation = Integer.parseInt(rente.getCodePrestation());
            switch (codePrestation) {
                case REGenresPrestations.RENTE_COMPLEMENTAIRE_VIEILLESSE_POUR_CONJOINT_EXTRAORDINAIRE:
                case REGenresPrestations.RENTE_COMPLEMENTAIRE_VIEILLESSE_POUR_CONJOINT_ORDINAIRE:
                    return rente;
            }
        }
        return null;
    }

    public static IRERenteEcheances getRenteVieillesseEnCours(IREEcheances echeance, String mois) {
        for (IRERenteEcheances rente : echeance.getRentesDuTiers()) {
            if (REModuleAnalyseEcheanceUtils.isRenteVieillesseEnCours(rente, echeance.getDateNaissanceTiers(),
                    echeance.getCsSexeTiers(), mois)) {
                return rente;
            }
        }
        return null;
    }

    public static boolean hasAgeAvsAvant2012(String csSexe, String dateNaissance) {
        return REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe, dateNaissance, "12.2011") >= 0;
    }

    /**
     * <p>
     * D�fini si un tiers a une rente anticip�e en cours, arrive � l'�ge AVS et dont le droit n'a pas �t� recalcul�
     * </p>
     * 
     * @param echeance
     * @param mois
     * @return
     */
    public static boolean hasRenteAnticipeeDemandantRecalcul(IREEcheances echeance, String moisTraitement) {

        int nbMoisViePourAgeAvs = PRTiersHelper.getAgeAvs(echeance.getCsSexeTiers(), echeance.getDateNaissanceTiers()) * 12;
        int nbMoisVie = REModuleAnalyseEcheanceUtils.getNbMoisVie(echeance.getDateNaissanceTiers(), moisTraitement);

        // si le tiers n'a pas encore atteint l'�ge AVS, il n'est pas n�cessaire d'aller plus loin
        if (nbMoisViePourAgeAvs > nbMoisVie) {
            return false;
        }

        // on recherche les rentes avec ann�e d'anticipation pour le tiers
        Set<IRERenteEcheances> rentesAnticipeesDuTiers = new HashSet<IRERenteEcheances>();
        for (IRERenteEcheances rente : echeance.getRentesDuTiers()) {
            if (!JadeStringUtil.isBlankOrZero(rente.getAnneeAnticipation())) {
                rentesAnticipeesDuTiers.add(rente);
            }
        }

        // si des rentes anticip�es sont pr�sentes, on teste plus en avant
        if (rentesAnticipeesDuTiers.size() > 0) {
            // r�cup�ration du mois suivant le mois d'anniversaire du tiers durant lequel il a atteint l'�ge AVS
            String moisApresAgeAvs = JadeDateUtil.convertDateMonthYear(PRTiersHelper.getDateDebutDroitAVS(
                    echeance.getDateNaissanceTiers(), echeance.getCsSexeTiers()));

            for (IRERenteEcheances uneRenteAnticipee : rentesAnticipeesDuTiers) {
                // si la rente n'est pas valid�e, on l'ignore et on passe � la suivante
                if (!IREPrestationAccordee.CS_ETAT_VALIDE.equals(uneRenteAnticipee.getCsEtat())) {
                    continue;
                }

                // si le d�but du droit commence apr�s le d�but du droit � une rente AVS
                // c'est que la rente anticip�e a �t� recalcul�e -> on retourne faux
                if (moisApresAgeAvs.equals(uneRenteAnticipee.getDateDebutDroit())
                        || JadeDateUtil.isDateMonthYearBefore(moisApresAgeAvs, uneRenteAnticipee.getDateDebutDroit())
                        || JadeDateUtil.isDateBefore("12.2011", moisApresAgeAvs)) { // Avant
                                                                                    // 2012
                                                                                    // les
                                                                                    // rentes
                                                                                    // anticip�es
                    // n'�tait pas forc�ment
                    // recalcul�es
                    return false;
                }
            }
            // si aucune rente anticip�e, en �tat valid�, ne commen�ait apr�s le d�but du droit � une rente AVS
            // un recalcule est n�cessaire -> on retourne vrai
            return true;
        }

        // si aucune rente anticip�e, on retourne faux
        return false;
    }

    public static boolean hasRenteEnCours(IREEcheances echeancesPourUnTiers, String moisTraitement) {

        Periode periodeMoisTraitement = new Periode(moisTraitement, moisTraitement);

        for (IRERenteEcheances uneRente : echeancesPourUnTiers.getRentesDuTiers()) {
            Periode periodeRente = new Periode(uneRente.getDateDebutDroit(), uneRente.getDateFinDroit());
            if (periodeMoisTraitement.comparerChevauchementMois(periodeRente) == ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasRenteVieillesseValideMoisSuivant(IREEcheances echeancesPourUnTiers, String moisTraitement) {

        String moisSuivant = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + moisTraitement, 1));
        Periode periodeMoisSuivant = new Periode(moisSuivant, moisSuivant);

        for (IRERenteEcheances uneRente : echeancesPourUnTiers.getRentesDuTiers()) {
            // CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRente
            // .getCodePrestation()));

            CodePrestation codePrestation = REModuleAnalyseEcheanceUtils.getCodePrestation(uneRente);

            if (codePrestation.isVieillesse()
                    && codePrestation.isRentePrincipale()
                    && REModuleAnalyseEcheanceUtils.isRenteVieillesseEnCours(uneRente,
                            echeancesPourUnTiers.getDateNaissanceTiers(), echeancesPourUnTiers.getCsSexeTiers(),
                            moisSuivant)) {
                Periode periodeRente = new Periode(uneRente.getDateDebutDroit(), uneRente.getDateFinDroit());
                if (periodeMoisSuivant.comparerChevauchementMois(periodeRente) == ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isAgeAvsDansMoisTraitement(String csSexe, String dateNaissance, String moisTraitement) {
        return REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe, dateNaissance, moisTraitement) == 0;
    }

    public static boolean isAgeAvsDepasseDansMoisCourant(String csSexe, String dateNaissance, String moisTraitement) {
        return REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe, dateNaissance, moisTraitement) == 1;
    }

    /**
     * Analyse une rente afin de savoir si elle est ajourn�e, et o� en est cet ajournement.<br/>
     * Motifs possibles de retour positif :
     * <ul>
     * <li>{@link REMotifEcheance#Ajournement}</li>
     * <li>{@link REMotifEcheance#AjournementDepasse}</li>
     * <li>{@link REMotifEcheance#AjournementRevocationDemandee}</li>
     * </ul>
     * Motif possible de retour n�gatif :
     * <ul>
     * <li>{@link REMotifEcheance#Interne_AjournementEnCours}</li>
     * </ul>
     * 
     * @param rente
     *            une rente charg� depuis les �ch�ances ({@link REEcheancesManager})
     * @param dateNaissanceTiers
     *            la date de naissance du tiers au format JJ.MM.AAAA
     * @param csSexeTiers
     *            le code syst�me du sexe du tiers ({@link ITIPersonne#CS_FEMME} ou {@link ITIPersonne#CS_HOMME})
     * @param mois
     *            le mois dans lequel on aimerait savoir l'�tat de l'ajournement, au format MM.AAAA
     * @return
     */
    public static REReponseModuleAnalyseEcheance isRenteAjournee(IRERenteEcheances rente, String dateNaissanceTiers,
            String csSexeTiers, String mois) {
        // si la rente n'est pas ajourn�e, on l'ignore
        if (!IREPrestationAccordee.CS_ETAT_AJOURNE.equals(rente.getCsEtat())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }
        // si une date d'�ch�ance est pr�sente
        if (!JadeStringUtil.isBlankOrZero(rente.getDateEcheance())) {

            // et qu'elle est �gal au mois de traitement
            if (JadeDateUtil.convertDateMonthYear(rente.getDateEcheance()).equals(mois)) {
                return REReponseModuleAnalyseEcheance.Vrai(rente, REMotifEcheance.AjournementRevocationDemandee,
                        rente.getIdTiersBeneficiaire());
            }
            if (JadeDateUtil.isDateMonthYearBefore(JadeDateUtil.convertDateMonthYear(rente.getDateEcheance()), mois)) {
                return REReponseModuleAnalyseEcheance.Vrai(rente,
                        REMotifEcheance.AjournementRevocationDemandeeDepassee, rente.getIdTiersBeneficiaire());
            }
        }

        // si une date de r�vocation est pr�sente, on ignore la rente
        if (!JadeStringUtil.isBlankOrZero(rente.getDateRevocationAjournement())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        Integer moisVieDuTiers = REModuleAnalyseEcheanceUtils.getNbMoisVie(dateNaissanceTiers, mois);
        Integer ageAjournementMaxTiers = (PRTiersHelper.getAgeAvs(csSexeTiers, dateNaissanceTiers) + 5) * 12;

        switch (ageAjournementMaxTiers.compareTo(moisVieDuTiers)) {
            case 0:
                // si le tiers aura l'�ge d'ajournement max dans le mois de traitement
                return REReponseModuleAnalyseEcheance.Vrai(rente, REMotifEcheance.Ajournement,
                        rente.getIdTiersBeneficiaire());
            case -1:
                // si le tiers a d�j� l'�ge d'ajournement max dans le mois de traitement
                return REReponseModuleAnalyseEcheance.Vrai(rente, REMotifEcheance.AjournementDepasse,
                        rente.getIdTiersBeneficiaire());
        }
        return REReponseModuleAnalyseEcheance.Faux(REMotifEcheance.Interne_AjournementEnCours,
                rente.getIdTiersBeneficiaire());
    }

    /**
     * Renseigne si la rente est une rente de vieillesse 'incomplete-switch' est voulut du fait que l'on veut sortir que
     * des motifs sp�cifique
     * 
     * @param rente
     *            La rente � �valuer
     * @param dateNaissanceTiers
     * @param csSexeTiers
     * @param mois
     * @return
     */
    public static boolean isRenteVieillesseEnCours(IRERenteEcheances rente, String dateNaissanceTiers,
            String csSexeTiers, String mois) {

        // si la rente est ajourn�e
        switch (REModuleAnalyseEcheanceUtils.isRenteAjournee(rente, dateNaissanceTiers, csSexeTiers, mois).getMotif()) {
            case Interne_AjournementEnCours:
                return true;
            default:
                break;
        }

        // CodePrestation codePrestation =
        // CodePrestation.getCodePrestation(Integer.parseInt(rente.getCodePrestation()));

        CodePrestation codePrestation = REModuleAnalyseEcheanceUtils.getCodePrestation(rente);

        return codePrestation.isVieillesse()
                && IREPrestationAccordee.CS_ETAT_VALIDE.equals(rente.getCsEtat())
                && (JadeDateUtil.isDateMonthYearAfter(mois, rente.getDateDebutDroit()) || mois.equals(rente
                        .getDateDebutDroit()))
                && (JadeDateUtil.isDateMonthYearBefore(mois, rente.getDateFinDroit()) || JadeStringUtil
                        .isBlankOrZero(rente.getDateFinDroit()));
    }

    /**
     * Retourne vrai si le tiers principale arrive � l'�ge AVS (en tenant compte du sexe) avant la date pass�e en
     * param�tre.
     * 
     * @param echeancesPourUnTiers
     * @param date
     * @return
     */
    public static boolean isSurvenanceDroitAVSAvant(IREEcheances echeancesPourUnTiers, String date) {

        String dateComparaison = null;
        if (JadeDateUtil.isGlobazDate(date)) {
            dateComparaison = JadeDateUtil.convertDateMonthYear(date);
        } else if (JadeDateUtil.isGlobazDateMonthYear(date)) {
            dateComparaison = date;
        } else {
            dateComparaison = "01.2012";
        }

        return REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(echeancesPourUnTiers.getCsSexeTiers(),
                echeancesPourUnTiers.getDateNaissanceTiers(), dateComparaison) == 1;
    }

}
