package globaz.cygnus.vb.contributions;

import globaz.globall.db.BSession;

/**
 * Utilitaire pour l'affichage des degrés et genres API dans les écrans
 * 
 * @author PBA
 */
public class RFContributionsAssistanceAIUtils {

    public static String getDetailCodeAPI(BSession session, String codeAPI) {

        StringBuilder detail = new StringBuilder();

        detail.append(codeAPI).append(" (");
        detail.append(RFContributionsAssistanceAIUtils.getLibelleTypeAPI(session, codeAPI));
        detail.append("/");
        detail.append(RFContributionsAssistanceAIUtils.getLibelleDegreAPI(session, codeAPI));
        detail.append(")");

        return detail.toString();
    }

    public static String getLibelleDegreAPI(BSession session, String codeAPI) {

        StringBuilder degre = new StringBuilder();

        switch (Integer.parseInt(codeAPI)) {
            case 81:
            case 84:
            case 85:
            case 89:
            case 91:
            case 95:
                degre.append(session.getLabel("JSP_CAAI_DETAIL_FAIBLE"));
                break;

            case 82:
            case 86:
            case 88:
            case 92:
            case 96:
                degre.append(session.getLabel("JSP_CAAI_DETAIL_MOYEN"));
                break;

            case 83:
            case 87:
            case 93:
            case 97:
                degre.append(session.getLabel("JSP_CAAI_DETAIL_GRAVE"));
                break;
        }

        return degre.toString();
    }

    public static String getLibelleTypeAPI(BSession session, String codeAPI) {

        StringBuilder type = new StringBuilder();

        switch (Integer.parseInt(codeAPI)) {
            case 81:
            case 82:
            case 83:
            case 84:
            case 91:
            case 92:
            case 93:
                type.append(session.getLabel("JSP_CAAI_DETAIL_INVALIDITE"));
                break;

            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 95:
            case 96:
            case 97:
                type.append(session.getLabel("JSP_CAAI_DETAIL_VIEILLESSE"));
                break;
        }

        return type.toString();
    }
}
