package globaz.hera.tools.nss;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.util.JAStringFormatter;
import globaz.hera.interfaces.ci.SFCiWrapper;
import globaz.hera.interfaces.ci.SFCompteIndividuelHelper;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.SFStringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
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
public class SFUtil {

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
        options.append("' " + ATTR_NSS + "=\"");
        options.append(nss);
        options.append("\"");

        /* NOM */
        options.append(" " + ATTR_NOM + "=\"");
        options.append(nom);
        options.append("\"");

        /* PRENOM */
        options.append(" " + ATTR_PRENOM + "=\"");
        options.append(prenom);
        options.append("\"");

        /* PROVENANCE */
        options.append(" " + ATTR_PROVENANCE + "=\"");
        options.append(provenance);
        options.append("\"");

        /* ID */
        options.append(" " + ATTR_ID_ASSURE + "='");
        options.append(idAssure);
        options.append("'");

        /* CODE SEXE */
        options.append(" " + ATTR_CODE_SEXE + "='");
        options.append(codeSexe);
        options.append("'");

        /* DATE NAISSANCE */
        options.append(" " + ATTR_DATE_NAISSANCE + "='");
        options.append(dateNaissance);
        options.append("'");

        /* DATE_DECES */
        options.append(" " + ATTR_DATE_DECES + "='");
        options.append(dateDeces);
        options.append("'");

        /* CODE PAYS */
        options.append(" " + ATTR_CODE_PAYS + "='");
        options.append(codePays);
        options.append("'");

        /* CODE Canton de domicile */
        options.append(" " + ATTR_CODE_CANTON_DOMICILE + "='");
        options.append(codeCantonDomicile);
        options.append("'");

        /* Code Etat civil */
        options.append(" " + ATTR_CODE_ETAT_CIVIL + "='");
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

    public static String formatDetailMembreFamilleVueGlobale(String nss, String nomPrenom, String dateNaissance,
            String dateDeces, String sexe, String nationalite) {

        if (JadeStringUtil.isBlankOrZero(nss)) {
            nss = "000.00.000.000";
        }
        String result = "<span style = \"font-family : Courier New;font-size:12px;\"><b>" + nss + "</b>/"
                + dateNaissance;

        if (!JadeStringUtil.isBlankOrZero(dateDeces)) {
            String d = PRDateFormater.convertDate_JJxMMxAAAA_to_JJxMMxAA(dateDeces);
            result += "<span style=\"color:red;\">" + "(<span style=\"font-family:wingdings\">U</span>" + d
                    + ")</span>";
        }
        result += "</span><br/>" + nomPrenom;
        return result;
    }

    /**
     * Méthode qui renvoie tous les paramètres formatés pour les rcListes (normal)
     * 
     * @param nss
     * @param nomPrenom
     * @param dateNaissance
     * @param sexe
     * @param nationalite
     * 
     * @return le détail formaté d'un tiers
     */
    public static String formatDetailRequerantListe(String nss, String nomPrenom, String dateNaissance, String sexe,
            String nationalite) {
        return "<b>" + nss + "</b><br>" + nomPrenom + " / " + dateNaissance + " / " + sexe + " / " + nationalite;
    }

    /**
     * Méthode qui renvoie tous les paramètres formatés pour les rcListes (spécial)
     * 
     * @param nss
     * @param nomPrenom
     * @param dateNaissance
     * @param sexe
     * @param nationalite
     * 
     * @return le détail formaté d'un tiers
     */
    public static String formatDetailRequerantListeSpecial(String nss, String nomPrenom, String dateNaissance,
            String sexe, String nationalite) {
        return "<span style=font-family:wingdings>U</span>&nbsp;<b><span  style=color:red>( </span>" + nss
                + "<span  style=color:red> )</span></b><br><span  style=color:red>( </span>" + nomPrenom + " / "
                + dateNaissance + " / " + sexe + " / " + nationalite + "<span style=color:red> )</span>";

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

        if ((nss != null) && nss.startsWith(NNSS_PREFIX)) {
            if ((nss.indexOf(".") == -1) && (nss.length() == NNSS_LENGTH_WITHOUT_DOT)) {
                isNnss = true;
            }

            if ((nss.indexOf(".") != -1) && (nss.length() == NNSS_LENGTH_WITH_DOT)) {
                isNnss = true;
            }
        }
        if (isNnss) {
            // TODO Formattage selon règle du nnss (756.xxxx.xxxx.xy )
            // nss = JAStringFormatter.formatNNSS(nss);
        }
        // Ancien nss
        else {
            nss = JAStringFormatter.formatAVS(nss);
        }
        return nss;
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

            SFTiersWrapper[] tiers = null;
            SFCiWrapper[] cis = null;

            if (seekIn.contains(PROVENANCE_TIERS)) {
                tiers = SFTiersHelper.getTiersAdresseLikeNoAVS(session, nssLike);
            }

            if (seekIn.contains(PROVENANCE_CI)) {
                cis = SFCompteIndividuelHelper.getCiLikeNoAVS(session, nssLike);
            }

            // Concaténation des tiers & ci dans une même map.
            // Si assuré trouvé dans tiers, il a la priorité sur les CI ->
            // bypass du ci
            Map map = new TreeMap();

            if (tiers != null) {
                for (int i = 0; i < tiers.length; i++) {
                    map.put(tiers[i].getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), tiers[i]);
                }
            }

            if (cis != null) {
                for (int i = 0; i < cis.length; i++) {

                    String nss = formatNss(cis[i].getProperty(SFCiWrapper.PROPERTY_NUMERO_SECURITE_SOCIALE));

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
                    if (obj instanceof SFTiersWrapper) {

                        String cslibellePays = session.getSystemCode("CIPAYORI",
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_PAYS));
                        String libellePays = session.getCodeLibelle(cslibellePays);

                        String libelleSexe = session.getCodeLibelle(
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_SEXE)).equals("Homme") ? "H"
                                : "F";

                        sb.append(appendOptionList(
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_NOM),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_PRENOM), PROVENANCE_TIERS,
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_TIERS),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_SEXE),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_DATE_DECES),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_PAYS),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_CANTON),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL),
                                libelleSexe, libellePays));
                    }
                    // TROUVE dans CI
                    else {
                        String nss = formatNss(((SFCiWrapper) obj)
                                .getProperty(SFCiWrapper.PROPERTY_NUMERO_SECURITE_SOCIALE));
                        String nom = "";
                        String prenom = "";
                        String nomPrenom = ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_NOM_PRENOM);

                        // Dans les ci, le champ nomPrenom est séparé par une
                        // virgule (en principe)
                        StringTokenizer token = new StringTokenizer(nomPrenom, ",");

                        if (token.countTokens() == 2) {
                            nom = token.nextToken();
                            nom = SFStringUtils.toWordsFirstLetterUppercase(nom);

                            prenom = token.nextToken();
                            prenom = SFStringUtils.toWordsFirstLetterUppercase(prenom);
                        } else {
                            nom = nomPrenom;
                            nom = SFStringUtils.toWordsFirstLetterUppercase(nom);
                        }

                        String cslibellePays = session.getSystemCode("CIPAYORI",
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_ID_PAYS_ORIGINE));
                        String libellePays = session.getCodeLibelle(cslibellePays);

                        String libelleSexe = session.getCodeLibelle(
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_SEXE)).equals("Homme") ? "H" : "F";

                        sb.append(appendOptionList(nss, nom, prenom, PROVENANCE_CI,
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_ID_COMPTE_INDIVIDUEL),
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_SEXE),
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_DATE_NAISSANCE), "", null, null,
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
    public SFUtil() {
        super();
    }

}
