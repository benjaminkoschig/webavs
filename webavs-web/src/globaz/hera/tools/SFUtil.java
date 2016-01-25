package globaz.hera.tools;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.util.JAStringFormatter;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.interfaces.ci.SFCiWrapper;
import globaz.hera.interfaces.ci.SFCompteIndividuelHelper;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Descpription
 * 
 * @author scr Date de création 27 oct. 05
 */
public class SFUtil {

    public static final String NNSS_PREFIX = "756";
    public static final String WHERE_NSS_DEBUT = "(TIHAVSP.HVNAVS like ";
    public static final String WHERE_NSS_FIN = ")";

    /**
     * getter pour l'attribut numeros securite sociale Si le code Canton n'est pas récupéré des tiers, il est récupéré
     * de la table SFMBRFA Spécifique pour la situation familiale.
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

            if (seekIn.contains(globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS)) {
                tiers = SFTiersHelper.getTiersAdresseLikeNoAVS(session, nssLike);
            }

            if (seekIn.contains(globaz.hera.tools.nss.SFUtil.PROVENANCE_CI)) {
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

                    String nss = globaz.hera.tools.nss.SFUtil.formatNss(cis[i]
                            .getProperty(SFCiWrapper.PROPERTY_NUMERO_SECURITE_SOCIALE));

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
                        String idCanton = ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_CANTON);
                        // Si le canton na pu être récuéré des tiers, on va le
                        // reprendre dans la tables des membres de la famille
                        // (si existant).
                        if (JadeStringUtil.isIntegerEmpty(idCanton)) {
                            SFMembreFamille mbr = new SFMembreFamille();
                            mbr.setSession(session);
                            mbr.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                            mbr.setIdTiers(((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_TIERS));
                            mbr.retrieve();
                            if (!mbr.isNew()) {
                                idCanton = mbr.getCsCantonDomicile();
                            }
                        }

                        String cslibellePays = session.getSystemCode("CIPAYORI",
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_PAYS));
                        String libellePays = session.getCodeLibelle(cslibellePays);

                        String libelleSexe = session.getCodeLibelle(
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_SEXE)).equals("Homme") ? "H"
                                : "F";

                        sb.append(globaz.hera.tools.nss.SFUtil.appendOptionList(
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_NOM),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_PRENOM),
                                globaz.hera.tools.nss.SFUtil.PROVENANCE_TIERS,
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_TIERS),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_SEXE),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_DATE_DECES),
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_ID_PAYS), idCanton,
                                ((SFTiersWrapper) obj).getProperty(SFTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL),
                                libelleSexe, libellePays));
                    }
                    // TROUVE dans CI
                    else {
                        String nss = globaz.hera.tools.nss.SFUtil.formatNss(((SFCiWrapper) obj)
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

                        sb.append(globaz.hera.tools.nss.SFUtil.appendOptionList(nss, nom, prenom,
                                globaz.hera.tools.nss.SFUtil.PROVENANCE_CI,
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_ID_COMPTE_INDIVIDUEL),
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_SEXE),
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_DATE_NAISSANCE), null,
                                ((SFCiWrapper) obj).getProperty(SFCiWrapper.PROPERTY_ID_PAYS_ORIGINE), null, null,
                                libelleSexe, libellePays));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return sb.toString();
    }

    public static String getWhereNSS(String schema, String likeNoAvs, String likeNoAvsNNSS) {

        String sqlWhereNSS = "";
        String noAvsForLike = JAStringFormatter.deformatAvs(likeNoAvs);
        int nbCaractereLikeNoAvs = noAvsForLike.length();

        int nbCaractereACompleter = 17 - nbCaractereLikeNoAvs;
        for (int i = 0; i < nbCaractereACompleter; i++) {
            noAvsForLike += "_";
        }

        // si nouveau : 756_ _ _ _ _ _ _ _ _ _
        if ("true".equalsIgnoreCase(likeNoAvsNNSS.trim())) {
            noAvsForLike = NSUtil.formatAVSNew(noAvsForLike, true);
        }

        // si ancien : 251_ _ _ _ _ _ _ _
        if ("false".equalsIgnoreCase(likeNoAvsNNSS.trim())) {
            noAvsForLike = NSUtil.formatAVSNew(noAvsForLike, false);
        }

        sqlWhereNSS = WHERE_NSS_DEBUT + "'" + noAvsForLike + "'" + WHERE_NSS_FIN;

        sqlWhereNSS = JadeStringUtil.change(sqlWhereNSS, "SCHEMA.", schema);

        return sqlWhereNSS;
    }

    /**
	 *
	 */
    public SFUtil() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

}
