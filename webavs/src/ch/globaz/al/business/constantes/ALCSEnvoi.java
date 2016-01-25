/**
 * 
 */
package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux envois
 * 
 * <ul>
 * <li>Type de job/envoi</li>
 * <li>Status d'un job/envoi</li>
 * </ul>
 * 
 * @author dhi
 * 
 */
public interface ALCSEnvoi {

    /**
     * CS - type de document ged pour publication
     */
    public static final String GED_DOCUMENT_TYPE = "61450005";
    /**
     * CS - type de document ged number pour publication
     */
    public static final String GED_DOCUMENT_TYPE_NUMBER = "61450006";
    /**
     * CS - répertoire partagé, path visible depuis le serveur applicatif
     */
    public static final String SHARED_PATH_FROM_APPLICATION_SERVER = "61450002";
    /**
     * CS - répertoire partagé, path visible depuis le client
     */
    public static final String SHARED_PATH_FROM_CLIENT = "61450001";
    /**
     * CS - répertoire partagé, path visible depuis le serveur de job
     */
    public static final String SHARED_PATH_FROM_JOB_SERVER = "61450003";
    /**
     * CS - répertoire partagé, path visible depuis le serveur de publication
     */
    public static final String SHARED_PATH_FROM_PUBLICATION_SERVER = "61450004";
    /**
     * CS - Status d'un envoi généré
     */
    public static final String STATUS_ENVOI_GENERATED = "61420001";
    /**
     * CS - Status d'un envoi imprimé
     */
    public static final String STATUS_ENVOI_IN_PROGRESS = "61420002";
    /**
     * CS - Status d'un envoi envoyé
     */
    public static final String STATUS_ENVOI_SENT = "61420003";
    /**
     * CS - type d'un envoi automatique (topaz)
     */
    public static final String TYPE_ENVOI_AUTOMATIC = "61430002";
    /**
     * CS - type d'un envoi manuel (word)
     */
    public static final String TYPE_ENVOI_MANUAL = "61430001";
    /**
     * CS - type d'un envoi process (topaz multiple)
     */
    public static final String TYPE_ENVOI_PROCESS = "61430003";

}
