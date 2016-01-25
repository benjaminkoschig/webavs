package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import java.util.Date;

/**
 * @author btc Cette classe génère tous les modules de facturation d'un passage, sans tenir compte de l'action! A
 *         utilier que par le main To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageFacturationBatch extends FAPassageFacturationProcess_OLD {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {

        System.out
                .println("Utiliser FAPassageFacturationProcess <user> <pwd> <idPassage> <from> <to> <email> <y> pour JADE\n");
        FAPassageFacturationBatch process = null;
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
        try {
            process = new FAPassageFacturationBatch();
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
                GlobazServer.getCurrentSystem().getApplication(process.getSession().getApplicationId());
                process.setEMailAddress(email);
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

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageFacturationBatch() {
        super();
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
                        + " (" + passage.getIdPassage() + ")");
            } else {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_ECHOUEE") + passage.getLibelle()
                        + " (" + passage.getIdPassage() + ")");
            }
            /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
             * Vérification si le passage peut être généré **********************
             * ****************************************************
             */
            boolean passageEstVerrouille = passage.isEstVerrouille().booleanValue();
            if (FAPassage.CS_ETAT_COMPTABILISE.equals(passage.getStatus()) || (passageEstVerrouille)) {
                this.logInfo4Process(false, "OBJEMAIL_FA_ISVERROUILLE_INFO");
                throw (new Exception("Facturation refusée car le passage est comptabilisé ou verrouillé."));
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

                FAAfactManager manager = null;
                if (!getTransaction().hasErrors()) {
                    /*
                     * on itère sur tous les modules liés au passage, repris par le modulePassageManager
                     */

                    for (int i = 0; i < modulePassageManager.size(); i++) {
                        FAModulePassage modulePassage = new FAModulePassage();
                        modulePassage = (FAModulePassage) modulePassageManager.getEntity(i);
                        // Le nom de la classe d'implémentation depuis la BD
                        String nomClasse = modulePassage.getNomClasse();
                        //
                        if (!JadeStringUtil.isEmpty(nomClasse) || !JadeStringUtil.isBlank(nomClasse)) {
                            doModuleAction(modulePassage, nomClasse, manager);
                        }

                    }
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
     * Method doModuleAction. Cette méthode génère tous les modules de facturation d'un passage, sans tenir compte de
     * l'action!
     * 
     * @param modulePassage
     * @param nomClasse
     * @param manager
     * @return boolean
     */
    @Override
    public boolean doModuleAction(FAModulePassage modulePassage, String nomClasse, FAAfactManager manager) {

        boolean success = doActionGenerer(modulePassage, nomClasse, manager);
        try {
            if (success && (!FAModuleFacturation.CS_MODULE_LISTE.equalsIgnoreCase(modulePassage.getIdTypeModule()))) {
                resetModuleAction(modulePassage, "");
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible remettre les actions des modules à zéro: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return success;
    }
}
