package globaz.pavo.process;

import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;

/**
 * Traitement journalier des comptes individules (ca 21 et 23). Date de création : (25.11.2002 11:52:37)
 * 
 * @author: Administrator
 */
public class CIUpdatePrepared extends BProcess {

    private static final long serialVersionUID = 3244249533210378727L;

    public static void main(String[] args) {
        try {

            String user = "";
            if (args.length > 0) {
                user = args[0];
            }
            String pwd = "";
            if (args.length > 1) {
                pwd = args[1];
            }
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(user, pwd);
            CIUpdatePrepared process = new CIUpdatePrepared((BSession) session);

            process.setEMailAddress("jmc@globaz.ch");
            if (args.length > 2) {
                process.setEMailAddress(args[2]);
            }
            process.setEchoToConsole(true);
            process.executeProcess();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Jade.getInstance().endProfiling();
        }
        System.exit(0);
    }

    private boolean echoToConsole = false;
    private String fromNumAvs = "";
    public int nbrToRead = 1000000;

    private String untillNumAvs = "";

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIUpdatePrepared() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIUpdatePrepared(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        CICompteIndividuelPreparedManager mgrPrp = new CICompteIndividuelPreparedManager();
        BStatement statement = null;
        CIEcritureFastManagerPrep ecrMgr = new CIEcritureFastManagerPrep();
        BPreparedStatement ecrPrepared = null;
        BPreparedStatement ciPrepared = null;
        try {

            // Preparation des prepstat
            ecrMgr.setSession(getSession());
            mgr.setSession(getSession());
            ecrPrepared = new BPreparedStatement(getTransaction());
            ecrPrepared.prepareStatement(ecrMgr._getSql(ecrPrepared));
            ciPrepared = new BPreparedStatement(getTransaction());
            ciPrepared.prepareStatement(mgrPrp._getSql(ciPrepared));
            // manager CI
            // mgr.setForNumeroAvs("10085523118");
            mgr.setFromNumeroAvs(fromNumAvs);
            mgr.setUntilNumeroAvs(untillNumAvs);
            mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);

            mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            // mgr.orderByAvs(false);
            mgr.wantCallMethodAfter(false);
            mgr.wantCallMethodBefore(false);
            // manager ecriture

            statement = mgr.cursorOpen(getTransaction());
            if (echoToConsole) {
                System.out.println("starting execution");
            }
            // Boucle pour la lecture
            CICompteIndividuel ci;
            int counter = 1;
            int countUpdate = 1;
            long time = System.currentTimeMillis();
            while ((ci = (CICompteIndividuel) mgr.cursorReadNext(statement)) != null) {
                ecrPrepared.clearParameters();

                ecrPrepared.setInt(1, Integer.parseInt(ci.getCompteIndividuelId()));

                CIEcritureFast entity = null;
                try {
                    while ((entity = (CIEcritureFast) ecrMgr.prepareReadNext(ecrPrepared)) != null) {
                        String employeur = entity.getEmployeur();
                        if (!JAUtil.isIntegerEmpty(employeur)) {
                            if (!employeur.equals(ci.getDernierEmployeur())) {
                                ciPrepared.clearParameters();
                                ciPrepared.setInt(1, Integer.parseInt(employeur));
                                ciPrepared.setInt(2, Integer.parseInt(ci.getCompteIndividuelId()));
                                ciPrepared.execute();
                                countUpdate++;
                                getTransaction().commit();

                            }
                            break;
                        }

                    }
                } catch (Exception e) {
                    JadeLogger.error(this, e.toString() + " - NumeroAVS=" + ci.getNumeroAvs());
                    try {
                        getTransaction().rollback();
                    } catch (Exception err) {
                        JadeLogger.error(this, err.toString() + " - NumeroAVS=" + ci.getNumeroAvs());
                    }
                }

                if (counter % 1000 == 0) {
                    if (echoToConsole) {
                        System.out.println(counter + " ci updated in " + (System.currentTimeMillis() - time) / 1000
                                + "sec.");
                    }
                }
                counter++;
            }
            if (echoToConsole) {
                System.out.println(counter + " parcourus");
                System.out.println(countUpdate + " updated");
                System.out.println("Process done.");
                getMemoryLog().logMessage(counter + " parcourus", FWMessage.INFORMATION, "Mise à jour CI");
                getMemoryLog().logMessage(countUpdate + " updated", FWMessage.INFORMATION, "Mise à jour CI");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mgr.cursorClose(statement);
                statement = null;
                ecrMgr.prepareClose(ecrPrepared);
                ciPrepared.closePreparedStatement();
            } catch (Exception e) {
            }
        }
        return !isAborted();
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le traitement de mise à jour des comptes individuels a echoué!";
        } else {
            return "Le traitement de mise à jour des comptes individuels s'est effectué avec succès.";
        }
    }

    /**
     * Returns the fromNumAvs.
     * 
     * @return String
     */
    public String getFromNumAvs() {
        return fromNumAvs;
    }

    /**
     * Returns the untillNumAvs.
     * 
     * @return String
     */
    public String getUntillNumAvs() {
        return untillNumAvs;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Sets the fromNumAvs.
     * 
     * @param fromNumAvs
     *            The fromNumAvs to set
     */
    public void setFromNumAvs(String fromNumAvs) {
        this.fromNumAvs = fromNumAvs;
    }

    /**
     * Sets the untillNumAvs.
     * 
     * @param untillNumAvs
     *            The untillNumAvs to set
     */
    public void setUntillNumAvs(String untillNumAvs) {
        this.untillNumAvs = untillNumAvs;
    }

}
