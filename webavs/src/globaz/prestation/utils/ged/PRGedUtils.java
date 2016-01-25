package globaz.prestation.utils.ged;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedDocument;
import globaz.jade.log.JadeLogger;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSCaisse;

/**
 * <p>
 * Utilitaire permettant de savoir si une GED est présente sur l'application, et si un document spécifique est défini
 * dans cette GED.
 * </p>
 * 
 * @author PBA
 */
public class PRGedUtils {

    public static final boolean isDocumentInGed(String documentNumber) {
        return PRGedUtils.isDocumentInGed(documentNumber, null);
    }

    /**
     * <p>
     * Effectue une recherche sur tout les 'document number' fournis en paramètre.
     * </p>
     * Si un de ces documents est géré par la GED, la méthode retournera <code>true</code> sinon retourne
     * <code>false</code>
     * 
     * @param documentsNumber Liste contenant les documents number à tester
     * @return <code>true</code> si un des documents number est géré par la GED sinon <code> false</code>
     */
    public static final boolean isDocumentsInGed(List<String> documentsNumber, BSession session) {
        if (documentsNumber == null) {
            return false;
        }
        for (String docNumber : documentsNumber) {
            if (PRGedUtils.isDocumentInGed(docNumber, session)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Recherche du document (dont le nom est passé en paramètre) dans la GED.<br/>
     * La session est nécessaire pour chercher dans les propriétés si une GED spécifique doit être utilisée pour la
     * recherche du document. La propriété <code>"ged.targetName"</code> sera utilisée (si elle existe) comme nom de
     * GED. Ce mécanisme permet d'avoir plusieurs GED sur l'application, et de rechercher les documents d'un module dans
     * la bonne GED.
     * </p>
     * <p>
     * Attention : la session doit être connectée à la bonne application selon le module dans lequel vous faite appelle
     * à cette utilitaire.
     * </p>
     * 
     * @param documentNumber
     *            le numéro de document (unique, très souvent le numéro de document Inforom)
     * @param session
     *            une session qui sera utilisée pour recherche dans les propriétés
     * @return <code>true</code> si le document est spécifié dans la GED, sinon <code>false</code>
     */
    public static final boolean isDocumentInGed(String documentNumber, BSession session) {
        return PRGedUtils.isDocumentInGed(documentNumber, null, session);
    }

    /**
     * <p>
     * Spécifique PCF, pour multi GED sur même application avec deux caisses dessus.
     * </p>
     * <p>
     * Ajoute un teste pour savoir si l'appelle est fait depuis l'application de la caisse principale ou non.
     * </p>
     * 
     * @param documentNumber
     * @param nomCaisse
     * @param session
     * @return
     */
    public static final boolean isDocumentInGed(String documentNumber, String csCaisse, BSession session) {

        if (PRGedUtils.isGedActive()) {

            try {

                if (!JadeStringUtil.isEmpty(csCaisse)) {
                    CSCaisse caisse = CSCaisse.getEnumFromCodeSystem(csCaisse);
                    if (caisse != null) {
                        switch (caisse) {
                            case AGENCE_LAUSANNE:
                                if (!Boolean.parseBoolean(session.getApplication().getProperty(
                                        "perseus.ged.caisse.secondaire"))) {
                                    return false;
                                }
                                break;
                            case CCVD:
                                if (!Boolean.parseBoolean(session.getApplication().getProperty(
                                        "perseus.ged.caisse.principale"))) {
                                    return false;
                                }
                                break;
                        }
                    }
                }

                List<JadeGedDocument> documents;
                if (session != null) {
                    String targetName = session.getApplication().getProperty("ged.targetName");
                    documents = JadeGedFacade.getDocumentList(targetName);
                } else {
                    documents = JadeGedFacade.getDocumentList();
                }

                for (JadeGedDocument unDocument : documents) {
                    if (documentNumber.equals(unDocument.getApplicationDocumentType())
                            || documentNumber.startsWith(unDocument.getApplicationDocumentType())) {
                        return true;
                    }
                }
            } catch (Exception ex) {
                JadeLogger.error(PRGedUtils.class, ex.toString());
                return false;
            }
        }
        return false;

    }

    /**
     * Retournera <code>true</code> si une GED est déclarée dans le fichier de configuration
     * JadeClientServiceLocator.xml.
     * 
     * @return
     */
    public static final boolean isGedActive() {
        return JadeGedFacade.isInstalled();
    }
}
