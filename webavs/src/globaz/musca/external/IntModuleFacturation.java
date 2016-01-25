package globaz.musca.external;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;

/**
 * Cette classe définit l'interface pour tous les modules de facturation. <BR>
 * Les modules sont executés un après un, si l'execution d'un module échoue le processus s'arrête. <BR>
 * Chaque méthode reçoit en parametre, le passage de facturation ainsi que le processus parent qui appelle le module <BR>
 * Pour signaler au processus parent qu'une erreur est survenue, ajouter un message dans la transaction parent:
 * getTransaction()._addError("Une erreur"). <BR>
 * Pour suivre en détail l'utilisation du module par la facturation
 * 
 * @see globaz.musca.process.FAPassageFacturationProcess <BR>
 *      Date de création : (01.04.2003 18:01:11)
 * @author: Administrator
 */
public interface IntModuleFacturation {

    // Retourne true si tous les Afacts doivent être effacés avant l'opération,
    // et false sinon
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Si le module souhaite que les afacts qu'il a généré soit supprimés avant la génération retourner true
     */
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModule) throws Exception;

    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Si le module souhaite que les afacts qu'il a généré soit supprimés avant la génération return true
     */
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModule) throws Exception;

    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Méthode principale appelée pour la génération. (Devrait changer... les méthodes regénérer et reprises devraient
     * être appelées) Cette méthode est toujours appelée (lors le la première génération, lors de la regénération, lors
     * de la reprise sur erreur)
     */
    public boolean generer(IFAPassage passage, BProcess context, String idModule) throws Exception;

    /**
     * Si le module a un document à imprimer et à envoyer par e-mail
     */
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Cette méthodes n'est pas encore appelée par le processus de comptabilisation
     */
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Cette méthode serait appelée lorsque le module a déjà été généré avec succès mais qu'une regénération a été
     * demandé. N'est pas encore appelée par le processus de génération: généré() est appelé
     */
    public boolean regenerer(IFAPassage passage, BProcess context, String idModule) throws Exception;

    /**
     * Cette méthodes n'est pas encore appelée par le processus de comptabilisation
     */
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Cette méthode serait appelée lorsque le module a déjà été généré mais c'est arrété sur une erreur N'est pas
     * encore appelée par le processus de génération: généré() est appelé
     */
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModule) throws Exception;

    /**
     * Taches à effectuer si l'on souhaite enlever le module du passage
     */
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception;
}
