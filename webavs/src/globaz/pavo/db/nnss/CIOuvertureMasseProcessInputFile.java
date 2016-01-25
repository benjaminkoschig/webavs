package globaz.pavo.db.nnss;

import globaz.commons.nss.db.NSSinfo;
import globaz.commons.nss.db.NSSinfoManager;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.JadeLogger;
import globaz.naos.nnss.AFOuvertureCIHTMLOutput;
import globaz.naos.nnss.AFOuvertureCILog;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CIOuvertureMasseProcessInputFile extends BProcess {

    private static final long serialVersionUID = -1161449515263497803L;
    public final static String ANNONCE_APP = "annonce";
    public final static String APPLICATIONID = "HERMES";
    public final static String CODE_AGENCE = "noAgence";
    public final static String CODE_APPLICATION_ARC = "11";
    public final static String CODE_CAISSE = "noCaisse";
    public final static String DEFAULT_APPLICATION_PAVO = "PAVO";
    public final static String KEY_SESSION_HERMES = "sessionHermes";

    public static void main(String[] args) {
        try {

            if (args.length < 3) {
                System.out
                        .println("java globaz.pavo.db.nnss.CIOuvertureMasseProcessInputFile <user> <password> <email>");
            }

            String user = "cicibatch";
            if (args.length > 0) {
                user = args[0];
            }

            String pwd = "cicibatch";
            if (args.length > 1) {
                pwd = args[1];
            }

            Jade.getInstance();
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(user, pwd);

            CIOuvertureMasseProcessInputFile process = new CIOuvertureMasseProcessInputFile((BSession) session);

            String email = "";
            if (args.length > 2) {
                email = args[2];
            }
            if (args.length > 3) {

                String hasAffilie = args[3];
                if ("true".equals(hasAffilie)) {
                    process.setHasAffilie(new Boolean(true));
                } else {
                    process.setHasAffilie(new Boolean(false));
                }
            }
            if (args.length > 4) {
                process.setMotifDefaut(args[4]);
            }
            if (args.length > 5) {
                process.setMotifRentier((args[5]));
            }
            if (args.length > 6) {
                process.setPath((args[6]));
            }

            process.setEMailAddress(email);
            JadeJobInfo job = BProcessLauncher.start(process);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Process Ouverture CI not executed successfully !");
                System.out.println(job.getFatalErrorMessage());
                System.exit(0);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process Cloture Ouverture CI executed successfully !");
                System.exit(0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            System.out.println("Process Ouverture CI has error(s) !");

            System.exit(0);

        } finally {
            Jade.getInstance().endProfiling();
        }

        System.exit(0);
    }

    private String employeur = "";
    private Boolean hasAffilie = null;
    private String motifDefaut = "";
    private String motifRentier = "";

    private String path = "";

    public CIOuvertureMasseProcessInputFile() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIOuvertureMasseProcessInputFile(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        AFOuvertureCIHTMLOutput doc = new AFOuvertureCIHTMLOutput();
        doc.setSession(getSession());
        AFOuvertureCILog log;
        List liste = new ArrayList();
        java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(path));
        String line = fileIn.readLine();
        java.util.StringTokenizer tokens;
        while ((line = fileIn.readLine()) != null) {
            tokens = new java.util.StringTokenizer(line, ";");
            String noAvs = tokens.nextToken();
            String noAffilie = "";
            if (hasAffilie.booleanValue()) {
                noAffilie = tokens.nextToken();
            }
            if (!JadeStringUtil.isIntegerEmpty(noAvs)) {

                log = new AFOuvertureCILog(noAffilie, noAvs, noAvs, "");
                AFOuvertureCILog annonce = envoiARC(noAvs, log, noAffilie);
                if (annonce.getAnnonce() == null) {
                    liste.add(log);
                }

            } else {
                // Pas de NNSS dans AFFAFIP => log
                log = new AFOuvertureCILog(noAffilie, noAvs, "-", getSession().getLabel("OUVERTURECI_LOG_NONNSS"));
                liste.add(log);
            }
        }

        doc.setData(liste);

        System.out.print("\n\n session terminée ");

        doc.setFilename("DOC_CIPOUVERTUREMASSEPROCESS.html");

        registerAttachedDocument(doc.getOutputFile());

        if (isOnError()) {
            return false;
        } else {
            return true;
        }
    }

    public AFOuvertureCILog annonceARC(HashMap attributs, AFOuvertureCILog log, String noAffillie) {
        // boolean withErrors = true;
        boolean withErrors = false;

        BTransaction transaction = getTransaction();
        String idAnnonce = null;
        if (transaction != null && !transaction.hasErrors()) {
            BITransaction remoteTransaction = null;
            BSession local = transaction.getSession();
            try {
                BISession remoteSession = getSessionAnnonce(local);
                remoteTransaction = ((BSession) remoteSession).newTransaction();
                remoteTransaction.openTransaction();
                // création de l'API
                IHEInputAnnonce remoteEcritureAnnonce = (IHEInputAnnonce) remoteSession
                        .getAPIFor(IHEInputAnnonce.class);
                // attributs standard ARC

                remoteEcritureAnnonce.wantCheckCiOuvert(IHEAnnoncesViewBean.WANT_CHECK_CI_OUVERT_FALSE);
                //
                if (employeur.equals("employ")) {
                    remoteEcritureAnnonce.setCategorie(IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR);
                } else {
                    remoteEcritureAnnonce.setCategorie(IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT);
                }

                remoteEcritureAnnonce.setIdProgramme(DEFAULT_APPLICATION_PAVO);

                String user = getSession().getUserId();

                if (!JAUtil.isStringEmpty(user)) {
                    remoteEcritureAnnonce.setUtilisateur(user);
                } else {
                    remoteEcritureAnnonce.setUtilisateur(local.getUserId());
                }
                remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, CODE_APPLICATION_ARC);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        "PAVO//" + JACalendar.todayJJsMMsAAAA());
                remoteEcritureAnnonce.setPriorite(IHELotViewBean.LOT_PTY_BASSE);
                remoteEcritureAnnonce.setNumeroAffilie(noAffillie);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE, getProperty(CODE_CAISSE, local));
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE, getProperty(CODE_AGENCE, local));
                // ajout de attributs spécifique à l'annonce
                remoteEcritureAnnonce.putAll(attributs);

                // System.out.print("\n"
                // +remoteEcritureAnnonce.getNumeroAffilie() );
                // todo: même transaction mais le rollback ne fonctionne pas ->
                // fw
                remoteEcritureAnnonce.wantCheckNumAffilie(IHEAnnoncesViewBean.WANT_CHECK_NUM_AFF_FALSE);
                remoteEcritureAnnonce.add(remoteTransaction);
                // remoteEcritureAnnonce.add(transaction);
                if (remoteTransaction.hasErrors()) {
                    if (withErrors) {
                        transaction.addErrors(local.getLabel("MSG_ANNONCE_INPUT_INVALID"));
                    }
                    JadeLogger.warn(remoteEcritureAnnonce, local.getLabel("MSG_ANNONCE_INPUT_INVALID") + " ("
                            + attributs.get(IHEAnnoncesViewBean.NUMERO_ASSURE) + "): " + remoteTransaction.getErrors());

                    // Logg
                    log.setMsg(local.getLabel("MSG_ANNONCE_INPUT_INVALID") + " ("
                            + attributs.get(IHEAnnoncesViewBean.NUMERO_ASSURE) + "): " + remoteTransaction.getErrors());

                    remoteTransaction.rollback();
                } else {
                    // annonce ok
                    idAnnonce = remoteEcritureAnnonce.getRefUnique();
                    remoteTransaction.commit();
                }
            } catch (Exception ex) {
                // annonce invalide
                if (withErrors) {
                    transaction.addErrors(local.getLabel("MSG_ANNONCE_INPUT_INVALID"));
                }
                JadeLogger.warn(
                        this,
                        local.getLabel("MSG_ANNONCE_INPUT_INVALID") + " ("
                                + attributs.get(IHEAnnoncesViewBean.NUMERO_ASSURE) + ")");
                // Logg

                log.setMsg(local.getLabel("MSG_ANNONCE_INPUT_INVALID") + " ("
                        + attributs.get(IHEAnnoncesViewBean.NUMERO_ASSURE) + ")");

                // FWDebug.println(FWDebug.ERROR, ex);

            } finally {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception ec) {
                }
            }
        }

        log.setAnnonce(idAnnonce);

        return log;
    }

    public AFOuvertureCILog envoiARC(String numAVS, AFOuvertureCILog log, String noAffilie) throws Exception {
        HashMap attributs = new HashMap();
        attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, getMotif(numAVS));
        // assuré
        attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, numAVS);
        // envoi
        AFOuvertureCILog annonce = annonceARC(attributs, log, noAffilie);

        return annonce;
    }

    public BIApplication getApplication(String application, BSession session) throws Exception {
        return GlobazSystem.getApplication(APPLICATIONID);
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError() && !isAborted()) {
            return "Le process  s'est effectué avec succès!";
        }
        return "Le process a échoué!";
    }

    public String getEmployeur() {
        return employeur;
    }

    public Boolean getHasAffilie() {
        return hasAffilie;
    }

    private String getMotif(String nss) throws Exception {
        NSSinfo info = null;
        try {
            NSSinfoManager mgr = new NSSinfoManager();
            mgr.setSession(getSession());
            mgr.setForNNSS(nss);
            mgr.setForCodeMutation("0");
            mgr.setForValidite("1");
            mgr.find();

            if (mgr.size() > 0) {
                info = (NSSinfo) mgr.getFirstEntity();
            } else {
                mgr.setForValidite("0");
                mgr.find();
                if (mgr.size() > 0) {
                    info = (NSSinfo) mgr.getFirstEntity();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return motifDefaut;
        }
        if (info == null) {
            return motifDefaut;
        }
        String dateNaissance = "";
        if (!JadeStringUtil.isBlankOrZero(info.getDateNaissance())) {
            dateNaissance = info.getDateNaissance();
        } else {
            return motifDefaut;
        }
        // Bidouille pour calcul du sexe homme =>1 femme >4
        String sexe = CICompteIndividuel.CS_FEMME;
        if ("1".equals(info.getSexe())) {
            sexe = CICompteIndividuel.CS_HOMME;
        }
        if (CIUtil.isRetraite(new JADate(dateNaissance), sexe, JACalendar.today().getYear())) {
            return motifRentier;
        }
        return motifDefaut;
    }

    public String getMotifDefaut() {
        return motifDefaut;
    }

    public String getMotifRentier() {
        return motifRentier;
    }

    public String getPath() {
        return path;
    }

    public final String getProperty(String propertyName, BSession session) throws Exception {
        CIApplication application = (CIApplication) session.getApplication();
        return application.getProperty(propertyName);
    }

    public BISession getSessionAnnonce(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(KEY_SESSION_HERMES);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(ANNONCE_APP, local).newSession(local);
            local.setAttribute(KEY_SESSION_HERMES, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public void setHasAffilie(Boolean hasAffilie) {
        this.hasAffilie = hasAffilie;
    }

    public void setMotifDefaut(String motifDefaut) {
        this.motifDefaut = motifDefaut;
    }

    public void setMotifRentier(String motifRentier) {
        this.motifRentier = motifRentier;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
