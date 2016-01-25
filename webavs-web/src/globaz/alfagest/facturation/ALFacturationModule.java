package globaz.alfagest.facturation;

import globaz.alfagest.db.ALEntetePrestation;
import globaz.alfagest.db.ALEntetePrestationManager;
import globaz.alfagest.db.ALRecap;
import globaz.alfagest.process.ALFacturationProcess;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.IntModuleFacturation;

/**
 * Cr�� le 18 janv. 06
 * 
 * @author dch
 * 
 *         Module de facturation ALFAGEST, appel� par MUSCA
 */
public class ALFacturationModule implements IntModuleFacturation {

    protected static int ALL_COTISATIONS = 0;
    protected static int PAR_COTISATIONS = 2;
    protected static int PERSO_COTISATIONS = 1;

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
        ALFacturationProcess myProcess = new ALFacturationProcess();

        // param�trisation du process
        myProcess.setTypeCotisation(ALFacturationModule.ALL_COTISATIONS);
        myProcess.setTypeModule(FAModuleFacturation.CS_MODULE_PRESTATIONSAF);
        myProcess.setParentWithCopy(context);
        myProcess.setNumeroPassage(passage.getIdPassage());
        myProcess.setDateComptable(passage.getDateFacturation());
        myProcess.setSession(context.getSession());
        myProcess.setTransaction(context.getTransaction());
        myProcess.setIdModuleFacturation(idModuleFacturation);
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
        // managers des prestations et des recaps
        ALEntetePrestationManager prestations = new ALEntetePrestationManager();

        // entit�s prestation et recap
        ALEntetePrestation prestation = null;
        ALRecap recap = new ALRecap();

        // transaction
        BTransaction transaction = context.getTransaction();

        // on charge l'ensemble des prestations pour le passage
        try {
            prestations.setIdPassageFacturation(passage.getId());

            prestations.setSession(context.getSession());
            prestations.find(transaction, 0);
        } catch (Exception e) {
            throw new Exception("Impossible de charger les prestations: " + e.toString());
        }

        // pour chaque prestation du passage, il faut appliquer le rollback CO -> TR
        // dans la prestation et dans la recap
        for (int i = 0; i < prestations.size(); i++) {
            // on charge la prestation...
            prestation = (ALEntetePrestation) prestations.get(i);

            // ...change l'�tat...
            prestation.setEtatPrestation("TR");
            // ...annule le passage...
            prestation.setIdPassageFacturation("");
            // ...enl�ve la date de facturation...
            prestation.setDateVersementCompensation("");

            // ...et enregistre
            try {
                prestation.save(transaction);
            } catch (Exception e) {
                throw new Exception("Impossible de mettre � jour une prestation: " + e.toString());
            }

            // traitement de la recap
            try {
                // on charge la recap...
                recap.setIdRecap(prestation.getIdRecap());
                recap.setSession(context.getSession());
                recap.retrieve(transaction);

                // on continue sur cette recap seulement si elle n'est pas d�j� TR
                if (!recap.getEtat().equals("TR")) {
                    // ..change l'�tat...
                    if (recap.getIdRecap().equals("99999999")) {
                        recap.setEtat("PR");
                    } else {
                        recap.setEtat("TR");
                    }

                    // ...et enregristre
                    recap.save(transaction);
                }

                // erreur dans la transaction?
                if (transaction.hasErrors()) {
                    throw new Exception(transaction.getErrors().toString());
                }
            } catch (Exception e) {
                throw new Exception("Impossible de mettre � jour une r�cap: " + e.getMessage());
            }
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