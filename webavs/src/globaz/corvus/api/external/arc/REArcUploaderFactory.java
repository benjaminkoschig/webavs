/**
 * 
 */
package globaz.corvus.api.external.arc;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.tools.PRSession;
import java.util.Map;

/**
 * 
 * Description : Factory de chargeur (uploader) d'arc.
 * 
 * Cette factory va instancier et retourner l'uploader suivant les type d'annonce à uploader.
 * 
 * @author SCR
 * 
 */
public abstract class REArcUploaderFactory {

    /**
     * la cle pour obtenir la classe d'implementation du CI_UPLOADER dans CORVUS.properties
     */
    private static final String CI_UPLOADER = "globaz.corvus.arc.uploader.inscription.ci";
    public static final int KEY_UPLOADER_AUGMENTATION = 200;

    public static final int KEY_UPLOADER_INSCRIPTION_CI = 100;

    /** contient tous les uploaders deja instancie */
    private static Map UPLOADERS;

    /**
     * la cle pour obtenir la classe d'implementation du AUGMENTATION_UPLOADER dans CORVUS.properties
     */
    // private static final String AUGMENTATION_UPLOADER =
    // "globaz.corvus.arc.uploader.augmentation";

    /**
     * getter pour l'attribut instance.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut instance
     * @throws Exception
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final synchronized IREUploader getUploader(BISession session, int uploaderKey) throws Exception {

        BISession sessionRente = PRSession.connectSession(session, "CORVUS");

        if (KEY_UPLOADER_INSCRIPTION_CI == uploaderKey) {
            // si l'uploader de ce type n'es pas dans la map on l'ajoute
            if (!UPLOADERS.containsKey(new Integer(KEY_UPLOADER_INSCRIPTION_CI))) {
                try {
                    String cName = ((BSession) sessionRente).getApplication().getProperty(CI_UPLOADER);
                    IREUploader uploader = (IREUploader) Class.forName(cName).newInstance();
                    uploader.setSession(session);

                    UPLOADERS.put(new Integer(uploaderKey), uploader);

                } catch (Exception e) {
                    throw new REUploaderException("impossible de creer une instance de la factory des Uploader : "
                            + uploaderKey);
                }
            }

        } else {
            throw new REUploaderException("La clé suivante n'est pas reconnue par la focaory: " + uploaderKey);
        }
        return (IREUploader) UPLOADERS.get(new Integer(uploaderKey));
    }

    /**
     * Crée une nouvelle instance de la classe REArcUploaderFactory.
     */
    protected REArcUploaderFactory() {
    }
}
