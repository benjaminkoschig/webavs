package globaz.osiris.batch;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.musca.db.facturation.FAFacturationExt;
import globaz.musca.db.facturation.FAFacturationExtManager;
import globaz.osiris.api.APIOperation;
import globaz.osiris.batch.manager.CAMiseAJourEcritureFacturationExterne;
import globaz.osiris.batch.manager.CAMiseAJourEcritureFacturationExterneManager;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import java.util.ArrayList;
import java.util.List;

public class CAMiseAJourEcritureFacturationExterneProcess extends BProcess {
    private static final long serialVersionUID = -4596651825018300484L;

    public static void main(String[] args) {
        try {
            String user = null;
            String pwd = null;
            String email = null;
            String verbose = "false";
            String simulation = "true";

            for (int i = 0; i < args.length; i++) {
                if (args[i].toLowerCase().equals("-user")) {
                    i++;
                    user = args[i];
                } else if (args[i].toLowerCase().equals("-pwd")) {
                    i++;
                    pwd = args[i];
                } else if (args[i].toLowerCase().equals("-email")) {
                    i++;
                    email = args[i];
                } else if (args[i].toLowerCase().equals("-verbose")) {
                    i++;
                    verbose = args[i].toLowerCase();
                } else if (args[i].toLowerCase().equals("-simulation")) {
                    i++;
                    simulation = args[i].toLowerCase();
                }
            }
            if (args.length < 3) {
                System.out
                        .println("Usage: CAMiseAJourEcritureFacturationExterneProcess -user userName -pwd password -email eMailAdress [-verbose {true|(false)}] [-simulation{(true)|false}]");
                System.exit(0);
            }
            if (JadeStringUtil.isBlank(user)) {
                System.out.println("Parameter \"-user\" missing !");
                System.exit(0);
            }
            if (JadeStringUtil.isBlank(pwd)) {
                System.out.println("Parameter \"-pwd\" missing !");
                System.exit(0);
            }
            if (JadeStringUtil.isBlank(email)) {
                System.out.println("Parameter \"-email\" missing !");
                System.exit(0);
            }
            if (!JadeStringUtil.isBlank(verbose)) {
                if (!"true".equalsIgnoreCase(verbose) && !"false".equalsIgnoreCase(verbose)) {
                    System.out.println("Parameter \"-verbose\" incorrect !");
                    System.exit(0);
                }
            }
            if (!JadeStringUtil.isBlank(simulation)) {
                if (!"true".equalsIgnoreCase(simulation) && !"false".equalsIgnoreCase(simulation)) {
                    System.out.println("Parameter \"-verbose\" incorrect !");
                    System.exit(0);
                }
            }
            BSession session = new BSession("OSIRIS");
            session.connect(user, pwd);
            CAMiseAJourEcritureFacturationExterneProcess process = new CAMiseAJourEcritureFacturationExterneProcess();
            process.setSendCompletionMail(true);
            process.setSession(session);
            process.setEMailAddress(email);
            process.setVerbose(verbose);
            process.setSimulation(simulation);
            // Démarrer le process
            JadeJobInfo job = BProcessLauncher.start(process);

            while (!job.isOut() && !job.isError()) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Process Mise à jour de FAFAEXT not executed successfully !");
                System.out.println(job.getFatalErrorMessage());
                System.exit(0);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process Mise à jour de FAFAEXT executed successfully !");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Process Mise à jour de FAFAEXT has error(s) !");

            System.exit(0);
        } finally {

        }
        System.exit(0);
    }

    private String annee = new String();
    private String idRubrique = new String();
    // Private members
    private String simulation = "true";

    private String verbose = "false";

    /**
     * Construit un nouveau process
     */
    public CAMiseAJourEcritureFacturationExterneProcess() {
        super();
    }

    /**
     * Construit un nouveau process fils
     * 
     * @param parent
     */
    public CAMiseAJourEcritureFacturationExterneProcess(BProcess parent) {
        super(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        // Transaction
        BTransaction cursorTransaction = null;
        BStatement statement = null;
        CAMiseAJourEcritureFacturationExterneManager mgr = null;
        CAMiseAJourEcritureFacturationExterne entity;
        // Sous contrôle d'exception
        try {
            // Mode simulation
            if ("true".equalsIgnoreCase(getSimulation())) {
                getMemoryLog().logMessage(getSession().getLabel("MODESIMULATION"), FWMessage.INFORMATION,
                        this.getClass().getName());
            }
            // Transaction pour open
            cursorTransaction = new BTransaction(getSession());
            cursorTransaction.openTransaction();
            // Manager principal
            mgr = new CAMiseAJourEcritureFacturationExterneManager();
            mgr.setSession(getSession());
            // Count
            setProgressScaleValue(mgr.getCount());
            // Ouverture du curseur
            statement = mgr.cursorOpen(cursorTransaction);
            // boucle principale
            while ((entity = (CAMiseAJourEcritureFacturationExterne) mgr.cursorReadNext(statement)) != null) {
                // Incrémenter
                incProgressCounter();
                // Recherche de la dernière opération de paiement
                CAOperationManager opManager = new CAOperationManager();
                opManager.setSession(getSession());
                opManager.setForIdSection(entity.getIdSection());
                opManager.setForEtat(APIOperation.ETAT_COMPTABILISE);
                opManager.setMontantNegatif(new Boolean("true"));
                List<String> list = new ArrayList<String>();
                list.add(APIOperation.CAPAIEMENT);
                list.add(APIOperation.CAPAIEMENTBVR);
                opManager.setForIdTypeOperationIn(list);
                opManager.setOrderBy(CAOperationManager.ORDER_DATEOP_DESC);
                opManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                opManager.find();
                if (!opManager.isEmpty()) {
                    CAOperation op = (CAOperation) opManager.getFirstEntity();
                    // Mise à jour de la facturation externe (FAFAEXT)
                    miseAJourFacturationExterne(entity, op);
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                // Clôture du curseur
                if (statement != null) {
                    try {
                        mgr.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception ex2) {
                getMemoryLog().logMessage(ex2.getMessage(), FWMessage.FATAL, this.getClass().getName());
            } finally {
                if (cursorTransaction != null) {
                    try {
                        if (isOnError()) {
                            cursorTransaction.rollback();
                        } else {
                            cursorTransaction.commit();
                        }
                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                    } finally {
                        try {
                            cursorTransaction.closeTransaction();
                            cursorTransaction = null;
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        // Fin du process
        return !isOnError();
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("MAJ_FAFAEXT_EMAIL_FAILED");
        } else {
            return getSession().getLabel("MAJ_FAFAEXT_EMAIL_SUCCESS");
        }
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getSimulation() {
        return simulation;
    }

    public String getVerbose() {
        return verbose;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        if ("true".equalsIgnoreCase(getSimulation())) {
            return GlobazJobQueue.READ_LONG;
        } else {
            return GlobazJobQueue.UPDATE_LONG;
        }
    }

    /**
     * Cette méthode permet de mettre à jour la facturation externe (date du dernier paiement et montant)
     * 
     * @param entity
     * @param op
     * @throws Exception
     */
    private void miseAJourFacturationExterne(CAMiseAJourEcritureFacturationExterne entity, CAOperation op)
            throws Exception {
        // Recherche des opérations de facturation externe
        FAFacturationExtManager faManager = new FAFacturationExtManager();
        faManager.setSession(getSession());
        faManager.setForNumAffilie(entity.getIdExterneRole());
        faManager.setForRole(entity.getIdRole());
        faManager.setForNumPeriode(entity.getNumPeriode());
        faManager.setNumPassageIsNullOrZero(new Boolean(false));
        faManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        faManager.find();
        for (int i = 0; i < faManager.size(); i++) {
            FAFacturationExt facExt = (FAFacturationExt) faManager.getEntity(i);
            facExt.setCotisationPaye(JANumberFormatter.deQuote(facExt.getCotisation()));
            facExt.setDatePaiement(op.getDate());

            if (!"true".equalsIgnoreCase(getSimulation())) {
                facExt.update(getTransaction());
                // Commit
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                    if ("true".equalsIgnoreCase(getVerbose())) {
                        getMemoryLog().logMessage("UPDATE: " + entity.toMyString(), FWMessage.INFORMATION,
                                this.getClass().getName());
                    }
                    // Rollback
                } else {
                    getTransaction().rollback();
                    getMemoryLog().logMessage("ROLLBACK: " + entity.toMyString(), FWMessage.FATAL,
                            this.getClass().getName());
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                    getTransaction().clearErrorBuffer();
                }
            } else {
                if ("true".equalsIgnoreCase(getVerbose())) {
                    getMemoryLog().logMessage("UPDATE: " + entity.toMyString(), FWMessage.INFORMATION,
                            this.getClass().getName());
                }
            }
        }
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    /**
     * @param String
     */
    public void setSimulation(String string) {
        simulation = string;
    }

    /**
     * @param String
     *            string
     */
    public void setVerbose(String string) {
        verbose = string;
    }
}
