package globaz.apg.groupdoc.ccju;

import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.adapter.groupdoc.JadeGedAdapterAPIParameters;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Date;
import java.util.Properties;

/**
 * <code>GroupdocPropagateUtil</code> est une classe utilitaire qui crée/met à jour les dossiers dans Groupdoc pour la
 * CCJU.
 * 
 * @author Emmanuel Fleury
 */
public final class GroupdocPropagateUtil {
    private static String adapterName = null;

    private final static String NUMERO_AFFILIE_0 = "000.0000";

    // private final static String NUMERO_AVS_0 = "000.0000.0000.00";
    private final static String NUMERO_AVS_0 = "000.00.000.000";
    private static boolean shouldPropagate = false;
    private static boolean verbose = false;

    /**
     * Indique le nom de l'adaptateur GED à utiliser.
     * 
     * @return le nom de l'adaptateur GED à utiliser
     */
    public final static String getAdapterName() {
        return GroupdocPropagateUtil.adapterName;
    }

    /**
     * Indique si la propagation des dossiers doit être effectuée en mode verbose ou non.
     * 
     * @return true si la propagation des dossiers doit être effectuée en mode verbose, false sinon
     */
    public final static boolean isVerbose() {
        return GroupdocPropagateUtil.verbose;
    }

    /**
     * Crée un dossier 123.4567-123.45.678.901-A07
     * 
     * @param affilie
     *            l'affilie
     * @param allocataire
     *            l'allocataire
     * @param dateCreation
     *            la date de création du dossier
     * @exception Throwable
     *                en cas d'erreur
     */
    public final static void propagateData(IPRAffilie affilie, PRTiersWrapper allocataire, String dateCreation)
            throws Exception {
        if ((affilie != null) || (allocataire != null)) {
            String numAvs = GroupdocPropagateUtil.NUMERO_AVS_0;
            if (allocataire != null) {
                numAvs = NSUtil.formatAVSUnknown(allocataire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            }
            String numAffilie = GroupdocPropagateUtil.NUMERO_AFFILIE_0;
            if (affilie != null) {
                numAffilie = affilie.getNumAffilie();
            }
            Properties properties = null;
            String annee2 = JadeStringUtil.substring(JadeDateUtil.getYMDDate(new Date()), 2, 2);
            String type = "A" + annee2;
            properties = new Properties();
            properties.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "allocataires");
            properties.setProperty("numero.role.formatte", numAffilie);
            properties.setProperty("pyxis.tiers.numero.avs.formatte", numAvs);
            properties.setProperty("babel.type.id", type);
            String indexKey = numAffilie + "-" + numAvs + "-" + type;
            properties.setProperty(JadeGedTargetProperties.INDEX_KEY, indexKey);
            String indexText = type + "-" + numAffilie + "-" + numAvs;
            properties.setProperty(JadeGedTargetProperties.INDEX_TEXT, indexText);
            properties.setProperty(JadeGedTargetProperties.DESCRIPTION_1, type + " - " + numAffilie);
            if (allocataire != null) {
                properties.setProperty(
                        JadeGedTargetProperties.DESCRIPTION_2,
                        allocataire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + allocataire.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            }
            if (allocataire != null) {
                properties.setProperty(JadeGedTargetProperties.DESCRIPTION_3,
                        allocataire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
            }
            properties.setProperty(JadeGedTargetProperties.DESCRIPTION_4, numAvs);
            properties.setProperty(JadeGedAdapterAPIParameters.CODE_BASE_DOCUMENTS_PARENTE, "AFFIL");
            properties.setProperty(JadeGedAdapterAPIParameters.CODE_DOSSIER_CLASSEMENT_PARENT, numAffilie);
            properties.setProperty(JadeGedAdapterAPIParameters.CODE_LIEN_DOSSIER_PARENT, "10600");
            properties.setProperty(JadeGedAdapterAPIParameters.CODE_DEPARTEMENT, "AF");
            if (!JadeStringUtil.isBlank(dateCreation)) {
                properties.setProperty(JadeGedAdapterAPIParameters.DATE_OUVERTURE_DOSSIER,
                        JACalendar.format(dateCreation, JACalendar.FORMAT_YYYYMMDD));
            }
            JadeGedFacade.propagate(GroupdocPropagateUtil.getAdapterName(), properties);
            if (GroupdocPropagateUtil.isVerbose()) {
                JadeLogger.info(GroupdocPropagateUtil.class, "Folder propagated: " + indexKey);
            }
        }
    }

    /**
     * Indique le nom de l'adaptateur GED à utiliser.
     * 
     * @param adapterName
     *            le nom de l'adaptateur GED à utiliser
     */
    protected final static void setAdapterName(String adapterName) {
        GroupdocPropagateUtil.adapterName = adapterName;
    }

    /**
     * Indique si la propagation des dossiers doit être effectuée ou non.
     * 
     * @param shouldPropagate
     *            true si la propagation des dossiers doit être effectuée, false sinon
     */
    protected final static void setShouldPropagate(boolean shouldPropagate) {
        GroupdocPropagateUtil.shouldPropagate = shouldPropagate;
    }

    /**
     * Indique si la propagation des dossiers doit être effectuée en mode verbose ou non.
     * 
     * @param verbose
     *            true si la propagation des dossiers doit être effectuée en mode verbose , false sinon
     */
    protected final static void setVerbose(boolean verbose) {
        GroupdocPropagateUtil.verbose = verbose;
    }

    /**
     * Indique si la propagation des dossiers doit être effectuée.
     * 
     * @return true si la propagation des dossiers doit être effectuée, false sinon
     */
    public final static boolean shouldPropagate() {
        return GroupdocPropagateUtil.shouldPropagate;
    }

    /**
     * Private constructor
     */
    private GroupdocPropagateUtil() {
    }
}
