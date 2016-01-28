package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.message.JadePublishQueueInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAModulePlan;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageManager;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.util.FAChrono;
import globaz.musca.util.FAUtil;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.OsirisDef;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * G�n�ration de la facturation. Date de cr�ation : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageFacturationProcess extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String USER_MODULE_FACTURATION_TRACER = "ModuleFacturationTracer";

    public static void main(String[] args) {

        System.out.println("Utilisateur : <[user]> \n Mot de passe : <[password]>");
        FAPassageManager passageMana = null;
        FAPassageFacturationProcess comptabiliser = null;
        String user = null;
        String password = null;
        String eMailAdress = null;

        if (args.length > 0) {
            user = args[0];
            // System.out.println(idPassage);
            if (args.length > 1) {
                password = args[1];
                if (args.length > 2) {
                    eMailAdress = args[2];
                }
            }
        } else {
            return;
        }

        try {
            Jade.getInstance();
            // Connection � l'application
            BSession session = new BSession("MUSCA");
            session.connect(user, password);

            passageMana = new FAPassageManager();
            passageMana.setSession(session);
            passageMana.setForStatus(FAPassage.CS_ETAT_VALIDE);
            passageMana.find();
            if (passageMana.size() > 0) {
                for (int i = 0; i < passageMana.size(); i++) {
                    FAPassage passage = (FAPassage) passageMana.getEntity(i);
                    comptabiliser = new FAPassageFacturationProcess();
                    comptabiliser.setSession(session);
                    comptabiliser.setTransaction(new BTransaction(comptabiliser.getSession()));
                    comptabiliser.getTransaction().openTransaction();
                    comptabiliser.setIdPassage(passage.getIdPassage());
                    comptabiliser.setEMailAddress(eMailAdress);
                    comptabiliser.setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
                    comptabiliser.setSendCompletionMail(false);
                    // comptabiliser._executeProcess();
                    // comptabiliser.publishDocumentBis();
                    JadeJobInfo job = BProcessLauncher.start(comptabiliser);
                    while ((!job.isOut()) && (!job.isError())) {
                        Thread.sleep(1000);
                        job = JadeJobServerFacade.getJobInfo(job.getUID());
                    }
                    // Thread.sleep(1000);
                    if (job.isError()) {
                        // erreurs critique, je retourne le code de retour not
                        // ok
                        System.out
                                .println("Le process de comptabilisation n'a pas �t� ex�cut� avec succ�s pour le journal "
                                        + passage.getIdPassage());
                        System.out.println(job.getFatalErrorMessage());
                    } else {
                        // pas d'erreurs critique, je retourne le code de retour
                        // ok
                        System.out.println("Le process de comptabilisation a �t� ex�cut� avec succ�s pour le journal "
                                + passage.getIdPassage());
                    }
                    List<?> queues = JadePublishServerFacade.getServerInfo().getQueueNames();
                    while (true) {
                        Thread.sleep(1000);
                        boolean stillRunning = false;
                        for (Iterator<?> iter = queues.iterator(); iter.hasNext();) {
                            String queueName = (String) iter.next();
                            JadePublishQueueInfo queueInfo = JadePublishServerFacade.getQueueInfo(queueName);
                            if ((queueInfo.getNumberOfWaitingJobs() > 0) || (queueInfo.getNumberOfRunningJobs() > 0)) {
                                stillRunning = true;
                                break;
                            }
                        }
                        if (!stillRunning) {
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Il n'y a pas de journal � l'�tat valid�");
            }
            System.out.println("Arr�t du programme lanc� !");

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if ((comptabiliser != null) && (comptabiliser.getTransaction() != null)) {
                try {
                    comptabiliser.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    private globaz.globall.api.BIApplication appOsiris = null;
    private Boolean auto = new Boolean(true); // Mode automatique
    // g�n�r�s si pas en mode automatique
    private boolean enchainerComptabilisation = false; // Pour PHENIX
    private boolean isTracingModuleEnable = false;
    private String module = null; // Module � partir duquel les autres seront

    private globaz.globall.api.BISession sessionOsiris = null;

    /**
     * Constructeur FAPassageFacturationProcess
     */
    public FAPassageFacturationProcess() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.04.2005)
     * 
     * @auteur: RRI
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        try {
            isTracingModuleEnable = FAUtil.hasCurrentUserComplement(getSession(),
                    FAPassageFacturationProcess.USER_MODULE_FACTURATION_TRACER);
        } catch (Exception e) {
            getMemoryLog().logMessage("Can't enable module facturation tracing : " + e.toString(),
                    FWMessage.INFORMATION, this.getClass().getName());
            isTracingModuleEnable = false;
        }

        try {
            setSendMailOnError(true);
            setSendCompletionMail(true);

            // On r�cup�re le passage gr�ce a l'idPassage
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve(getTransaction());

            /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
             * Teste si le passage peut �tre g�n�r� Ne peut pas �tre g�n�r� si: d�j� comptabilis�, verrouill�, annul� Ou
             * si la p�riode comptable n'est pas ouverte ou cl�tur�e.
             **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
            boolean periodeComptable = controlePeriodeComptable(passage);
            boolean passageEstVerrouille = passage.isEstVerrouille().booleanValue();

            if (!periodeComptable) {
                throw (new Exception("Facturation refus�e car la p�riode comptable n'est pas ouverte ou est cl�tur�e"));
            }
            if (FAPassage.CS_ETAT_COMPTABILISE.equals(passage.getStatus()) || passageEstVerrouille
                    || passage.getStatus().equals(FAPassage.CS_ETAT_ANNULE)) {
                this.logInfo4Process(false, "OBJEMAIL_FA_ISVERROUILLE_INFO");
                throw (new Exception("Facturation refus�e car le passage est comptabilis�, verrouill� ou annul�."));
            }

            try {
                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Initialisation (= verrouillage)
                 **********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
                _initializePassage(passage);

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Ajouts des modules syst�mes
                 **********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
                // Instancier le ModuleFacturationManager
                FAModuleFacturationManager moduleFacturationManager = new FAModuleFacturationManager();
                moduleFacturationManager.setSession(getSession());

                // Tous les modules du passage contr�l�s par leur manager
                FAModulePassageManager modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(getSession());
                modulePassageManager.setForIdPassage(getIdPassage());
                // Trier par niveau d'appel
                modulePassageManager.orderByNiveauAppel();
                modulePassageManager.find(getTransaction());

                // Si le passage est OUVERT, pr�pare tous les modules du plan
                // associ�s au passage concern�
                if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {
                    prepareAllModule(moduleFacturationManager, modulePassageManager);
                    // recharger le manager
                    modulePassageManager.find(getTransaction());
                }

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Lance la m�thode permettant de savoir si tous les modules sont g�ner�s (variable allGen) dans la
                 * n�gative ET si l'action demand�e est une comptabilisation, le processus s'arr�tera l�.
                 **********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
                boolean allGen = areAllGen(modulePassageManager);
                if (!allGen && getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {
                    getTransaction().addErrors("Il reste des modules non g�n�r�s, impossible de comptabiliser");
                    this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
                }
                if (!allGen && getActionModulePassage().equals(FAModulePassage.CS_ACTION_IMPRIMER)) {
                    getTransaction().addErrors("Il reste des modules non g�n�r�s, impossible d'imprimer");
                    this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
                }

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Ex�cution des modules Si "auto" est activ�, on lance les modules en automatique
                 * ********************************
                 **********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
                if ((auto.booleanValue() || (getModule() == null)) && !getTransaction().hasErrors()) {
                    iterateModuleAuto(modulePassageManager);
                } else if ((module != null) && !getTransaction().hasErrors()) {
                    iterateModuleFrom(modulePassageManager, module);
                }

                // 26.04.2005, RRI: si un module force l'enchainement sur la
                // comptabilisation,
                // on comptabilise � la suite de la g�n�ration.
                if (!getTransaction().hasErrors() && getEnchainerComptabilisation()) {
                    setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
                    iterateModuleAuto(modulePassageManager);
                }

                /*
                 * Si l'action demand�e �tait une COMPTAPILISATION et qu'elle s'est bien pass�e, on met le passage sur
                 * COMPTABILISE, sinon, il reste sur OUVERT ou TRAITEMENT. Pour la g�n�ration (--> Traitement), cela se
                 * fait au d�but du traitement si ce dernier � lieu
                 */
                if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)
                        && !getTransaction().hasErrors() && !isAborted()) {
                    passage.setStatus(FAPassage.CS_ETAT_COMPTABILISE);
                }

            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            } finally {

                // Sujet de l'E-mail
                if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {
                    if (isAborted()) {
                        setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_COMPTABILISATION_ABORT")
                                + passage.getLibelle() + " (" + getIdPassage() + ")");
                    } else {
                        if (!getTransaction().hasErrors()) {
                            // Succes
                            setEMailObject(getSession().getLabel("5006") + passage.getLibelle() + " (" + getIdPassage()
                                    + ")");
                        } else {
                            // Echec
                            setEMailObject(getSession().getLabel("5007") + passage.getLibelle() + " (" + getIdPassage()
                                    + ")");
                        }
                    }
                } else {
                    if (isAborted()) {
                        setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_ABORT")
                                + passage.getLibelle() + " (" + getIdPassage() + ")");
                    } else {
                        if (!getTransaction().hasErrors()) {
                            setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_REUSSIE")
                                    + passage.getLibelle() + " (" + getIdPassage() + ")");
                        } else {
                            setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_ECHOUEE")
                                    + passage.getLibelle() + " (" + getIdPassage() + ")");
                        }
                    }
                }

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * V�rification du log
                 **********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
                // logger toutes les infos issues des modules
                this.logInfo4Process(getTransaction().getErrors().toString());

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Fin de la proc�dure
                 **********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
                _finalizePassage(passage);

                sendEmail();
            }
        } catch (Exception e) {
            this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
            return false;
        }
        return !isAborted();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.03.2003 13:28:02)
     * 
     * @author: BTC
     */
    public boolean addModulesSystem(FAModuleFacturationManager moduleFacturationManager,
            FAModulePassageManager modulePassageManager) {

        // module du plan de facturation
        FAModulePlan modulePlan = new FAModulePlan();
        modulePlan.setSession(getSession());
        // Ajouter les modules p�riodiques aux modules du passage
        // ----------------------------------------------------
        for (int i = 0; i < moduleFacturationManager.size(); i++) {
            // tous les modules de facturation
            FAModuleFacturation moduleFacturation = null;
            moduleFacturation = (FAModuleFacturation) moduleFacturationManager.getEntity(i);
            // utiliser <= car il se peut qu'il n'y a encore aucun module du
            // m�me type
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
                    getMemoryLog().logMessage("Erreur lors de l'ajout de modules system: " + e.getMessage(),
                            FWViewBeanInterface.ERROR, this.getClass().getName());
                } // Le module n'existe pas et fait partie du plan de
                  // facturation, donc l'ajouter
                if (modulePassage.isNew()) {
                    if (FAModuleFacturation.CS_MODULE_SYSTEM.equalsIgnoreCase(moduleFacturation.getIdTypeModule())) {
                        // ajouter le module de type p�riodique dans les modules
                        // du passage
                        FAModulePassage nouveauModulePassage = new FAModulePassage();
                        nouveauModulePassage.setSession(getSession());
                        nouveauModulePassage.setIdModuleFacturation(moduleFacturation.getIdModuleFacturation());
                        nouveauModulePassage.setIdPassage(modulePassage.getIdPassage());
                        // l'action du module (passer au d�but par l'action du
                        // servlet
                        nouveauModulePassage.setIdAction(getActionModulePassage());
                        try {
                            nouveauModulePassage.add(getTransaction());
                            if (!getTransaction().hasErrors()) {
                                // commiter � chaque ajout de module
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
     * V�rifie si tous les modules ont �t� g�n�r�s ou plus (comptabilis�, avec err) Proc�dure � faire avant la
     * comptabilisation
     * 
     * @author RRI (12.04.2005)
     * @return true si tous les modules ont �t�s g�n�r�s, false sinon
     */
    private boolean areAllGen(FAModulePassageManager modulePassageManager) {
        boolean allGen = true;
        FAModulePassage modulePassage = null;

        for (int i = 0; i < modulePassageManager.size(); i++) {
            modulePassage = (FAModulePassage) modulePassageManager.getEntity(i);
            if (!modulePassage.getIdAction().equals(FAModulePassage.CS_ACTION_GENERE)
                    && !modulePassage.getIdAction().equals(FAModulePassage.CS_ACTION_COMPTABILISE)
                    && !modulePassage.getIdAction().equals(FAModulePassage.CS_ACTION_ERREUR_COMPTA)) {
                allGen = false;
                break;
            }
        }
        return allGen;
    }

    private boolean controlePeriodeComptable(FAPassage passage) {
        try {
            BISession sessionOsiris = getApplicationOsiris().newSession(getSession());
            APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) sessionOsiris
                    .getAPIFor(APIGestionComptabiliteExterne.class);
            compta.setTransaction(getTransaction());
            compta.setProcess(this);

            return compta.isPeriodeComptableOuverte(passage.getDateFacturation());
        } catch (Exception e) {
            this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
            return false;
        }
    }

    /**
     * Method doActionComptabiliser.
     * 
     * @param modulePassage
     * @return boolean
     */
    public void doActionComptabiliser(FAModulePassage module, String nomClasse) {
        try {
            // Instancier une classe anonyme avec son nom
            passage.setModuleEnCours(module.getIdTypeModule());
            passage.setModulePassageEnCours(module);
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            // comptabiliser la facturation
            // this.logInfo4Process(false, false, "OBJEMAIL_FA_COMPTABILISERIMPL_INFO");

            // Teste si le module a d�j� �t� comptabilis� ou en erreur
            if (module.getIdAction().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {

                interface_modulefacturation.recomptabiliser(passage, this);
            } else if (module.getIdAction().equalsIgnoreCase(FAModulePassage.CS_ACTION_ERREUR_COMPTA)) {
                // Pour l'instant, la reprise sur Erreur de comptabilisation
                // n'est pas fonctionnelle (mais impl�ment�e).
                // Remettre l'action RepriseOnErrorCompt � la place de
                // Comptabiliser le moment voulu

                interface_modulefacturation.repriseOnErrorCompta(passage, this);
            } else {

                if (isTracingModuleEnable) {
                    doActionWithTracingModule(FAModulePassage.CS_ACTION_COMPTABILISE, module.getLibelleFr(),
                            interface_modulefacturation, module.getIdModuleFacturation());
                } else {
                    interface_modulefacturation.comptabiliser(passage, this);
                }
            }

            // On valide si pas d'erreur
            if (!getTransaction().hasErrors() && !getMemoryLog().isOnFatalLevel() && !getMemoryLog().isOnErrorLevel()) {
                getTransaction().commit();
                resetModuleAction(module, FAModulePassage.CS_ACTION_COMPTABILISE);
            } else {
                // On provoque l'arr�t du module en g�n�rant une erreur
                if (getMemoryLog().isOnErrorLevel()) {
                    System.out.println(">" + getMemoryLog().getMessagesInString());
                }
                throw new Exception();
            }

        } catch (ClassNotFoundException e) {
            resetModuleAction(module, FAModulePassage.CS_ACTION_ERREUR_COMPTA);
            getTransaction().addErrors("La classe � ex�cuter est introuvable:" + nomClasse);
            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODULENOMCLASSE_WARNING");
        } catch (Exception ex) {
            resetModuleAction(module, FAModulePassage.CS_ACTION_ERREUR_COMPTA);
            this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
            getTransaction().addErrors(
                    "Un module n'a pas pu �tre ex�cut�. Regarder les modules du passage pour plus d'infos");
        }
    }

    /**
     * M�thode appl�e si la g�n�ration d'un module est demand�e. Cette m�thode n'est jamais appl�e dans le cas o� on se
     * trouve en mode Automatique ET que le module a d�j� �t� g�n�r�.
     * 
     * @param module
     *            Le module a g�n�rer
     * @param nomClasse
     *            Le nom du module
     * @author RRI
     */
    public void doActionGenerer(FAModulePassage module, String nomClasse) {
        try {
            // Instancier une classe anonyme avec son nom
            passage.setModuleEnCours(module.getIdTypeModule());
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();

            // g�n�rer la facturation
            // this.logInfo4Process(false, false, "OBJEMAIL_FA_GENERERIMPL_INFO");
            // this.logInfo4Process(nomClasse);

            // Teste si le module a d�j� �t� g�n�r� ou en erreur
            if (module.getIdAction().equals(FAModulePassage.CS_ACTION_GENERE)) {
                if (interface_modulefacturation.avantRegenerer(passage, this, module.getIdModuleFacturation())) {
                    _deleteAfactDuModule(null, module);
                }

                interface_modulefacturation.regenerer(passage, this, module.getIdModuleFacturation());
            } else if (module.getIdAction().equalsIgnoreCase(FAModulePassage.CS_ACTION_ERREUR_GEN)) {
                if (interface_modulefacturation.avantRepriseErrGen(passage, this, module.getIdModuleFacturation())) {
                    _deleteAfactDuModule(null, module);
                }

                interface_modulefacturation.repriseOnErrorGen(passage, this, module.getIdModuleFacturation());
            } else {

                if (isTracingModuleEnable) {
                    doActionWithTracingModule(FAModulePassage.CS_ACTION_GENERE, module.getLibelleFr(),
                            interface_modulefacturation, module.getIdModuleFacturation());
                } else {
                    interface_modulefacturation.generer(passage, this, module.getIdModuleFacturation());
                }
            }

            // On valide si pas d'erreur
            if (!getTransaction().hasErrors() && !getMemoryLog().isOnFatalLevel() && !getMemoryLog().isOnErrorLevel()) {
                getTransaction().commit();
                resetModuleAction(module, FAModulePassage.CS_ACTION_GENERE);
            } else {
                // On provoque l'arr�t du module en g�n�rant une erreur
                throw new Exception();
            }

        } catch (ClassNotFoundException e) {
            this.logInfo4Process(false, false, "OBJEMAIL_FA_MODULENOMCLASSE_WARNING");
            getTransaction().addErrors("Le module n'a pas pu �tre retrouv�: " + nomClasse);
            resetModuleAction(module, FAModulePassage.CS_ACTION_ERREUR_GEN);
        } catch (Exception ex) {
            this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
            getTransaction().addErrors(
                    "Le module n'a pas pu �tre ex�cut� correctement: " + nomClasse + ": " + ex.getMessage());
            resetModuleAction(module, FAModulePassage.CS_ACTION_ERREUR_GEN);
            try {
                getTransaction().rollback();
            } catch (Exception ei) {
            }
        }
    }

    /**
     * Method doActionImprimer.
     * 
     * @param modulePassage
     */
    public boolean doActionImprimer(FAModulePassage modulePassage, String nomClasse) {
        try {
            // Instancier une classe anonyme avec son nom
            passage.setModuleEnCours(modulePassage.getIdTypeModule());
            // this.logInfo4Process(false, false, "OBJEMAIL_FA_IMPRIMERIMPL_INFO");
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();

            if (isTracingModuleEnable) {
                doActionWithTracingModule(FAModulePassage.CS_ACTION_IMPRIMER, modulePassage.getLibelleFr(),
                        interface_modulefacturation, modulePassage.getIdModuleFacturation());
            } else {
                interface_modulefacturation.imprimer(passage, this);
            }

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
            // Instancier une classe d'impl�mentation avec son nom
            passage.setModuleEnCours(modulePassage.getIdTypeModule());
            // this.logInfo4Process(false, false, "OBJEMAIL_FA_SUPPRIMERIMPL_INFO");
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            success = interface_modulefacturation.supprimer(passage, this);
            if (!success) {

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

    private void doActionWithTracingModule(String theActionId, String theModuleLibelle,
            IntModuleFacturation theInterface_modulefacturation, String idModuleFacturation) throws Exception {

        String textFixeException = "Can't trace module due to wrong arguments : ";
        StringBuffer theWrongArguments = new StringBuffer();

        if (JadeStringUtil.isBlankOrZero(theActionId)) {
            theWrongArguments.append(" theActionId is empty ");
        }

        if (JadeStringUtil.isBlankOrZero(theModuleLibelle)) {
            theWrongArguments.append(" theModuleLibelle is empty ");
        }

        if (theInterface_modulefacturation == null) {
            theWrongArguments.append(" theInterface_modulefacturation is null ");
        }

        if (theWrongArguments.length() > 0) {
            throw new Exception(textFixeException + theWrongArguments.toString());
        }

        String theAction = "";
        FAChrono chrono = new FAChrono();
        chrono.start();

        if (FAModulePassage.CS_ACTION_GENERE.equalsIgnoreCase(theActionId)) {
            theAction = " Action g�n�rer ";
            theInterface_modulefacturation.generer(passage, this, idModuleFacturation);
        } else if (FAModulePassage.CS_ACTION_COMPTABILISE.equalsIgnoreCase(theActionId)) {
            theAction = " Action comptabiliser ";
            theInterface_modulefacturation.comptabiliser(passage, this);
        } else if (FAModulePassage.CS_ACTION_IMPRIMER.equalsIgnoreCase(theActionId)) {
            theAction = " Action imprimer ";
            theInterface_modulefacturation.imprimer(passage, this);
        }

        chrono.stop();

        getMemoryLog().logMessage(theModuleLibelle + theAction + " : " + chrono.giveDurationHeureMin(),
                FWMessage.INFORMATION, this.getClass().getName());
    }

    /**
     * Ex�cution d'un module envoy� en param�tre: Si l'action demand�e est une g�n�ration le module sera g�n�r�, si
     * c'est une comptabilisation, il sera comptabilis�.
     * 
     * @param module
     *            Le module � ex�cuter
     * @param nomClasse
     *            Le nom du module sous forme de String
     * @return boolean
     */
    public void executeModule(FAModulePassage module, String nomClasse) {

        if (!JadeStringUtil.isBlank(module.getIdAction())) {

            // Si l'action demand�e est une G�n�ration
            if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_GENERE)) {
                // Si le passage est "Ouvert", le mettre en �tat "Traitement"
                if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(getPassage().getStatus())) {
                    passage.setStatus(FAPassage.CS_ETAT_TRAITEMENT);
                }
                doActionGenerer(module, nomClasse);

            } // Si l'action demand�e est une Comptabilisation
            else if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {

                // System.out.println("oca>> start " + nomClasse + " at " + new Date());
                doActionComptabiliser(module, nomClasse);
                // System.out.println("oca>> end   " + nomClasse + " at " + new Date());

            } // Action IMPRIMER (A laisser?)
            else if (FAModulePassage.CS_ACTION_IMPRIMER.equalsIgnoreCase(module.getIdAction())) {
                doActionImprimer(module, nomClasse);
            }
        }
    }

    public BIApplication getApplicationOsiris() {
        // Si application pas ouverte
        if (appOsiris == null) {
            try {
                appOsiris = GlobazSystem.getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appOsiris;
    }

    /**
     * @return
     */
    public Boolean getAuto() {
        return auto;
    }

    public boolean getEnchainerComptabilisation() {
        return enchainerComptabilisation;
    }

    /**
     * @return
     */
    public String getModule() {
        return module;
    }

    public BISession getSessionOsiris(BISession session) {
        // Si session pas ouverte
        if (sessionOsiris == null) {
            try {
                sessionOsiris = getApplicationOsiris().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionOsiris;
    }

    /**
     * It�re sur tous les modules, cepandant, les modules ayant d�j� �t� ex�cut�s pour l'action demand�es sont ignor�s.
     * 
     * @param modulePassageManager
     *            Tous les modules du passage
     * @author RRI
     */
    private void iterateModuleAuto(FAModulePassageManager modulePassageManager) {
        // Pr�paration du compteur
        try {
            setProgressScaleValue(modulePassageManager.getCount());
        } catch (Exception e) {
        }

        // on it�re sur tous les modules li�s au passage
        FAModulePassage modulePassage = null;
        FAModulePassage dernierModulePassage = null;
        int i = 0;
        int cpt = 0;

        while ((i < modulePassageManager.size()) && !getTransaction().hasErrors()) {
            modulePassage = (FAModulePassage) modulePassageManager.getEntity(i);
            cpt++;
            // setProgressDescription(modulePassage.getLibelle());
            setProgressDescription(modulePassage.getLibelle() + " <br>" + cpt + "/" + modulePassageManager.size()
                    + "<br>");
            if (isAborted()) {
                cpt--;
                setProgressDescription("Traitement interrompu<br>Module en cours de traitement : "
                        + dernierModulePassage.getLibelle() + " <br>" + cpt + "/" + modulePassageManager.size()
                        + "<br>");
                // getActionModulePassage().equals(FAModulePassage.CS_ACTION_GENERE)
                if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_GENERE)) {
                    dernierModulePassage.setIdAction(FAModulePassage.CS_ACTION_ERREUR_GEN);
                } else {
                    dernierModulePassage.setIdAction(FAModulePassage.CS_ACTION_ERREUR_COMPTA);
                }
                try {
                    dernierModulePassage.update(getTransaction());
                    getTransaction().commit();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            } else {
                String nomClasse = modulePassage.getNomClasse();

                setProgressCounter(i);

                if (!JadeStringUtil.isEmpty(nomClasse) || !JadeStringUtil.isBlank(nomClasse)) {
                    // Si le passage est d�j� g�n�r� (resp. comptabilis�), on
                    // l'ignore (cela ne d�clanche pas une erreur)
                    if (modulePassage.getIdAction().equalsIgnoreCase(getActionModulePassage())) {
                    } else if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_GENERE)
                            && (modulePassage.getIdAction().equalsIgnoreCase(FAModulePassage.CS_ACTION_ERREUR_COMPTA) || modulePassage
                                    .getIdAction().equalsIgnoreCase(FAModulePassage.CS_ACTION_COMPTABILISE))) {
                    } else {
                        executeModule(modulePassage, nomClasse);
                    }
                }
                if (dernierModulePassage != null) {
                    dernierModulePassage = modulePassage;
                } else {
                    dernierModulePassage = (FAModulePassage) modulePassageManager.getFirstEntity();
                }
                i++;
            }
        }
    }

    /**
     * Ex�cute les modules � partir d'un point bien pr�cis. Ces derniers sont g�n�r�s m�me s'ils l'ont d�j� �t�s
     * auparavant.
     * 
     * @param modulePassageManager
     *            Tous les modules du passage
     * @param module
     *            Le module servant de point de d�part
     * @author RRI
     */
    private void iterateModuleFrom(FAModulePassageManager modulePassageManager, String module) {
        // Pr�paration du compteur
        try {
            setProgressScaleValue(modulePassageManager.getCount());
        } catch (Exception e) {
        }

        // on it�re sur tous les modules li�s au passage
        FAModulePassage modulePassage = null;
        FAModulePassage dernierModulePassage = null;
        int i = 0;
        int cpt = 0;
        boolean beginExec = false; // Tant que cette variable est "false", on
        // n'ex�cute pas.

        while ((i < modulePassageManager.size()) && !getTransaction().hasErrors()) {
            modulePassage = (FAModulePassage) modulePassageManager.getEntity(i);
            cpt++;
            // setProgressDescription(modulePassage.getLibelle());
            setProgressDescription(modulePassage.getLibelle() + " <br>" + cpt + "/" + modulePassageManager.size()
                    + "<br>");
            if (isAborted()) {
                cpt--;
                setProgressDescription("Traitement interrompu<br>Prochain module � traiter : "
                        + dernierModulePassage.getLibelle() + " <br>" + cpt + "/" + modulePassageManager.size()
                        + "<br>");
                // getActionModulePassage().equals(FAModulePassage.CS_ACTION_GENERE)
                if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_GENERE)) {
                    dernierModulePassage.setIdAction(FAModulePassage.CS_ACTION_ERREUR_GEN);
                } else {
                    dernierModulePassage.setIdAction(FAModulePassage.CS_ACTION_ERREUR_COMPTA);
                }
                try {
                    dernierModulePassage.update(getTransaction());
                    getTransaction().commit();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            } else {

                incProgressCounter();
                String nomClasse = modulePassage.getNomClasse();

                // setProgressCounter(i);

                if (!JadeStringUtil.isEmpty(nomClasse) || !JadeStringUtil.isBlank(nomClasse)) {
                    if (modulePassage.getIdModuleFacturation().equals(module) || beginExec) {
                        executeModule(modulePassage, nomClasse);
                        beginExec = true;
                    }
                }
                if (dernierModulePassage != null) {
                    dernierModulePassage = modulePassage;
                } else {
                    dernierModulePassage = (FAModulePassage) modulePassageManager.getFirstEntity();
                }

                i++;
            }
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Method Ajoute les modules systemes
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
            // ajout de tous les modules de facturation de type int�r�ts
            // sur cotisations arri�r�es.
            // ---------------------------------------------------------------------
            moduleFacturationManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_IM_COTARR);
            moduleFacturationManager.find(getTransaction());
            addModulesSystem(moduleFacturationManager, modulePassageManager);
            // ajout de tous les modules de facturation de type int�r�ts
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
     * Method resetModuleAction.
     * 
     * @param modulePassage
     * @throws Exception
     */
    public void resetModuleAction(FAModulePassage modulePassage, String actionName) {
        // On cr�e une nouvelle transaction, car celle utilis�e peut avoir une
        // erreur,
        // et il n'est donc pas possible de faire de commit dessus
        try {
            BTransaction transac = new BTransaction(getSession());

            modulePassage.setIdAction(actionName);
            modulePassage.update(transac);
            transac.commit();

            transac.closeTransaction();
        } catch (Exception e) {
            clearLogInfo4Process();
            this.logInfo4Process(false, false, "OBJEMAIL_FA_DOACTIONZERO_WARNING");
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
            this._addError(getTransaction(), "Erreur lors de la mise � z�ro des actions des modules du passage. ");
        }
    }

    /**
     * Envois un email contenant les r�sultats (memoryLog). Temporaire, � supprimer lorsque FW aura �t� modifi�.
     * 
     * @throws Exception
     */
    private void sendEmail() throws Exception {
        if (hasAttachedDocuments() || ((getParent() != null) && getParent().hasAttachedDocuments())) {
            String emailContent = "";
            Enumeration<?> e = getMemoryLog().enumMessages();
            while (e.hasMoreElements()) {
                emailContent += ((FWMessage) e.nextElement()).getFullMessage() + "\n";
            }

            if (!JadeStringUtil.isBlank(emailContent)) {
                JadeSmtpClient.getInstance().sendMail(JadeSmtpClient.getInstance().getSenderEmailAddress(),
                        getEMailAddress(), getEMailObject(), emailContent, null);
            }
        }
    }

    /**
     * @param boolean1
     */
    public void setAuto(Boolean b) {
        auto = b;
    }

    public void setEnchainerComptabilisation(boolean b) {
        enchainerComptabilisation = b;
    }

    /**
     * @param string
     */
    public void setModule(String string) {
        module = string;
    }
}
