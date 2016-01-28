package globaz.prestation.interfaces.util.nss;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.ci.PRCiWrapper;
import globaz.prestation.interfaces.ci.PRCompteIndividuelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Descpription
 * 
 * @author scr Date de création 22 sept. 05
 */
public class PRUtil {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    public static final String ATTR_CODE_CANTON_DOMICILE = "codeCantonDomicile";

    // Nouveau numéro de sécurité sociale
    // Est considéré comme nnss (pour les critères de recherches), les no avs
    // qui commence par 756
    // et de longueur == à 16 position avec point ou 13 position sans points.
    // NNSS = 756.xxxx.xxxx.xy

    /** DOCUMENT ME! */
    public static final String ATTR_CODE_ETAT_CIVIL = "codeEtatCivil";

    /** DOCUMENT ME! */
    public static final String ATTR_CODE_PAYS = "codePays";

    /** DOCUMENT ME! */
    public static final String ATTR_CODE_SEXE = "codeSexe";

    /** DOCUMENT ME! */
    public static final String ATTR_DATE_DECES = "dateDeces";

    /** DOCUMENT ME! */
    public static final String ATTR_DATE_NAISSANCE = "dateNaissance";

    /** DOCUMENT ME! */
    public static final String ATTR_ID_ASSURE = "idAssure";

    /** DOCUMENT ME! */
    public static final String ATTR_NOM = "nom";

    /** DOCUMENT ME! */
    public static final String ATTR_NSS = "nss";

    /** DOCUMENT ME! */
    public static final String ATTR_PRENOM = "prenom";

    /** DOCUMENT ME! */
    public static final String ATTR_PROVENANCE = "provenance";

    // Nouveau numéro de sécurité sociale : longeur = 16 pos. avec points
    /** DOCUMENT ME! */
    public static final int NNSS_LENGTH_WITH_DOT = 16;

    // Nouveau numéro de sécurité sociale : longeur = 13 pos. sans les points
    /** DOCUMENT ME! */
    public static final int NNSS_LENGTH_WITHOUT_DOT = 13;

    /** DOCUMENT ME! */
    public static final String NNSS_PREFIX = "756";

    // Ancien numéro de sécurité sociale : longeur = 14 pos. avec points
    /** DOCUMENT ME! */
    public static final int ONSS_LENGTH_WITH_DOT = 14;

    // Ancien numéro de sécurité sociale : longeur = 11 pos. sans points
    /** DOCUMENT ME! */
    public static final int ONSS_LENGTH_WITHOUT_DOT = 11;

    /** DOCUMENT ME! */
    public static final String PROVENANCE_CI = "CI";

    /** DOCUMENT ME! */
    public static final String PROVENANCE_TIERS = "TIERS";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public static String appendOptionList(String nss, String nom, String prenom, String provenance, String idAssure,
            String codeSexe, String dateNaissance, String dateDeces, String codePays, String codeCantonDomicile,
            String codeEtatCivil, String libelleCodeSexe, String libelleCodePays) {
        StringBuffer options = new StringBuffer();
        options.append("<option value='");
        options.append(NSUtil.formatWithoutPrefixe(nss, nss.length() > 14 ? true : false));

        /* NSS */
        options.append("' " + PRUtil.ATTR_NSS + "=\"");
        options.append(nss);
        options.append("\"");

        /* NOM */
        options.append(" " + PRUtil.ATTR_NOM + "=\"");
        options.append(nom);
        options.append("\"");

        /* PRENOM */
        options.append(" " + PRUtil.ATTR_PRENOM + "=\"");
        options.append(prenom);
        options.append("\"");

        /* PROVENANCE */
        options.append(" " + PRUtil.ATTR_PROVENANCE + "=\"");
        options.append(provenance);
        options.append("\"");

        /* ID */
        options.append(" " + PRUtil.ATTR_ID_ASSURE + "='");
        options.append(idAssure);
        options.append("'");

        /* CODE SEXE */
        options.append(" " + PRUtil.ATTR_CODE_SEXE + "='");
        options.append(codeSexe);
        options.append("'");

        /* DATE NAISSANCE */
        options.append(" " + PRUtil.ATTR_DATE_NAISSANCE + "='");
        options.append(dateNaissance);
        options.append("'");

        /* DATE_DECES */
        options.append(" " + PRUtil.ATTR_DATE_DECES + "='");
        options.append(dateDeces);
        options.append("'");

        /* CODE PAYS */
        options.append(" " + PRUtil.ATTR_CODE_PAYS + "='");
        options.append(codePays);
        options.append("'");

        /* CODE Canton de domicile */
        options.append(" " + PRUtil.ATTR_CODE_CANTON_DOMICILE + "='");
        options.append(codeCantonDomicile);
        options.append("'");

        /* Code Etat civil */
        options.append(" " + PRUtil.ATTR_CODE_ETAT_CIVIL + "='");
        options.append(codeEtatCivil);
        options.append("'");

        /**/
        options.append(">");
        options.append(nss);
        options.append(" - ");
        options.append(nom + " " + prenom);

        if (!JadeStringUtil.isEmpty(dateNaissance)) {
            options.append(" - ");
            options.append(dateNaissance);
        }

        if (!JadeStringUtil.isEmpty(libelleCodeSexe)) {
            options.append(" - ");
            options.append(libelleCodeSexe);
        }

        if (!JadeStringUtil.isEmpty(libelleCodePays)) {
            options.append(" - ");
            options.append(libelleCodePays);
        }

        options.append("</option>\n");

        return options.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @throws NSSException
     *             DOCUMENT ME!
     */
    public static void checkNSSCompliance(INSSViewBean viewBean) throws NSSException {
        if (!(viewBean instanceof INSSViewBean)) {
            throw new NSSException(viewBean.getClass().getName() + " doit implémenter INSSViewBean");
        }
    }

    /**
     * 
     * Formatte un no avs en prenans en compte le nnss.
     * 
     * @param unformattedNss
     * @return
     */
    public static String formatNss(String unformattedNss) {
        // Dans les ci, le nss est formatté sans point.
        String nss = unformattedNss;

        // Est considéré comme nnss (pour les critères de recherches), les no
        // avs qui commence par 756
        // et de longueur == à 16 position avec point ou 13 position sans
        // points.

        boolean isNnss = false;

        if ((nss != null) && nss.startsWith(PRUtil.NNSS_PREFIX)) {
            if ((nss.indexOf(".") == -1) && (nss.length() == PRUtil.NNSS_LENGTH_WITHOUT_DOT)) {
                isNnss = true;
            }

            if ((nss.indexOf(".") != -1) && (nss.length() == PRUtil.NNSS_LENGTH_WITH_DOT)) {
                isNnss = true;
            }
        }
        if (isNnss) {
            // Formattage selon règle du nnss (756.xxxx.xxxx.xy )
            nss = NSUtil.formatAVSNewNum(nss);
        }
        // Ancien nss
        else {
            nss = JAStringFormatter.formatAVS(nss);
        }
        return nss;
    }

    /**
     * Retourne le codeIsoLangue du tiers passé en paramètre
     * 
     * @param tiers
     * 
     * @return le codeIsoLangue (FR, DE, ou IT) --> FR par défaut
     */
    public static String getISOLangueTiers(String codeIsoLangue) {
        if (JadeStringUtil.isEmpty(codeIsoLangue)) {
            return "FR";
        }
        // Si code isoLangue reçu en lettres
        else if (codeIsoLangue.equalsIgnoreCase("FR")) {
            return "FR";
        } else if (codeIsoLangue.equalsIgnoreCase("DE")) {
            return "DE";
        } else if (codeIsoLangue.equalsIgnoreCase("IT")) {
            return "IT";
        }
        // Si code isoLangue reçu en code système
        else if (codeIsoLangue.equalsIgnoreCase("503001")) {
            return "FR";
        } else if (codeIsoLangue.equalsIgnoreCase("503002")) {
            return "DE";
        } else if (codeIsoLangue.equalsIgnoreCase("503004")) {
            return "IT";
        }
        // Sinon, retour de valeur par défaut
        else {
            return "FR"; // default
        }
    }

    /**
     * getter pour l'attribut numeros securite sociale
     * 
     * <pre>
     * example de saisie utilisateur :  nssLike
     * 			756_________        -> considéré comme nnss
     * 			756.34______        -> considéré comme nnss
     * 			272.34______        -> considéré comme ancien no avs
     * 			27234_______        -> considéré comme ancien no avs
     * 			27234_______        -> considéré comme ancien no avs
     * 			756.34._____        -> considéré comme ancien no avs
     * 
     * Règles d'identification entre ancien et nouveau numéro sécurité sociale :>
     * 
     * Si commence par 756 et ne contient pas de point en 7ème position, est considéré comme une recherche sur un
     * nnss Sinon, ancien numéro de sécurité sociale.</p>
     * 
     * Les no avs dans les Tiers sont avec des '.' Les no avs dans les CI sont sans les '.'>
     * 
     * Les noms dans les Tiers sont en minuscules Les noms dans les CI sont en majuscule
     * </pre>
     * 
     * @param session
     *            La session
     * @param nssLike
     *            no de sécurite sociale
     * @param seekIn
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut numeros securite sociale
     */
    public static String getNumerosSecuriteSocialeOptionList(BSession session, String nssLike, String isNNSS,
            List seekIn) {
        StringBuffer sb = new StringBuffer();

        try {
            if (nssLike == null) {
                return "";
            }

            nssLike = NSUtil.unFormatAVS(nssLike);

            // Considéré comme nnss
            if (isNNSS.equals("true")) {
                // if (nssLike.startsWith(NNSS_PREFIX) && nssLike.length()>11) {
                // On formatte le no avs avec des points s'il n'en a pas.
                if (nssLike.indexOf(".") == -1) {
                    nssLike = NSUtil.formatAVSNew(nssLike, true);
                }
            }
            // Considéré comme ancien noavs
            else {
                // On formatte le no avs avec des points s'il n'en a pas.
                if (nssLike.indexOf(".") == -1) {
                    nssLike = JAStringFormatter.formatAVS(nssLike);
                }
            }

            PRTiersWrapper[] tiers = null;
            PRCiWrapper[] cis = null;

            if (seekIn.contains(PRUtil.PROVENANCE_TIERS)) {
                if (isNNSS.equals("true")) {
                    if (seekIn.contains("forceSingleAdrMode")) {
                        tiers = PRTiersHelper
                                .getTiersAdresseLikeNoAVSForceSingleAdrMode(session, nssLike, Boolean.TRUE);
                    } else {
                        tiers = PRTiersHelper.getTiersAdresseLikeNoAVSForceFormat(session, nssLike, Boolean.TRUE);
                    }
                } else {
                    if (seekIn.contains("forceSingleAdrMode")) {
                        tiers = PRTiersHelper.getTiersAdresseLikeNoAVSForceSingleAdrMode(session, nssLike,
                                Boolean.FALSE);
                    } else {
                        tiers = PRTiersHelper.getTiersAdresseLikeNoAVSForceFormat(session, nssLike, Boolean.FALSE);
                    }
                }
            }

            // On ne rajoute les CI que dans le cas ou la recherche dans les
            // tiers ne trouve rien.
            // Dans le cas d'une recherche par un ancien nss, on va récupérer
            // son nouveau nss,
            // et si son ancien nss se trouve également dans les CI la liste
            // affichera l'ancien et le
            // nouveau nss. Du coup, en cas de sélection de l'ancien nss il sera
            // inséré dans les tiers
            // et risque de doublon.
            if ((tiers == null) && seekIn.contains(PRUtil.PROVENANCE_CI)) {
                cis = PRCompteIndividuelHelper.getCiLikeNoAVS(session, nssLike);
            }

            // Concaténation des tiers & ci dans une même map.
            // Si assuré trouvé dans tiers, il a la priorité sur les CI ->
            // bypass du ci
            Map map = new TreeMap();

            if (tiers != null) {
                for (int i = 0; i < tiers.length; i++) {
                    map.put(tiers[i].getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), tiers[i]);
                }
            }

            if (cis != null) {
                for (int i = 0; i < cis.length; i++) {

                    String nss = PRUtil.formatNss(cis[i].getProperty(PRCiWrapper.PROPERTY_NUMERO_SECURITE_SOCIALE));

                    if (map.containsKey(nss)) {
                        continue;
                    } else {
                        map.put(nss, cis[i]);
                    }
                }
            }

            if (map.size() == 0) {
                return "";
            } else {
                for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
                    String key = (String) iter.next();
                    Object obj = map.get(key);

                    // Trouvé dans TIERS
                    if (obj instanceof PRTiersWrapper) {

                        String cslibellePays = session.getSystemCode("CIPAYORI",
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_ID_PAYS));
                        String libellePays = session.getCodeLibelle(cslibellePays);

                        String libelleSexe = session.getCodeLibelle(
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_SEXE)).equals("Homme") ? "H"
                                : "F";

                        sb.append(PRUtil.appendOptionList(
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_NOM),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                                PRUtil.PROVENANCE_TIERS,
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_SEXE),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_DATE_DECES),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_ID_PAYS),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_ID_CANTON),
                                ((PRTiersWrapper) obj).getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL),
                                libelleSexe, libellePays));
                    }
                    // TROUVE dans CI
                    else {
                        String nss = PRUtil.formatNss(((PRCiWrapper) obj)
                                .getProperty(PRCiWrapper.PROPERTY_NUMERO_SECURITE_SOCIALE));
                        String nom = "";
                        String prenom = "";
                        String nomPrenom = ((PRCiWrapper) obj).getProperty(PRCiWrapper.PROPERTY_NOM_PRENOM);

                        // Dans les ci, le champ nomPrenom est séparé par une
                        // virgule (en principe)
                        StringTokenizer token = new StringTokenizer(nomPrenom, ",");

                        if (token.countTokens() == 2) {
                            nom = token.nextToken();
                            nom = PRStringUtils.toWordsFirstLetterUppercase(nom);

                            prenom = token.nextToken();
                            prenom = PRStringUtils.toWordsFirstLetterUppercase(prenom);
                        } else {
                            nom = nomPrenom;
                            nom = PRStringUtils.toWordsFirstLetterUppercase(nom);
                        }

                        String cslibellePays = session.getSystemCode("CIPAYORI",
                                ((PRCiWrapper) obj).getProperty(PRCiWrapper.PROPERTY_ID_PAYS_ORIGINE));
                        String libellePays = session.getCodeLibelle(cslibellePays);

                        String libelleSexe = session.getCodeLibelle(
                                ((PRCiWrapper) obj).getProperty(PRCiWrapper.PROPERTY_SEXE)).equals("Homme") ? "H" : "F";

                        sb.append(PRUtil.appendOptionList(nss, nom, prenom, PRUtil.PROVENANCE_CI,
                                ((PRCiWrapper) obj).getProperty(PRCiWrapper.PROPERTY_ID_COMPTE_INDIVIDUEL),
                                ((PRCiWrapper) obj).getProperty(PRCiWrapper.PROPERTY_SEXE),
                                ((PRCiWrapper) obj).getProperty(PRCiWrapper.PROPERTY_DATE_NAISSANCE), "", null, null,
                                null, libelleSexe, libellePays));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return sb.toString();
    }

    /**
     * Crée une nouvelle instance de la classe PRUtil.
     */
    public PRUtil() {
        super();
    }

}
