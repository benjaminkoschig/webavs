package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAModulePlan;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;
import java.util.Date;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageFacturationProcess_OLD extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {

        System.out
                .println("Utiliser FAPassageFacturationProcess <user> <pwd> <idPassage> <from> <to> <email> <y> pour JADE\n");
        FAPassageFacturationProcess_OLD process = null;
        String user = "tstscs";
        String pwd = "eak2cfc";
        String idPassage = "";
        String from = null; // "180.100";
        String to = null; // "190.000";
        String email = "btc@globaz.ch";
        String useJade = "n";
        long t0 = (new Date()).getTime();
        if (args.length > 0) {
            user = args[0];
        }
        if (args.length > 1) {
            pwd = args[1];
        }
        if (args.length > 2) {
            idPassage = args[2];
        }
        if (args.length > 3) {
            from = args[3];
        }
        if (args.length > 4) {
            to = args[4];
        }
        if (args.length > 5) {
            useJade = args[5];
        }
        System.out.println("User : " + user);
        System.out.println("Password : " + pwd);
        System.out.println("IdPassage : " + idPassage);
        System.out.println("From : " + from);
        System.out.println("To : " + to);
        System.out.println("useJade:" + useJade);
        System.out.println("Email : " + email);
        process = new FAPassageFacturationProcess_OLD();
        try {
            process.setIdPassage(idPassage);
            if (from != null) {
                process.setFromIdExterneRole(from);
            }
            if (to != null) {
                process.setTillIdExterneRole(to);
            }
            // utilisation de JADE
            if ("y".equalsIgnoreCase(useJade)) {
                FAGenericProcess.jadeProfile(process);
            } else {

                BSession session = new BSession("MUSCA");
                session.connect(user, pwd);

                process.setSession(session);
                process.setEMailAddress(email);
                process.setActionModulePassage(FAModulePassage.CS_ACTION_VIDE);
                process.setTransaction(new BTransaction(process.getSession()));
                process.getTransaction().openTransaction();
                process.executeProcess();

            }

            System.out.println("Programme terminé ! Copier le fichier PDF avant de presser <Enter>....");
            System.out.println("Temps : " + (((new Date()).getTime() - t0) / 1000.0));
            System.in.read();
            System.out.println("Arrêt du programme lancé !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    private boolean enchainerComptabilisation = false;

    /**
     * Constructeur FAPassageFacturationProcess
     */
    public FAPassageFacturationProcess_OLD() {
        super();
        setSendMailOnError(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @auteur: BTC
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // On récupère le passage grâce a idPassage
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve(getTransaction());

            if ((passage != null) && !passage.isNew()) {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_REUSSIE") + passage.getLibelle()
                        + " (" + getIdPassage() + ")");
            } else {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_ECHOUEE") + passage.getLibelle()
                        + " (" + getIdPassage() + ")");
            }

            /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
             * Vérification si le passage peut être généré
             **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
            boolean passageEstVerrouille = passage.isEstVerrouille().booleanValue();
            if (FAPassage.CS_ETAT_COMPTABILISE.equals(passage.getStatus()) || passageEstVerrouille
                    || passage.getStatus().equals(FAPassage.CS_ETAT_ANNULE)) {
                this.logInfo4Process(false, "OBJEMAIL_FA_ISVERROUILLE_INFO");
                throw (new Exception("Facturation refusée car le passage est comptabilisé, verrouillé ou annulé."));
            }

            try {
                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Initialisation *********************************************** **************************
                 */
                _initializePassage(passage);

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Ajouts des modules systèmes **********************************
                 * ***************************************
                 */

                // Instancier le ModuleFacturationManager
                FAModuleFacturationManager moduleFacturationManager = new FAModuleFacturationManager();
                moduleFacturationManager.setSession(getSession());

                // Tous les modules du passage contrôlés par leur manager
                FAModulePassageManager modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(getSession());
                modulePassageManager.setForIdPassage(getIdPassage());
                // Trier par niveau d'appel
                modulePassageManager.orderByNiveauAppel();
                modulePassageManager.find(getTransaction());

                // preparer tous les modules du plan associés au passage pour le
                // passage ouvert
                if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {
                    prepareAllModule(moduleFacturationManager, modulePassageManager);
                    // recharger le manager
                    modulePassageManager.find(getTransaction());
                }

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Traitement des modules dépendants ****************************
                 * *********************************************
                 */
                regenerateModulesDependants(modulePassageManager);
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                } else {
                    getTransaction().rollback();
                }

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Exécution des modules **************************************** *********************************
                 */
                FAAfactManager manager = null;

                // 15.02.2005: BTC : si un module force l'enchainement sur la
                // comptabilisation, on comptabilise à la suite.
                if (!getTransaction().hasErrors() && isEnchainerComptabilisation()) {
                    iterateModulePassage(modulePassageManager, manager);
                }

            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            } finally {
                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Vérification du log ****************************************** *******************************
                 */
                // logger toutes les erreurs des modules
                this.logInfo4Process(getTransaction().getErrors().toString());
                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Fin de la procédure ****************************************** *******************************
                 */
                _finalizePassage(passage);
            }
        } catch (Exception e) {
            this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
            return false;
        }

        return !isAborted();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.03.2003 13:28:02)
     * 
     * @author: BTC
     */
    public boolean addModulesSystem(FAModuleFacturationManager moduleFacturationManager,
            FAModulePassageManager modulePassageManager) {
        // module du plan de facturation
        FAModulePlan modulePlan = new FAModulePlan();
        modulePlan.setSession(getSession());
        // Ajouter les modules périodiques aux modules du passage
        // ----------------------------------------------------
        for (int i = 0; i < moduleFacturationManager.size(); i++) {
            // tous les modules de facturation
            FAModuleFacturation moduleFacturation = null;
            moduleFacturation = (FAModuleFacturation) moduleFacturationManager.getEntity(i);
            // utiliser <= car il se peut qu'il n'y a encore aucun module du
            // même type
            for (int j = 0; j <= modulePassageManager.size(); j++) {
                // tous les modules du passage
                FAModulePassage modulePassage = new FAModulePassage();
                modulePassage.setSession(getSession());
                modulePassage.setIdPassage(passage.getIdPassage());
                modulePassage.setIdModuleFacturation(moduleFacturation.getIdModuleFacturation());
                try {
                    modulePassage.retrieve(getTransaction());
                    if (getTransaction().hasErrors()) {
                        throw (new Exception());
                    }
                } catch (Exception e) {
                    this.logInfo4Process(false, false, "OBJEMAIL_FA_MODPERIODIQUEAJOUT_WARNING");
                    abort();
                    return false;
                } // Controler le plan de facturation
                modulePlan.setIdPlanFacturation(passage.getIdPlanFacturation());
                modulePlan.setIdModuleFacturation(moduleFacturation.getIdModuleFacturation());
                try {
                    modulePlan.retrieve(getTransaction());
                } catch (Exception e) {
                    getMemoryLog().logMessage("Impossible de retourner les modules du plan: " + e.getMessage(),
                            FWViewBeanInterface.ERROR, this.getClass().getName());
                } // Le module n'existe pas et fait partie du plan de
                  // facturation, donc l'ajouter
                if (modulePassage.isNew()) {
                    if (FAModuleFacturation.CS_MODULE_SYSTEM.equalsIgnoreCase(moduleFacturation.getIdTypeModule())) {
                        // ajouter le module de type périodique dans les modules
                        // du passage
                        FAModulePassage nouveauModulePassage = new FAModulePassage();
                        nouveauModulePassage.setSession(getSession());
                        nouveauModulePassage.setIdModuleFacturation(moduleFacturation.getIdModuleFacturation());
                        nouveauModulePassage.setIdPassage(modulePassage.getIdPassage());
                        // l'action du module (passer au début par l'action du
                        // servlet
                        nouveauModulePassage.setIdAction(getActionModulePassage());
                        try {
                            nouveauModulePassage.add(getTransaction());
                            if (!getTransaction().hasErrors()) {
                                // committer à chaque ajout de module
                                getTransaction().commit();
                            } else {
                                throw (new Exception());
                            }
                        } catch (Exception e) {

                            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODPERIODIQUEAJOUT_ERROR");
                            try {
                                getTransaction().rollback();
                            } catch (Exception ee) {
                                // stopper le process
                                abort();
                                return false;
                            }
                        }
                    }
                }
            }
        }

        if (!isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method doActionComptabiliser.
     * 
     * @param modulePassage
     * @return boolean
     */
    public boolean doActionComptabiliser(String nomClasse, FAAfactManager manager) {

        boolean success = false;
        try {
            // Instancier une classe anonyme avec son nom
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            // comptabiliser la facturation
            this.logInfo4Process(false, false, "OBJEMAIL_FA_COMPTABILISERIMPL_INFO");
            success = interface_modulefacturation.comptabiliser(passage, this);
        } catch (ClassNotFoundException e) {
            // Errreur de classe
            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODULENOMCLASSE_WARNING");
            this.logInfo4Process(nomClasse);
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la comptabilisation: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return success;
    }

    /**
     * Method doActionGenerer.
     * 
     * @param modulePassage
     * @return boolean
     */
    public boolean doActionGenerer(FAModulePassage modulePassage, String nomClasse, FAAfactManager manager) {
        boolean success = false;
        try {
            // Instancier une classe anonyme avec son nom
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();

            // régénérer la facturation
            success = interface_modulefacturation.avantRegenerer(passage, this, modulePassage.getIdModuleFacturation());
            // si pas manuel, effacer les afacts
            // supprimer les afacts existants liés au module
            if (!FAModuleFacturation.CS_MODULE_AFACT.equalsIgnoreCase(modulePassage.getIdTypeModule())) {
                _deleteAfactDuModule(manager, modulePassage);
            } // générer la facturation
            this.logInfo4Process(false, false, "OBJEMAIL_FA_GENERERIMPL_INFO");
            this.logInfo4Process(nomClasse);
            success = interface_modulefacturation.generer(passage, this, modulePassage.getIdModuleFacturation());
            if (!getTransaction().hasErrors()) {
                // committer si toute la procédure de génération est réussi
                getTransaction().commit();
            }

        } catch (ClassNotFoundException e) {
            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODULENOMCLASSE_WARNING");
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la génération: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }
        return success;
    }

    /**
     * Method doActionImprimer.
     * 
     * @param modulePassage
     */
    public boolean doActionImprimer(FAModulePassage modulePassage, String nomClasse) {
        try {
            // Instancier une classe anonyme avec son nom
            this.logInfo4Process(false, false, "OBJEMAIL_FA_IMPRIMERIMPL_INFO");
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            interface_modulefacturation.imprimer(passage, this);
        } catch (ClassNotFoundException e) {
            clearLogInfo4Process();
            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODULENOMCLASSE_WARNING");
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de l'impression: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());

        }
        return true;
    }

    /**
     * Method doActionSupprimer.
     * 
     * @param modulePassage
     * @return boolean
     */
    public boolean doActionSupprimer(FAModulePassage modulePassage, String nomClasse, FAAfactManager manager) {
        boolean success = false;
        try {
            // Instancier une classe d'implémentation avec son nom
            this.logInfo4Process(false, false, "OBJEMAIL_FA_SUPPRIMERIMPL_INFO");
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            success = interface_modulefacturation.supprimer(passage, this);
            if (!success) {
                // supprimer les afacts existants liés au module
                // _deleteAfactDuModule(manager, modulePassage);
            } // supprimer ce module de FAModulePassage
            modulePassage.delete(getTransaction());
            getTransaction().commit();
        } catch (ClassNotFoundException e) {
            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODULENOMCLASSE_WARNING");
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la suppression: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }
        return success;
    }

    /**
     * Method doModulActionGenerer.
     * 
     * @param modulePassage
     * @param nomClasse
     * @param manager
     * @return boolean
     */
    public boolean doModuleAction(FAModulePassage modulePassage, String nomClasse, FAAfactManager manager) {

        // Teste si le module du passage est en adéquation avec l'action
        // demandée par l'utilisateur(
        boolean success = true;
        if (!JadeStringUtil.isBlank(modulePassage.getIdAction())) {
            // Action SUPPRIMER
            if (FAModulePassage.CS_ACTION_SUPPRIMER.equalsIgnoreCase(modulePassage.getIdAction())) {
            } // Action GENERER
            else if (FAModulePassage.CS_ACTION_VIDE.equalsIgnoreCase(modulePassage.getIdAction())) {
                // BTC:02.03.2005: Si le passage est ouvert, le mettre en état
                // traitement
                if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {
                    passage.setStatus(FAPassage.CS_ETAT_TRAITEMENT);
                }
                success = doActionGenerer(modulePassage, nomClasse, manager);
            } // Action COMPTABILISER
            else if (FAModulePassage.CS_ACTION_GENERE.equalsIgnoreCase(modulePassage.getIdAction())) {
                if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(getPassage().getStatus())) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("OBJEMAIL_FA_COMPTABILISER_OUVERT_WARNING") + " : passage: "
                                    + getIdPassage(), FWMessage.INFORMATION, this.getClass().getName());
                    success = false;
                } else {
                    success = doActionComptabiliser(nomClasse, manager);
                }
            } // Action IMPRIMER
            else if (FAModulePassage.CS_ACTION_IMPRIMER.equalsIgnoreCase(modulePassage.getIdAction())) {
                success = doActionImprimer(modulePassage, nomClasse);
            }
            // 12.11.2004:BTC: mettre l'action à comptabiliser si le type du
            // module n'est pas une LISTE
            // et que l'action était sur générer, sinon le mettre à vide
            try {
                if (success && (!FAModuleFacturation.CS_MODULE_LISTE.equalsIgnoreCase(modulePassage.getIdTypeModule()))) {
                    if (FAModulePassage.CS_ACTION_VIDE.equalsIgnoreCase(modulePassage.getIdAction())) {
                        resetModuleAction(modulePassage, FAModulePassage.CS_ACTION_GENERE);
                    } else {
                        resetModuleAction(modulePassage, "");
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible remettre les actions des modules à zéro: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, this.getClass().getName());
            }
        }
        return success;
    }

    /**
     * @return
     */
    public boolean isEnchainerComptabilisation() {
        return enchainerComptabilisation;
    }

    public boolean iterateModulePassage(FAModulePassageManager modulePassageManager, FAAfactManager manager) {

        if (!getTransaction().hasErrors()) {
            // on itère sur tous les modules liés au passage, donc présents dans
            // modulePassageManager donné en paramètre
            FAModulePassage modulePassage = null;

            for (int i = 0; i < modulePassageManager.size(); i++) {
                modulePassage = (FAModulePassage) modulePassageManager.getEntity(i);
                // Le nom de la classe d'implémentation depuis la BD
                String nomClasse = modulePassage.getNomClasse();

                if (!JadeStringUtil.isEmpty(nomClasse) || !JadeStringUtil.isBlank(nomClasse)) {
                    // si le module n'est pas encore sur comptabiliser,
                    // le mettre sur comptabiliser
                    if (!getTransaction().hasErrors()
                            && (!FAModulePassage.CS_ACTION_GENERE.equalsIgnoreCase(modulePassage.getIdAction()))) {
                        try {
                            resetModuleAction(modulePassage, FAModulePassage.CS_ACTION_GENERE);
                        } catch (Exception e) {
                            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR,
                                    this.getClass().getName());
                        }
                    }
                    // si le module est de type COT_PERS_SORTIE, enchainer sur
                    // la comptabilisation
                    // TODO: il faudrait permettre au module lui-même de
                    // communiquer s'il veut une comptabilisation enchaînée
                    if (FAModuleFacturation.CS_MODULE_RELEVE.equalsIgnoreCase(modulePassage.getIdTypeModule())) {
                        setEnchainerComptabilisation(true);
                    }
                }
            }
        }
        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Method prepareAllModule.
     * 
     * @param moduleFacturationManager
     * @param modulePassageManager
     */
    public void prepareAllModule(FAModuleFacturationManager moduleFacturationManager,
            FAModulePassageManager modulePassageManager) throws Exception {

        if (FAPassage.CS_TYPE_PERIODIQUE.equals(passage.getIdTypeFacturation())
                || FAPassage.CS_TYPE_EXTERNE.equals(passage.getIdTypeFacturation())) {
            // ajout de tous les modules de facturation de type compensation
            // ---------------------------------------------------------------------
            moduleFacturationManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COMPENSATION);
            moduleFacturationManager.find(getTransaction());
            addModulesSystem(moduleFacturationManager, modulePassageManager);

            // ajout de tous les modules de facturation de type intérêts
            // sur cotisations arriérées.
            // ---------------------------------------------------------------------
            moduleFacturationManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_IM_COTARR);
            moduleFacturationManager.find(getTransaction());
            addModulesSystem(moduleFacturationManager, modulePassageManager);

            // ajout de tous les modules de facturation de type intérêts
            // tardifs
            // ---------------------------------------------------------------------
            moduleFacturationManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_IM_TARDIF);
            moduleFacturationManager.find(getTransaction());
            addModulesSystem(moduleFacturationManager, modulePassageManager);
        }

        // pour terminer, ajout de tous les modules de facturation de type
        // SYSTEM
        // ----------------------------------------------------------------------

        moduleFacturationManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_SYSTEM);
        moduleFacturationManager.find(getTransaction());
        addModulesSystem(moduleFacturationManager, modulePassageManager);
    }

    /**
     * Method regenerateModulesDependants.
     * 
     * @param modulePassageManager
     */
    public void regenerateModulesDependants(FAModulePassageManager modulePassageManager) {
        // on itère sur tous les modules liés au passage, repris par le
        // modulePassageManager
        for (int i = 0; i < modulePassageManager.size(); i++) {
            FAModulePassage modulePassage = new FAModulePassage();
            modulePassage = (FAModulePassage) modulePassageManager.getEntity(i);

            // ATTENTION, STRING NULL ne fonctionne pas correctement! utilisé le
            // "0"
            if (!("0".equalsIgnoreCase(modulePassage.getIdAction()))) {
                // regénérer les modules avec niveau d'appel inférieurs
                for (int idep = i; idep < modulePassageManager.size(); idep++) {
                    FAModulePassage modulePassageDep = new FAModulePassage();
                    modulePassageDep = (FAModulePassage) modulePassageManager.getEntity(idep);

                    // conditions de régénération
                    if (!FAModulePassage.CS_ACTION_IMPRIMER.equals(modulePassage.getIdAction())
                            || !FAModulePassage.CS_ACTION_SUPPRIMER.equals(modulePassage.getIdAction())) {
                        // regénérer le module avec l'action du servlet passée
                        // en param
                        // [GENERER, COMPTABILISER]
                        if (FAModuleFacturation.CS_MODULE_LISTE.equalsIgnoreCase(modulePassageDep.getIdTypeModule())) {
                            modulePassageDep.setIdAction(FAModulePassage.CS_ACTION_IMPRIMER);
                        } else {
                            modulePassageDep.setIdAction(getActionModulePassage());
                        }
                        // updater le module
                        try {
                            modulePassageDep.update(getTransaction());
                        } catch (Exception e) {
                            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR,
                                    this.getClass().getName());
                        }
                    }
                } // quitter le traitement des modules dépendants dès qu'un
                  // module est à regénérer
                break;
            }
        }
    }

    /**
     * Method resetModuleAction.
     * 
     * @param modulePassage
     * @throws Exception
     */
    public void resetModuleAction(FAModulePassage modulePassage, String actionName) throws Exception {
        try {
            modulePassage.setIdAction(actionName);
            modulePassage.update(getTransaction());
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
            }
        } catch (Exception e) {
            clearLogInfo4Process();
            this.logInfo4Process(false, false, "OBJEMAIL_FA_DOACTIONZERO_WARNING");
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
            this._addError(getTransaction(), "Erreur lors de la mise à zéro des actions des modules du passage. ");
            try {
                // Rollback de la transaction()
                getTransaction().rollback();
            } catch (Exception ee) {
                throw (new Exception("Facturation: rollback de la transaction échoué."));
            }
        }
    }

    /**
     * @param b
     */
    public void setEnchainerComptabilisation(boolean b) {
        enchainerComptabilisation = b;
    }
}
