package globaz.prestation.acor;

import globaz.globall.db.BSession;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.webavs.common.CommonProperties;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class PRACORConst {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // etat civil
    public static final String CA_CELIBATAIRE = "1";
    public static final String CA_CHAINE_VIDE = " ";
    public static final String CA_CODE_3_VIDE = "000";
    // champs par defaut pour des valeurs nulles
    public static final String CA_DATE_VIDE = "00000000";
    public static final String CA_DATE_VIDE_6POS = "000000";
    public static final String CA_DIVORCE = "4";
    public static final String CA_ENTIER_VIDE = "0";
    // boolean
    public static final String CA_FAUX = "F";

    public static final String CA_FEMME = "f";
    // les genres de cartes APG
    public static final String CA_GENRE_CARTE_DEMANDE = "1";
    public static final String CA_GENRE_CARTE_DUPLICATA = "2";
    public static final String CA_GENRE_CARTE_RESTITUTION = "4";

    public static final String CA_GENRE_CARTE_RETROACTIF = "3";
    // CA = CODES SPECIFIQUES A ACOR
    // sexe
    public static final String CA_HOMME = "h";
    public static final String CA_LIEN_DIVORCE = "4";

    // le type de lien familial
    public static final String CA_LIEN_MARIE = "2";

    public static final String CA_LIEN_SEPARE = "5";
    public static final String CA_LIEN_VEUF = "3";

    public static final String CA_LPART_DECES = "8";
    public static final String CA_LPART_DISSOUS = "7";
    public static final String CA_LPART_ENREGISTRE = "6";
    public static final String CA_MARIE = "2";
    public static final String CA_MONTANT_ASSISTANCE = "4";
    public static final String CA_MONTANT_BASE = "2";
    public static final String CA_MONTANT_COTISATIONS = "-1";
    public static final String CA_MONTANT_ENFANT = "3";

    public static final String CA_MONTANT_EXPLOITATION = "5";
    // le type de decompte ij
    public static final String CA_MONTANT_GLOBAL = "1";
    public static final String CA_MONTANT_IMPOTS = "-2";
    public static final String CA_MONTANT_SUPPL_PERS_SEULE = "6";
    public static final String CA_MONTANT_SUPPL_READAPTATION = "7";
    /**
     * @deprecated
     */
    @Deprecated
    public static final String CA_NO_AVS_BIDON_FEMME = "00000500000";
    /**
     * @deprecated
     */
    @Deprecated
    public static final String CA_NO_AVS_BIDON_HOMME = "00000100000";
    /**
     * @deprecated
     */
    @Deprecated
    public static final String CA_NO_AVS_VIDE = "00000000000";
    // pseudo boolean
    public static final String CA_NON = "0";

    public static final String CA_NON_RENSEIGNE = ".";
    public static final String CA_NSS_VIDE = "7560000000000";
    // le code d'origine
    public static final String CA_ORIGINE_INCONNU = "999";

    public static final String CA_OUI = "1";
    public static final String CA_PERIODE_AFFILIATION_ASSURANCE_FACULTATIVE = "af";

    public static final String CA_PERIODE_ASSURANCE_ETRANGERE = "ae";
    public static final String CA_PERIODE_AU_BENEFICE_IJ = "ij";

    // le type de période
    public static final String CA_PERIODE_DOMICILE_EN_SUISSE = "do";
    public static final String CA_PERIODE_ENFANT_RECUEILLI_GRATUITEMENT = "rc";
    public static final String CA_PERIODE_ETUDE = "et";
    public static final String CA_PERIODE_EXEMPTION_COTISATION = "ex";
    public static final String CA_PERIODE_GARDE_ETC_BTE = "be";
    public static final String CA_PERIODE_NATIONALITE_SUISSE = "na";

    public static final String CA_PERIODE_REFUS_AF = "fa";
    public static final String CA_PERIODE_TRAVAIL_EN_SUISSE = "tr";
    public static final String CA_REEL_VIDE = "0.0";
    public static final String CA_SEPARE = "5";
    public static final String CA_STATUT_INDEPENDANT = "2";

    public static final String CA_STATUT_NON_ACTIF = "3";

    // le statut salarial
    public static final String CA_STATUT_SALARIE = "1";
    public static final String CA_STATUT_SALARIE_ET_INDEPENDANT = "4";
    // la suisse
    public static final String CA_SUISSE = "100";

    public static final String CA_TYPE_CALCUL_PREVISIONNEL = "1";
    public static final String CA_TYPE_CALCUL_PROVISOIRE = "2";

    // type de calcul
    public static final String CA_TYPE_CALCUL_STANDARD = "0";
    // type de demande de prestation
    public static final String CA_TYPE_DEMANDE_APG = "a";
    public static final String CA_TYPE_DEMANDE_COMMUNICATION_PER = "c";
    public static final String CA_TYPE_DEMANDE_IJ = "j";

    public static final String CA_TYPE_DEMANDE_INVALIDITE = "i";
    public static final String CA_TYPE_DEMANDE_MATERNITE = "m";
    public static final String CA_TYPE_DEMANDE_REMBOURSEMENT = "r";
    public static final String CA_TYPE_DEMANDE_SPLITTING = "l";

    public static final String CA_TYPE_DEMANDE_SURVIVANT = "s";

    public static final String CA_TYPE_DEMANDE_VIEILLESSE = "v";
    // le type d'ij
    public static final String CA_TYPE_IJ_GRANDE = "1";
    public static final String CA_TYPE_IJ_PETITE = "2";

    public static final String CA_TYPE_MESURE_EXTERNE = "2";
    // le type de mesure
    public static final String CA_TYPE_MESURE_INTERNE = "1";

    public static final String CA_TYPE_SALAIRE_4SEMAINES = "4";
    public static final String CA_TYPE_SALAIRE_ANNUEL = "6";

    public static final String CA_TYPE_SALAIRE_HEBDOMADAIRE = "3";
    // type de rémunéeration
    public static final String CA_TYPE_SALAIRE_HORAIRE = "1";
    public static final String CA_TYPE_SALAIRE_JOURNALIER = "2";
    public static final String CA_TYPE_SALAIRE_MENSUEL = "5";
    // le type de versement à l'assuré
    public static final String CA_VERSEMENT_ASSURE = "1";
    public static final String CA_VERSEMENT_MONTANT = "2";
    public static final String CA_VERSEMENT_POURCENTAGE = "3";
    public static final String CA_VEUF = "3";
    public static final String CA_VRAI = "T";

    private static final String CS_CELIBATAIRE = "515001";
    private static final String CS_DIVORCE = "515003";
    public static final String CS_FEMME = "516002";
    // CS = CODES SYSTEMES DU FRAMEWORK
    public static final String CS_HOMME = "516001";
    private static final String CS_LPART_DISSOUS = "515008";
    private static final String CS_LPART_DISSOUS_DECES = "515009";
    private static final String CS_LPART_ENREGISTRE = "515007";
    private static final String CS_LPART_SEPARE_DE_FAIT = "515010";
    private static final String CS_MARIE = "515002";
    private static final String CS_SEPARE = "515005";
    private static final String CS_SEPARE_DE_FAIT = "515006";

    private static final String CS_VEUF = "515004";
    public static final String DOSSIER_DEM_COUR = "dem_cour\\";
    public static final String DOSSIER_IN_HOST = "in_host\\";

    public static String EXECUTABLE_ACOR = "aoappc\\aostart.exe";

    public static final String NF_ANNONCE_PAY = "annonce.pay";
    public static final String NF_ANNONCE_RR = "annonce.rr";

    public static final String NF_ANNONCE_XML = "annonce.xml";
    public static final String NF_ASSURES = "ASSURES";
    public static final String NF_CI = "CI";
    public static final String NF_DEM_GDO = "DEM_GEDO";
    // noms par défaut des fichiers attendus par ACOR
    public static final String NF_DEMANDE = "DEMANDE";
    public static final String NF_ENFANTS = "ENFANTS";

    // bz-NEW_ACOR_IJ
    public static final String NF_EURO_FORM = "EURO_FORM";

    public static final String NF_FAMILLES = "FAMILLES";
    public static final String NF_FCALCUL_XML = "f_calcul.xml";
    public static final String NF_PERIODES = "PERIODES";
    public static final String NF_RENTES = "RENTES";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Transforme un code acor (année anticipation) en code systeme du groupe REANNANTI.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caAnneeAnticipationToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("REANNANTI", caCode);
    }

    /**
     * Transforme un code acor (cas speciaux) en code systeme du groupe RECASSPEC.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCasSpeciauxToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RECASSPEC", caCode);
    }

    /**
     * Transforme un code acor (clé infirmité) en code systeme du groupe REINFIRMIT.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCleInfirmite
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCleInfirmiteToCS(BSession session, String caCleInfirmite) {
        // le code est le code utilisateur
        return session.getSystemCode("REINFIRMIT", caCleInfirmite);
    }

    /**
     * Transforme un code acor (code auxiliaire) en code systeme du groupe RECODEAUXI.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCodeAuxilliaireToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RECODEAUXI", caCode);
    }

    /**
     * Transforme un code acor (code mutation) en code systeme du groupe RECODMUT.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCodeMutationToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RECODMUT", caCode);
    }

    /**
     * Transforme un code acor (code prescription appliquée) en code systeme du groupe REPRESCAPP.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCodePrescriptionAppliqueeToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("REPRESCAPP", caCode);
    }

    /**
     * Transforme un code acor (code supplément veuvage) en code systeme du groupe RESUPPVEUV.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCodeSupplementVeuvageToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RESUPPVEUV", caCode);
    }

    /**
     * Transforme un code acor (code survivant invalide) en code systeme du groupe RECODSURIN.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caCodeSurvivantInvalideToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RECODSURIN", caCode);
    }

    /**
     * Transforme un code acor (droit applique) en code systeme du groupe REREVISION.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caDroitRenteAppliqueToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("REREVISION", caCode);
    }

    /**
     * Transforme un code acor (etat civil) en code systeme du groupe SFECIVIL.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caEtatCivilToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("PYETATCIVI", caCode);
    }

    /**
     * Transforme un code acor (fraction de rente) en code systeme du groupe REFRACTREN.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caFractionRenteToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("REFRACTREN", caCode);
    }

    /**
     * Transforme un code acor (genre de prestation) en code systeme du groupe REGENRPRST.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caGenrePrestationToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("REGENRPRST", caCode);
    }

    /**
     * Transforme un code acor de genre de readaptation en code systeme du groupe IJ_GENRE_READAPTATION.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caGenreReadaptation
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caGenreReadaptationToCS(BSession session, String caGenreReadaptation) {
        // le code est le code utilisateur
        return session.getSystemCode("IJ_GENRE_READAPTATION", caGenreReadaptation);
    }

    /**
     * Transforme un code acor (resultat calcul comparatif) en code systeme du groupe RERESCOMP.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caResultatCalculComparatifToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RERESCOMP", caCode);
    }

    /**
     * Transforme un code acor de statut professionnel en code systeme du groupe IJSTATPRO.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caStatutProfessionnel
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caStatutProfessionnelToCS(BSession session, String caStatutProfessionnel) {
        // le code est le code utilisateur
        return session.getSystemCode("IJSTATPRO", caStatutProfessionnel);
    }

    /**
     * Transforme un code acor de type base en code systeme du groupe IJTYPBASE.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caTypeBase
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caTypeBaseToCS(BSession session, String caTypeBase) {
        // le code est le code utilisateur
        return session.getSystemCode("IJTYPBASE", caTypeBase);
    }

    /**
     * Transforme un code acor (type de calcul comparatif) en code systeme du groupe RETYPCCOMP.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param caCode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String caTypeCalculComparatifToCS(BSession session, String caCode) {
        // le code est le code utilisateur
        return session.getSystemCode("RETYPCCOMP", caCode);
    }

    public static final String csAnneAnticipationToAcor(BSession session, String csAnneeAnticipation) {
        // le code correspond au code utilisateur
        return session.getCode(csAnneeAnticipation);

    }

    /**
     * Retourne le code compatible ACOR pour le canton (code OFAS).
     * 
     * @param csCanton
     *            le code systeme du canton a traduire.
     * 
     * @return le code OFAS du canton ou le code OFAS de l'étranger si inconnu ou vide.
     */
    public static final String csCantonToAcor(String csCanton) {
        if (JadeStringUtil.isIntegerEmpty(csCanton)) {
            return PRACORConst.CA_CODE_3_VIDE;
        }
        return TILocalite.getCodeOFASCanton(csCanton);
    }

    public static final String csEtatCivilHeraForIJToAcor(BSession session, String csEtatCivil) {

        if (JadeStringUtil.isBlankOrZero(csEtatCivil)) {
            return PRACORConst.CA_CELIBATAIRE;
        }

        String res = session.getCode(csEtatCivil);
        // Cas spécial, si Etat civil est séparé de fait ou judiciairement (5 ou 6), il faut retourné le code MARIE (2),
        // car
        // la valeur 5 n'est pas admise pour les IJ pour les annonces 8F (contrairement au rentes ou aux IJ ancienne
        // révision).
        // Idem pour cas de LPART
        if (ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(csEtatCivil)
                || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT.equals(csEtatCivil)) {
            return PRACORConst.CA_MARIE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT.equals(csEtatCivil)
                || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_JUDICIAIREMENT.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_ENREGISTRE;
        } else {
            return res;
        }
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param csEtatCivil
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    public static final String csEtatCivilHeraToAcor(BSession session, String csEtatCivil) {

        return PRACORConst.csEtatCivilHeraToAcorForRentes(session, csEtatCivil);
        // BZ-5083
        // if (JadeStringUtil.isBlankOrZero(csEtatCivil)) {
        // return PRACORConst.CA_CELIBATAIRE;
        // }
        //
        // String res = session.getCode(csEtatCivil);
        // if (JadeStringUtil.isBlankOrZero(csEtatCivil)) {
        // return PRACORConst.CA_CELIBATAIRE;
        // } else {
        // return session.getCode(csEtatCivil);
        // }
    }

    public static final String csEtatCivilHeraToAcorForRentes(BSession session, String csEtatCivil) {

        if (JadeStringUtil.isBlankOrZero(csEtatCivil)) {
            return PRACORConst.CA_CELIBATAIRE;
        }

        String res = session.getCode(csEtatCivil);

        // BZ-5083 Cas spécial, si Etat civil est séparé de fait (5), il faut retourné le code MARIE (2),
        // if (JadeStringUtil.isBlankOrZero(csEtatCivil)) {
        // return PRACORConst.CA_CELIBATAIRE;
        // } else {
        // return session.getCode(csEtatCivil);
        // }
        if (ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(csEtatCivil)) {
            return PRACORConst.CA_MARIE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_ENREGISTRE;
        } else {
            return res;
        }

    }

    /**
     * Retourne le code csEtatCivil pour le code csEtatCivilHera.
     * 
     * @param csEtatCivilHera
     *            le code systeme de l'etat civil a traduire.
     * 
     * @return le code csEtatcivil correspondant ou CS_CELIBATAIRE si inconnu ou vide.
     */
    public static final String csEtatCivilHeraToCsEtatCivil(String csEtatCivilHera) {

        /*
         * Remarque : les états "séparés de fait" n'existe que dans Web@AVS et ne doivent pas être envoyé à la centrale.
         */

        if (ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE.equals(csEtatCivilHera)) {
            return PRACORConst.CS_CELIBATAIRE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE.equals(csEtatCivilHera)) {
            return PRACORConst.CS_DIVORCE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_JUDICIAIREMENT.equals(csEtatCivilHera)) {
            return PRACORConst.CS_LPART_DISSOUS;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equals(csEtatCivilHera)) {
            return PRACORConst.CS_MARIE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE.equals(csEtatCivilHera)) {
            return PRACORConst.CS_LPART_ENREGISTRE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF.equals(csEtatCivilHera)) {
            return PRACORConst.CS_VEUF;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES.equals(csEtatCivilHera)) {
            return PRACORConst.CS_LPART_DISSOUS_DECES;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT.equals(csEtatCivilHera)) {
            return PRACORConst.CS_SEPARE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_JUDICIAIREMENT.equals(csEtatCivilHera)) {
            // n'existe pas à la centrale donc retourne LPart enregistré
            return PRACORConst.CS_LPART_ENREGISTRE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(csEtatCivilHera)) {
            // n'existe pas à la centrale donc retourne Marié
            return PRACORConst.CS_MARIE;
        } else if (ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT.equals(csEtatCivilHera)) {
            // n'existe pas à la centrale (code 10) donc retourne LPart enregistré
            return PRACORConst.CS_LPART_ENREGISTRE;
        } else {
            return PRACORConst.CS_CELIBATAIRE;
        }
    }

    /**
     * Retourne le code comptabile ACOR pour l'etat civil (code RR).
     * 
     * @param csEtatCivil
     *            le code systeme de l'etat civil a traduire.
     * 
     * @return le code ACOR correspondant ou CA_CELIBATAIRE si inconnu ou vide.
     */
    public static final String csEtatCivilToAcor(String csEtatCivil) {
        if (PRACORConst.CS_CELIBATAIRE.equals(csEtatCivil)) {
            return PRACORConst.CA_CELIBATAIRE;
        } else if (PRACORConst.CS_DIVORCE.equals(csEtatCivil)) {
            return PRACORConst.CA_DIVORCE;
        } else if (PRACORConst.CS_MARIE.equals(csEtatCivil) || PRACORConst.CS_SEPARE_DE_FAIT.equals(csEtatCivil)) {
            return PRACORConst.CA_MARIE;
        } else if (PRACORConst.CS_VEUF.equals(csEtatCivil)) {
            return PRACORConst.CA_VEUF;
        } else if (PRACORConst.CS_SEPARE.equals(csEtatCivil)) {
            return PRACORConst.CA_SEPARE;
        } else if (PRACORConst.CS_LPART_DISSOUS.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_DISSOUS;
        } else if (PRACORConst.CS_LPART_DISSOUS_DECES.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_DECES;
        } else if (PRACORConst.CS_LPART_ENREGISTRE.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_ENREGISTRE;
        } else if (PRACORConst.CS_LPART_SEPARE_DE_FAIT.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_ENREGISTRE;
        } else {
            return PRACORConst.CA_CELIBATAIRE;
        }
    }

    public static final String csEtatCivilToAcorForRentes(String csEtatCivil) {
        if (PRACORConst.CS_CELIBATAIRE.equals(csEtatCivil)) {
            return PRACORConst.CA_CELIBATAIRE;
        } else if (PRACORConst.CS_DIVORCE.equals(csEtatCivil)) {
            return PRACORConst.CA_DIVORCE;
        } else if (PRACORConst.CS_MARIE.equals(csEtatCivil)) {
            return PRACORConst.CA_MARIE;
        } else if (PRACORConst.CS_VEUF.equals(csEtatCivil)) {
            return PRACORConst.CA_VEUF;
        } else if (PRACORConst.CS_SEPARE.equals(csEtatCivil)) {
            return PRACORConst.CA_SEPARE;
        } else if (PRACORConst.CS_SEPARE_DE_FAIT.equals(csEtatCivil)) {
            return PRACORConst.CA_MARIE;
        } else if (PRACORConst.CS_LPART_DISSOUS.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_DISSOUS;
        } else if (PRACORConst.CS_LPART_DISSOUS_DECES.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_DECES;
        } else if (PRACORConst.CS_LPART_ENREGISTRE.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_ENREGISTRE;
        } else if (PRACORConst.CS_LPART_SEPARE_DE_FAIT.equals(csEtatCivil)) {
            return PRACORConst.CA_LPART_ENREGISTRE;
        } else {
            return PRACORConst.CA_CELIBATAIRE;
        }
    }

    /**
     * Retourne le code compatible ACOR pour l'état (nombre clé des états).
     * 
     * <p>
     * En principe, le code système du pays est déjà le code OFAS.
     * </p>
     * 
     * @param csEtat
     *            le code systeme de l'etat a traduire.
     * 
     * @return le code ACOR correspondant ou CA_SUISSE si inconnu ou vide.
     */
    public static final String csEtatToAcor(String csEtat) {
        if (JadeStringUtil.isBlank(csEtat)) {
            return PRACORConst.CA_CODE_3_VIDE;
        }

        return csEtat; // FIXME: validation du code de l'etat
    }

    public static final String csFractionRenteToAcor(BSession session, String cs) {
        // le code est le code utilisateur
        return session.getCode(cs);
    }

    /**
     * transforme un code systeme de type IJ_TYPE en le code correspondant pour ACOR.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csGenreIndemnite
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csGenreIndemniteIJToACOR(BSession session, String csGenreIndemnite) {
        // le code correspond au code utilisateur
        return session.getCode(csGenreIndemnite);
    }

    public static final String csGenrePrestationToAcor(BSession session, String csGenrePrestation) {
        // le code correspond au code utilisateur
        return session.getCode(csGenrePrestation);

    }

    /**
     * transforme un code systeme de type IJ_GENRE_READAPTATION en le code correspondant pour ACOR.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csGenreReadaptation
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csGenreReadaptationToACOR(BSession session, String csGenreReadaptation) {
        // le code correspond au code utilisateur
        return session.getCode(csGenreReadaptation);
    }

    /**
     * Retourne le code compatible ACOR décrivant le genre de service.
     * 
     * @param session
     *            la session courante
     * @param csGenreService
     *            le code système du genre de service à convertir
     * 
     * @return le code du genre service.
     */
    public static final String csGenreServiceToAcor(BSession session, String csGenreService) {
        return session.getCode(csGenreService);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csModeCalcul
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csModeCalculToAcor(BSession session, String csModeCalcul) {
        // le code correspond au code utilisateur
        return session.getCode(csModeCalcul);
    }

    /**
     * transforme un code systeme de type IJMOTIFINT en le code correspondant pour ACOR.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csMotifInterruption
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csMotifInterruptionToAcor(BSession session, String csMotifInterruption) {
        // le code correspond au code utilisateur
        return session.getCode(csMotifInterruption);
    }

    /**
     * transforme un code systeme de en le code correspondant pour ACOR.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csGenreIndemnite
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csPeriodeToAcor(BSession session, String csPeriode) {
        // le code est le code utilisateur
        return session.getCode(csPeriode);
    }

    /**
     * Transforme la periodicite salaire definie dans IJ en code acor.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csPeriodiciteSalaire
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csPeriodiciteSalaireIJToAcor(BSession session, String csPeriodiciteSalaire) {
        // le code correspond au code utilisateur
        return session.getCode(csPeriodiciteSalaire);
    }

    /**
     * Transforme le code système de la périodicité d'un salaire (IAPDroitLAPG.CS_PERIODIC...) en code compatible ACOR.
     * 
     * @param csPeriodiciteSalaire
     *            DOCUMENT ME!
     * 
     * @return le code ACOR ou chaine vide si inconnu
     */
    public static final String csPeriodiciteSalaireToAcor(String csPeriodiciteSalaire) {
        if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES.equals(csPeriodiciteSalaire)) {
            return PRACORConst.CA_TYPE_SALAIRE_4SEMAINES;
        } else if (IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE.equals(csPeriodiciteSalaire)) {
            return PRACORConst.CA_TYPE_SALAIRE_ANNUEL;
        } else if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equals(csPeriodiciteSalaire)) {
            return PRACORConst.CA_TYPE_SALAIRE_HORAIRE;
        } else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS.equals(csPeriodiciteSalaire)) {
            return PRACORConst.CA_TYPE_SALAIRE_MENSUEL;
        } else {
            return PRACORConst.CA_CHAINE_VIDE;
        }
    }

    /**
     * Retourne le code compatible ACOR pour le sexe.
     * 
     * @param csSexe
     *            le code systeme du sexe à traduire.
     * 
     * @return le code ACOR correspondant ou CA_HOMME si inconnu ou vide.
     */
    public static final String csSexeToAcor(String csSexe) {
        if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return PRACORConst.CA_FEMME;
        } else if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return PRACORConst.CA_HOMME;
        } else {
            return PRACORConst.CA_HOMME;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csSituationAssure
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csSituationAssureToACOR(BSession session, String csSituationAssure) {
        // le code correspond au code utilisateur
        return session.getCode(csSituationAssure);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csStatutProfessionnel
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csStatutProfessionnelToACOR(BSession session, String csStatutProfessionnel) {
        // le code correspond au code utilisateur
        return session.getCode(csStatutProfessionnel);
    }

    /**
     * transforme un code du groupe IJTYPBASE en code acor correspondant.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csTypeBase
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csTypeBaseToAcor(BSession session, String csTypeBase) {
        // le code correspond au code utilisateur
        return session.getCode(csTypeBase);
    }

    /**
     * retourne le code correspondant de ACOR pour le code systeme type de lien.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param csTypeLien
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String csTypeLienToACOR(BSession session, String csTypeLien) {
        if (JadeStringUtil.isBlank(csTypeLien)) {
            return PRACORConst.CA_CELIBATAIRE;
        }

        // le code correspond au code utilisateur
        return session.getCode(csTypeLien);
    }

    public static final String csVetoToAcor(BSession session, String csVeto) {
        // le code correspond au code utilisateur
        return session.getCode(csVeto);

    }

    /**
     * Retourne le chemin complet par défaut vers le dossier ACOR.
     * 
     * @param session
     *            la session pour cette application
     * 
     * @return le chemin complet termine par un séparateur de dossier.
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final String dossierACOR(BSession session) throws PRACORException {

        try {
            return session.getApplication().getProperty(CommonProperties.KEY_DOSSIER_ACOR);
        } catch (Exception e) {
            throw new PRACORException(session.getLabel("PROPRIETE_DOSSIER_ACOR_INTROUVABLE"), e);
        }
    }
}
