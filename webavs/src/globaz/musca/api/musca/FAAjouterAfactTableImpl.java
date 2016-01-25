package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.process.FAPassageAjouterTableProcess;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class FAAjouterAfactTableImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public FAAjouterAfactTableImpl() {
        super();
    }

    /**
     * M�thode appel�e pour annuler le passage
     */
    public boolean annuler(IFAPassage passage, BProcess context) throws Exception {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRecomptabiliser(globaz .musca.api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return true;
    }

    /**
     * Si le module souhaite que les afacts qu'il a g�n�r� soit supprim�s avant la g�n�ration retourner true
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrCom(globaz. musca.api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return true;
    }

    /**
     * Si le module souhaite que les afacts qu'il a g�n�r� soit supprim�s avant la g�n�ration return true
     */
    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca .api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * M�thode principale appel�e pour la g�n�ration. (Devrait changer... les m�thodes reg�n�rer et reprises devraient
     * �tre appel�es) Cette m�thode est toujours appel�e (lors le la premi�re g�n�ration, lors de la reg�n�ration, lors
     * de la reprise sur erreur)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModule) throws Exception {
        // process de facturation
        FAPassageAjouterTableProcess myProcess = new FAPassageAjouterTableProcess();

        // param�trisation du process
        myProcess.setParentWithCopy(context);
        myProcess.setNumPassage(passage.getIdPassage());
        myProcess.setSession(context.getSession());
        myProcess.setTransaction(context.getTransaction());
        myProcess.setIdModuleFacturation(idModule);

        // lancement du process
        return myProcess._executeProcessFacturation();

        // pour lancer un rollback en manuel
        // rollback(passage, context);
        // return true;
    }

    /**
     * Si le module a un document � imprimer et � envoyer par e-mail
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Cette m�thodes n'est pas encore appel�e par le processus de comptabilisation
     */
    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Cette m�thode serait appel�e lorsque le module a d�j� �t� g�n�r� avec succ�s mais qu'une reg�n�ration a �t�
     * demand�. N'est pas encore appel�e par le processus de g�n�ration: g�n�r�() est appel�
     */
    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * Cette m�thodes n'est pas encore appel�e par le processus de comptabilisation
     */
    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Cette m�thode serait appel�e lorsque le module a d�j� �t� g�n�r� mais c'est arr�t� sur une erreur N'est pas
     * encore appel�e par le processus de g�n�ration: g�n�r�() est appel�
     */
    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * Taches � effectuer si l'on souhaite enlever le module du passage
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
