package globaz.naos.nnss;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AFOuvertureCIProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ANNONCE_APP = "annonce";
    public final static String APPLICATIONID = "HERMES";
    public final static String CODE_AGENCE = "noAgence";
    public final static String CODE_APPLICATION_ARC = "11";
    public final static String CODE_CAISSE = "noCaisse";
    public final static String DEFAULT_APPLICATION_NAOS = "NAOS";

    public final static String EMAIL = " ";
    public final static String KEY_SESSION_HERMES = "sessionHermes";

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

            String motif = "";
            if (args.length > 2) {
                motif = args[2];
            }

            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(user, pwd);
            AFOuvertureCIProcess process = new AFOuvertureCIProcess((BSession) session);

            String email = AFOuvertureCIProcess.EMAIL;
            if (args.length > 3) {
                email = args[3];
            }

            process.setEMailAddress(email);
            if (args.length > 4) {
                process.setFromNumeroAffilie(args[4]);
            }

            if (args.length > 5) {
                process.setUntilNumeroAffilie(args[5]);
            }

            process.setMotif(motif);

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

            // erreur critique, je retourne un code d'erreur 200
            System.exit(0);

        } finally {
            Jade.getInstance().endProfiling();
        }
        System.exit(0);
    }

    private ArrayList annonceAvs = null;
    private String date = "31.12.2008";
    private String fromNumeroAffilie = "";
    private String motif = "";
    private String tabAnnAvs[];

    private String untilNumeroAffilie = "";

    public AFOuvertureCIProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public AFOuvertureCIProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        int totalAffilie = 0;
        AFOuvertureCILog log;
        ArrayList liste = new ArrayList();
        annonceAvs = new ArrayList();

        AFOuvertureCIHTMLOutput doc = new AFOuvertureCIHTMLOutput();
        doc.setSession(getSession());

        // Manager pour la table Affiliation => AFAFFIP
        AFAffiliationManager mgr_aff = new AFAffiliationManager();
        mgr_aff.setSession(getSession());
        mgr_aff.setOrderBy(AFAffiliationManager.ORDER_AFFILIENUMERO);

        // Manager pour l'historique des tiers => TIPAVSP
        TIPersonneAvsManager mgr_AVS = new TIPersonneAvsManager();
        mgr_AVS.setSession(getSession());
        System.out.print("\n Début du Process \n");

        // critère de sélection dans AFAFFIP
        mgr_aff.setToDateNNSS(date);
        mgr_aff.setFromAffilieNumero(fromNumeroAffilie);
        mgr_aff.setToAffilieNumero(untilNumeroAffilie);
        mgr_aff.setForTypeNNSS("true");

        // ouverture
        BStatement statement = mgr_aff.cursorOpen(getTransaction());
        AFAffiliation aff = null;

        mgr_AVS.changeManagerSize(1);

        try {

            while ((aff = (AFAffiliation) mgr_aff.cursorReadNext(statement)) != null) {

                totalAffilie++;

                mgr_AVS.setForIdTiers(aff.getIdTiers());
                mgr_AVS.find();
                Iterator ite = mgr_AVS.iterator();
                TITiersViewBean co = (TITiersViewBean) ite.next();

                if (!JadeStringUtil.isIntegerEmpty(co.getNumAvsActuel())) {

                    if (co.getNumAvsActuel().trim().length() < 16) {
                        // NNSS non présent table AFFAFIP => recherche
                        // concordance dans NNSSRA
                        // System.out.print("\t\t\t -> n'est pas un NNSS\n");
                        String s = null;

                        try {
                            s = globaz.commons.nss.NSUtil.returnNNSS(getSession(),
                                    NSUtil.unFormatAVS(co.getNumAvsActuel()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (s == null) {
                            // pas dispo NNSSRA => log
                            // System.out.print("\t pas dispo dans NNSSRA (AVS = "+s+")\n");
                            log = new AFOuvertureCILog(aff.getAffilieNumero(), aff.getIdTiers(), co.getNumAvsActuel(),
                                    getSession().getLabel("OUVERTURECI_LOG_NOCONNSS"));
                            liste.add(log);
                        } else {
                            // Dispo dans NNSSRA => ANNONCE
                            // System.out.print("\t !! DISPO dans NNSSRA (AVS = "+s+")\n");

                            log = new AFOuvertureCILog(aff.getAffilieNumero(), aff.getIdTiers(), s, "");

                            s = NSUtil.formatAVSNewNum(s);
                            AFOuvertureCILog annonce = envoiARC(s, log, aff.getAffilieNumero());
                            if (annonce.getAnnonce() == null) {
                                liste.add(log);
                            }

                        }
                    } else if (globaz.commons.nss.NSUtil.nssCheckDigit(co.getNumAvsActuel())) {
                        // NNSS Present dans table AFFAFIP => ANNONCE

                        log = new AFOuvertureCILog(aff.getAffilieNumero(), aff.getIdTiers(), co.getNumAvsActuel(), "");

                        AFOuvertureCILog annonce = envoiARC(co.getNumAvsActuel(), log, aff.getAffilieNumero());
                        if (annonce.getAnnonce() == null) {
                            liste.add(log);
                        }

                    } else {
                        System.out.print("\t !! NNSS invalide \n");
                        log = new AFOuvertureCILog(aff.getAffilieNumero(), aff.getIdTiers(), co.getNumAvsActuel(),
                                getSession().getLabel("OUVERTURECI_LOG_NNSSINV"));
                        liste.add(log);
                    }
                } else {
                    // Pas de NNSS dans AFFAFIP => log
                    // System.out.print("\t pas de NSS \n");
                    log = new AFOuvertureCILog(aff.getAffilieNumero(), aff.getIdTiers(), "-", getSession().getLabel(
                            "OUVERTURECI_LOG_NONNSS"));
                    liste.add(log);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mgr_aff.cursorClose(statement);
        }

        doc.setData(liste);
        System.out.print("\n recherche : " + totalAffilie);
        System.out.print("\n\n session terminée ");
        doc.setFilename("DOC_CIPROCESS.html");

        this.registerAttachedDocument(doc.getOutputFile());
        // doc.getOutputFile();

        return false;
    }

    public AFOuvertureCILog annonceARC(HashMap attributs, AFOuvertureCILog log, String noAffilie) {
        // boolean withErrors = true;
        boolean withErrors = false;

        BTransaction transaction = getTransaction();
        String idAnnonce = null;
        if ((transaction != null) && !transaction.hasErrors()) {
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
                remoteEcritureAnnonce.setCategorie(IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT);
                remoteEcritureAnnonce.setIdProgramme(AFOuvertureCIProcess.DEFAULT_APPLICATION_NAOS);
                remoteEcritureAnnonce.setNumeroAffilie(noAffilie);
                String user = getSession().getUserId();

                if (!JAUtil.isStringEmpty(user)) {
                    remoteEcritureAnnonce.setUtilisateur(user);
                } else {
                    remoteEcritureAnnonce.setUtilisateur(local.getUserId());
                }
                remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION,
                        AFOuvertureCIProcess.CODE_APPLICATION_ARC);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        "PAVO//" + JACalendar.todayJJsMMsAAAA());
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                        getProperty(AFOuvertureCIProcess.CODE_CAISSE, local));
                remoteEcritureAnnonce.setPriorite(IHELotViewBean.LOT_PTY_BASSE);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                        getProperty(AFOuvertureCIProcess.CODE_AGENCE, local));
                // ajout de attributs spécifique à l'annonce
                remoteEcritureAnnonce.putAll(attributs);
                remoteEcritureAnnonce.wantCheckNumAffilie(IHEAnnoncesViewBean.WANT_CHECK_NUM_AFF_FALSE);

                // System.out.print("\n"
                // +remoteEcritureAnnonce.getNumeroAffilie() );
                // todo: même transaction mais le rollback ne fonctionne pas ->
                // fw
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
                if (remoteTransaction != null) {
                    try {
                        remoteTransaction.closeTransaction();
                    } catch (Exception ec) {
                    }
                }
            }
        }

        log.setAnnonce(idAnnonce);
        return log;
    }

    public AFOuvertureCILog envoiARC(String numAVS, AFOuvertureCILog log, String noAffilie) {
        HashMap attributs = new HashMap();
        attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, getMotif());
        // assuré
        attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, numAVS);
        // envoi
        AFOuvertureCILog annonce = annonceARC(attributs, log, noAffilie);

        return annonce;
    }

    public BIApplication getApplication(String application, BSession session) throws Exception {
        return GlobazSystem.getApplication(AFOuvertureCIProcess.APPLICATIONID);
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError() && !isAborted()) {
            return "Le process d'ouverture des indépendants / Non-Actif s'est effectué avec succès!";
        }
        return "Le process d'ouverture des indépendants / Non-Actif a échoué!";
    }

    /**
     * @return
     */
    public String getFromNumeroAffilie() {
        return fromNumeroAffilie;
    }

    public String getMotif() {
        return motif;
    }

    public final String getProperty(String propertyName, BSession session) throws Exception {
        AFApplication application = (AFApplication) session.getApplication();
        return application.getProperty(propertyName);
    }

    public BISession getSessionAnnonce(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(AFOuvertureCIProcess.KEY_SESSION_HERMES);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(AFOuvertureCIProcess.ANNONCE_APP, local).newSession(local);
            local.setAttribute(AFOuvertureCIProcess.KEY_SESSION_HERMES, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * @return
     */
    public String getUntilNumeroAffilie() {
        return untilNumeroAffilie;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setFromNumeroAffilie(String string) {
        fromNumeroAffilie = string;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * @param string
     */
    public void setUntilNumeroAffilie(String string) {
        untilNumeroAffilie = string;
    }

}
