package ch.globaz.al.facturation;

import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import ch.globaz.al.api.facturation.ALFacturationInterfaceImpl;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.facturation.process.ALFacturationProcess;

public class ALFacturationModule implements IntModuleFacturation {

    /**
     * M�thode appel�e pour annuler le passage
     */
    public boolean annuler(IFAPassage passage, BProcess context) throws Exception {
        rollback(passage, context);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRecomptabiliser(globaz.musca.api.IFAPassage,
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
        rollback(passage, context);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrCom(globaz.musca.api.IFAPassage,
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
        rollback(passage, context);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca.api.IFAPassage,
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
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // process de facturation
        ALFacturationProcess factuAF = new ALFacturationProcess();

        // param�trisation du process
        factuAF.setTypeCoti(ALConstPrestations.TYPE_INDIRECT_GROUPE);
        factuAF.setIdTypeFacturation(passage.getIdTypeFacturation());
        factuAF.setIdModuleFacturation(idModuleFacturation);
        factuAF.setParentWithCopy(context);
        factuAF.setNumeroPassage(passage.getIdPassage());
        factuAF.setDateComptable(passage.getDateFacturation());
        factuAF.setPeriodeComptable(passage.getDatePeriode());
        factuAF.setSession(context.getSession());
        factuAF.setTransaction(context.getTransaction());

        // lancement du process
        return factuAF._executeProcess();
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
     * 
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
     * Effectue un rollback du passage sur les prestations, CO -> TR
     */
    private void rollback(IFAPassage passage, BProcess context) throws Exception {
        BTransaction transaction = context.getTransaction();
        ALFacturationInterfaceImpl factuAPI = new ALFacturationInterfaceImpl();
        factuAPI.rollbackRecaps(passage.getId(), transaction);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }
    }

    /**
     * Taches � effectuer si l'on souhaite enlever le module du passage
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
