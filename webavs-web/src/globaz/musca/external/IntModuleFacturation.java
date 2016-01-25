package globaz.musca.external;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;

/**
 * Cette classe d�finit l'interface pour tous les modules de facturation. <BR>
 * Les modules sont execut�s un apr�s un, si l'execution d'un module �choue le processus s'arr�te. <BR>
 * Chaque m�thode re�oit en parametre, le passage de facturation ainsi que le processus parent qui appelle le module <BR>
 * Pour signaler au processus parent qu'une erreur est survenue, ajouter un message dans la transaction parent:
 * getTransaction()._addError("Une erreur"). <BR>
 * Pour suivre en d�tail l'utilisation du module par la facturation
 * 
 * @see globaz.musca.process.FAPassageFacturationProcess <BR>
 *      Date de cr�ation : (01.04.2003 18:01:11)
 * @author: Administrator
 */
public interface IntModuleFacturation {

    // Retourne true si tous les Afacts doivent �tre effac�s avant l'op�ration,
    // et false sinon
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Si le module souhaite que les afacts qu'il a g�n�r� soit supprim�s avant la g�n�ration retourner true
     */
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModule) throws Exception;

    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Si le module souhaite que les afacts qu'il a g�n�r� soit supprim�s avant la g�n�ration return true
     */
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModule) throws Exception;

    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception;

    /**
     * M�thode principale appel�e pour la g�n�ration. (Devrait changer... les m�thodes reg�n�rer et reprises devraient
     * �tre appel�es) Cette m�thode est toujours appel�e (lors le la premi�re g�n�ration, lors de la reg�n�ration, lors
     * de la reprise sur erreur)
     */
    public boolean generer(IFAPassage passage, BProcess context, String idModule) throws Exception;

    /**
     * Si le module a un document � imprimer et � envoyer par e-mail
     */
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Cette m�thodes n'est pas encore appel�e par le processus de comptabilisation
     */
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Cette m�thode serait appel�e lorsque le module a d�j� �t� g�n�r� avec succ�s mais qu'une reg�n�ration a �t�
     * demand�. N'est pas encore appel�e par le processus de g�n�ration: g�n�r�() est appel�
     */
    public boolean regenerer(IFAPassage passage, BProcess context, String idModule) throws Exception;

    /**
     * Cette m�thodes n'est pas encore appel�e par le processus de comptabilisation
     */
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception;

    /**
     * Cette m�thode serait appel�e lorsque le module a d�j� �t� g�n�r� mais c'est arr�t� sur une erreur N'est pas
     * encore appel�e par le processus de g�n�ration: g�n�r�() est appel�
     */
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModule) throws Exception;

    /**
     * Taches � effectuer si l'on souhaite enlever le module du passage
     */
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception;
}
