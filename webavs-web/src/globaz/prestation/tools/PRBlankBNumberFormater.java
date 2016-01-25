package globaz.prestation.tools;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.globall.db.BApplication;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.prestation.application.PRAbstractApplication;

/**
 * 
 * @author SCR
 * 
 *         Classe abstraite fournissant les méthodes nécessairse pour générer des numéros métiers (no affilié, nss)
 *         valide et vide, formatté ou non.
 * 
 */
public abstract class PRBlankBNumberFormater {

    private static final String BLANK_NNSS_FORMATTE = "000.0000.0000.00";
    private static final String BLANK_NNSS_NON_FORMATTE = "0000000000000";

    private static final String BLANK_NO_AVS_FORMATTE = "000.00.000.000";
    private static final String BLANK_NO_AVS_NON_FORMATTE = "000.00.000.000";

    public static JadePublishDocumentInfo fillEmptyNoAffilie(JadePublishDocumentInfo docInfo) throws Exception {

        IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

        String noAffilieVide = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();

        docInfo.setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, noAffilieVide);
        docInfo.setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                affilieFormatter.unformat(noAffilieVide));

        return docInfo;
    }

    public static JadePublishDocumentInfo fillEmptyNss(BApplication application, JadePublishDocumentInfo docInfo) {

        String nssFormate = PRBlankBNumberFormater.getEmptyNssFormatte(application);
        String nssNonFormate = PRBlankBNumberFormater.getEmptyNssNonFormatte(application);

        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, nssFormate);
        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE, nssNonFormate);

        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_FORMATTE, PRBlankBNumberFormater.BLANK_NNSS_FORMATTE);
        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_NON_FORMATTE,
                PRBlankBNumberFormater.BLANK_NNSS_NON_FORMATTE);

        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_FORMATTE,
                PRBlankBNumberFormater.BLANK_NO_AVS_FORMATTE);
        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_NON_FORMATTE,
                PRBlankBNumberFormater.BLANK_NO_AVS_NON_FORMATTE);

        return docInfo;
    }

    public static JadePublishDocumentInfo fillNss(PRAbstractApplication application, JadePublishDocumentInfo docInfo,
            String nss) {

        String _nss = null;

        try {
            _nss = PRAbstractApplication.getAvsFormater().unformat(nss);
        } catch (Exception e) {
            _nss = JadeStringUtil.removeChar(nss, '.');
        }

        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, nss);
        docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE, _nss);

        if (PRBlankBNumberFormater.isNss(_nss)) {
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_FORMATTE, nss);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_NON_FORMATTE, _nss);

            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NO_AVS_FORMATTE);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_NON_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NO_AVS_NON_FORMATTE);

        }

        else if (PRBlankBNumberFormater.isAvs(_nss)) {
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_FORMATTE, nss);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_NON_FORMATTE, _nss);

            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NNSS_FORMATTE);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_NON_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NNSS_NON_FORMATTE);

        } else {
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NO_AVS_FORMATTE);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NAVS_NON_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NO_AVS_NON_FORMATTE);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NNSS_FORMATTE);
            docInfo.setDocumentProperty(TIDocumentInfoHelper.TIERS_NSS_NON_FORMATTE,
                    PRBlankBNumberFormater.BLANK_NNSS_NON_FORMATTE);
        }
        return docInfo;
    }

    public static String getEmptyNoAffilieFormatte() {

        try {
            IFormatData formater = PRAbstractApplication.getAffileFormater();

            StringBuffer emptyNoAffilie = new StringBuffer();
            for (int i = 0; i < 15; i++) {
                try {
                    emptyNoAffilie.append("0");
                    String sFormatte = formater.format(emptyNoAffilie.toString());
                    formater.check(sFormatte);
                    return sFormatte;
                } catch (Exception e) {
                    ;
                }
            }
        } catch (Exception e) {
            ;
        }
        // Valeur par défaut si rien trouvé
        return "000.000";
    }

    public static String getEmptyNssFormatte(BApplication application) {

        try {
            if (Jade.getInstance().isNNSS()) {
                return PRBlankBNumberFormater.BLANK_NNSS_FORMATTE;
            } else {
                return PRBlankBNumberFormater.BLANK_NO_AVS_FORMATTE;
            }
        } catch (Exception e) {
            return PRBlankBNumberFormater.BLANK_NNSS_FORMATTE;
        }
    }

    public static String getEmptyNssNonFormatte(BApplication application) {

        try {
            if (Jade.getInstance().isNNSS()) {
                return PRBlankBNumberFormater.BLANK_NNSS_NON_FORMATTE;
            } else {
                return PRBlankBNumberFormater.BLANK_NO_AVS_NON_FORMATTE;
            }
        } catch (Exception e) {
            return PRBlankBNumberFormater.BLANK_NNSS_NON_FORMATTE;
        }
    }

    private static boolean isAvs(String numAvsActuelNonFormatte) {
        boolean rep = false;
        if ((numAvsActuelNonFormatte != null) && (numAvsActuelNonFormatte.length() <= 11)) {
            rep = true;
        }
        return rep;
    }

    private static boolean isNss(String numAvsActuelNonFormatte) {
        boolean rep = false;
        if ((numAvsActuelNonFormatte != null) && (numAvsActuelNonFormatte.length() == 13)) {
            rep = true;
        }
        return rep;
    }

}
