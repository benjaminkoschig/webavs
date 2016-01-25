package ch.globaz.orion.axis.handler;

/**
 * Classe représentant les tags d'entête du SOAP
 * 
 * @author FGO
 * 
 */
public interface SoapHeaderTags {
    /** niveau du log message (level) */
    public final static String LOG_MESSAGE_LEVEL = "level";
    /** nom local du log message (local-message) */
    public final static String LOG_MESSAGE_LOCAL_NAME = "local-message";
    /** id du message du log message (message-id) */
    public final static String LOG_MESSAGE_MESSAGE_ID = "message-id";
    /** message du log message (messages) */
    public final static String LOG_MESSAGE_MESSAGES = "messages";
    /** paramètre du log message (parameter) */
    public final static String LOG_MESSAGE_PARAMETER = "parameter";
    /** source du log message (source) */
    public final static String LOG_MESSAGE_SOURCE = "source";
    /** nom local des messages (local-messages) */
    public final static String LOG_MESSAGES_LOCAL_NAME = "local-messages";
    /** nom du propriétaire local (owner) */
    public final static String OWNER_LOCAL_NAME = "owner";
    /** nom du propriétaire local (owner) */
    public final static String OWNER_USER_LOCAL_NAME = "control";
    /** password de la session locale (password-session) */
    public final static String PASSWORD_SESSION_LOCAL_NAME = "password-session";
    /** prefixe (globaz) */
    public final static String PREFIX_NAMESPACE = "globaz";
    /** adresse du target (http://eb.globaz.ch) */
    public final static String TARGET_URI = "http://eb.globaz.ch";
    /** User Name de la session */
    public final static String USER_EMAIL_SESSION_LOCAL_NAME = "username-email";
    /** User Name de la session */
    public final static String USER_LANGUE_SESSION_LOCAL_NAME = "username-langue";
    /** User Name de la session */
    public final static String USER_NAME_SESSION_LOCAL_NAME = "username-session";
    /** utilisateur de la session locale (user-session) */
    public final static String USER_SESSION_LOCAL_NAME = "user-session";
}
