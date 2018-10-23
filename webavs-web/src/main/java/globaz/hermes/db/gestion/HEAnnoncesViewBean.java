package globaz.hermes.db.gestion;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HECheckRights;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.access.HEInfosManager;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.db.parametrage.HEChampannonceListViewBean;
import globaz.hermes.db.parametrage.HEChampannonceViewBean;
import globaz.hermes.db.parametrage.HEChampobligatoireListViewBean;
import globaz.hermes.db.parametrage.HEChampobligatoireViewBean;
import globaz.hermes.db.parametrage.HEChampsViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationListViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationViewBean;
import globaz.hermes.db.parametrage.HELienannonceListViewBean;
import globaz.hermes.db.parametrage.HEMotifcodeapplication;
import globaz.hermes.db.parametrage.HEMotifcodeapplicationManager;
import globaz.hermes.db.parametrage.HEMotifsListViewBean;
import globaz.hermes.db.parametrage.HEMotifsViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonce;
import globaz.hermes.db.parametrage.HEParametrageannonceManager;
import globaz.hermes.utils.AVSUtils;
import globaz.hermes.utils.ChampsMap;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.util.CIUtil;

/**
 * Classe représentant une liste d'annonces pour un lot<br>
 * Fichier HEANNOP put(CODE_APPLICATION, _codeApplication); put(CODE_ENREGISTREMENT, _enregistrement);
 * put(MOTIF_ANNONCE, _motif);
 *
 * @author ADO
 */

public class HEAnnoncesViewBean extends BEntity implements FWViewBeanInterface {

    private static final long serialVersionUID = -5040562161747156899L;
    public static String[] agenceFields = { IHEAnnoncesViewBean.NUMERO_AGENCE, IHEAnnoncesViewBean.NUMERO_AGENCE_2,
            IHEAnnoncesViewBean.NUMERO_AGENCE_3, IHEAnnoncesViewBean.NUMERO_AGENCE_4,
            IHEAnnoncesViewBean.NUMERO_AGENCE_5, IHEAnnoncesViewBean.NUMERO_AGENCE_6,
            IHEAnnoncesViewBean.NUMERO_AGENCE_7, IHEAnnoncesViewBean.NUMERO_AGENCE_8,
            IHEAnnoncesViewBean.NUMERO_AGENCE_CI, IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE,
            IHEAnnoncesViewBean.NUMERO_AGENCE_FIXANT_RENTE, IHEAnnoncesViewBean.NUMERO_CAISSE,
            IHEAnnoncesViewBean.NUMERO_CAISSE__CI, IHEAnnoncesViewBean.NUMERO_CAISSE_2,
            IHEAnnoncesViewBean.NUMERO_CAISSE_3, IHEAnnoncesViewBean.NUMERO_CAISSE_4,
            IHEAnnoncesViewBean.NUMERO_CAISSE_5, IHEAnnoncesViewBean.NUMERO_CAISSE_6,
            IHEAnnoncesViewBean.NUMERO_CAISSE_7, IHEAnnoncesViewBean.NUMERO_CAISSE_8,
            IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE, IHEAnnoncesViewBean.NUMERO_CAISSE_FIXANT_RENTE,
            IHEAnnoncesViewBean.CS_NUM_CAISSE_PAIE_PRESTATION, IHEAnnoncesViewBean.CS_NUM_AGENCE_PAIE_PRESTATION };
    // liste des champs correspondants à un numéro AVS
    public static String[] avsFields = { IHEAnnoncesViewBean.NUMERO_ASSURE, IHEAnnoncesViewBean.NUMERO_ASSURE_1,
            IHEAnnoncesViewBean.NUMERO_ASSURE_2, IHEAnnoncesViewBean.NUMERO_ASSURE_3,
            IHEAnnoncesViewBean.NUMERO_ASSURE_A_COMPLETER, IHEAnnoncesViewBean.NUMERO_ASSURE_ANTERIEUR,
            IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972,
            IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
            IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE, IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE,
            IHEAnnoncesViewBean.NUMERO_ASSURE_PARTENAIRE, IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERE_ENFANT,
            IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERSONNE_REQUERANTE, IHEAnnoncesViewBean.CS_NUMERO_ASSURE,
            IHEAnnoncesViewBean.CS_NUMERO_ASSURE_A_ONZE_CHIFFRES,
            IHEAnnoncesViewBean.CS_NUM_ASSURE_AYANT_DROIT_PRESTATION, IHEAnnoncesViewBean.CS_PREM_NUM_ASSURE_COMPL,
            IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS,
            IHEAnnoncesViewBean.CS_NUMERO_ASSURE_AYANT_DROIT_CONJOINT, IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERE_ENFANT,
            IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE,
            IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE, IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
            IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION };

    // liste des champs à saisir sous forme de CS
    public static String[] codeSystemFields = {};

    public static String[] countryFields = { IHEAnnoncesViewBean.ETAT_ORIGINE, IHEAnnoncesViewBean.ETAT_ORIGINE_1,
            IHEAnnoncesViewBean.ETAT_ORIGINE_2, IHEAnnoncesViewBean.ETAT_ORIGINE_3 };

    /* liste des champs de monnaie */
    public static String[] currencyFields = { IHEAnnoncesViewBean.REVENU, IHEAnnoncesViewBean.TOTAL_REVENUS };

    public static String[] customFields = { IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_01,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_02,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_03,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_04,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_05,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_06,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_07,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_08,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_09,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_10,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_11 };

    // liste des champs qui sont des cas particuliers
    public static String[] customSelectFields = { IHEAnnoncesViewBean.SEXE, IHEAnnoncesViewBean.SEXE_1,
            IHEAnnoncesViewBean.DOMICILE_EN_SUISSE_CODE_INFORMATION, IHEAnnoncesViewBean.AYANT_DROIT };

    // liste des champs à saisir au format JJMMAA
    public static String[] dateFields_JJMMAA = { IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA,
            IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA, IHEAnnoncesViewBean.DATE_TRANSMISSION,
            IHEAnnoncesViewBean.CS_PERIODE_DE_SERVICE_A_JJMMAA, IHEAnnoncesViewBean.CS_DEBUT_DROIT_ALLOCATION,
            IHEAnnoncesViewBean.CS_FIN_DROIT_ALLOCATION, IHEAnnoncesViewBean.CS_PERIODE_DE_SERVICE_DE_JJMMAA,
            IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA, IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE1,
            IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE2, IHEAnnoncesViewBean.CS_PERIODE_DE_JJMMAA_PERIODE3,
            IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA, IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE1,
            IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE2, IHEAnnoncesViewBean.CS_PERIODE_A_JJMMAA_PERIODE3 };

    // liste des champs à saisir au format JJMMAAAA
    public static String[] dateFields_JJMMAAAA = { IHEAnnoncesViewBean.DATE_NAISSANCE_2_JJMMAAAA,
            IHEAnnoncesViewBean.DATE_NAISSANCE_3_JJMMAAAA, IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA,
            IHEAnnoncesViewBean.DATE_ORDRE_JJMMAAAA };

    // liste des champs à saisir avec des calendriers au format MMAA
    public static String[] dateFields_MMAA = { IHEAnnoncesViewBean.DATE_CLOTURE_MMAA,
            IHEAnnoncesViewBean.DATE_CLOTURE_OU_ORDRE_SPLITTING_MMAA, IHEAnnoncesViewBean.DATE_DEBUT_1ER_DOMICILE_MMAA,
            IHEAnnoncesViewBean.DATE_DEBUT_2EME_DOMICILE_MMAA, IHEAnnoncesViewBean.DATE_DEBUT_3EME_DOMICILE_MMAA,
            IHEAnnoncesViewBean.DATE_DEBUT_4EME_DOMICILE_MMAA, IHEAnnoncesViewBean.DATE_FIN_1ER_DOMICILE_MMAA,
            IHEAnnoncesViewBean.DATE_FIN_2EME_DOMICILE_MMAA, IHEAnnoncesViewBean.DATE_FIN_2EME_DOMICILE_MMAA,
            IHEAnnoncesViewBean.DATE_FIN_3EME_DOMICILE_MMAA, IHEAnnoncesViewBean.DATE_FIN_4EME_DOMICILE_MMAA,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_2,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_3,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_4,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_5,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_6,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_7,
            IHEAnnoncesViewBean.DATE_MMAA_OU_ZERO_AVANT_01071972_8, IHEAnnoncesViewBean.CS_MOIS_COMPTABLE_ET_ANNEE,
            IHEAnnoncesViewBean.CS_DATE_DECES };

    public static String[] debutFinChiffreClef = { IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_01,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_02,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_03,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_04,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_05,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_06,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_07,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_08,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_09,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_10,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_11 };

    // HEAnnoncesViewBean.MOTIF_ANNONCE
    // liste des champs cachés pour la saisie
    /* HEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, */
    public static String[] forbiddenFields = { IHEAnnoncesViewBean.CODE_APPLICATION,
            IHEAnnoncesViewBean.CODE_ENREGISTREMENT, IHEAnnoncesViewBean.RESERVE_A_BLANC,
            IHEAnnoncesViewBean.NUMERO_AGENCE, IHEAnnoncesViewBean.NUMERO_CAISSE, IHEAnnoncesViewBean.RESERVE_BLANC_2,
            IHEAnnoncesViewBean.RESERVE_ZERO_2, IHEAnnoncesViewBean.RESERVE_ZEROS, IHEAnnoncesViewBean.CODE_1_OU_2 };
    // liste des champs cachés pour l'affichage
    public static String[] hiddenFields = { IHEAnnoncesViewBean.CODE_APPLICATION,
            IHEAnnoncesViewBean.CODE_ENREGISTREMENT, IHEAnnoncesViewBean.RESERVE_A_BLANC,
            IHEAnnoncesViewBean.RESERVE_BLANC_2, IHEAnnoncesViewBean.RESERVE_ZERO_2,
            IHEAnnoncesViewBean.RESERVE_ZEROS };

    public static String[] MOTIF_IMPRESSION_CA = { "36", "46" };
    public static String[] MOTIFS_ATTEST_DATE = { "11", "13", "15", "19", "31", "33", "36", "" };

    // liste de motifs de CA
    public static String[] MOTIFS_CA = { "11", "21", "13", "15", "25", "19", "31", "41", "33", "43", "35", "61" };

    // liste de motifs remplacement d'un CA égaré
    public static String[] MOTIFS_CA_EGARE = { "31", "41" };

    public static String[] MOTIFS_DECL_SALAIRE = { "61", "41", "25", "63", "43", "21", "65", "67" };
    // liste de motifs d'ouverture
    public static String[] MOTIFS_OUVERTURE = { "21", "25", "41", "43", "61", "63", "65", "67", "81", "85", "68" };
    // liste de motifs de RCI
    public static String[] MOTIFS_RCI = { "71", "75", "79", "81", "85" };
    // liste de motifs de révocation
    public static String[] MOTIFS_REVOCATION = { "99" };
    public static String[] nameFields = { IHEAnnoncesViewBean.ETAT_NOMINATIF, IHEAnnoncesViewBean.ETAT_NOMINATIF_1,
            IHEAnnoncesViewBean.ETAT_NOMINATIF_2, IHEAnnoncesViewBean.ETAT_NOMINATIF_3 };

    /* a mettre en commentaire fin */
    // liste des champs en lecture seule
    public static String[] readOnlyFields = { IHEAnnoncesViewBean.NUMERO_ANNONCE, IHEAnnoncesViewBean.CODE_1_OU_2 /*
                                                                                                                   * ,
                                                                                                                   * MOTIF_ANNONCE
                                                                                                                   */
    };

    /* liste des champs référence interne */
    public static String[] referenceInterne = { IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
            IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE };
    public final static String WANT_CHECK_CI_OUVERT_FALSE = "false";
    public final static String WANT_CHECK_CI_OUVERT_TRUE = "true";

    public static String formatDate(String key, String value) {
        String newValue = "";

        if (!HEAnnoncesViewBean.isDateField(key)) {
            return value;
        }

        // si la valeur contient pas de point, elle est correcte
        boolean containsDot = false;

        for (int i = 0; i < value.length(); i++) {

            if (!Character.isDigit(value.charAt(i))) { // c'est pas un digit
                containsDot = true;
            } else { // c'est un caractère
                newValue += value.charAt(i);
            }
        }

        if (!containsDot) {
            return value;
        }

        int size = HEAnnoncesViewBean.getDateFormat(key); // renvoit 4, 6,8

        // newValue est à JJMMAAAA
        if (newValue.length() <= size) {
            return newValue;
        }

        switch (size) {

            case 4: // 4 = MMAA
            {
                return newValue.substring(2, 4) + newValue.substring(6, 8);
            }

            case 6: // 6 = JJMMAA
            {
                return newValue.substring(0, 4) + newValue.substring(6, 8);
            }

            case 8: // 8 = JJMMAAAA
            {
                return newValue;
            }
        }

        return value;
    }

    public static String getChampsAsCodeSystemDefaut(String keyChamp) {

        if (keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE) || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_1)
                || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_2)
                || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_3)) {
            return "100";
        }

        return "";
    }

    public static String getCustomSelectFieldValue(String value, String keyChamp, String langue) {
        if (keyChamp.equals(IHEAnnoncesViewBean.SEXE) || keyChamp.equals(IHEAnnoncesViewBean.SEXE_1)) {
            if (langue.equals("FR") && "1".equals(value)) {
                return "1 - Homme";
            } else if (langue.equals("FR") && "2".equals(value)) {
                return "2 - Femme";
            } else if (langue.equals("DE") && "1".equals(value)) {
                return "1 - Männlich";
            } else if (langue.equals("DE") && "2".equals(value)) {
                return "2 - Weiblich";
            } else {
                return "";
            }
        } else if (keyChamp.equals(IHEAnnoncesViewBean.DOMICILE_EN_SUISSE_CODE_INFORMATION)) {
            if (langue.equals("DE")) {
                if ("1".equals(value)) {
                    return "1 - Per. von/bis CH";
                } else if ("2".equals(value)) {
                    return "2 - Per. o. WS CH";
                } else if ("3".equals(value)) {
                    return "3 - Abk. D, SF, N";
                } else {
                    return "";
                }
            } else {
                if ("1".equals(value)) {
                    return "1 - En Suisse du/au";
                } else if ("2".equals(value)) {
                    return "2 - Pas de domicile en Suisse";
                } else if ("3".equals(value)) {
                    return "3 - Allemagne, Finlande, Norvège";
                } else {
                    return "";
                }
            }
        } else if (keyChamp.equals(IHEAnnoncesViewBean.AYANT_DROIT)) {
            if (langue.equals("DE")) {
                if ("1".equals(value)) {
                    return "1 - Selber";
                } else if ("0".equals(value)) {
                    return "0 - Anderer";
                } else {
                    return "";
                }
            } else {
                if ("1".equals(value)) {
                    return "1 - Même personne";
                } else if ("0".equals(value)) {
                    return "0 - Autre";
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    public static Vector getCustomSelectFieldValues(String keyChamp, String langue) {
        Vector vList = new Vector();

        if (keyChamp.equals(IHEAnnoncesViewBean.SEXE) || keyChamp.equals(IHEAnnoncesViewBean.SEXE_1)) {

            if (langue.equals("FR")) {

                // vList.add(new String[] { "", "" });
                vList.add(new String[] { "1", "1 - Homme" });
                vList.add(new String[] { "2", "2 - Femme" });
            } else if (langue.equals("DE")) {

                // vList.add(new String[] { "", "" });
                vList.add(new String[] { "1", "1 - Männlich" });
                vList.add(new String[] { "2", "2 - Weiblich" });
            }

            return vList;
        } else if (keyChamp.equals(IHEAnnoncesViewBean.DOMICILE_EN_SUISSE_CODE_INFORMATION)) {

            // vList.add(new String[] { "", "" });
            if (langue.equals("DE")) {

                vList.add(new String[] { "", "" });
                vList.add(new String[] { "1", "1 - Per. von/bis CH" });
                vList.add(new String[] { "2", "2 - Per. o. WS CH" });
                vList.add(new String[] { "3", "3 - Abk. D, SF, N" });
            } else {

                vList.add(new String[] { "", "" });
                vList.add(new String[] { "1", "1 - En Suisse du/au" });
                vList.add(new String[] { "2", "2 - Pas de domicile en Suisse" });
                vList.add(new String[] { "3", "3 - Allemagne, Finlande, Norvège" });
            }

            return vList;
        } else if (keyChamp.equals(IHEAnnoncesViewBean.AYANT_DROIT)) {

            if (langue.equals("DE")) {
                vList.add(new String[] { "1", "1 - Selber" });
                vList.add(new String[] { "0", "0 - Anderer" });
            } else {
                vList.add(new String[] { "1", "1 - Même personne" });
                vList.add(new String[] { "0", "0 - Autre" });
            }
        }

        return vList;
    }

    public static int getDateFormat(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_JJMMAA).contains(key)) {
            return 6;
        }

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_JJMMAAAA).contains(key)) {
            return 8;
        }

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_MMAA).contains(key)) {
            return 4;
        }

        return 0;
    }

    public static boolean isAgenceField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.agenceFields).contains(key));
    }

    public static boolean isCodeSysteme(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.codeSystemFields).contains(key));
    }

    public static boolean isCountryField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.countryFields).contains(key));
    }

    public static boolean isCurrencyField(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.currencyFields).contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCustomField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.customFields).contains(key));
    }

    public static boolean isCustomInputField(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_MMAA).contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCustomSelectField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.customSelectFields).contains(key));
    }

    public static boolean isDate_MMAA(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_MMAA).contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDateField(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_JJMMAA).contains(key)) {
            return true;
        }

        if (Arrays.asList(HEAnnoncesViewBean.dateFields_JJMMAAAA).contains(key)) {
            return true;
        }

        return false;
    }

    public static boolean isDebutFinChiffreClef(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.debutFinChiffreClef).contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isForbiddenField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.forbiddenFields).contains(key));
    }

    public static boolean isHiddenField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.hiddenFields).contains(key));
    }

    public static boolean isInformationField(String key) {

        if (IHEAnnoncesViewBean.PARTIE_INFORMATION.equals(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMotifAttestDateCA(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_ATTEST_DATE).contains(value));
    }

    public static boolean isMotifCA(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_CA).contains(value));
    }

    public static boolean isMotifCAEgare(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_CA_EGARE).contains(value));
    }

    // indique si la valeur passée est un motif où il faut ajouter cet assuré à
    // la déclaration de salaire
    // ensuite PAVO traite le numéro affilié en insérant dans déclaration de
    // salaire
    public static boolean isMotifForDeclSalaire(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_DECL_SALAIRE).contains(value));
    }

    public static boolean isMotifImpressionCA(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIF_IMPRESSION_CA).contains(value));
    }

    public static boolean isMotifOuverture(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_OUVERTURE).contains(value));
    }

    /**
     * Indique si le motif (ex : 75) est un motif RCI
     */
    public static boolean isMotifRCI(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_RCI).contains(value));
    }

    public static boolean isMotifRevocation(String value) {
        return (Arrays.asList(HEAnnoncesViewBean.MOTIFS_REVOCATION).contains(value));
    }

    public static boolean isNameField(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.nameFields).contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumeroAVS(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.avsFields).contains(key));
    }

    public static boolean isReadOnlyField(String key) {
        return (Arrays.asList(HEAnnoncesViewBean.readOnlyFields).contains(key));
    }

    public static boolean isReferenceInterne(String key) {

        if (Arrays.asList(HEAnnoncesViewBean.referenceInterne).contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    //
    protected HEChampannonceListViewBean champAnnonceList = new HEChampannonceListViewBean();
    /** (RNLENR) */
    protected String champEnregistrement = new String();
    // les champs obligatoire
    protected HEChampobligatoireListViewBean champsObligatoiresListe;

    // table idChamp - libellé
    protected ChampsMap champsTable = new ChampsMap();
    /** Manager CS Statut */
    private FWParametersSystemCodeManager csStatut;

    /** (RNDDAN) */
    protected String dateAnnonce = new String();
    // //
    private String dateEngagement = "";
    /** (RNDECP) */
    private String dateReception = new String();
    private final String HEANNOP_ARCHIVE = "HEANNOR";

    // **** nom des tables ******///
    private final String HEANNOP_EN_COURS = "HEANNOP";

    /** Fichier HEANNOP */
    /** (RNIANN) */
    protected String idAnnonce = new String();

    /** (RMILOT) */
    protected String idLot = new String();

    /** (RNTMES) */
    protected String idMessage = new String();

    /** (RNTPRO) */
    protected String idProgramme = new String();

    // ////////////////////////////////////////////////////
    private String information = "";

    /** les paramètres saisis */
    protected HashMap inputTable = new HashMap();

    // ////////////////////////////////////////////////////
    private boolean isArchivage = false;

    /** annonce validée ou non */
    protected boolean isValidated;

    // lien avec annonce retour
    protected HELienannonceListViewBean lienAnnonceListe;

    /** (RNMOT) */
    private String motif = new String();

    // le triplet motif - critere - code application
    protected HEMotifcodeapplicationManager motifCodeAppListe;

    // /////////////////////////////////////////////////////
    private String motifCU;

    private Boolean nnss = new Boolean(false);

    public final int NUM_AVS_LENGTH = 11;

    public final int NUM_NSS_LENGTH = 13;

    // //
    private String numeroAffilie = "";

    // //
    private String numeroAffilieForDeclSalaire = "";

    /** (RNAVS) */
    private String numeroAVS = new String();

    private String numeroAvsNNSS = "";

    /** (RNCAIS) */
    private String numeroCaisse = new String();

    private String numeroEmploye = "";

    private String numeroSuccursale = "";

    //
    // le parametrage courant de l'annonce en cours de création
    protected HEParametrageannonce paramAnnonce;

    protected String prioriteLot = HELotViewBean.CS_LOT_PTY_HAUTE;

    /** (RNREFU) */
    protected String refUnique = new String();

    // ////////////////////////////////////////////////////
    private String revenu = "";

    /** (RNTSTA) */
    protected String statut = new String();

    /**
     * le type de lot auquel appartient cette annonce, par défaut, c'est HELotViewBean.CS_TYPE_ENVOI
     */
    private String typeLot = HELotViewBean.CS_TYPE_ENVOI;
    // ///////////// Liste des champs //////////////////////////

    /** (RNLUTI) */
    protected String utilisateur = new String();

    protected String wantCheckCiOuvert = HEAnnoncesViewBean.WANT_CHECK_CI_OUVERT_TRUE;

    public static final String CODE_ARC_61 = "61";
    public static final String CODE_ARC_11 = "11";
    public static final String CODE_ARC_31 = "31";

    /**
     * Champ permettant de savoir si un arc 61 a été créé lors de la création manuelle d'un arc 11 ou 31.
     * Si la case était cochée, alors un arc 61 est créé
     */
    private Boolean isArc61Cree = new Boolean(false);

    /**
     * Constructeur du type HEAnnoncesViewBean
     */
    protected HEAnnoncesViewBean() {
        super();
        // on init à aucun motif
        // put(MOTIF_ANNONCE, HEMotifsViewBean.CS_AUCUN_MOTIF);
    }

    /**
     * Constructor HEAnnoncesViewBean.
     *
     * @param bSession
     */
    public HEAnnoncesViewBean(BSession bSession) {
        this();
        setSession(bSession);
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {

        if (getChampEnregistrement().startsWith("1101")) {

            // je supprime
            HEAttenteRetourListViewBean list = new HEAttenteRetourListViewBean();
            list.setSession(getSession());
            list.setForReferenceUnique(getRefUnique());
            list.setForMotif(getMotif());
            list.wantCallMethodAfter(false);
            list.wantCallMethodAfterFind(false);
            list.wantCallMethodBefore(false);
            list.wantCallMethodBeforeFind(false);
            list.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < list.size(); i++) {
                HEAttenteRetourViewBean entity = (HEAttenteRetourViewBean) list.getEntity(i);
                entity.wantCallMethodAfter(false);
                entity.wantCallMethodBefore(false);
                entity.wantCallValidate(false);
                entity.delete(transaction);
            }
        }
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la mise à jour de l'entité dans la BD
     * <p>
     * L'exécution de la mise à jour n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {

        //
        HECodeapplicationListViewBean codeApp = new HECodeapplicationListViewBean();
        codeApp.setSession(getSession());
        codeApp.setForCodeUtilisateur(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION));
        codeApp.find(transaction);

        String currentCodeApp = ((HECodeapplicationViewBean) codeApp.getEntity(0)).getIdCode();

        //
        if ((codeApp.size() == 2) && this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("38")) {

            // argh, c'est un 38, il faut regarder le code 1 ou 2
            if (this.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("1")) {
                currentCodeApp = "111011";
            } else if (this.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("2")) {
                currentCodeApp = "111040";
            }
        }

        HEMotifsListViewBean motifListViewBean = new HEMotifsListViewBean();
        motifListViewBean.setSession(getSession());
        motifListViewBean.setForCodeUtilisateur(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        motifListViewBean.find(transaction);

        //
        HEParametrageannonceManager paramManager = new HEParametrageannonceManager();
        paramManager.setSession(getSession());
        paramManager.setForAfterCodeEnregistrementDebut(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        paramManager.setForBeforeCodeEnregistrementFin(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        paramManager.setForIdCSCodeApplication(currentCodeApp);
        paramManager.find(transaction);
        paramAnnonce = (HEParametrageannonce) paramManager.getEntity(0);

        //
        lienAnnonceListe = new HELienannonceListViewBean();
        lienAnnonceListe.setSession(getSession());
        lienAnnonceListe.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
        lienAnnonceListe.setForIdMotif(((HEMotifsViewBean) motifListViewBean.getEntity(0)).getIdCode());
        lienAnnonceListe.wantCallMethodAfter(false);
        lienAnnonceListe.find(transaction);

        // BZ 9043
        if (!transaction.hasErrors()) {
            parseARC(transaction);
        }
        //
        try {
            HEAttenteRetourListViewBean attentesM = new HEAttenteRetourListViewBean();
            attentesM.setSession(getSession());
            attentesM.setForIdAnnonce(getIdAnnonce());
            attentesM.find(transaction);

            for (int i = 0; i < attentesM.size(); i++) {
                HEAttenteRetourViewBean entity = (HEAttenteRetourViewBean) attentesM.getEntity(i);
                entity.wantCallMethodAfter(false);
                entity.wantCallMethodBefore(false);
                entity.setNumeroAvs(getNumeroAVS());
                entity.setNumeroAvsNNSS(getNumeroAvsNNSS());
                entity.update(transaction);
            }
        } catch (Exception e) {
            JadeLogger.error(e, "an exception occured");
        }
    }

    /**
     * Renvoie la valeur de la propriété tableName (nom de la table)
     *
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {

        if (isArchivage) {
            return HEANNOP_ARCHIVE;
        } else {
            return HEANNOP_EN_COURS;
        }
    }

    /**
     * Lit les enregistrements de la DB
     *
     * @param statement
     *            statement
     * @exception Exception
     *                si la lecture échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric("RNIANN");
        idLot = statement.dbReadNumeric("RMILOT");
        dateAnnonce = statement.dbReadDateAMJ("RNDDAN");
        utilisateur = statement.dbReadString("RNLUTI");
        idProgramme = statement.dbReadString("RNTPRO");
        champEnregistrement = statement.dbReadString("RNLENR");
        refUnique = statement.dbReadString("RNREFU");
        statut = statement.dbReadNumeric("RNTSTA");
        idMessage = statement.dbReadNumeric("RNTMES");
        dateReception = statement.dbReadDateAMJ("RNDECP");

        //
        // setNumeroAVS(statement.dbReadAVS("RNAVS"));
        setMotif(statement.dbReadString("RNMOT"));
        setNumeroCaisse(statement.dbReadString("RNCAIS"));

        // Modification NNSS
        nnss = statement.dbReadBoolean("RNBNNS");
        if (nnss.booleanValue()) {
            numeroAvsNNSS = "true";
            setNumeroAVS(statement.dbReadString("RNAVS"));
        } else {
            numeroAvsNNSS = "false";
            setNumeroAVS(statement.dbReadString("RNAVS"));
        }

        setIsArc61Cree(statement.dbReadBoolean("RNBARC"));
    }

    /**
     * Valide la valeur des champs avant ajout
     *
     * @param statement
     *            statement
     */
    @Override
    protected void _validate(BStatement statement) {

        if (getUtilisateur().trim().length() == 0) {
            statement.getTransaction().addErrors(getSession().getLabel("HERMES_00021"));
        }

        /** Balaye les champs date pour vérifier leur conformité * */
        Set s = getInputTable().keySet();
        Iterator it = s.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();

            if (HEAnnoncesViewBean.isDateField(key)) { // vérification des champs date

                String dateField = (String) getInputTable().get(key);

                if (!isValidDate(key, dateField)) { // champs date non valide
                    _addError(statement.getTransaction(), getSession().getLabel("HERMES_00003") + dateField);
                    // log(this.getClass() + "= La date n'est pas valide : " +
                    // dateField + " pour le champ " + key + "\n(" +
                    // getChampEnregistrement() + ")", FWLogIt.ERROR);
                }
            } else if (HEAnnoncesViewBean.isNumeroAVS(key)) {
                String sexe = (String) getInputTable().get(IHEAnnoncesViewBean.SEXE);
                String sexe1 = (String) getInputTable().get(IHEAnnoncesViewBean.SEXE_1);
                String avs = (String) getInputTable().get(key);
                String isNNSS = (String) getInputTable().get(key + HENNSSUtils.PARAM_NNSS);
                avs = avs.trim();
                avs = StringUtils.removeDots(avs);

                if (avs.length() != 0) { // le numéro AVS est spécifié

                    try {
                        if (!HENNSSUtils.isNNSS(isNNSS) && !HENNSSUtils.isNNSSLength(avs)) {
                            // c'est un numéro AVS
                            if (!JadeStringUtil.isBlank(sexe) && !"15".equals(getMotif()) && !"25".equals(getMotif())) {
                                JAUtil.checkAvs(avs, Integer.parseInt(sexe));
                            } else if (!JadeStringUtil.isBlank(sexe1)) {
                                JAUtil.checkAvs(avs, Integer.parseInt(sexe1));
                            } else {
                                JAUtil.checkAvs(avs);
                            }
                        } else {
                            // C'est un NNS donc contrôle du NNSS
                            // TODO : validation du NNSS
                        }
                    } catch (Exception e) {

                        // log(e.toString() + "\n(" + getChampEnregistrement() +
                        // ")", FWLogIt.ERROR);
                        _addError(statement.getTransaction(),
                                getSession().getLabel("HERMES_00022") + " " + NSUtil.formatAVSUnknown(avs));
                    }
                }
            } else if (HEAnnoncesViewBean.isNameField(key)) {

                if (HELotViewBean.CS_TYPE_ENVOI.equals(getTypeLot())
                        && !"PAVO".equalsIgnoreCase(getIdProgramme().trim())) {

                    if ((getInputTable().get(key) != null) && (((String) getInputTable().get(key)).trim().length() != 0)
                            && ((((String) getInputTable().get(key)).lastIndexOf(",") == -1)
                                    || (((String) getInputTable().get(key))
                                            .lastIndexOf(",") != ((String) getInputTable().get(key)).indexOf(",")))) {
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_00024"));
                    }
                }
            } else if (HEAnnoncesViewBean.isDebutFinChiffreClef(key)) {

                try {
                    String debutFinChiffreClef = (String) getInputTable().get(key);
                    String debut = debutFinChiffreClef.substring(0, 4);
                    String fin = debutFinChiffreClef.substring(4, 8);

                    if (!isValidYear(debut)) {
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_10003") + " " + debut);
                    }

                    if (!isValidYear(fin)) {
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_10004") + " " + fin);
                    }
                } catch (Exception e) {
                    _addError(statement.getTransaction(), getSession().getLabel("HERMES_10003"));
                    _addError(statement.getTransaction(), getSession().getLabel("HERMES_10004"));
                }
            }
        }
        /** Balaye les champs obligatoires pour vérifier leur présence * */
        for (int i = 0; i < champAnnonceList.size(); i++) {
            HEChampannonceViewBean champAnnonce = (HEChampannonceViewBean) champAnnonceList.getEntity(i);

            // exceptions
            if ((getMotif().equals("33") || getMotif().equals("43") || getMotif().equals("35"))
                    && champAnnonce.getIdChamp().equals(IHEAnnoncesViewBean.NUMERO_ASSURE_1)) {
                champAnnonce.setObligatoire(false);
            }

            if (isMotifToOverrideChampObligatoire()) {

                // soit le numéro AVS, soit le reste...
                String avs = (String) getInputTable().get(IHEAnnoncesViewBean.NUMERO_ASSURE);

                if (avs == null) {
                    avs = "";
                }

                avs = avs.trim();

                if (avs.length() == 0) {

                    // pas de numéro AVS, les autres sont obligatoires
                    if (champAnnonce.getIdChamp().equals(IHEAnnoncesViewBean.NUMERO_ASSURE)) {
                        champAnnonce.setObligatoire(false);
                    }
                } else {

                    // seul le numéro AVS est obligatoire
                    if (!champAnnonce.getIdChamp().equals(IHEAnnoncesViewBean.NUMERO_ASSURE)
                            && !champAnnonce.getIdChamp().equals(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE)) {

                        //
                        champAnnonce.setObligatoire(false);
                    }
                }
            }

            if (champAnnonce.isObligatoire()) {
                if (champsTable.containsKey(champAnnonce.getIdChamp())) {

                    // le champ est obligatoire
                    if (JadeStringUtil.isBlank((String) inputTable.get(champAnnonce.getIdChamp()))) {

                        // log("\n(" + getChampEnregistrement() + ")",
                        // FWLogIt.ERROR);
                        _addError(statement.getTransaction(), FWMessageFormat.format(
                                getSession().getLabel("HERMES_00004"), champsTable.valueAt(champAnnonce.getIdChamp())));
                    }
                }
            }
        }

        /**
         * si motif est un motif d'ouverture, on vérifie s'il est pas déja ouvert
         */
        /** et autres règles dépendant du motif */
        try {
            if (HEAnnoncesViewBean.WANT_CHECK_CI_OUVERT_TRUE.equals(wantCheckCiOuvert)) {
                if (this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("61")
                        || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("63")
                        || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("65")
                        || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("67")) {

                    if ((this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE).length() >= NUM_AVS_LENGTH)
                            && ((HEApplication) getSession().getApplication()).isCiOuvert(getSession(),
                                    this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
                        _addError(statement.getTransaction(),
                                FWMessageFormat.format(getSession().getLabel("HERMES_00005"), globaz.commons.nss.NSUtil
                                        .formatAVSUnknown(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))));
                    }
                }
            }

            if ((JadeStringUtil.isEmpty((String) getInputTable().get(IHEAnnoncesViewBean.NUMERO_ASSURE))
                    && (this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("13")
                            || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("19")))) {

                try {
                    if (CIUtil.is30YearsAgo(this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA))) {
                        // l'assuré a plus de 30 ans, donc erreur
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_00007"));
                    }
                } catch (Exception e) {
                    _addError(statement.getTransaction(), e.getMessage());
                }
            }

            if (this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("11")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("21")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("25")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("41")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("43")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("61")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("63")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("65")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("67")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("81")
                    || this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals("85")) {

                // pour les 11 et les 21 c'est l'inverse, il faut avoit 17 ans
                String dateNaissance = this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA);

                try {
                    // Modification NNSS : impossible de tester quoi que ce soit
                    // depuis le NNSS
                    if (JadeStringUtil.isBlank(dateNaissance)
                            && !HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
                        dateNaissance = AVSUtils.getBirthDateFromAVS(
                                StringUtils.removeDots(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)));
                    }
                } catch (Exception e) {
                    _addError(statement.getTransaction(), getSession().getLabel("HERMES_10023"));
                }

                if (!JadeStringUtil.isBlank(dateNaissance)) {
                    try {
                        // on a une date de naissance, on contrôle l'age minimum
                        if (HEUtil.isDateNaissanceIncompletAutorisee(getSession())
                                && !JadeStringUtil.isBlank(dateNaissance) && dateNaissance.length() >= 5
                                && dateNaissance.substring(0, 5).contains("00.00")) {

                            if (!DateUtils.is17YearsAgoDateNaissanceIncomplete(Integer.parseInt(
                                    dateNaissance.substring(dateNaissance.length() - 4, dateNaissance.length())))) {
                                // l'assuré a moins de 17 ans, donc erreur
                                _addError(statement.getTransaction(), getSession().getLabel("HERMES_00008"));
                            }

                        } else {
                            if (!DateUtils.is17YearsAgo(dateNaissance)) {
                                // l'assuré a moins de 17 ans, donc erreur
                                _addError(statement.getTransaction(), getSession().getLabel("HERMES_00008"));
                            }
                        }

                    } catch (Exception e) {
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_10024"));
                    }
                }
                try {
                    // PO 5846 on vérifie la validité de l'année de naissance
                    // elle ne peut pas être inférieur à 1848
                    if (!JadeStringUtil.isBlank(dateNaissance)) {
                        // AAAA
                        int anneeNaissance = Integer.parseInt(JadeStringUtil.substring(dateNaissance, 6));
                        if (anneeNaissance < 1848) {
                            _addError(statement.getTransaction(),
                                    statement.getTransaction().getSession().getLabel("VALIDATE_DATE_NAISSANCE_1848"));
                        }
                    }

                } catch (Exception e) {
                    _addError(statement.getTransaction(), e.getMessage());
                }

            } else if (HELotViewBean.CS_TYPE_ENVOI.equals(getTypeLot())
                    && "99".equals(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                this.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, false);
            }

            if ("95".equals(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {

                if (this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)
                        .equals(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE))) {
                    _addError(statement.getTransaction(), getSession().getLabel("HERMES_00028"));
                }
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }

        try {

            // on vérifie la validité de la date pour les ARC type RCI
            // l'année de naissance JJ.MM.AAAA
            // Conversion
            if (!HEUtil.isNNSSActif(getSession()) && HEAnnoncesViewBean.isMotifRCI(getMotif())) {

                // AAAA
                int anneeNaissanceAssure = Integer.parseInt(DateUtils.getYear(
                        AVSUtils.getBirthDateFromAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)),
                        DateUtils.JJMMAAAA_DOTS));

                //
                if (!checkDate(anneeNaissanceAssure, this.getField(IHEAnnoncesViewBean.DATE_DEBUT_1ER_DOMICILE_MMAA),
                        this.getField(IHEAnnoncesViewBean.DATE_FIN_1ER_DOMICILE_MMAA))) {
                    _addError(statement.getTransaction(),
                            statement.getTransaction().getSession().getLabel("HERMES_00030"));
                }

                //
                if (!checkDate(anneeNaissanceAssure, this.getField(IHEAnnoncesViewBean.DATE_DEBUT_2EME_DOMICILE_MMAA),
                        this.getField(IHEAnnoncesViewBean.DATE_FIN_2EME_DOMICILE_MMAA))) {
                    _addError(statement.getTransaction(),
                            statement.getTransaction().getSession().getLabel("HERMES_00031"));
                }

                //
                if (!checkDate(anneeNaissanceAssure, this.getField(IHEAnnoncesViewBean.DATE_DEBUT_3EME_DOMICILE_MMAA),
                        this.getField(IHEAnnoncesViewBean.DATE_FIN_3EME_DOMICILE_MMAA))) {
                    _addError(statement.getTransaction(),
                            statement.getTransaction().getSession().getLabel("HERMES_00032"));
                }

                //
                if (!checkDate(anneeNaissanceAssure, this.getField(IHEAnnoncesViewBean.DATE_DEBUT_4EME_DOMICILE_MMAA),
                        this.getField(IHEAnnoncesViewBean.DATE_FIN_4EME_DOMICILE_MMAA))) {
                    _addError(statement.getTransaction(),
                            statement.getTransaction().getSession().getLabel("HERMES_00033"));
                }
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     *
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {

        // Traitement par défaut : pas de clé alternée
        // throw new Exception("Alternate key not implemented for this entity");
        statement.writeKey("RNAVS", this._dbWriteString(statement.getTransaction(),
                StringUtils.removeDots(getNumeroAVS()).trim(), "numero avs RNAVS"));
        statement.writeKey("RNMOT", this._dbWriteString(statement.getTransaction(), getMotif(), "motif RNMOT"));
        statement.writeKey("RMILOT", this._dbWriteNumeric(statement.getTransaction(), getIdLot(), "idLot RMILOT"));
    }

    /**
     * Ecrit la clef primaire
     *
     * @param statement
     *            statement
     * @exception Exception
     *                si l'écriture échoue
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RNIANN", this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), ""));
    }

    /**
     * Ecrit les valeurs de l'objet dans la table de la bdd
     *
     * @param statement
     *            statement
     * @exception Exception
     *                si l'écriture échoue
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RNIANN", this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField("RMILOT", this._dbWriteNumeric(statement.getTransaction(), getIdLot(), "idLot"));
        statement.writeField("RNDDAN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateAnnonce(), "dateAnnonce"));
        statement.writeField("RNLUTI",
                this._dbWriteString(statement.getTransaction(), getUtilisateur().toUpperCase(), "utilisateur"));
        statement.writeField("RNTPRO",
                this._dbWriteString(statement.getTransaction(), getIdProgramme(), "idProgramme"));
        statement.writeField("RNLENR", this._dbWriteString(statement.getTransaction(),
                StringUtils.formatEnregistrement(getChampEnregistrement()), "enregistrement"));
        statement.writeField("RNREFU", this._dbWriteString(statement.getTransaction(), getRefUnique(), "refUnique"));
        statement.writeField("RNTSTA", this._dbWriteNumeric(statement.getTransaction(), getStatut(), "statut"));
        statement.writeField("RNTMES", this._dbWriteNumeric(statement.getTransaction(), getIdMessage(), "idMessage"));

        //
        if (HEUtil.isNNSSActif(getSession())) {
            statement.writeField("RNAVS",
                    this._dbWriteString(statement.getTransaction(), StringUtils.removeDots(getNumeroAVS()).trim()));
        } else {
            statement.writeField("RNAVS",
                    this._dbWriteString(statement.getTransaction(),
                            StringUtils.padAfterString(StringUtils.removeDots(getNumeroAVS()).trim(), "0", 11),
                            "numero avs RNAVS"));
        }
        statement.writeField("RNMOT", this._dbWriteString(statement.getTransaction(), getMotif(), "motif RNMOT"));
        statement.writeField("RNCAIS",
                this._dbWriteString(statement.getTransaction(), getNumeroCaisse(), "numero caisse RNCAIS"));
        statement.writeField("RNDECP",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateReception(), "dateReception RNDECP"));

        if ("true".equalsIgnoreCase(getNumeroAvsNNSS())) {
            setNnss(new Boolean("true"));
        }
        statement.writeField("RNBNNS",
                this._dbWriteBoolean(statement.getTransaction(), getNnss(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nnss"));
        statement.writeField("RNBARC", this._dbWriteBoolean(statement.getTransaction(), getIsArc61Cree(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isArc61Cree"));
    }

    public boolean checkDate(int anneeNaissance, String dateDebut, String dateFin) {

        if (!JadeStringUtil.isBlank(dateDebut) && !JadeStringUtil.isBlank(dateFin)) {
            dateDebut = StringUtils.removeDots(dateDebut);
            dateFin = StringUtils.removeDots(dateFin);

            int anneeDebut;
            int anneeFin;

            try {

                // anneedebut sous format AAAA
                anneeDebut = DateUtils.enlargeYear(Integer.parseInt(dateDebut) % 100);

                // anneeFin sous format AAAA
                anneeFin = DateUtils.enlargeYear(Integer.parseInt(dateFin) % 100);
            } catch (NumberFormatException e) {
                return false;
            } catch (ParseException e) {
                return false;
            }

            int moisDebut = Integer.parseInt(dateDebut) / 100;
            int moisFin = Integer.parseInt(dateFin) / 100;

            if (anneeDebut > anneeFin) {
                return false;
            }

            if ((anneeDebut == anneeFin) && (moisDebut > moisFin)) {
                return false;
            }

            // est-ce que l'anne de dàbut est plus petite que l'année de
            // naissance de l'assuré
            if (anneeDebut < anneeNaissance) {
                return false;
            }

            // année courante sous format AAAA
            int anneeCourante = Calendar.getInstance().get(Calendar.YEAR);
            int moisCourant = Calendar.getInstance().get(Calendar.MONTH) + 1;

            // est-ce que l'année de début est plus grande que l'année en cours
            if (anneeDebut > anneeCourante) {
                return false;
            }

            if ((anneeDebut == anneeCourante) && (moisDebut > moisCourant)) {
                return false;
            }

            // est-ce que l'année de fin est plus grande que l'année en cours
            if (anneeFin > anneeCourante) {
                return false;
            }

            if ((anneeFin == anneeCourante) && (moisFin > moisCourant)) {
                return false;
            }
        }

        return true;
    }

    //
    protected final String checkLength(String s, char completionChar, int maxLength, int ALIGNMENT)
            throws HEInputAnnonceException {

        if (s.length() > maxLength) {

            // "La valeur saisie est trop longue (Le champ n° " + s +
            // " a une longueur de " + s.length() + " caractères au lieu des " +
            // maxLength + " autorisés)"
            throw new HEInputAnnonceException(
                    FWMessageFormat.format(getSession().getLabel("HERMES_00023"), s, "" + s.length(), "" + maxLength));
        }

        if (s.length() == maxLength) {
            return s;
        }

        StringBuffer sb = new StringBuffer(s);
        char[] fillCar = new char[maxLength - sb.length()];
        Arrays.fill(fillCar, completionChar);

        switch (ALIGNMENT) {

            case HEChampannonceViewBean.CADRAGE_GAUCHE: {
                sb.append(fillCar);

                return sb.toString();
            }

            case HEChampannonceViewBean.CADRAGE_DROITE: {
                sb.insert(0, fillCar);

                return sb.toString();
            }

            case HEChampannonceViewBean.CADRAGE_DROITE_BLANC: {
                s = this.checkLength(s, '0', maxLength, HEChampannonceViewBean.CADRAGE_DROITE);

                if (JAUtil.isIntegerEmpty(s)) {
                    fillCar = new char[maxLength];
                    Arrays.fill(fillCar, ' ');

                    return new String(fillCar);
                }

                return s;
            }

            default: {
                return sb.toString();
            }
        }
    }

    //
    protected final String checkLength(String s, int maxLength) throws HEInputAnnonceException {
        return this.checkLength(s, ' ', maxLength, HEChampannonceViewBean.CADRAGE_GAUCHE);
    }

    /**
     * Method checkUniqueARC. on vérifie que si y'a un numéro AVS
     *
     * @param transaction
     * @return boolean true si l'arc est unique, false s'il existe déjà
     */
    protected void checkUniqueARC(BTransaction transaction) throws Exception {
        String msg = "";
        String codeApp = this.getField(IHEAnnoncesViewBean.CODE_APPLICATION);

        if (codeApp.equals("38") || codeApp.equals("39")) {
            return;
        }

        // seulement si c'est un code enregistrement 01
        if (StringUtils.unPad(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT)).equals("1")) {

            if (this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE).trim().length() != 0) {
                HEOutputAnnonceListViewBean annonces = new HEOutputAnnonceListViewBean(getSession());
                annonces.setForInProgress(true);
                annonces.setForNumeroAVS(
                        StringUtils.removeDots(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE).trim()));
                annonces.setForCodeApplicationOR("11", "21");
                // annonces.setForNotLikeCodeAppl("38");
                // annonces.setForNotLikeCodeAppl2("39");

                if (HEAnnoncesViewBean.isMotifOuverture(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                    annonces.setForOuvertureOnly();
                }

                if (HEAnnoncesViewBean.isMotifRCI(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                    annonces.setForClotureOnly();
                }

                if (HEAnnoncesViewBean.isMotifImpressionCA(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                    annonces.setForImpressionCAOnly(true);
                }

                if (!HEAnnoncesViewBean.isMotifOuverture(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                        && !HEAnnoncesViewBean.isMotifRCI(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                        && !HEAnnoncesViewBean.isMotifImpressionCA(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                    annonces.setForMotif(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
                }

                annonces.find(transaction);

                // BZ 8558 ajout de la comparaison de date d'annonce pour envoyer mesage d'erreur.
                for (int i = 0; i < annonces.size(); i++) {
                    HEAnnoncesViewBean annonceExistante = (HEAnnoncesViewBean) annonces.getEntity(i);

                    String dateAnnonceExistante = JACalendar.format(annonceExistante.getDateAnnonce(),
                            JACalendar.FORMAT_DDMMYYYY);
                    if (!getDateAnnonce().equalsIgnoreCase(dateAnnonceExistante)) {

                        StringBuffer warningBuffer = new StringBuffer();
                        String nssconverted = NSUtil.formatAVSUnknown(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));

                        warningBuffer.append(getSession().getLabel("HERMES_00009"));
                        warningBuffer.append("\n");
                        warningBuffer.append(getSession().getLabel("HERMES_MSG_ANNONCE_INPUT_INVALID"));
                        warningBuffer.append(" ");
                        warningBuffer.append(nssconverted);
                        warningBuffer.append(" ");
                        warningBuffer.append(getSession().getLabel("HERMES_MSG_ANNONCE_INPUT_INVALID_SUITE"));

                        // envoi du mail à l'utilisateur
                        JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(),
                                getSession().getLabel("HERMES_JOURNAL_WARNING_EMAIL_OBJECT") + " " + nssconverted,
                                warningBuffer.toString(), null);
                    }

                }

                // si on trouve plus de 1 arc, on ajoute une erreur
                // "arc déjà existant"
                if (annonces.size() != 0) {
                    throw new Exception(getSession().getLabel("HERMES_00009"));
                }
            }
        }
    }

    public final void computeNeededFields(String motif) {

        // try {
        this.computeNeededFields(motif, false);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public final void computeNeededFields(String motif, boolean wantCallMethodAfter) {

        try {
            // création d'une nouvelle session pour éviter le
            // "cursor state not valid"
            BSession newSession = (BSession) getSession().getApplication().newSession(getSession());
            motifCodeAppListe = new HEMotifcodeapplicationManager();
            motifCodeAppListe.setSession(newSession);
            motifCodeAppListe.setForIdCodeApplication(IHEAnnoncesViewBean.CS_11_ANNONCE_ARC);
            if (motif.equals("")) {
                motif = "xxx";
            }

            motifCodeAppListe.setForIdMotif(motif);

            // SELON LES CAS OUI
            motifCodeAppListe.wantCallMethodAfter(wantCallMethodAfter);
            motifCodeAppListe.find();
        } catch (Exception e) {
            JadeLogger.info(this,
                    "Impossible de recupérer les champs pour le motif :" + motif + ", erreur :" + e.getMessage());
            JadeLogger.info(this, e);
        }
    }

    public final void computeNeededFields(String motif, String critere) {
        try {
            /* Vérification des motifs en fonction des rôles */

            try {
                HECheckRights checkRights;

                checkRights = new HECheckRights(getSession().getCode(motif), getSession());
                checkRights.checkRole();
            } catch (Exception e2) {

                // _addError(statement.getTransaction(), e2.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);

                setMessage(FWMessageFormat.format(getSession().getLabel("HERMES_DROIT_MOTIF"),
                        getSession().getUserName(), getSession().getCode(motif)));

            }
            BSession newSession = (BSession) getSession().getApplication().newSession(getSession());
            /* fin vérification */
            FWParametersSystemCodeManager paramSC = ((HEApplication) getSession().getApplication())
                    .getCsChampsListe(newSession);
            motifCodeAppListe = new HEMotifcodeapplicationManager();
            motifCodeAppListe.setSession(newSession);
            motifCodeAppListe.setForIdCodeApplication(IHEAnnoncesViewBean.CS_11_ANNONCE_ARC);
            motifCodeAppListe.setForIdMotif(motif);
            motifCodeAppListe.setForIdCritereMotif(critere);
            motifCodeAppListe.wantCallMethodAfter(false);

            motifCodeAppListe.find();

            if (motifCodeAppListe.size() == 0) {
                System.err.println("triplet pas trouvé");
                _addError(null, getSession().getLabel("HERMES_00010"));
            } else {
                HEMotifcodeapplication critereMotifCodeApp = (HEMotifcodeapplication) motifCodeAppListe
                        .getFirstEntity();
                // Je récupère les champs obligatoires
                champsObligatoiresListe = new HEChampobligatoireListViewBean();
                champsObligatoiresListe.setSession(newSession);
                champsObligatoiresListe.setForIdCritereMotif(critereMotifCodeApp.getIdMotifCodeApplication());
                champsObligatoiresListe.find();

                // System.out.println("Champs obligatoires : ");
                champsTable = new ChampsMap();

                // mis ça dans le cas de trop de clics sur les critères
                for (int i = 0; i < champsObligatoiresListe.size(); i++) {
                    HEChampobligatoireViewBean champObligatoire = (HEChampobligatoireViewBean) champsObligatoiresListe
                            .getEntity(i);
                    HEChampannonceViewBean champ = new HEChampannonceViewBean();
                    champ.setSession(newSession);
                    champ.setIdChampAnnonce(champObligatoire.getIdChampAnnonce());
                    champ.retrieve();
                    champsTable.putParamAnnonce(champ.getIdParametrageAnnonce());

                    champsTable.put(champ.getIdChamp(),
                            (paramSC.getCodeSysteme(champ.getIdChamp())).getCurrentCodeUtilisateur().getLibelle(),
                            Integer.parseInt(champ.getLongueur()));
                }
            }
        } catch (Exception e) {
            JadeLogger.info(this, "Impossible de recupérer les champs pour le motif :" + motif + " critère +" + critere
                    + ", erreur :" + e.getMessage());
            JadeLogger.info(this, e);
        }
    }

    public boolean getArchivage() {
        return isArchivage;
    }

    public final java.lang.String getChampEnregistrement() {
        return champEnregistrement;
    }

    public Vector getChampsAsCodeSystem(String keyChamp) {
        Vector vList = new Vector();

        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";

        if (keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE) || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_1)
                || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_2)
                || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_3)) {
            vList.add(list);

            try {
                HEApplication app = (HEApplication) getSession().getApplication();
                FWParametersSystemCodeManager manager = app.getCsPaysListe(getSession());

                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];

                    FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                    String codePays = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                    list[0] = codePays;
                    list[1] = entity.getCurrentCodeUtilisateur().getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {

                // si probleme, retourne list vide.
                e.printStackTrace();
            }

            return vList;
        }

        return new Vector();
    }

    public final ChampsMap getChampsTable() {
        return champsTable;
    }

    public String getCSStatutLibelle() throws Exception {

        if (csStatut == null) {
            csStatut = new FWParametersSystemCodeManager();
            csStatut.setSession(getSession());
            csStatut.setForIdGroupe(IHEAnnoncesViewBean.CS_GROUPE_STATUT);
            csStatut.find();
        }

        return csStatut.getCodeSysteme(statut).getCurrentCodeUtilisateur().getLibelle();
    }

    public final java.lang.String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * @return
     */
    public String getDateEngagement() throws Exception {
        if (JadeStringUtil.isEmpty(dateEngagement)) {
            String motif = JadeStringUtil.isEmpty(getMotif()) ? this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)
                    : getMotif();
            if (HEAnnoncesViewBean.isMotifForDeclSalaire(motif)) {
                // si la référence unique est vide -> on retrieve
                if (JadeStringUtil.isEmpty(getRefUnique())) {
                    this.retrieve();
                }
                if (!JadeStringUtil.isEmpty(getRefUnique())) {
                    HEInfosManager infoMgr = new HEInfosManager();
                    infoMgr.setSession(getSession());
                    infoMgr.setForIdArc(getRefUnique());
                    infoMgr.setForTypeInfo(HEInfos.CS_DATE_ENGAGEMENT);
                    infoMgr.find();
                    if (infoMgr.size() > 0) {
                        dateEngagement = ((HEInfos) infoMgr.getFirstEntity()).getLibInfo();
                    } else {
                        dateEngagement = null;
                    }
                } else {
                    throw new Exception();
                }
            } else {
                dateEngagement = null;
            }
        }
        return dateEngagement;
    }

    /**
     * @return Returns the dateReception.
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * renvoit la valeur par défaut de la référence interne
     *
     * @param int part - 0 le user name, 1 champ libre, 2 la date
     */
    public String getDefaultReferenceInterne(int part) {

        return "";
    }

    /**
     * Récupère une valeur en fonction d'une des constantes de classe Attention, si c'est une liste qui est demandée ce
     * sera la première valeur qui sera renvoyée Ici, le champ demandé peut-être à null
     *
     * @param FIELD
     *            le champ demandé
     * @return le champ ou null si le champ n'existe pas pour cette annonce
     */
    public String getField(String FIELD) throws HEOutputAnnonceException {
        return this.getField(FIELD, true);
    }

    /**
     * Récupère une valeur en fonction d'une des constantes de classe Attention, si c'est une liste qui est demandée ce
     * sera la première valeur qui sera renvoyée Ici, le champ demandé peut-être à null
     *
     * @param FIELD
     *            le champ demandé
     * @param nullAllowed
     *            true si le champ peut-être null ou false autrement
     * @return le champ ou null si le champ n'existe pas pour cette annonce (nullAllowed==true)
     * @throws HEInputException
     *             si le champ n'existe pas et est obligatoire (nullAllowed==false)
     */
    public String getField(String FIELD, boolean nullAllowed) throws HEOutputAnnonceException {

        if (nullAllowed) {
            return getOptionalField(FIELD, 0);
        } else {
            return getMandatoryField(FIELD, 0);
        }
    }

    /**
     * Method getFieldValues.
     *
     * @return Hashtable
     * @throws Exception
     */
    protected Hashtable getFieldValues() throws Exception {
        Hashtable tmp = new Hashtable();

        // computeNeededFields(getMotif());
        for (int i = 0; i < champAnnonceList.size(); i++) {
            HEChampannonceViewBean champ = (HEChampannonceViewBean) champAnnonceList.getEntity(i);
            tmp.put(champ.getIdChampAnnonce(), this.getField(champ.getIdChamp()));
        }

        return tmp;
    }

    public final java.lang.String getIdAnnonce() {
        return idAnnonce;
    }

    public final java.lang.String getIdLot() {
        return idLot;
    }

    public final java.lang.String getIdMessage() {
        return idMessage;
    }

    public String getIdParamAnnonce() {
        return paramAnnonce.getIdParametrageAnnonce();
    }

    public final java.lang.String getIdProgramme() {

        if (idProgramme == null) {
            return "";
        }

        return idProgramme;
    }

    public String getInformation() throws Exception {
        return this.getInformation(getSession().getUserId());
    }

    public String getInformation(String userId) throws Exception {

        if (!StringUtils.isStringEmpty(information)) {
            return information;
        } else {

            if (this.isRevenuCache(userId)) {
                information = getSession().getLabel("HERMES_10001");
            } else {
                information = this.getField(IHEAnnoncesViewBean.PARTIE_INFORMATION);
            }

            return information;
        }
    }

    public HashMap getInputTable() {
        return inputTable;
    }

    //
    private String getMandatoryField(String field, int pos) throws HEOutputAnnonceException {
        Object o = inputTable.get(field);

        if (o == null) {
            HEChampsViewBean champsView = new HEChampsViewBean();
            champsView.setSession(getSession());
            champsView.setIdCode(field.toString());

            try {
                champsView.retrieve();
            } catch (Exception e) {
                throw new HEOutputAnnonceException("Champ inconnu demandé");
            }

            throw new HEOutputAnnonceException(
                    FWMessageFormat.format(getSession().getLabel("HERMES_00004"), champsView.getLibelle()));
        } else if ((o instanceof String) && "".equals(o)) {
            HEChampsViewBean champsView = new HEChampsViewBean();
            champsView.setSession(getSession());
            champsView.setIdCode(field.toString());

            try {
                champsView.retrieve();
            } catch (Exception e) {
                throw new HEOutputAnnonceException("Champ inconnu demandé");
            }

            throw new HEOutputAnnonceException(
                    FWMessageFormat.format(getSession().getLabel("HERMES_00004"), champsView.getLibelle()));
        } else {
            return (String) o;
        }
    }

    public java.lang.String getMotif() {
        return motif;
    }

    public java.lang.String getMotifCU() {

        try {
            return ((HEApplication) getSession().getApplication()).getCsMotifsListe(getSession())
                    .getCodeSysteme("11100002").getCurrentCodeUtilisateur().getCode();
        } catch (Exception e) {

            return "";
        }
    }

    public String getMotifCU(String motifID) {

        try {
            return ((HEApplication) getSession().getApplication()).getCsMotifsListe(getSession())
                    .getCodeSysteme(motifID).getCurrentCodeUtilisateur().getCodeUtilisateur();
        } catch (Exception e) {

            return "??";
        }
    }

    public Boolean getNnss() {
        return nnss;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getNumeroAffilieForDeclSalaire() throws Exception {
        if (JadeStringUtil.isEmpty(numeroAffilieForDeclSalaire)) {
            String motif = JadeStringUtil.isEmpty(getMotif()) ? this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)
                    : getMotif();
            if (HEAnnoncesViewBean.isMotifForDeclSalaire(motif)) {
                // si la référence unique est vide -> on retrieve
                if (JadeStringUtil.isEmpty(getRefUnique())) {
                    this.retrieve();
                }
                if (!JadeStringUtil.isEmpty(getRefUnique())) {
                    HEInfosManager infoMgr = new HEInfosManager();
                    infoMgr.setSession(getSession());
                    infoMgr.setForIdArc(getRefUnique());
                    infoMgr.setForTypeInfo(HEInfos.CS_NUMERO_AFFILIE);
                    infoMgr.find();
                    if (infoMgr.size() > 0) {
                        numeroAffilieForDeclSalaire = ((HEInfos) infoMgr.getFirstEntity()).getLibInfo();
                    } else {
                        numeroAffilieForDeclSalaire = null;
                    }
                } else {
                    throw new Exception();
                }
            } else {
                numeroAffilieForDeclSalaire = null;
            }
        }
        return numeroAffilieForDeclSalaire;
    }

    public java.lang.String getNumeroAVS() {

        return numeroAVS;
    }

    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    public java.lang.String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getNumeroEmploye() throws Exception {
        if (JadeStringUtil.isEmpty(numeroEmploye)) {
            String motif = JadeStringUtil.isEmpty(getMotif()) ? this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)
                    : getMotif();
            if (HEAnnoncesViewBean.isMotifForDeclSalaire(motif)) {
                // si la référence unique est vide -> on retrieve
                if (JadeStringUtil.isEmpty(getRefUnique())) {
                    this.retrieve();
                }
                if (!JadeStringUtil.isEmpty(getRefUnique())) {
                    HEInfosManager infoMgr = new HEInfosManager();
                    infoMgr.setSession(getSession());
                    infoMgr.setForIdArc(getRefUnique());
                    infoMgr.setForTypeInfo(HEInfos.CS_NUMERO_EMPLOYE);
                    infoMgr.find();
                    if (infoMgr.size() > 0) {
                        numeroEmploye = ((HEInfos) infoMgr.getFirstEntity()).getLibInfo();
                    } else {
                        numeroEmploye = null;
                    }
                } else {
                    throw new Exception();
                }
            } else {
                numeroEmploye = null;
            }
        }
        return numeroEmploye;
    }

    public String getNumeroSuccursale() throws Exception {
        if (JadeStringUtil.isEmpty(numeroSuccursale)) {
            String motif = JadeStringUtil.isEmpty(getMotif()) ? this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)
                    : getMotif();
            if (HEAnnoncesViewBean.isMotifForDeclSalaire(motif)) {
                // si la référence unique est vide -> on retrieve
                if (JadeStringUtil.isEmpty(getRefUnique())) {
                    this.retrieve();
                }
                if (!JadeStringUtil.isEmpty(getRefUnique())) {
                    HEInfosManager infoMgr = new HEInfosManager();
                    infoMgr.setSession(getSession());
                    infoMgr.setForIdArc(getRefUnique());
                    infoMgr.setForTypeInfo(HEInfos.CS_NUMERO_SUCCURSALE);
                    infoMgr.find();
                    if (infoMgr.size() > 0) {
                        numeroSuccursale = ((HEInfos) infoMgr.getFirstEntity()).getLibInfo();
                    } else {
                        numeroSuccursale = null;
                    }
                } else {
                    throw new Exception();
                }
            } else {
                numeroSuccursale = null;
            }
        }
        return numeroSuccursale;
    }

    //
    private String getOptionalField(String field, int pos) {
        Object o = inputTable.get(field);

        if (o == null) {
            return "";
        } else { // soit une liste, soit une String
            return (String) o;
        }
    }

    public String getPresetValue(String key) {
        Calendar c = Calendar.getInstance();

        if (key.equals(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA)) {
            return JAUtil.padInteger("" + c.get(Calendar.DAY_OF_MONTH), 2) + "."
                    + JAUtil.padInteger("" + (c.get(Calendar.MONTH) + 1), 2) + "."
                    + JAUtil.padInteger("" + (c.get(Calendar.YEAR)), 4);
        }

        return "";
    }

    public String getPrioriteLot() {
        return prioriteLot;
    }

    public String getReadOnlyFieldValue(String keyChamp, HashMap params) {

        try {
            if (keyChamp.equals(IHEAnnoncesViewBean.MOTIF_ANNONCE)) {
                if (params.get("motif") != null) {
                    String motif = String.valueOf(params.get("motif"));
                    return ((HEApplication) getSession().getApplication()).getCsMotifsListe(getSession())
                            .getCodeSysteme(motif).getCurrentCodeUtilisateur().getCodeUtilisateur();
                } else {
                    return String.valueOf(params.get(IHEAnnoncesViewBean.MOTIF_ANNONCE));
                }
            } else if (keyChamp.equals(IHEAnnoncesViewBean.AYANT_DROIT)) {
                int critere = -1;

                if (params.get("critere") != null) {
                    critere = Integer.parseInt(String.valueOf(params.get("critere")));
                } else {
                    return String.valueOf(params.get(IHEAnnoncesViewBean.AYANT_DROIT));
                }

                switch (critere) {

                    case 44: {
                        return "1";
                    }

                    case 68: {
                        return "1";
                    }

                    default: {
                        return "0";
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public final java.lang.String getRefUnique() {
        return refUnique;
    }

    public String getRevenu() throws Exception {
        return this.getRevenu(getSession().getUserId());
    }

    /**
     * Method getRevenu.
     *
     * @return String
     * @throws HEOutputAnnonceException
     * @author ald, 13.04.04 cette méthode retourne le montant du revenu quand l'utilisateur à les droits nécessaires.
     *         Sinon cette méthode renvoie "(caché)" spécifiée dans les labels d'hermes
     */
    public String getRevenu(String userId) throws Exception {

        // tester si l'on pas déjà demandé le montant
        if (!StringUtils.isStringEmpty(revenu)) {
            return revenu;
        } else {

            if (this.isRevenuCache(userId)) {
                revenu = getSession().getLabel("HERMES_10001");
            } else {
                revenu = this.getField(IHEAnnoncesViewBean.REVENU);
            }

            return revenu;
        }
    }

    public String getSqlForCopyAnnonce() {

        try {
            StringBuffer sqlFieldsBuffer = new StringBuffer("INSERT INTO ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOR ");
            sqlFieldsBuffer.append("SELECT * FROM ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOP ");
            sqlFieldsBuffer.append("WHERE RNREFU = ? ");
            sqlFieldsBuffer.append("AND RNMOT= ?");

            return sqlFieldsBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getSqlForCopyCi() {

        try {
            StringBuffer sqlFieldsBuffer = new StringBuffer("INSERT INTO ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOR ");
            sqlFieldsBuffer.append("SELECT * FROM ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOP ");
            sqlFieldsBuffer.append("WHERE RNREFU = ? ");
            sqlFieldsBuffer.append("AND RNMOT= ?");
            sqlFieldsBuffer.append("AND RMILOT= ?");

            return sqlFieldsBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getSqlForCopyRetour() {

        try {
            StringBuffer sql = new StringBuffer("INSERT INTO ");
            sql.append(_getCollection());
            sql.append("HEAREAR ");
            sql.append("SELECT * FROM ");
            sql.append(_getCollection());
            sql.append("HEAREAP ");
            sql.append("WHERE ROLRUN = ? ");
            sql.append("AND ROMOT = ?");

            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getSqlForDeleteAnnoncesSerie() {

        try {
            StringBuffer sqlFieldsBuffer = new StringBuffer("DELETE FROM ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOP ");
            sqlFieldsBuffer.append("WHERE RNREFU = ? ");
            sqlFieldsBuffer.append("AND RNMOT = ?");
            sqlFieldsBuffer.append("AND RNTSTA = ?");

            return sqlFieldsBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getSqlForDeleteAttentesSerie() {

        try {

            // update sans BETWEEN car je veux qu'il inclut les deux bornes
            StringBuffer sqlFieldsBuffer = new StringBuffer("DELETE FROM ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEAREAP ");
            sqlFieldsBuffer.append("WHERE ROLRUN = ? ");
            sqlFieldsBuffer.append("AND ROMOT = ?");

            return sqlFieldsBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getSqlForDeleteCi() {

        try {
            StringBuffer sqlFieldsBuffer = new StringBuffer("DELETE FROM ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOP ");
            sqlFieldsBuffer.append("WHERE RNREFU = ? ");
            sqlFieldsBuffer.append("AND RNMOT = ?");
            sqlFieldsBuffer.append("AND RNTSTA = ?");
            sqlFieldsBuffer.append("AND RMILOT= ?");

            return sqlFieldsBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public String getSqlForDeleteOrdresSerie() {

        try {
            StringBuffer sqlFieldsBuffer = new StringBuffer("DELETE FROM ");
            sqlFieldsBuffer.append(_getCollection());
            sqlFieldsBuffer.append("HEANNOP ");
            sqlFieldsBuffer.append("WHERE RNREFU = ? ");
            sqlFieldsBuffer.append("AND RNMOT = ?");
            sqlFieldsBuffer.append("AND RNTSTA = ?");
            sqlFieldsBuffer.append("AND RMILOT = ?");

            return sqlFieldsBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }

    public final java.lang.String getStatut() {
        return statut;
    }

    /**
     * Renvoi le type du lot auquel cette annonce appartient
     *
     * @return String typeLot (cf HELotViewbean)
     */
    public String getTypeLot() {
        return typeLot;
    }

    protected String getUserInitials() {

        if (getSession() == null) {
            return "";
        }

        String firstname = getSession().getUserInfo().getFirstname();
        String lastname = getSession().getUserInfo().getLastname();

        if (!JadeStringUtil.isBlank(firstname) || !JadeStringUtil.isBlank(lastname)) {
            return (firstname.substring(0, 1) + lastname.substring(0, 1)).toUpperCase();
        } else {
            return "na";
        }
    }

    public final java.lang.String getUtilisateur() {

        if (JadeStringUtil.isBlank(utilisateur)) {
            return (getSession().getUserId() == null) ? "" : getSession().getUserId();
        }

        return utilisateur;
    }

    public void initChampEnregistrementFromAttr() throws java.lang.Exception {

        try {

            // Je regarde si le code application existe (AJPPCOS)
            HECodeapplicationListViewBean caM = new HECodeapplicationListViewBean();
            caM.setForCodeUtilisateur(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION, false));
            caM.setSession(getSession());
            caM.find();

            // le code app
            HECodeapplicationViewBean codeApp = (HECodeapplicationViewBean) caM.getEntity(0);

            if (caM.size() == 0) {
                throw new HEInputAnnonceException(
                        "Ce code application n'existe pas : " + this.getField(IHEAnnoncesViewBean.CODE_APPLICATION));
            }

            HEParametrageannonceManager paramManager = new HEParametrageannonceManager();
            paramManager.setSession(getSession());
            paramManager.setForAfterCodeEnregistrementDebut(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
            paramManager.setForBeforeCodeEnregistrementFin(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
            paramManager.setForIdCSCodeApplication(codeApp.getIdCode());
            paramManager.find();

            if (paramManager.size() < 1) {
                throw new HEInputAnnonceException("Cet enregistrement pour ce code application est invalide");
            } // - je charge les champs de l'annonce

            paramAnnonce = (HEParametrageannonce) paramManager.getEntity(0);

            StringBuffer sbAuto = new StringBuffer();
            char[] c = new char[120];
            Arrays.fill(c, 0, 119, ' ');
            sbAuto.append(c);

            // Je vérifie que les champs obligatoires (HECHANP) sont remplis
            // System.out.println(getSession());
            try {
                champAnnonceList = new HEChampannonceListViewBean();
                champAnnonceList.setSession(getSession());
                champAnnonceList.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
                champAnnonceList.find();

                if (champAnnonceList.size() == 0) {
                    throw new Exception(getSession().getLabel("HERMES_00011"));
                }

                for (int i = 0; i < champAnnonceList.size(); i++) {
                    HEChampannonceViewBean champAnnonce = (HEChampannonceViewBean) champAnnonceList.getEntity(i);

                    // ***********************************************************************************************
                    // cadrage et positionnement
                    String field = this.getField(champAnnonce.getIdChamp());

                    /** DEBUG ONLY * */
                    /*
                     * System.out.println(champ.getLibelle() + "=>" + field + "< (" + field.length() + ")");
                     */
                    /** * */
                    /*
                     * au cas où faille formater la date
                     */
                    if (HEAnnoncesViewBean.isDateField(champAnnonce.getIdChamp())) {

                        if (Arrays.asList(HEAnnoncesViewBean.dateFields_JJMMAAAA).contains(champAnnonce.getIdChamp())) {
                            _checkDate(null, field, getSession().getLabel("HERMES_00003"));
                        }

                        field = HEAnnoncesViewBean.formatDate(champAnnonce.getIdChamp(), field);
                    }

                    /*
                     * pour les montants
                     */
                    if (HEAnnoncesViewBean.isCurrencyField(champAnnonce.getIdChamp())) {
                        field = StringUtils.unformatCurrency(field);
                    }

                    /*
                     * au cas où la caisse ou l'agence soit pas formatté
                     */
                    /*
                     * au cas où faille formater le numéro AVS en enlevant les points
                     */
                    if (HEAnnoncesViewBean.isNumeroAVS(champAnnonce.getIdChamp())
                            || HEAnnoncesViewBean.isDateField(champAnnonce.getIdChamp())) {
                        field = StringUtils.removeDots(field);
                    }
                    // modification NNSS
                    if (HEAnnoncesViewBean.isNumeroAVS(champAnnonce.getIdChamp())) {
                        if (HENNSSUtils.isNNSS((this.getField(champAnnonce.getIdChamp() + HENNSSUtils.PARAM_NNSS)))) {
                            field = HENNSSUtils.convertNNSStoNegatif(field);
                        }
                    }

                    if (HEAnnoncesViewBean.isDate_MMAA(champAnnonce.getIdChamp())) {
                        field = StringUtils.removeDotsOnly(field);
                    }

                    if (champAnnonce.getIdChamp().equals(IHEAnnoncesViewBean.NUMERO_AFILLIE)) {
                        field = StringUtils.removeDots(field);
                    }

                    int debut = Integer.parseInt(champAnnonce.getDebut()) - 1;
                    int fin = 0;
                    String s = "";

                    switch (Integer.parseInt(champAnnonce.getCadrage())) {

                        case HEChampannonceViewBean.CADRAGE_DROITE:
                            s = this.checkLength(field, HEChampannonceViewBean.CAR_CADRAGE_DROITE,
                                    Integer.parseInt(champAnnonce.getLongueur()),
                                    HEChampannonceViewBean.CADRAGE_DROITE);

                            break;

                        case HEChampannonceViewBean.CADRAGE_GAUCHE:
                            s = this.checkLength(field, HEChampannonceViewBean.CAR_CADRAGE_GAUCHE,
                                    Integer.parseInt(champAnnonce.getLongueur()),
                                    HEChampannonceViewBean.CADRAGE_GAUCHE);

                            break;

                        case HEChampannonceViewBean.CADRAGE_DROITE_BLANC:
                            s = this.checkLength(field, HEChampannonceViewBean.CAR_CADRAGE_DROITE,
                                    Integer.parseInt(champAnnonce.getLongueur()),
                                    HEChampannonceViewBean.CADRAGE_DROITE_BLANC);

                            break;

                        default:
                            s = this.checkLength(field, Integer.parseInt(champAnnonce.getLongueur()));

                            break;
                    }

                    fin = debut + s.length();
                    sbAuto.replace(debut, fin, s);
                    setChampEnregistrement(sbAuto.toString());
                }
            } catch (Exception iae) {
                throw iae;
            }

            if (sbAuto.length() != 120) {
                throw new HEInputAnnonceException(
                        FWMessageFormat.format(getSession().getLabel("HERMES_00012"), "" + sbAuto.length(), sbAuto));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isCiSecure() {

        try {
            return ((HEApplication) getSession().getApplication()).isCISecure(getSession(), getNumeroAVS());
        } catch (Exception e) {
            setMessage(e.getMessage());
            e.printStackTrace();

            // si il y a une erreur, par sécurité, on protège le ci
            return true;
        }

    }

    /**
     * @return
     */
    protected boolean isMotifToOverrideChampObligatoire() {
        return "11".equals(getMotif()) || "13".equals(getMotif()) || "19".equals(getMotif()) || "21".equals(getMotif())
                || "41".equals(getMotif()) || "31".equals(getMotif()) || "35".equals(getMotif());
    }

    public boolean isRevenuCache() {
        return this.isRevenuCache(getSession().getUserId());
    }

    public boolean isRevenuCache(String userId) {

        try {
            return ((HEApplication) getSession().getApplication()).isRevenuCache(userId, getSession(), getNumeroAVS());
        } catch (Exception e) {
            setMessage(e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Valide la valeur des champs avant ajout
     *
     * @param statement
     *            statement
     */
    protected final boolean isValidDate(String key, String date) {

        if ((date.trim().length() == 0) || JAUtil.isIntegerEmpty(date)) {
            return true;
        }

        date = HEAnnoncesViewBean.formatDate(key, date);

        // la date ne contient que des chiffres...
        // regardons si elle est valide
        String fdt = ""; // "ddMMyyyy";

        switch (date.length()) {

            case 0: {
                return true;
            }

            case 4: {
                fdt = "MMyy";

                break;
            }

            case 6: {
                fdt = "ddMMyy";

                break;
            }

            case 8: {
                fdt = "ddMMyyyy";

                break;
            }
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(fdt);
        sdf.setLenient(false);

        try {
            sdf.parse(date);
        } catch (Exception e) {

            // erreur gérée
            return false;
        }

        return true;
    }

    protected boolean isValidYear(String year) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
        sdf.setLenient(false);

        try {
            sdf.parse(year);

            return true;
        } catch (Exception e) {

            // erreur générée
            return false;
        }
    }

    /**
     * Création d'annonces
     *
     * @idParametrageAnnonce l'identifiant de paramétrage
     */
    public final void parseARC(BTransaction transaction) throws HEInputAnnonceException {

        try {

            // System.out.println("--------" + idParametrageAnnonce);
            StringBuffer sbAuto = new StringBuffer();
            char[] c = new char[120];
            Arrays.fill(c, 0, 119, ' ');
            sbAuto.append(c);

            // Je vérifie que les champs obligatoires (HECHANP) sont remplis
            // System.out.println(getSession());
            try {
                champAnnonceList = new HEChampannonceListViewBean();
                champAnnonceList.setSession(getSession());
                champAnnonceList.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
                champAnnonceList.find(transaction);

                if (champAnnonceList.size() == 0) {
                    _addError(transaction, getSession().getLabel("HERMES_00011"));
                    System.out.println("----------------------------------------");
                    System.out.println("HEAnnoncesViewBean.parseARC(): paramAnnonce:");
                    System.out.println("id=" + paramAnnonce.getId());
                    System.out.println("idParametrageAnnonce=" + paramAnnonce.getIdParametrageAnnonce());
                    System.out.println("idCSCodeApplication=" + paramAnnonce.getIdCSCodeApplication());
                    System.out.println("idUtilisateur=" + paramAnnonce.getIdUtilisateur());
                    System.out.println("codeEnregistrementDebut=" + paramAnnonce.getCodeEnregistrementDebut());
                    System.out.println("codeEnregistrementFin=" + paramAnnonce.getCodeEnregistrementFin());
                    System.out.println("csCodeApplication=" + paramAnnonce.getCsCodeApplication());
                    System.out.println("----------------------------------------");
                }

                for (int i = 0; i < champAnnonceList.size(); i++) {
                    HEChampannonceViewBean champAnnonce = (HEChampannonceViewBean) champAnnonceList.getEntity(i);

                    // cadrage et positionnement
                    String field = this.getField(champAnnonce.getIdChamp());

                    /** * */
                    /*
                     * au cas où faille formater la date
                     */
                    if (HEAnnoncesViewBean.isDateField(champAnnonce.getIdChamp())) {

                        if (Arrays.asList(HEAnnoncesViewBean.dateFields_JJMMAAAA).contains(champAnnonce.getIdChamp())) {
                            if (HEUtil.isDateNaissanceIncompletAutorisee(getSession())) {
                                if (!field.contains("0000")) {
                                    _checkDate(transaction, field, getSession().getLabel("HERMES_00003"));
                                }

                            } else {
                                _checkDate(transaction, field, getSession().getLabel("HERMES_00003"));
                            }
                        }

                        field = HEAnnoncesViewBean.formatDate(champAnnonce.getIdChamp(), field);
                    }

                    /*
                     * pour les montants
                     */
                    if (HEAnnoncesViewBean.isCurrencyField(champAnnonce.getIdChamp())) {
                        field = StringUtils.unformatCurrency(field);
                    }

                    /*
                     * au cas où la caisse ou l'agence soit pas formatté
                     */
                    /*
                     * au cas où faille formater le numéro AVS en enlevant les points
                     */
                    if (HEAnnoncesViewBean.isNumeroAVS(champAnnonce.getIdChamp())
                            || HEAnnoncesViewBean.isDateField(champAnnonce.getIdChamp())) {
                        field = StringUtils.removeDots(field);
                    }
                    // modification NNSS

                    if (HEAnnoncesViewBean.isNumeroAVS(champAnnonce.getIdChamp())
                            && !IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS.equals(champAnnonce.getIdChamp())) {
                        if (HENNSSUtils.isNNSSLength(field)) {
                            field = HENNSSUtils.convertNNSStoNegatif(field);
                        }
                    }

                    if (HEAnnoncesViewBean.isDate_MMAA(champAnnonce.getIdChamp())) {
                        field = StringUtils.removeDotsOnly(field);
                    }

                    if (champAnnonce.getIdChamp().equals(IHEAnnoncesViewBean.NUMERO_AFILLIE)) {
                        field = StringUtils.removeDots(field);
                    }

                    int debut = Integer.parseInt(champAnnonce.getDebut()) - 1;
                    int fin = 0;
                    String s = "";

                    switch (Integer.parseInt(champAnnonce.getCadrage())) {

                        case HEChampannonceViewBean.CADRAGE_DROITE:
                            s = this.checkLength(field, HEChampannonceViewBean.CAR_CADRAGE_DROITE,
                                    Integer.parseInt(champAnnonce.getLongueur()),
                                    HEChampannonceViewBean.CADRAGE_DROITE);

                            break;

                        case HEChampannonceViewBean.CADRAGE_GAUCHE:
                            s = this.checkLength(field, HEChampannonceViewBean.CAR_CADRAGE_GAUCHE,
                                    Integer.parseInt(champAnnonce.getLongueur()),
                                    HEChampannonceViewBean.CADRAGE_GAUCHE);

                            break;

                        case HEChampannonceViewBean.CADRAGE_DROITE_BLANC:
                            s = this.checkLength(field, HEChampannonceViewBean.CAR_CADRAGE_DROITE,
                                    Integer.parseInt(champAnnonce.getLongueur()),
                                    HEChampannonceViewBean.CADRAGE_DROITE_BLANC);

                            break;

                        default:
                            s = this.checkLength(field, Integer.parseInt(champAnnonce.getLongueur()));

                            break;
                    }

                    fin = debut + s.length();
                    sbAuto.replace(debut, fin, s);
                }
            } catch (Exception iae) {
                _addError(transaction, iae.getMessage());
            }

            if (sbAuto.length() != 120) {
                _addError(transaction,
                        FWMessageFormat.format(getSession().getLabel("HERMES_00012"), "" + sbAuto.length(), sbAuto));
            }
            setChampEnregistrement(sbAuto.toString());
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("HERMES_00013"));
        }
    }

    /**
     * Permet de saisir un champ d'une annonce
     *
     * @param Object
     *            value (Vector ou String) la valeur
     */
    public final void put(String FIELD_NAME, Object value) {

        try {

            if (FIELD_NAME.equals(IHEAnnoncesViewBean.CODE_APPLICATION)
                    && (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION) != null)
                    && !this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals(value)) {
                setRefUnique("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        inputTable.put(FIELD_NAME, value);
    }

    /**
     * Method putAll.
     *
     * @param m
     */
    public void putAll(Map m) {

        try {

            if (m.containsKey(IHEAnnoncesViewBean.CODE_APPLICATION)
                    && inputTable.containsKey(IHEAnnoncesViewBean.CODE_APPLICATION)) {
                String newCodeApp = (String) m.get(IHEAnnoncesViewBean.CODE_APPLICATION);
                String oldCodeApp = this.getField(IHEAnnoncesViewBean.CODE_APPLICATION);

                if ((oldCodeApp != null) && !newCodeApp.equals(oldCodeApp)) {

                    // rupture sur le code application
                    setRefUnique(null);
                }
            }
            /*
             * Iterator keyIterator = m.keySet().iterator(); while (keyIterator.hasNext()) { }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

        inputTable.putAll(m);
    }

    public void setArchivage(boolean value) {
        isArchivage = value;
    }

    public void setArchivagePavo(Boolean want) {
        isArchivage = want.booleanValue();
    }

    /**
     * Définit la valeur de la propriété enregistrement la chaine de 120 caractére de l'annonce
     *
     * @param newEnregistrement
     *            newEnregistrement
     */
    public final void setChampEnregistrement(String newEnregistrement) {
        champEnregistrement = newEnregistrement;
    }

    /**
     * Définit la valeur de la propriété dateAnnonce la date de l'annonce
     *
     * @param newDateAnnonce
     *            newDateAnnonce
     */
    public final void setDateAnnonce(String newDateAnnonce) {
        dateAnnonce = newDateAnnonce;
    }

    /**
     * @param string
     */
    public void setDateEngagement(String string) {
        dateEngagement = string;
    }

    /**
     * @param dateReception
     *            The dateReception to set.
     */
    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    /**
     * Définit la valeur de la propriété idAnnonce c'est la clef primaire de l'annonce
     *
     * @param newIdAnnonce
     *            newIdAnnonce
     */
    public final void setIdAnnonce(String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    /**
     * Définit la valeur de la propriété idLot clef étrangère permettant d'identifier le lot auquel appartient cette
     * annonce
     *
     * @param newIdLot
     *            newIdLot
     */
    public final void setIdLot(String newIdLot) {
        idLot = newIdLot;
    }

    /**
     * Définit la valeur de la propriété idMessage le message à inscrire
     *
     * @param newIdMessage
     *            newIdMessage
     */
    public final void setIdMessage(String newIdMessage) {
        idMessage = newIdMessage;
    }

    /**
     * Définit la valeur de la propriété idProgramme Le programme qui fait cet ajour (HELIOS, PAVO...)
     *
     * @param newIdProgramme
     *            newIdProgramme
     */
    public final void setIdProgramme(String newIdProgramme) {
        idProgramme = newIdProgramme;
    }

    /**
     * <b>SURTOUT NE PAS APPELLER !!!</b><br>
     * Cette méthode est destinée aux appels RMI effectués dans le FW<br>
     * En aucun cas ne doit-elle être appellée manuellement<br>
     */
    public void setInputTable(HashMap table) {
        inputTable = table;
    }

    public final void setMotif(java.lang.String newMotif) {
        motif = newMotif;
    }

    public void setMotifCU(java.lang.String newMotifCU) {
        motifCU = newMotifCU;
    }

    public void setNnss(Boolean nnss) {
        this.nnss = nnss;
    }

    /**
     * @param string
     */
    public void setNumeroAffilie(String string) {
        numeroAffilie = string;

    }

    public Boolean getIsArc61Cree() {
        return isArc61Cree;
    }

    public void setIsArc61Cree(Boolean isArc61Cree) {
        this.isArc61Cree = isArc61Cree;
    }

    public final void setNumeroAVS(java.lang.String newNumeroAVS) {
        numeroAVS = newNumeroAVS;
    }

    public void setNumeroAvsNNSS(String numeroAvsNNSS) {
        this.numeroAvsNNSS = numeroAvsNNSS;
    }

    public final void setNumeroCaisse(java.lang.String newNumeroCaisse) {
        newNumeroCaisse = StringUtils.unPad(newNumeroCaisse);
        newNumeroCaisse = StringUtils.removeUnsignigicantZeros(newNumeroCaisse);
        numeroCaisse = newNumeroCaisse;
    }

    public void setNumeroEmploye(String numeroEmploye) {
        this.numeroEmploye = numeroEmploye;
    }

    public void setNumeroSuccursale(String numeroSuccursale) {
        this.numeroSuccursale = numeroSuccursale;
    }

    public void setPrioriteLot(String prioriteLot) {
        this.prioriteLot = prioriteLot;
    }

    public final void setRefUnique(String newRefUnique) {

        try {
            refUnique = newRefUnique.trim();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            refUnique = null;
        }
    }

    /**
     * Définit la valeur de la propriété statut le statut de l'annonce (En attente, A traiter...)
     *
     * @param newStatut
     *            newStatut
     */
    public final void setStatut(String newStatut) {
        statut = newStatut;
    }

    /**
     * Fixe le type du lot auquel cette annonce appartient
     *
     * @param String
     *            typeLot (cf HELotViewBean.CS_TYPE_ENVOI et HELotViewBean.CS_TYPE_RECEPTION)
     */
    public void setTypeLot(String _typeLot) {
        typeLot = _typeLot;
    }

    /**
     * Définit la valeur de la propriété utilisateur L'utilisateur qui créé cette annonce
     *
     * @param newUtilisateur
     *            newUtilisateur
     */
    public final void setUtilisateur(String newUtilisateur) {
        utilisateur = newUtilisateur;
    }

    /**
     * @param string
     */
    public void setWantCheckCiOuvert(String string) {
        wantCheckCiOuvert = string;
    }

    public String toMyString() {
        return getChampEnregistrement();
    }

    protected void traiterChampNumAssure() throws HEOutputAnnonceException, Exception {
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));

            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.NUMERO_ASSURE + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

            setNumeroAvsNNSS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE + HENNSSUtils.PARAM_NNSS));
        } else if (!this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE));
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

            setNumeroAvsNNSS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE + HENNSSUtils.PARAM_NNSS));
        } else if (!this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERSONNE_REQUERANTE).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERSONNE_REQUERANTE));
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERSONNE_REQUERANTE))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERSONNE_REQUERANTE + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

            setNumeroAvsNNSS(
                    this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERSONNE_REQUERANTE + HENNSSUtils.PARAM_NNSS));
        } else if (!this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE));
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }
            setNumeroAvsNNSS(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE + HENNSSUtils.PARAM_NNSS));
        } else if (!this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT));
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }
            setNumeroAvsNNSS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT + HENNSSUtils.PARAM_NNSS));
        } else {
            if (!HEUtil.isNNSSActif(getSession())) {
                try {
                    AVSUtils avs = null;
                    if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA))) {
                        avs = new AVSUtils(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF),
                                this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA),
                                Integer.parseInt(this.getField(IHEAnnoncesViewBean.SEXE)));
                        avs.setFormat(DateUtils.JJMMAAAA);
                    }
                    if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA))) {
                        avs = new AVSUtils(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF),
                                this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA),
                                Integer.parseInt(this.getField(IHEAnnoncesViewBean.SEXE)));
                        avs.setFormat(DateUtils.JJMMAA);
                    }
                    if (avs != null) {
                        setNumeroAVS(JadeStringUtil.removeChar(avs.getNumeroAvs(), '.'));
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    setNumeroAVS(getNumeroAVS());
                }
            }
        }
        // ajout spécial prestation, IJ
        if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_AYANT_DROIT_CONJOINT).trim())) {
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_AYANT_DROIT_CONJOINT))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_AYANT_DROIT_CONJOINT + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

        }
        // ajout spécial prestation, APG
        if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERE_ENFANT).trim())) {
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERE_ENFANT))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_PERE_ENFANT + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

        }
        // ajout spécial prestation, rentes
        if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT).trim())) {
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

        }
        if (!JadeStringUtil
                .isEmpty(this.getField(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE).trim())) {
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

        }
        if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE).trim())) {
            if (HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE + HENNSSUtils.PARAM_NNSS, "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

        }
        if (!JadeStringUtil
                .isEmpty(this.getField(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION).trim())) {
            if (HENNSSUtils
                    .isNNSSLength(this.getField(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION))) {
                // Modif NNSS, pour setter le flag Attention parse ARC
                put(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION + HENNSSUtils.PARAM_NNSS,
                        "true");
                setNnss(Boolean.TRUE);
            } else {
                setNnss(Boolean.FALSE);
            }

        }
    }
}