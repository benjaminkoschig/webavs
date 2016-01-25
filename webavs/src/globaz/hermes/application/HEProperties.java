package globaz.hermes.application;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.jade.client.util.JadeStringUtil;

public class HEProperties {
    public static String PROP_CHECK_DOUBLON_LOT_ACTIVE = "batch.check.doublon.lot";
    public static String PROP_CODE_APP_RENTES = "CODAPPREN";
    // exclure certain arcs dans l'impression des cartes d'assurés et des
    // attestations
    public static String PROP_CODE_ARC_EXCLUS_ATT_CA = "CODEEXATCA";
    // exclure certain arcs dans le traitement en fonction de la rérence interne
    public static String PROP_CODE_ARC_EXCLUSION = "CODEARCEXC";
    // BZ 5522 : Exclusion du traitement des ordres reçus de la centrale
    public static String PROP_CODE_EXCL_TRAITEMENT_ORDRE = "EXCTRAITOR";

    // exclure certain arcs dans l'extraction des series pour les rentes
    // CCVD/AGLSNE
    public static String PROP_CODE_EXTRACT_EXCLUSION = "CODEEXTEXC";
    public static String PROP_INCLURE_CI_ADDITIONNEL = "INCLCIADD";
    // BZ 5522 : ne considérer que cette référence pour le chargement des arcs de la centrale
    public static String PROP_REFERENCE_FILTRE_ARC = "FILTREREF";

    public static String getParameter(String key, BSession session, BTransaction transaction) {
        try {
            FWFindParameterManager param = new FWFindParameterManager();
            FWFindParameter parametre;
            param.setSession(session);
            param.setIdApplParametre(HEApplication.DEFAULT_APPLICATION_HERMES);
            param.setIdCleDiffere(key);
            param.find(transaction);
            parametre = (FWFindParameter) param.getFirstEntity();
            return parametre.getValeurAlphaParametre();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isCheckDoublonActif(BSession session) {
        try {
            String propValeur = ((HEApplication) session.getApplication())
                    .getProperty(HEProperties.PROP_CHECK_DOUBLON_LOT_ACTIVE);
            if (JadeStringUtil.isEmpty(propValeur)) {
                return true;
            } else {
                return Boolean.getBoolean(propValeur);
            }
        } catch (Exception e) {
            return true;
        }
    }
}
