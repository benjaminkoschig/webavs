package globaz.hermes.zas;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HEEnvoiEmailsGroupe;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Vector;

public class HEPostFile {
    public static final String ARCHIVES_FOLDER = "archives";
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    public static final String ENCODING = "Cp037";

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage : HEPutZAS <uid> <pwd>");
            System.exit(HEPostFile.CODE_RETOUR_ERREUR);
        }
        System.out.println("**********  HEPut excuting with user:" + args[0] + " password:" + args[1] + "  **********");
        /*
         * je prends le ftp.file.input
         */

        // démarrage en mode CommandeLineJob pour ne pas exécuter les processus SEDEX
        Jade.getInstanceForCommandLineJob();

        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[0], args[1]);
            HEPostFile un = new HEPostFile();
            un.setSession(session);

            if (un.getSession().getApplication().getProperty("zas.log").equals("true")) {
                String outLog = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/HEPutZAS/out/"
                        + un.getSession().getApplication().getProperty("zas.log.put.out")
                        + DateUtils.getLocaleDateAndTime() + ".log";
                String errLog = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/HEPutZAS/err/"
                        + un.getSession().getApplication().getProperty("zas.log.put.err")
                        + DateUtils.getLocaleDateAndTime() + ".log";
                StringUtils.createDirectory(outLog);
                StringUtils.createDirectory(errLog);
                PrintStream streamOut = new PrintStream(new FileOutputStream(outLog));
                System.setOut(streamOut);
                PrintStream streamErr = new PrintStream(new FileOutputStream(errLog));
                System.setErr(streamErr);
            }
            un.emailResponsable = new HEEnvoiEmailsGroupe(un.getSession(), HEEnvoiEmailsGroupe.responsable_ARC);
            System.out.println(DateUtils.getTimeStamp() + un.emailResponsable.size()
                    + "  mail(s) to send process errors");
            System.out.println(DateUtils.getTimeStamp() + "Importing data from HERMES db into file");
            System.out.println(DateUtils.getTimeStamp() + "Opening a new transaction...");
            BTransaction transaction = null;
            try {
                transaction = (BTransaction) un.crtSession.newTransaction();
                transaction.openTransaction();
                System.out.println(DateUtils.getTimeStamp() + "New transaction opened");
                System.out.println(DateUtils.getTimeStamp() + "Generating the file...");
                File f = un.generateFile(transaction);
                System.out.println(DateUtils.getTimeStamp() + "File generated");
                System.out.println(DateUtils.getTimeStamp() + "Confirm the set of advertisements...");
                un.confirmLots(transaction);
                un.confirmAnnInscrCentrale(transaction);
                System.out.println(DateUtils.getTimeStamp() + "The set confirmed");
                if (!transaction.isRollbackOnly()) {
                    System.out.println(DateUtils.getTimeStamp() + "File is sending...");
                    HEPostFile.sendFile(f.getAbsolutePath(), session);
                    System.out.println(DateUtils.getTimeStamp() + "File sended");
                    // aucune erreur dans la transaction et le file a pu être
                    // posté
                    transaction.commit();
                    System.out.println(DateUtils.getTimeStamp() + "Treatment ok : TRANSACTION COMMIT");
                    transaction.closeTransaction();
                    un.deleteFileAnnInscrCentrale();
                    System.exit(HEPostFile.CODE_RETOUR_OK);
                } else {
                    System.err.println(transaction.getErrors());
                    transaction.rollback();
                    System.err.println("TRANSACTION ROLLBACK !!!");
                    System.exit(HEPostFile.CODE_RETOUR_ERREUR);
                }
            } catch (Exception e) {
                un.emailResponsable.sendMail(un.getSession().getLabel("HERMES_10019"), transaction.getErrors() + "\n"
                        + e.getMessage());
                if (transaction.hasErrors()) {
                    System.err.println(transaction.getErrors());
                }
                System.err.println(e.getMessage());
                transaction.rollback();
                System.err.println("TRANSACTION ROLLBACK !!!");
                System.exit(HEPostFile.CODE_RETOUR_ERREUR);
            } finally {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(HEPostFile.CODE_RETOUR_ERREUR);
        }
        System.exit(HEPostFile.CODE_RETOUR_ERREUR);
    }

    private static void sendFile(String fileName, BSession session) throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpPath = application.getProperty("ftp.centrale.path");
        if (JadeStringUtil.isEmpty(ftpPath)) {
            throw new Exception("paramétrage du serveur ftp erroné!");
        }
        String ftpFile = application.getProperty("ftp.file.output");
        if (JadeStringUtil.isEmpty(ftpFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpFile;
        try {
            if (JadeFsFacade.exists(ftpUri)) {
                throw new Exception("Le fichier est déjà présent sur le serveur FTP !");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        String nomfichier = application.getProperty("ftp.file.output");
        if (JadeStringUtil.isEmpty(ftpFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String archivesUri = JadeFilenameUtil.normalizePathRoot(JadeFilenameUtil.normalizePathRoot(Jade.getInstance()
                .getSharedDir()) + HEPostFile.ARCHIVES_FOLDER)
                + nomfichier
                + "_"
                + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD)
                + HEUtil.getTimeHHMMSS();
        try {
            JadeFsFacade.copyFile(fileName, archivesUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de l'archivage du fichier:" + e.getMessage());
        }
        try {
            JadeFsFacade.copyFile(fileName, ftpUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de l'envoi du fichier:" + e.getMessage());
        }

    }

    private File annCentrale = null;
    private CIAnnonceCentrale annonceCentrale = null;
    private BSession crtSession;
    private HEEnvoiEmailsGroupe emailResponsable = null;
    private String fichierAnnCenPath;

    private String idLotAnnInscrCI = "";

    private int nbRecords = 0;

    private Vector vecLots = new Vector();

    public HEPostFile() {
        super();
        nbRecords = 0;
    }

    /**
     * Method ajouterAnnonceCentrale.
     * 
     * @param fos
     * @param crtTransaction
     */
    private void ajouterAnnonceCentrale(File out, BTransaction crtTransaction) {
        try {
            System.out.println("Recherche d'un lot d'annonces d'enregistrement CI...");
            CIAnnonceCentraleManager annonceInsc = new CIAnnonceCentraleManager();
            annonceInsc.setSession(getSession());
            annonceInsc.setForStatut(CIAnnonceCentrale.CS_ETAT_GENERE);
            annonceInsc.setOrder("KRDENV");
            annonceInsc.find(crtTransaction);
            System.out.println(annonceInsc.size() + " lot(s) trouv\u00e9(s)");
            if (annonceInsc.size() > 0) {
                System.out.println("On envoie le premier en fonction de la date d'envoi");
                // il y a des annnonce d'inscriptions CI à envoyer à la centrale
                annonceCentrale = (CIAnnonceCentrale) annonceInsc.getFirstEntity();
                fichierAnnCenPath = Jade.getInstance().getSharedDir()
                        + CIAnnonceCentrale.PREFIX_FICHIER_SORTIE
                        + DateUtils.convertDate(annonceCentrale.getDateEnvoi(), DateUtils.JJMMAAAA_DOTS,
                                DateUtils.AAAAMMJJ);
                System.out.println("chargement du fichier " + fichierAnnCenPath);
                annCentrale = new File(fichierAnnCenPath);
                if (annCentrale.exists()) {
                    // on a un fichier a envoyer, on va tenter de l'ajouter au
                    // fichier Hermes courant
                    File crtSauvegarde = new File(out.getName() + ".save");
                    copyFile(out, crtSauvegarde);
                    try {
                        FileInputStream fis = new FileInputStream(annCentrale);
                        OutputStream outStream = new FileOutputStream(out.getAbsolutePath(), true);
                        byte[] buf = new byte[512];
                        int nread = 0;
                        System.out.println("Ajout des annonces inscriptions CI dans le fichier");
                        while ((nread = fis.read(buf)) >= 0) {
                            outStream.write(buf, 0, nread);
                        }
                        fis.close();
                        outStream.close();
                        System.out.println("Fin de l'ajout des inscriptions CI");
                        idLotAnnInscrCI = annonceCentrale.getAnnonceCentraleId();
                    } catch (Exception e) {
                        if (out.exists()) {
                            out.delete();
                        }
                        if (crtSauvegarde.exists()) {
                            System.out.println(DateUtils.getTimeStamp() + "The save file is reloaded");
                            copyFile(crtSauvegarde, out);
                        }
                        throw e;
                    } finally {
                        if (crtSauvegarde.exists()) {
                            crtSauvegarde.delete();
                        }
                    }
                } else {
                    System.out.println("Impossible de trouver le fichier " + fichierAnnCenPath);
                    emailResponsable.sendMail(getSession().getLabel("HERMES_10019"),
                            getSession().getLabel("HERMES_10025"));
                }
            }
        } catch (Exception e) {
            // envoi de l'erreur au(x) responsable(s)
            try {
                System.err.println("Warning, une erreur est survenue lors de l'ajout annonce centrale :"
                        + e.getMessage());
                JadeLogger.info("HEReprise, ajouterAnnonceCentrale(...)", e);
                emailResponsable.sendMail(getSession().getLabel("HERMES_10019"), getSession().getLabel("HERMES_10025")
                        + "\n" + e.getMessage());
            } catch (Exception err) {
                JadeLogger.info("HEReprise, ajouterAnnonceCentrale(...)", err);
            } finally {
                // ceci pour éviter la suppression du fichier!
                fichierAnnCenPath = "";
                // ceci pour éviter de valider le lot des annonces inscriptions
                // trouvées.
                idLotAnnInscrCI = "";
            }
        }
    }

    /**
     * Method confirmAnnInscrCentrale.
     * 
     * @param transaction
     */
    private void confirmAnnInscrCentrale(BTransaction transaction) throws Exception {
        if (!StringUtils.isStringEmpty(idLotAnnInscrCI)) {
            CIAnnonceCentrale lotEnvoye = new CIAnnonceCentrale();
            lotEnvoye.setSession(getSession());
            lotEnvoye.setAnnonceCentraleId(idLotAnnInscrCI);
            lotEnvoye.retrieve(transaction);
            if (!lotEnvoye.isNew()) {
                lotEnvoye.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
                lotEnvoye.setIdEtat(CIAnnonceCentrale.CS_ETAT_ENVOYE);
                lotEnvoye.update(transaction);
                System.out.println("Mise à jour de la date et de l'état du lot des annonces inscriptions CI");
            } else {
                System.err.println("Impossible de confirmer le lot des annonces inscriptions CI num :"
                        + idLotAnnInscrCI);
                throw new Exception("Impossible de confirmer le lot des annonces inscriptions CI num :"
                        + idLotAnnInscrCI);
            }
        }
    }

    public void confirmLots(BTransaction crtTransaction) throws Exception {
        for (int i = 0; i < vecLots.size(); i++) {
            String idLot = (String) vecLots.elementAt(i);
            System.out.println("Receipt for job " + idLot);
            HELotViewBean lot = new HELotViewBean();
            lot.setSession(crtSession);
            lot.setIdLot(idLot);
            lot.wantCallMethodAfter(false);
            lot.wantCallMethodBefore(false);
            lot.retrieve(crtTransaction);
            /*
             * changer le statut des annonces de ce lot
             */
            HEOutputAnnonceListViewBean annonces = new HEOutputAnnonceListViewBean();
            annonces.setSession(crtSession);
            annonces.setForIdLot(idLot);
            annonces.wantCallMethodAfter(false);
            annonces.wantCallMethodAfterFind(false);
            annonces.wantCallMethodBefore(false);
            annonces.wantCallMethodBeforeFind(false);
            annonces.find(crtTransaction, BManager.SIZE_NOLIMIT);
            for (int j = 0; j < annonces.size(); j++) {
                HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) annonces.getEntity(j);
                if (annonce.getChampEnregistrement().startsWith("38")
                        || annonce.getChampEnregistrement().startsWith("39")) {
                    annonce.setStatut(IHEAnnoncesViewBean.CS_TERMINE);
                } else {
                    annonce.setStatut(IHEAnnoncesViewBean.CS_A_TRAITER);
                }
                annonce.wantCallMethodBefore(false);
                annonce.wantCallValidate(false);
                annonce.wantCallMethodAfter(false);
                annonce.update(crtTransaction);
            }
            // on met seulement la quittance comme quoi le lot a été envoyé
            lot.setQuittance("1");
            lot.setEtat(HELotViewBean.CS_LOT_ETAT_ENVOYE);
            lot.setDateTraitement(JACalendar.todayJJsMMsAAAA());
            lot.setHeureTraitement(DateUtils.getCurrentTimeHMS());
            // TODO : changer l'heure, la date
            lot.update(crtTransaction);
        }
    }

    public void copyFile(File in, File out) throws Exception {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
    }

    /**
     * @param transaction
     */
    private void deleteFileAnnInscrCentrale() throws Exception {
        if (!StringUtils.isStringEmpty(fichierAnnCenPath)) {
            // effacer le fichier
            System.out.println("Delete file with PAVO advertissements : " + fichierAnnCenPath);
            File f = new File(fichierAnnCenPath);
            if (!f.delete()) {
                System.err.println("Erreur in PAVO advertissement deletion, please delete the file "
                        + fichierAnnCenPath);
                JadeLogger.info("HEReprise, ajouterAnnonceCentrale(...)",
                        "Erreur in PAVO advertissement deletion, please delete the file " + fichierAnnCenPath);
                emailResponsable.sendMail(getSession().getLabel("HERMES_10019"),
                        FWMessageFormat.format(getSession().getLabel("HERMES_10026"), fichierAnnCenPath));
            }
        }
    }

    public File generateFile(BTransaction crtTransaction) throws Exception {
        String fileName = Jade.getInstance().getSharedDir()
                + crtSession.getApplication().getProperty("ftp.file.output");
        File out = new File(fileName);
        if (out.exists()) {
            out.delete();
        }
        BufferedWriter fos;
        boolean isFileEBCDIC = "true".equals(crtSession.getApplication().getProperty("ftp.file.input.ebcdic"));
        /** config fichier, le fichier reçu a-t'il des carriage return ? * */
        boolean hasCarriageReturns = "true".equals(crtSession.getApplication().getProperty(
                "ftp.file.input.carriagereturn"));
        if (isFileEBCDIC) {
            // j'écris en EBCDIC
            fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), HEPostFile.ENCODING));
        } else {
            fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
        }
        // EBCDICFileWriter fos = new EBCDICFileWriter(out);
        System.out.println(DateUtils.getTimeStamp() + "fichier vide charg\u00e9");
        // écriture de l'entête
        System.out.println(DateUtils.getTimeStamp() + "ajout de l'ent\u00eate");
        fos.write(getLotHeader());
        if (hasCarriageReturns) {
            fos.write("\n");
        }
        System.out.println(DateUtils.getTimeStamp() + "recherche des annonces Hermes \u00e0 envoy\u00e9...");
        genererLotPtyHaute(crtTransaction, fos, hasCarriageReturns);
        // envoyer également un lot de priorité basse
        genereLotPtyBasse(crtTransaction, fos, hasCarriageReturns);
        // écriture de la fin
        fos.write(getLotFooter());
        if (hasCarriageReturns) {
            fos.write("\n");
        }
        fos.close();
        // écriture des annonces à la centrale
        ajouterAnnonceCentrale(out, crtTransaction);
        return out;
    }

    private void genereLotPtyBasse(BTransaction crtTransaction, BufferedWriter fos, boolean hasCarriageReturns)
            throws Exception {
        HELotListViewBean listLot = rechercherLots(crtTransaction, HELotViewBean.CS_LOT_PTY_BASSE, "RMILOT");
        System.out.println(listLot.size() + " lots priorités basses \u00e0 envoy\u00e9(s)");
        for (int i = 0; i < listLot.size(); i++) {
            traiterLot(crtTransaction, fos, hasCarriageReturns, (HELotViewBean) listLot.getEntity(i));
        }
    }

    private void genererLotPtyHaute(BTransaction crtTransaction, BufferedWriter fos, boolean hasCarriageReturns)
            throws Exception, IOException {
        HELotListViewBean listLot = rechercherLots(crtTransaction, HELotViewBean.CS_LOT_PTY_HAUTE, "RMDDEN, RMDHEU");
        System.out.println(listLot.size() + " lots priorités hautes \u00e0 envoy\u00e9(s)");
        for (int i = 0; i < listLot.size(); i++) {
            traiterLot(crtTransaction, fos, hasCarriageReturns, (HELotViewBean) listLot.getEntity(i));
        }
    }

    public String getLotFooter() throws Exception {
        StringBuffer footer = new StringBuffer("9901");
        footer.append(crtSession.getApplication().getProperty("noCaisse"));
        footer.append("000".equals(crtSession.getApplication().getProperty("noAgence")) ? "" : crtSession
                .getApplication().getProperty("noAgence"));
        // footer += "026001";
        footer = new StringBuffer(globaz.hermes.utils.StringUtils.padAfterString(footer.toString(), " ", 24));
        footer.append("T0");
        footer.append(globaz.hermes.utils.DateUtils.convertDate(globaz.hermes.utils.DateUtils.getCurrentDateAMJ(),
                globaz.hermes.utils.DateUtils.AAAAMMJJ, globaz.hermes.utils.DateUtils.JJMMAA));
        footer.append(globaz.hermes.utils.StringUtils.padBeforeString(nbRecords + "", "0", 6));
        // footer += globaz.hermes.utils.StringUtils.padBeforeString("13" + "",
        // "0", 6);
        if ("true".equals(crtSession.getApplication().getProperty("ftp.test"))) {
            footer.append("TEST");
        } else {
            footer.append("    ");
        }
        footer = new StringBuffer(globaz.hermes.utils.StringUtils.padAfterString(footer.toString(), " ", 120));
        return footer.toString();
    }

    public String getLotHeader() throws Exception {
        StringBuffer header = new StringBuffer("0101");
        header.append(crtSession.getApplication().getProperty("noCaisse"));
        header.append("000".equals(crtSession.getApplication().getProperty("noAgence")) ? "" : crtSession
                .getApplication().getProperty("noAgence"));
        header = new StringBuffer(globaz.hermes.utils.StringUtils.padAfterString(header.toString(), " ", 24));
        header.append("T0");
        header.append(globaz.hermes.utils.DateUtils.convertDate(globaz.hermes.utils.DateUtils.getCurrentDateAMJ(),
                globaz.hermes.utils.DateUtils.AAAAMMJJ, globaz.hermes.utils.DateUtils.JJMMAA));
        header = new StringBuffer(globaz.hermes.utils.StringUtils.padAfterString(header.toString(), " ", 38));
        if ("true".equals(crtSession.getApplication().getProperty("ftp.test"))) {
            header.append("TEST");
        } else {
            header.append("    ");
        }
        header = new StringBuffer(globaz.hermes.utils.StringUtils.padAfterString(header.toString(), " ", 120));
        return header.toString();
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return crtSession;
    }

    private HELotListViewBean rechercherLots(BTransaction crtTransaction, String priorite, String ordre)
            throws Exception {
        HELotListViewBean listLot = new HELotListViewBean();
        listLot.setSession(crtSession);
        listLot.setForType(HELotViewBean.CS_TYPE_ENVOI);
        listLot.setForQuittance("0");
        listLot.wantCallMethodBefore(false);
        listLot.wantCallMethodBeforeFind(false);
        listLot.wantCallMethodAfter(false);
        listLot.wantCallMethodAfterFind(false);
        listLot.setForPriorite(priorite);
        listLot.setOrder(ordre);
        listLot.find(crtTransaction, BManager.SIZE_NOLIMIT);
        return listLot;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session) {
        crtSession = session;
    }

    private void traiterLot(BTransaction crtTransaction, BufferedWriter fos, boolean hasCarriageReturns,
            HELotViewBean lot) throws Exception, IOException {
        vecLots.add(lot.getIdLot());
        HEOutputAnnonceListViewBean outList = new HEOutputAnnonceListViewBean();
        outList.setSession(crtSession);
        outList.setForIdLot(lot.getIdLot());
        outList.wantCallMethodAfter(false);
        outList.wantCallMethodAfterFind(false);
        outList.wantCallMethodBefore(false);
        outList.wantCallMethodBeforeFind(false);
        outList.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");
        outList.find(crtTransaction, BManager.SIZE_NOLIMIT);
        System.out.println(DateUtils.getTimeStamp() + outList.size() + " annonces \u00e0 envoy\u00e9(s) dans ce lot");
        for (int j = 0; j < outList.size(); j++) {
            HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) outList.getEntity(j);
            System.out.println(annonce.getChampEnregistrement());
            fos.write(StringUtils.formatEnregistrement(StringUtils.padAfterString(annonce.getChampEnregistrement(),
                    " ", 120)));
            if (hasCarriageReturns) {
                fos.write("\n");
            }
            nbRecords++;
        }
    }
}
