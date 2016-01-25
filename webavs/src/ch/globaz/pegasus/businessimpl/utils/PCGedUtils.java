package ch.globaz.pegasus.businessimpl.utils;

import globaz.globall.db.BSession;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedDocument;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.utils.ged.PRGedUtils;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;

/**
 * Classe encapsulant PRGedUtils, utilisé par PC pour gérer les actions liés à la GED
 * 
 * @author sce
 * 
 */
public class PCGedUtils {

    /** prefixe pegasus pour l'url */
    private static final String GED_PC_URL_PREFIX = "pegasus?userAction=pegasus.dossier.actionAfficherDossierGed";
    /* dans le cas de module PC sans GED */
    private static final String NO_GED_DEFINED = "none";
    /** Chaine pour génération de l'url */
    private static final String PARAM_ADDITIONER = "&";
    private static final String PARAM_EQUALS = "=";
    private static final String PARAM_ID_TIERS = "idTiersExtraFolder";
    private static final String PARAM_NO_AVS = "noAVSId";
    private static final String PARAM_SERVICE_NAME = "serviceNameId";

    /**
     * Méthode générant l'url sur la ged pour le projet PC Est destiné à être utilisé dans les écrans.</br> Les
     * paramètres ne peuvent pas être null. Dans le cas contraire une <b>NullPointerException</b> est levé<br/>
     * 
     * @param noAvs
     *            le no avs concerné - <b>ne peut pas être null</b>
     * @param idTiers
     *            l'id du tiers concerné - <b>ne peut pas être null</b>
     * @return chaine de caratère retournant l'url du lien sur la GED
     * @throws PropertiesException
     *             levé si un problème survient lors de l'appel à la propriété serviceNameId
     * @throws NullPointerException
     *             si au moins un des deux paramètres est null
     */
    public static String generateAndReturnGEDUrl(String noAvs, String idTiers) throws PropertiesException {
        if ((null == noAvs) || (null == idTiers)) {
            throw new NullPointerException(
                    "The url for the ged cannot be build, the params cannot be null [noAvs, idTiers]");
        }
        String serviceNameId = PCproperties.getProperties(EPCProperties.DOMAINE_NOM_SERVICE_GED);

        StringBuilder url = new StringBuilder(PCGedUtils.GED_PC_URL_PREFIX);
        url.append(PCGedUtils.PARAM_ADDITIONER).append(PCGedUtils.PARAM_ID_TIERS).append(PCGedUtils.PARAM_EQUALS)
                .append(idTiers);
        url.append(PCGedUtils.PARAM_ADDITIONER).append(PCGedUtils.PARAM_NO_AVS).append(PCGedUtils.PARAM_EQUALS)
                .append(noAvs);
        url.append(PCGedUtils.PARAM_ADDITIONER).append(PCGedUtils.PARAM_SERVICE_NAME).append(PCGedUtils.PARAM_EQUALS)
                .append(serviceNameId);

        return url.toString();

    }

    public static final boolean isDocumentInGed(String documentNumber) {
        return PRGedUtils.isDocumentInGed(documentNumber, null);
    }

    /**
     * <p>
     * Recherche du document (dont le nom est passé en paramètre) dans la GED.<br/>
     * La session est nécessaire pour chercher dans les propriétés si une GED spécifique doit être utilisée pour la
     * recherche du document. La propriété <code>"ged.targetName"</code> sera utilisée (si elle existe) comme nom de
     * GED. Ce mécanisme permet d'avoir plusieurs GED sur l'application, et de rechercher les documents d'un module dans
     * la bonne GED.
     * </p>
     * 
     * @param documentNumber
     *            le numéro de document (unique, très souvent le numéro de document Inforom)
     * @param session
     *            une session qui sera utilisée pour recherche dans les propriétés
     * @return <code>true</code> si le document est spécifié dans la GED, sinon <code>false</code>
     * @throws PropertiesException
     * @throws JadeClassCastException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws JadeServiceActivatorException
     * @throws JadeServiceLocatorException
     */
    public static final boolean isDocumentInGed(String documentNumber, BSession session) throws PropertiesException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {

        if (session == null) {
            throw new RuntimeException("The session cannot be null to find if the document is realy destined to ged["
                    + PCGedUtils.class.getName() + "]");
        }

        // récipération de la valeur de la ged à utiliser
        String targetName = PCproperties.getProperties(EPCProperties.GED_PC_TARGET_NAME);

        if (isGedActiveAndDdeclaredForPC(targetName)) {
            List<JadeGedDocument> documents;

            documents = JadeGedFacade.getDocumentList(targetName);

            for (JadeGedDocument unDocument : documents) {
                if (documentNumber.equals(unDocument.getApplicationDocumentType())
                        || documentNumber.startsWith(unDocument.getApplicationDocumentType())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retournera <code>true</code> si une GED est déclarée dans le fichier de configuration
     * JadeClientServiceLocator.xml. et que
     * 
     * @return etat de déclaration de GED
     */
    public static final boolean isGedActive() {
        return JadeGedFacade.isInstalled();
    }

    /**
     * La ged doit être active, et la propriété PC (pegasus.ged.targetName) ne doit pas avoir la valeur "none"
     * 
     */
    public static final boolean isGedActiveAndDdeclaredForPC(String targetName) {
        return isGedActive() && !targetName.equals(PCGedUtils.NO_GED_DEFINED);
    }
}
