package globaz.hermes.zas;

import globaz.framework.process.FWProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.hermes.application.HEApplication;
import globaz.hermes.print.itext.HERappelDocument_Doc;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEImpressionRappels extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        boolean profile = false;
        try {
            if (args.length < 3) {
                System.out
                        .println(DateUtils.getTimeStamp()
                                + "java globaz.hermes.zas.HEImpressionRappels <uid> <pwd> <intervalle (=0 -> aucune limite inférieure)> ");
                System.exit(-1);
            }
            // créer la session
            BSession session = new BSession("HERMES");
            session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES").newSession(args[0], args[1]);
            HEImpressionRappels imprimerProcess = new HEImpressionRappels(args);
            imprimerProcess.setSession(session);
            imprimerProcess.setIntervalle(args[2]);
            Jade.getInstance().setHomeDir(session.getApplication().getProperty("zas.rappels.print.home.dir"));
            if (profile = ("true".equals(session.getApplication().getProperty("profiling")))) {
                Jade.getInstance().beginProfiling(HEImpressionRappels.class, args);
            }
            JadeLogger.enableTrace(false);
            imprimerProcess.executeProcess();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (profile) {
                Jade.getInstance().endProfiling();
            }
            System.exit(-1);
        }
    }

    private int intervalle = 0;
    private String pwd = "";

    private String userID = "";

    /**
     * Constructor for HEImprimerRappels.
     */
    public HEImpressionRappels() {
        super();
    }

    /**
     * Constructor for HEImprimerRappels.
     * 
     * @param session
     */
    public HEImpressionRappels(BSession session) {
        super(session);
    }

    /**
     * Constructor for HEImprimerRappels.
     * 
     * @param parent
     */
    public HEImpressionRappels(FWProcess parent) {
        super(parent);
    }

    public HEImpressionRappels(String[] args) throws Exception {
        super();
        //
        setUserID(args[0]);
        setPwd(args[1]);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            if (getSession().getApplication().getProperty("rappel.print.log").equals("true")) {
                String logOut = getSession().getApplication().getProperty("rappels.print.home.dir") + "/"
                        + getSession().getApplication().getProperty("rappels.log.dir") + "/" + DateUtils.getMonthYear()
                        + "/HEImpressionRappels/out/" + getSession().getApplication().getProperty("rappels.log.out")
                        + DateUtils.getLocaleDateAndTime() + ".log";
                String logErr = getSession().getApplication().getProperty("rappels.print.home.dir") + "/"
                        + getSession().getApplication().getProperty("rappels.log.dir") + "/" + DateUtils.getMonthYear()
                        + "/HEImpressionRappels/err/" + getSession().getApplication().getProperty("rappels.log.err")
                        + DateUtils.getLocaleDateAndTime() + ".log";
                StringUtils.createDirectory(logOut);
                StringUtils.createDirectory(logErr);
                PrintStream streamOut = new PrintStream(new FileOutputStream(logOut));
                System.setOut(streamOut);
                PrintStream streamErr = new PrintStream(new FileOutputStream(logErr));
                System.setErr(streamErr);
            }

            HERappelDocument_Doc imprimerRappel = new HERappelDocument_Doc();
            imprimerRappel.setSession(getSession());
            String nbresJours = ((HEApplication) getSession().getApplication())
                    .getProperty("rappels.nbresJours.attendre");
            System.out.println("Nombre de jours à attendre " + nbresJours);
            imprimerRappel.setNbJours(nbresJours);
            imprimerRappel.setIntervalle(getIntervalle());
            String email = ((HEApplication) getSession().getApplication()).getProperty("zas.user.email");
            imprimerRappel.setEmail(email);
            // set l'adresse email
            String emailAdmin = ((HEApplication) getSession().getApplication())
                    .getProperty("rappels.print.admin.email");
            setEMailAddress(emailAdmin);
            System.out.println(DateUtils.getTimeStamp() + "Lancement du process d'impression des rappels...");
            imprimerRappel.executeProcess();
            System.out.println(DateUtils.getTimeStamp() + "Fin de l'impression des rappels. Envoi du résultat à "
                    + emailAdmin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            _addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (getTransaction().isRollbackOnly()) {
            return "L'impression des rappels s'est terminé avec des erreurs";
        } else {
            return "L'impression des rappels s'est terminée sans erreur";
        }
    }

    /**
     * Returns the intervalle.
     * 
     * @return int
     */
    public int getIntervalle() {
        return intervalle;
    }

    /**
     * Returns the pwd.
     * 
     * @return String
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Returns the userID.
     * 
     * @return String
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Sets the intervalle.
     * 
     * @param intervalle
     *            The intervalle to set
     */
    public void setIntervalle(String intervalle) {
        this.intervalle = Integer.parseInt(intervalle);
    }

    /**
     * Sets the pwd.
     * 
     * @param pwd
     *            The pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Sets the userID.
     * 
     * @param userID
     *            The userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

}
