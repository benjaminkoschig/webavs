/*
 * Créé le 14 mai 07
 */

package globaz.prestation.tools.nnss;

import globaz.commons.nss.NSUtil;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.interfaces.util.nss.NSSException;
import globaz.pyxis.db.tiers.ITIHistoriqueAvsDefTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HPE
 */

public class PRNSSUtil {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public enum FormatNSS {
        COMPLET_FORMATE("756.1111.1111.11", 16);
        // COMPLET_NON_FORMATE,
        // PARTIEL_FORMATE,
        // PARTIEL_NON_FORMATE;

        private String format;
        private int size;

        private FormatNSS(String format, int size) {
            this.format = format;
            this.size = size;
        }

        public final int getSize() {
            return size;
        }

    }

    public static final int NNSS_LENGTH_WITH_DOT = 16;

    public static final int NNSS_LENGTH_WITHOUT_DOT = 13;

    public static final String NNSS_PREFIX = "756";

    public static final int ONSS_LENGTH_WITH_DOT = 14;

    public static final int ONSS_LENGTH_WITHOUT_DOT = 11;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public static final String SECONDARY_ORDER_BY = " ,WAITIE";

    public static void checkNSSCompliance(INSSViewBean viewBean) throws NSSException {
        if (!(viewBean instanceof INSSViewBean)) {
            throw new NSSException(viewBean.getClass().getName() + " doit implémenter INSSViewBean");
        }
    }

    /**
     * Méthode qui renvoie tous les paramètres formatés pour les page de détails
     * 
     * @param nss
     * @param nomPrenom
     * @param dateNaissance
     * @param sexe
     * @param nationalite
     * @return le détail formaté d'un tiers
     */
    public static String formatDetailRequerantDetail(String nss, String nomPrenom, String dateNaissance, String sexe,
            String nationalite) {
        return nss + " / " + nomPrenom + " / " + dateNaissance + " / " + sexe + " / " + nationalite;
    }

    public static String formatDetailRequerantDetailCourt(String nss, String nomPrenom, String dateNaissance) {
        return nss + " / " + nomPrenom + " / " + dateNaissance;
    }

    /**
     * Méthode qui renvoie tous les paramètres formatés pour les rcListes
     * 
     * @param nss
     * @param nomPrenom
     * @param dateNaissance
     * @param sexe
     * @return le détail formaté d'un tiers
     */
    public static String formatDetailRequerantListe(String nss, String nomPrenom, String dateNaissance, String sexe) {
        return nss + "<br />" + nomPrenom + " / " + dateNaissance + " / " + sexe;
    }

    /**
     * Méthode qui renvoie tous les paramètres formatés pour les rcListes
     * 
     * @param nss
     * @param nomPrenom
     * @param dateNaissance
     * @param sexe
     * @param nationalite
     * @return le détail formaté d'un tiers
     */
    public static String formatDetailRequerantListe(String nss, String nomPrenom, String dateNaissance, String sexe,
            String nationalite) {
        return "<b>" + nss + "</b><br />" + nomPrenom + " / " + dateNaissance + " / " + sexe + " / " + nationalite;
    }

    /**
     * Méthode qui renvoie tous les paramètres formatés pour les rcListes
     * 
     * @param nss
     * @param nomPrenom
     * @param dateNaissance
     * @param sexe
     * @param nationalite
     * @return le détail formaté d'un tiers
     */
    public static String formatDetailRequerantListeCourt(String nss, String nomPrenom, String dateNaissance) {
        return "<b>" + nss + "</b><br />" + nomPrenom + " / " + dateNaissance;
    }

    public static String formatDetailRequerantListeDecede(String nss, String nomPrenom, String dateNaissance,
            String sexe, String nationalite, String dateDeces) {
        return "<b>" + nss + "<span  style=color:red> ( </span><span style=font-family:wingdings>U</span>&nbsp;"
                + dateDeces + "<span  style=color:red> )</span></b><br />" + nomPrenom + " / " + dateNaissance + " / "
                + sexe + " / " + nationalite;
    }

    /**
     * Méthode qui formatte le where pour les NNSS
     * 
     * @param schema
     * @param likeNoAvs
     * @param likeNoAvsNNSS
     * @return le nécessaire pour les manager avec les NNSS
     */
    public static String getWhereNSS(String schema, String likeNoAvs, String likeNoAvsNNSS) {
        return PRNSSUtil.getWhereNSS(schema, likeNoAvs, likeNoAvsNNSS, false);
    }

    /**
     * Méthode qui formatte le where pour les NNSS
     * 
     * @param schema
     * @param likeNoAvs
     * @param likeNoAvsNNSS
     * @return le nécessaire pour les manager avec les NNSS
     */
    public static String getWhereNSS(String schema, String likeNoAvs, String likeNoAvsNNSS, boolean withSchema) {

        StringBuilder sqlWhereNSS = new StringBuilder();
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

        sqlWhereNSS.append("(");
        if (withSchema) {
            sqlWhereNSS.append("SCHEMA.");
        }
        sqlWhereNSS.append(ITIHistoriqueAvsDefTable.TABLE_NAME).append(".").append(ITIHistoriqueAvsDefTable.NUMERO_AVS)
                .append(" LIKE '").append(noAvsForLike).append("')");

        return JadeStringUtil.change(sqlWhereNSS.toString(), "SCHEMA.", schema);
    }

    /**
     * Test si la chaîne passée en argument possède le format NSS complet 756.1234.1234.12
     * 
     * @param nss
     * @return
     */
    public static boolean isValidNSS(String nss, FormatNSS formatNSS) {
        if ((nss == null) || (nss.length() < 10) || (formatNSS == null)) {
            return false;
        }
        switch (formatNSS) {
            case COMPLET_FORMATE:
                if (nss.length() != formatNSS.getSize()) {
                    return false;
                }
                if (!"756".equals(nss.subSequence(0, 3))) {
                    return false;
                }
                List<Integer> dotCheck = new ArrayList<Integer>();
                dotCheck.add(3);
                dotCheck.add(8);
                dotCheck.add(13);
                for (Integer dotPosition : dotCheck) {
                    if (!".".equals(nss.substring(dotPosition, dotPosition + 1))) {
                        return false;
                    }
                }
                Map<Integer, Integer> valueCheck = new HashMap<Integer, Integer>();
                valueCheck.put(0, 3);
                valueCheck.put(4, 8);
                valueCheck.put(9, 13);
                valueCheck.put(14, 16);
                for (Integer startPosition : valueCheck.keySet()) {
                    String value = nss.substring(startPosition, valueCheck.get(startPosition));
                    if (!JadeNumericUtil.isInteger(value)) {
                        return false;
                    }
                }
                return true;

            default:
                return false;
        }
    }
}
