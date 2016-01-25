package globaz.hermes.test;

import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean;
import globaz.hermes.process.HEExtraitAnnonceProcess;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HECompareAnnonce;
import globaz.hermes.utils.StringUtils;
import globaz.pavo.application.CIApplication;
import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.TreeSet;

// import globaz.pavo.application.CIApplication;
/**
 * Insérez la description du type ici. Date de création : (07.05.2003 10:49:06)
 * 
 * @author: ado
 */
public class HETest {
    static boolean b;
    private static boolean wantSession = true;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(String[] args) {
        HETest test = null;
        try {
            test = new HETest(HETest.wantSession);
            // test.go(1);
            // test.go(2);
            // test.go(3);
            // test.go(4);
            // test.go(3);
            // test.go(22);
            // test.go(23);
            // test.go(24);
            // test.go(25);
            // test.go(26);
            // test.go(27);
            // test.go(30);
            // test.go(31);
            // test.go(32);
            // test.go(33);
            // test.go(38);
            test.go(37);
            if (HETest.wantSession) {
                if (test.getTransaction().hasErrors()) {
                    System.out.println(test.getTransaction().getErrors().toString());
                } else {
                    test.getTransaction().commit();
                    System.out.println("OK");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (test.getTransaction() != null) {
                try {
                    test.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);
    }

    private String password = "ssiiadm";
    private BSession session;
    private BTransaction transaction = null;

    private String username = "globazf";

    /**
     * Commentaire relatif au constructeur HETest.
     */
    public HETest() {
        try {
            session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect(username, password);
            //
            transaction = new BTransaction(session);
            transaction.openTransaction();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            try {
                transaction.closeTransaction();
            } catch (Exception e2) {
                e2.printStackTrace(System.err);
            }
            System.exit(0);
        }
    }

    /**
     * Commentaire relatif au constructeur HETest.
     */
    public HETest(boolean loadSession) {
        if (loadSession) {
            try {
                session = new BSession("HERMES");
                session.setIdLangueISO("FR");
                session.connect(username, password);
                //
                transaction = new BTransaction(session);
                transaction.openTransaction();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                try {
                    transaction.closeTransaction();
                } catch (Exception e2) {
                    e2.printStackTrace(System.err);
                }
                System.exit(0);
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:30:33)
     * 
     * @return globaz.globall.db.BSession
     */
    public globaz.globall.db.BSession getSession() {
        return session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:31:58)
     * 
     * @return globaz.globall.db.BTransaction
     */
    public globaz.globall.db.BTransaction getTransaction() {
        return transaction;
    }

    public void go(int methodNumber) throws Exception {
        Class thisClass = this.getClass();
        java.lang.reflect.Method[] methods = thisClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("go" + methodNumber)) {
                System.out.println("**************************");
                System.out.println("Executing " + methods[i].getName());
                System.out.println("**************************");
                methods[i].invoke(this, new Object[0]);
            }
        }
    }

    public void go1() throws Exception {
        String numS = "28977443";
        String numAVS = "";
        for (int i = 0; i < 999; i++) {
            numAVS = numS + StringUtils.padBeforeString("" + i, "0", 3);
            try {
                JAUtil.checkAvs(numAVS);
                System.out.println(numAVS);
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println(numAVS);
            }
        }
    }

    public void go10() throws Exception {
        // requêtes
        String r1;
        r1 = "SELECT * FROM (SELECT R3.*, SUBSTR(R3.RNLENR,CAST(R3.RDNDEB AS INTEGER),CAST(R3.RDNLON AS INTEGER)) AS NUMAVS FROM (SELECT DISTINCT R2.ROTATT AS RETOUR, AJPPCOU2.PCOUID AS IDRETOUR,AJPPCOU2.PCOLUT AS LIBRETOUR, NOVAF.HEPAREP.*, NOVAF.HECHANP.*, R2.* FROM NOVAF.HEPAREP AS HEPAREP2, NOVAF.AJPPCOU AS AJPPCOU2, NOVAF.HECHANP INNER JOIN ((SELECT NOVAF.AJPPCOS.PPTYGR, NOVAF.AJPPCOU.PCOUID, NOVAF.AJPPCOU.PCOLUT, R1.*, NOVAF.AJPPCOS.PCOSID  FROM (SELECT NOVAF.HEAREAP.*, NOVAF.HEANNOP.*, Left(NOVAF.HEANNOP.RNLENR,2) AS CODEAPPLICATION,CAST(SUBSTR(NOVAF.HEANNOP.RNLENR,3,2) AS INTEGER) AS CODEENR FROM NOVAF.HEAREAP,NOVAF.HEANNOP WHERE NOVAF.HEAREAP.RNIANN=NOVAF.HEANNOP.RNIANN) AS R1,NOVAF.AJPPCOS,NOVAF.AJPPCOU WHERE NOVAF.AJPPCOS.PPTYGR='HECODAPP' AND NOVAF.AJPPCOU.PCOSID=NOVAF.AJPPCOS.PCOSID AND R1.CODEAPPLICATION=NOVAF.AJPPCOU.PCOUID AND NOVAF.AJPPCOU.PLAIDE='F') AS R2 INNER JOIN NOVAF.HEPAREP ON R2.PCOSID=NOVAF.HEPAREP.RETLIB) ON NOVAF.HECHANP.REIPAE=NOVAF.HEPAREP.REIPAE  WHERE NOVAF.HECHANP.RDTCHA=118007 AND AJPPCOU2.PLAIDE='F' AND (R2.ROTATT=HEPAREP2.REIPAE AND HEPAREP2.RETLIB=AJPPCOU2.PCOSID) AND NOVAF.HEPAREP.RENCED=CODEENR) AS R3 ) AS MAINQUERY  WHERE NUMAVS like '39874269119%' AND SUBSTR(RNLENR,100,2)='95' AND UCASE(RNLUTI)='USERFR' AND ROLRUN='3899' ORDER BY NUMAVS,ROLRUN  FOR FETCH ONLY OPTIMIZE FOR 100 ROWS ";
        // nom du driver JDBC
        // driver JDBC
        // URL vers la source de données
        // String url = "jdbc:as400://ASGLOB1";
        String url = "jdbc:as400://ASGLOB1;data truncation=false;prefetch=false;cursor hold=false";
        // ;sort=language"
        Connection con = DriverManager.getConnection(url, "ssii", "ssiiadm");
        Statement s = con.createStatement();
        ResultSet rSet;
        ResultSetMetaData metaData;
        System.out.println("EXECUTING R1");
        rSet = s.executeQuery(r1);
        metaData = rSet.getMetaData();
        while (rSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(metaData.getColumnName(i + 1) + "=" + rSet.getObject(metaData.getColumnName(i + 1)));
            }
            System.out.println("*********************************");
        }
    }

    // retrouve un tiers dans pyxis
    public void go11() throws Exception {
    }

    // lier caisse aux annonces atetndues
    public void go12() throws Exception {
        HEOutputAnnonceListViewBean annonces = new HEOutputAnnonceListViewBean(getSession());
        annonces.setLikeEnregistrement("25");
        annonces.setForRefUnique("003893");
        annonces.find(getTransaction());
        for (int i = 0; i < annonces.size(); i++) {
            HEOutputAnnonceViewBean annonce = (HEOutputAnnonceViewBean) annonces.getEntity(i);
            if (!annonce.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {
                System.out.println(annonce.getChampsTable());
                System.out.println(annonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_2) + "-"
                        + annonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_2));
            }
        }
    }

    // lier caisse aux annonces atetndues
    public void go13() throws Exception {
    }

    // lier caisse aux annonces atetndues
    public void go14() throws Exception {
        HEOutputAnnonceViewBean annonce = new HEOutputAnnonceViewBean(getSession());
        annonce.setIdAnnonce("4201");
        annonce.retrieve(getTransaction());
        System.out.println(annonce.getChampEnregistrement());
        System.out.println("-" + annonce.getField(annonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)));
    }

    // retrouve un tiers dans pyxis
    public void go18() throws Exception {
    }

    public void go19() throws Exception {
        System.out.println(StringUtils.unPad("1"));
        System.out.println(StringUtils.unPad("01"));
        System.out.println(StringUtils.unPad("001"));
    }

    public void go20() throws Exception {
        HEInputAnnonceViewBean annonce = new HEInputAnnonceViewBean();
        annonce.setSession(session);
        annonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
        annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "1");
        annonce.put(IHEAnnoncesViewBean.CODE_1_OU_2, "1");
        annonce.add(getTransaction());
        System.out.println(annonce.getChampEnregistrement());
        annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "2");
        annonce.add(getTransaction());
        System.out.println(annonce.getChampEnregistrement());
    }

    public void go21() throws Exception {
        int i = -2;
        System.out.println(i);
        i = -i;
        System.out.println(i);
    }

    public void go22() throws Exception {
        FWParametersSystemCodeManager pays = new FWParametersSystemCodeManager();
        pays.setSession(session);
        pays.setForIdGroupe("CIPAYORI");
        pays.setForActif(Boolean.TRUE);
        pays.find(BManager.SIZE_NOLIMIT);
        System.out.println(pays.size() + " pays trouvés");
        for (int i = 0; i < pays.size(); i++) {
            FWParametersSystemCode paysCode = (FWParametersSystemCode) pays.getEntity(i);
            System.out.println(paysCode.getCurrentCodeUtilisateur().getCodeUtiLib());
        }
    }

    public void go23() throws Exception {
        // HEAnnoncesOrphelinesListViewBean liste = new
        // HEAnnoncesOrphelinesListViewBean();
        HEOutputAnnonceListViewBean liste = new HEOutputAnnonceListViewBean();
        liste.setSession(getSession());
        liste.setForRefUnique("1236090");
        liste.find(transaction);
        System.out.println(liste.getSize());
    }

    public void go24() {
        String st[] = { "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://ASGLOB1", "FILE", "c:/tablesSecu.txt" };
        BTCreateSQLTableCopyScript.main(st);
    }

    public void go25() {
        try {
            System.out.println(((HEApplication) getSession().getApplication()).getCsStatutListe(getSession())
                    .getCodeSysteme("117003").getCurrentCodeUtilisateur().getLibelle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void go26() throws Exception {
        // HEInputAnnonceViewBean input = new HEInputAnnonceViewBean();
        // HEOutputAnnonceViewBean output = new HEOutputAnnonceViewBean();
        // /////////////
        // input.setSession(getSession());
        // input.put(input.CODE_APPLICATION, "11");
        // input.put(input.CODE_ENREGISTREMENT, "01");
        // input.put(input.NUMERO_ASSURE, "12939175111");
        // input.put(input.MOTIF_ANNONCE, "71");
        // //input.add(getTransaction());
        // /////////////
        // output.setSession(getSession());
        // output.setIdAnnonce("6");
        // /*
        // output.retrieve(getTransaction());
        // System.out.println(output.getChampEnregistrement());
        // System.out.println(output.getField(output.ETAT_NOMINATIF));
        // System.out.println(output.getField(output.NUMERO_ASSURE));
        // */
        // //
        // // HEOutputAnnonceListViewBean liste = new
        // HEOutputAnnonceListViewBean(getSession());
        // // liste.setForCodeApplication("25");
        // // liste.setForRefUnique("2");
        // // liste.find(getTransaction());
        // // for (int i = 0; i < liste.size(); i++) {
        // // HEOutputAnnonceViewBean entity = (HEOutputAnnonceViewBean)
        // liste.getEntity(i);
        // // System.out.println(entity.getField(entity.CODE_APPLICATION) + " "
        // + entity.getField(entity.CODE_ENREGISTREMENT));
        // // }
        // HEOutputAnnonceListViewBean liste = new
        // HEOutputAnnonceListViewBean(getSession());
        // liste.setForRefUnique("2");
        // liste.find(getTransaction());
        // for (int i = 0; i < liste.size(); i++) {
        // HEOutputAnnonceViewBean entity = (HEOutputAnnonceViewBean)
        // liste.getEntity(i);
        // System.out.println(entity.getField(entity.CODE_APPLICATION) + " " +
        // entity.getField(entity.CODE_ENREGISTREMENT));
        // }
        // 64869870917,65
        HEInputAnnonceViewBean in = new HEInputAnnonceViewBean(getSession());
        in.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
        in.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        in.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "65");
        in.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "64869870917");
        in.add(getTransaction());
        // 64869870925,67
        in = new HEInputAnnonceViewBean(getSession());
        in.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
        in.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        in.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "67");
        in.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "64869870925");
        in.add(getTransaction());
        // for (int i = 0; i < tableau.length; i++) {
        // input.put(in.REFERENCE_INTERNE_CAISSE,StringUtils.padBeforeString(""+i,"0",6));
        // }
    }

    public void go27() throws Exception {
        HEInputAnnonceViewBean in = new HEInputAnnonceViewBean(getSession());
        in.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
        in.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        in.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "95");
        in.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "64869870917");
        in.add(getTransaction());
        in.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
        in.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "05");
        in.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "95");
        in.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "64869870917");
        in.add(getTransaction());
    }

    public void go28() throws Exception {
        HEInputAnnonceViewBean vBean = new HEInputAnnonceViewBean();
        vBean.setSession(getSession());
        vBean.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
        vBean.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "001");
        vBean.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "77");
        vBean.put(IHEAnnoncesViewBean.CODE_1_OU_2, "1");
        vBean.add(transaction);
    }

    public void go29() throws Exception {
        HEAttenteEnvoiListViewBean liste = new HEAttenteEnvoiListViewBean();
        liste.setSession(getSession());
        liste.find(getTransaction());
        System.out.println(liste.size());
        for (int i = 0; i < liste.size(); i++) {
            System.out.println(liste.getEntity(i));
        }
    }

    public void go30() {
        int yearDifference = 11;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - yearDifference);
        System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
        for (int i = 0; i < 10; i++) {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
            System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
        }
    }

    public void go31() throws Exception {
        // HEExtraitAnnonceProcess papier =
        // new HEExtraitAnnonceProcess(
        // getSession(),
        // CIApplication.APPLICATION_PAVO_REP,
        // "ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
        // papier.setDocumentTitle(
        // "ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
        // papier.setTransaction(transaction);
        // papier.setSendMailOnError(true);
        // papier.setDeleteOnExit(false);
        // papier.setEMailAddress(
        // ((HEApplication) getSession().getApplication()).getProperty(
        // "zas.user.email"));
        // if (getSession().getIdLangue().equals("FR")) {
        // papier.setEmailSubjectError("L'impression du RCI a échouée");
        // papier.setEmailSubjectOK(
        // "L'impression du RCI ("
        // + papier.getDocumentTitle().substring(
        // 0,
        // papier.getDocumentTitle().lastIndexOf("_"))
        // + ") a réussie.");
        // } else {
        // papier.setEmailSubjectError("ERROR : IK-Zusammenrufe drucken");
        // papier.setEmailSubjectOK(
        // "OK : IK-Zusammenrufe drucken ("
        // + papier.getDocumentTitle().substring(
        // 0,
        // papier.getDocumentTitle().lastIndexOf("_"))
        // + ")");
        // }
        // papier.setReferenceUnique("1584");
        // papier.execute();
    }

    public void go32() throws Exception {
        HEOutputAnnonceViewBean annonce = new HEOutputAnnonceViewBean(getSession());
        annonce.setIdAnnonce("11818"); // 83225
        annonce.retrieve(transaction);
        System.out.println(annonce);
        System.out.println(annonce);
    }

    public void go33() {
        try {
            BIApplication arcApp = GlobazSystem.getApplication("HERMES");
            BISession remoteSession = arcApp.newSession(getSession());
            getSession().connectSession(remoteSession);
            BITransaction remoteTransaction = ((BSession) remoteSession).newTransaction();
            IHEInputAnnonce remoteEcritureAnnonce = (IHEInputAnnonce) remoteTransaction.getISession().getAPIFor(
                    IHEInputAnnonce.class);
            // attributs standard ARC
            remoteEcritureAnnonce.setIdProgramme("TEST");
            remoteEcritureAnnonce.setUtilisateur(remoteTransaction.getISession().getUserId());
            remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
            remoteEcritureAnnonce.put(
                    IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                    "PAVO//"
                            + DateUtils.convertDate(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ,
                                    DateUtils.JJMM_DOTS));
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "8A");
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE, "026");
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE, "001");
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "75");
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "21172774110");
            remoteEcritureAnnonce.add(remoteTransaction);
            if (remoteSession.hasErrors()) {
                System.err.println(remoteSession.getErrors());
            }
            if (remoteTransaction.hasErrors()) {
                System.err.println(remoteTransaction.getErrors());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void go34() throws Exception {
    // HEHolidays holidays =
    // new HEHolidays(
    // ClassLoader.getSystemClassLoader().getResourceAsStream(
    // "holidays.xml"));
    // System.out.println(holidays.contains(new JADate(18, 5, 2004)));
    //
    // }
    // public void go36() throws Exception{
    // HEEnvoiEmailsGroupe test = new HEEnvoiEmailsGroupe(session);
    // }
    public void go37() throws Exception {
        // // test d'impression pour lettre d'accompagnement au CA
        // /* Génération lettres pour les CA et les CI */
        //
        // ///
        //
        // HEImpressionCA impressionCA = new HEImpressionCA();
        // impressionCA.setSession(getSession());
        // impressionCA.setTransaction(getTransaction());
        // Vector vLot = new Vector();
        // HELotViewBean lot = new HELotViewBean();
        // lot.setIdLot("380");
        // lot.setSession(getSession());
        // lot.retrieve(getTransaction());
        // vLot.add(lot);
        //
        // HELotViewBean lot2 = new HELotViewBean();
        // lot2.setIdLot("381");
        // lot2.setSession(getSession());
        // lot2.retrieve(getTransaction());
        // vLot.add(lot2);
        //
        // impressionCA.setEmailDestinataire("ado@globaz.ch");
        // // impressionCA.doBusiness(vLot);

    }

    public void go38() throws Exception {
        // test d'impression pour lettre d'accompagnement au CA
        /* Génération lettres pour les CA et les CI */

        // /
        HEExtraitAnnonceProcess papier = new HEExtraitAnnonceProcess(getSession(), CIApplication.APPLICATION_PAVO_REP,
                "ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
        papier.setDocumentTitle("ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
        papier.setTransaction(getTransaction());
        papier.setSendMailOnError(true);
        papier.setDeleteOnExit(false);
        papier.setEMailAddress("ado@globaz.ch");

        if (getSession().getIdLangue().equals("FR")) {
            papier.setEmailSubjectError("L'impression du RCI a échouée");
            papier.setEmailSubjectOK("L'impression du RCI ("
                    + papier.getDocumentTitle().substring(0, papier.getDocumentTitle().lastIndexOf("_"))
                    + ") a réussie.");
        } else {
            papier.setEmailSubjectError("ERROR : IK-Zusammenrufe drucken");
            papier.setEmailSubjectOK("OK : IK-Zusammenrufe drucken ("
                    + papier.getDocumentTitle().substring(0, papier.getDocumentTitle().lastIndexOf("_")) + ")");
        }

        System.out.print("Extrait trié selon :");
        TreeSet toPrintRefs = new TreeSet(new HECompareAnnonce());
        /*
         * RNREFU 111323 111348 111360 111361 111362
         */
        HEOutputAnnonceViewBean vbToPrint = new HEOutputAnnonceViewBean();
        // vbToPrint.setSession(getSession());
        // vbToPrint.setIdAnnonce("111323");
        // vbToPrint.retrieve(getTransaction());
        // toPrintRefs.add(vbToPrint);
        /** ********** */
        vbToPrint = new HEOutputAnnonceViewBean();
        vbToPrint.setSession(getSession());
        vbToPrint.setIdAnnonce("111348");
        vbToPrint.retrieve(getTransaction());
        toPrintRefs.add(vbToPrint);
        // /*************/
        // vbToPrint = new HEOutputAnnonceViewBean();
        // vbToPrint.setSession(getSession());
        // vbToPrint.setIdAnnonce("111360");
        // vbToPrint.retrieve(getTransaction());
        // toPrintRefs.add(vbToPrint);

        papier.setReferenceUniqueList((TreeSet) toPrintRefs.clone());

        papier.executeProcess();

    }

    public void go4() throws Exception {
        // CICompteIndividuelProcess.main(new String[] { "" });
        HEInputAnnonceViewBean input = new HEInputAnnonceViewBean(session);
        input.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
        input.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "71");
        input.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, "ADO");
        input.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "28977443158");
        input.put(IHEAnnoncesViewBean.DOMICILE_EN_SUISSE_CODE_INFORMATION, "1");
        input.add(transaction);
    }

    public void go5() throws Exception {
        // requêtes
        String r1;
        r1 = "SELECT * FROM CFCP.AJPLCOS2 INNER JOIN CFCP.AJPPCOU ON (CFCP.AJPLCOS2.PCOSID=CFCP.AJPPCOU.PCOSID) WHERE PLAIDE='F' AND PCOITC=11100001 AND PPTYGR='HECODAPP' AND PCOUID='11' ORDER BY PCOUID, PCOSLI FETCH FIRST 100 ROWS ONLY OPTIMIZE FOR 100 ROWS";
        String r2;
        r2 = "SELECT * FROM CFCP.AJPLCOS2 INNER JOIN CFCP.AJPPCOU ON (CFCP.AJPLCOS2.PCOSID=CFCP.AJPPCOU.PCOSID) WHERE PLAIDE='F' AND PCOITC=11100002 AND PPTYGR='HEMOTIFS' AND PCOUID='95' ORDER BY PCOUID, PCOSLI FETCH FIRST 100 ROWS ONLY OPTIMIZE FOR 100 ROWS";
        // nom du driver JDBC
        // driver JDBC
        // URL vers la source de données
        String url = "jdbc:db2://sglobax2:3700/db2oai";
        Connection con = DriverManager.getConnection(url, "db2oai", "db2oai");
        Statement s = con.createStatement();
        System.out.println("EXECUTING R1");
        ResultSet rSet = s.executeQuery(r1);
        ResultSetMetaData metaData = rSet.getMetaData();
        System.out.println("*********************************");
        while (rSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(metaData.getColumnName(i + 1) + "=" + rSet.getObject(metaData.getColumnName(i + 1)));
            }
            System.out.println("*********************************");
        }
        System.out.println("EXECUTING R1");
        rSet = s.executeQuery(r1);
        metaData = rSet.getMetaData();
        System.out.println("*********************************");
        while (rSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(metaData.getColumnName(i + 1) + "=" + rSet.getObject(metaData.getColumnName(i + 1)));
            }
            System.out.println("*********************************");
        }
        System.out.println("EXECUTING R2");
        rSet = s.executeQuery(r2);
        metaData = rSet.getMetaData();
        while (rSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(metaData.getColumnName(i + 1) + "=" + rSet.getObject(metaData.getColumnName(i + 1)));
            }
            System.out.println("*********************************");
        }
    }

    public void go6() throws Exception {
        // requêtes
        // String r1 =
        // "SELECT * FROM CFCP.AJPLCOS2 INNER JOIN CFCP.AJPPCOU ON (CFCP.AJPLCOS2.PCOSID=CFCP.AJPPCOU.PCOSID) WHERE PLAIDE='F' AND PCOITC=11100001 AND PPTYGR='HECODAPP' AND PCOUID='11' ORDER BY PCOUID, PCOSLI FETCH FIRST 100 ROWS ONLY OPTIMIZE FOR 100 ROWS";
        String r2 = "SELECT * FROM CFCP.AJPLCOS2 INNER JOIN CFCP.AJPPCOU ON (CFCP.AJPLCOS2.PCOSID=CFCP.AJPPCOU.PCOSID) WHERE PLAIDE='F' AND PCOITC=11100002 AND PPTYGR='HEMOTIFS' AND PCOUID='95' ORDER BY PCOUID, PCOSLI FETCH FIRST 100 ROWS ONLY OPTIMIZE FOR 100 ROWS";
        // nom du driver JDBC
        // driver JDBC
        // URL vers la source de données
        String url = "jdbc:as400://ASGLOB1";
        Connection con = DriverManager.getConnection(url, "ssii", "ssiiadm");
        Statement s = con.createStatement();
        ResultSet rSet;
        ResultSetMetaData metaData;
        /*
         * System.out.println("EXECUTING R1"); rSet = s.executeQuery(r1); metaData = rSet.getMetaData();
         * System.out.println("*********************************"); while (rSet.next()) { for (int i = 0; i <
         * metaData.getColumnCount(); i++) { System.out.println(metaData.getColumnName(i + 1) + "=" +
         * rSet.getObject(metaData.getColumnName(i + 1))); } System.out.println("*********************************"); }
         */
        /*
         * System.out.println("EXECUTING R1"); rSet = s.executeQuery(r1); metaData = rSet.getMetaData();
         * System.out.println("*********************************"); while (rSet.next()) { for (int i = 0; i <
         * metaData.getColumnCount(); i++) { System.out.println(metaData.getColumnName(i + 1) + "=" +
         * rSet.getObject(metaData.getColumnName(i + 1))); } System.out.println("*********************************"); }
         */
        System.out.println("EXECUTING R2");
        rSet = s.executeQuery(r2);
        metaData = rSet.getMetaData();
        while (rSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(metaData.getColumnName(i + 1) + "=" + rSet.getObject(metaData.getColumnName(i + 1)));
            }
            System.out.println("*********************************");
        }
    }

    public void go7() throws Exception {
        int nblines = 0;
        File directory = new File("z:/temp");
        String[] files = directory.list();
        RandomAccessFile raf;
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
            raf = new RandomAccessFile("z:/temp/" + files[i], "r");
            while (raf.readLine() != null) {
                nblines++;
            }
            System.out.println(nblines);
        }
    }

    public void go8() throws Exception {
        // HEExtraitAnnonceProcess papier =
        // new HEExtraitAnnonceProcess(
        // getSession(),
        // CIApplication.APPLICATION_PAVO_REP,
        // "ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
        // papier.setDocumentTitle(
        // "ZIK_" + DateUtils.getDateJJMMAAAA_Dots() + "_");
        // papier.setTransaction(transaction);
        // papier.setSendMailOnError(true);
        // papier.setDeleteOnExit(false);
        // papier.setEMailAddress(
        // ((HEApplication) getSession().getApplication()).getProperty(
        // "zas.user.email"));
        // if (getSession().getIdLangue().equals("FR")) {
        // papier.setEmailSubjectError("L'impression du RCI a échouée");
        // papier.setEmailSubjectOK(
        // "L'impression du RCI ("
        // + papier.getDocumentTitle().substring(
        // 0,
        // papier.getDocumentTitle().lastIndexOf("_"))
        // + ") a réussie.");
        // } else {
        // papier.setEmailSubjectError("ERROR : IK-Zusammenrufe drucken");
        // papier.setEmailSubjectOK(
        // "OK : IK-Zusammenrufe drucken ("
        // + papier.getDocumentTitle().substring(
        // 0,
        // papier.getDocumentTitle().lastIndexOf("_"))
        // + ")");
        // }
        // TreeSet refs = new TreeSet();
        // refs.add("292");
        // refs.add("293");
        // refs.add("294");
        // papier.setReferenceUniqueList(refs);
        // papier.execute();
        // while (!papier.isCompleted());
    }

    public void go9() throws Exception {
        // globaz.pyxis.db.tiers.TIAdministrationManager admin = new
        // globaz.pyxis.db.tiers.TIAdministrationManager();
        // admin.setSession(session);
        // admin.setForGenreAdministration(globaz.pyxis.db.tiers.TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
        // admin.setForCodeAdministration("150");
        // admin.find();
        // System.out.println(admin.size());
        // for (int i = 0; i < admin.size(); i++) {
        // globaz.pyxis.db.tiers.TIAdministrationViewBean tiVBean =
        // (globaz.pyxis.db.tiers.TIAdministrationViewBean) admin.getEntity(i);
        // System.out.println(tiVBean.getAdresseAsString());
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:30:33)
     * 
     * @param newSession
     *            globaz.globall.db.BSession
     */
    public void setSession(globaz.globall.db.BSession newSession) {
        session = newSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:31:58)
     * 
     * @param newTransaction
     *            globaz.globall.db.BTransaction
     */
    public void setTransaction(globaz.globall.db.BTransaction newTransaction) {
        transaction = newTransaction;
    }
}
