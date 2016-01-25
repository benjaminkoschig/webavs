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
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Module de facturation ALFAGEST, appelé par MUSCA
 */
public class ALFacturationModule implements IntModuleFacturation {

    protected static int ALL_COTISATIONS = 0;
    protected static int PAR_COTISATIONS = 2;
    protected static int PERSO_COTISATIONS = 1;

    /**
     * Méthode appelée pour annuler le passage
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
     * Si le module souhaite que les afacts qu'il a généré soit supprimés avant la génération retourner true
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
     * Si le module souhaite que les afacts qu'il a généré soit supprimés avant la génération return true
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
     * Méthode principale appelée pour la génération. (Devrait changer... les méthodes regénérer et reprises devraient
     * être appelées) Cette méthode est toujours appelée (lors le la première génération, lors de la regénération, lors
     * de la reprise sur erreur)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // process de facturation
        ALFacturationProcess myProcess = new ALFacturationProcess();

        // paramétrisation du process
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
     * 
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
     * Effectue un rollback du passage sur les prestations, CO -> TR
     */
    private void rollback(IFAPassage passage, BProcess context) throws Exception {
        // managers des prestations et des recaps
        ALEntetePrestationManager prestations = new ALEntetePrestationManager();

        // entités prestation et recap
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

            // ...change l'état...
            prestation.setEtatPrestation("TR");
            // ...annule le passage...
            prestation.setIdPassageFacturation("");
            // ...enlève la date de facturation...
            prestation.setDateVersementCompensation("");

            // ...et enregistre
            try {
                prestation.save(transaction);
            } catch (Exception e) {
                throw new Exception("Impossible de mettre à jour une prestation: " + e.toString());
            }

            // traitement de la recap
            try {
                // on charge la recap...
                recap.setIdRecap(prestation.getIdRecap());
                recap.setSession(context.getSession());
                recap.retrieve(transaction);

                // on continue sur cette recap seulement si elle n'est pas déjà TR
                if (!recap.getEtat().equals("TR")) {
                    // ..change l'état...
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
                throw new Exception("Impossible de mettre à jour une récap: " + e.getMessage());
            }
        }
    }

    /**
     * Taches à effectuer si l'on souhaite enlever le module du passage
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}