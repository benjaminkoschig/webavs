package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.process.FAPassageAjouterTableProcess;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
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
     * Méthode appelée pour annuler le passage
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
     * Si le module souhaite que les afacts qu'il a généré soit supprimés avant la génération retourner true
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
     * Si le module souhaite que les afacts qu'il a généré soit supprimés avant la génération return true
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
     * Méthode principale appelée pour la génération. (Devrait changer... les méthodes regénérer et reprises devraient
     * être appelées) Cette méthode est toujours appelée (lors le la première génération, lors de la regénération, lors
     * de la reprise sur erreur)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModule) throws Exception {
        // process de facturation
        FAPassageAjouterTableProcess myProcess = new FAPassageAjouterTableProcess();

        // paramétrisation du process
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
     * Si le module a un document à imprimer et à envoyer par e-mail
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Cette méthodes n'est pas encore appelée par le processus de comptabilisation
     */
    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Cette méthode serait appelée lorsque le module a déjà été généré avec succès mais qu'une regénération a été
     * demandé. N'est pas encore appelée par le processus de génération: généré() est appelé
     */
    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * Cette méthodes n'est pas encore appelée par le processus de comptabilisation
     */
    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Cette méthode serait appelée lorsque le module a déjà été généré mais c'est arrété sur une erreur N'est pas
     * encore appelée par le processus de génération: généré() est appelé
     */
    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    /**
     * Taches à effectuer si l'on souhaite enlever le module du passage
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
