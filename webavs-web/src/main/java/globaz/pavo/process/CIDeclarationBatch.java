package globaz.pavo.process;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSDeclarationListeManager;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationHTMLOutput;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationPUCSIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationParametreIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationRecord;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationSummaryHTMLOutput;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationTextIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationsAnciensClientsIterator;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationOutput;
import globaz.pavo.util.CIUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CIDeclarationBatch extends BProcess {

    private static final long serialVersionUID = 7619281974843040844L;
    public static String CS_AC = "327003";
    public static String CS_AMI = "327002";
    public static String CS_CLIENTS_GLOBAZ = "327001";
    public static String CS_DATEN_TRAGER = "327000";
    public static String CS_PUCS = "327004";
    public static String CS_PUCS_II = "327007";

    public static String getNomFormatCI(String nomPrenom) {
        if (JadeStringUtil.isBlank(nomPrenom)) {
            // tel quel
            return nomPrenom;
        }

        nomPrenom = JadeStringUtil.change(nomPrenom, "ä", "AE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ë", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ï", "I");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ö", "OE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ü", "UE");

        nomPrenom = JadeStringUtil.change(nomPrenom, "Ä", "AE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ë", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ï", "I");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ö", "OE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ü", "UE");

        nomPrenom = JadeStringUtil.change(nomPrenom, "é", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "è", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ô", "O");
        nomPrenom = JadeStringUtil.change(nomPrenom, "à", "A");

        nomPrenom = nomPrenom.trim().toUpperCase();

        return nomPrenom;
    }

    public static boolean isAvs0(String avs) {
        if (avs == null) {
            return true;
        }
        try {
            if (Integer.parseInt(avs.trim()) == 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        String uid = "";
        if (args.length > 0) {
            uid = args[0];
        }
        String pwd = "";
        if (args.length > 1) {
            pwd = args[1];
        }
        String email = "";
        if (args.length > 2) {
            email = args[2];
        }

        CIDeclarationBatch process = null;
        try {
            for (int i = 0; i < args.length; i++) {
                System.out.println("Valeur de l'argument : " + i + " " + args[i]);
            }

            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(uid, pwd);
            process = new CIDeclarationBatch((BSession) session);
            process.setEMailAddress(email);
            process.setEchoToConsole(true);
            process.setIsBatch(new Boolean(true));
            String file = args[3];
            if (!JadeStringUtil.isBlank(file)) {
                process.setFilename(file);
            } else {
                process.setFilename("inscriptions.txt");
            }
            process.setAccepteLienDraco("True");
            process.setType(args[4]);
            process.setAccepteEcrituresNegatives("True");
            process.setAccepteAnneeEnCours("false");
            process.setAnneeCotisation(args[5]);
            if (args.length > 5) {
                if ("true".equals(args[6])) {
                    process.setSimulation("simulation");
                }
            }

            JadeJobInfo job = BProcessLauncher.start(process);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Process Cloture Journaux CIs not executed successfully !");
                System.out.println(job.getFatalErrorMessage());
                // System.exit(CODE_RETOUR_ERREUR);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process Cloture Journaux CIsexecuted successfully !");
                // System.exit(CODE_RETOUR_OK);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Process Cloture Journaux CIs has error(s) !");

            // erreur critique, je retourne un code d'erreur 200
            // System.exit(CODE_RETOUR_ERREUR);
        } finally {

        }
        System.exit(0);
    }

    private String accepteAnneeEnCours = "";
    private String accepteEcrituresNegatives = "";
    private String accepteLienDraco = "";
    private String anneeCotisation = "";
    private String filename = "";

    private String forNumeroAffilie = "";
    private boolean hasChild = false;
    private Boolean isBatch = new Boolean(false);
    private boolean isErrorMontant = false;
    private boolean isErrorNbInscriptions = false;
    private String nombreInscriptions = "";

    private String simulation = "";

    private String totalControle = "";

    private String Type = "";

    /**
     * Commentaire relatif au constructeur CIDeclaration.
     */
    public CIDeclarationBatch() {
        super();
    }

    public CIDeclarationBatch(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CIDeclaration.
     */
    public CIDeclarationBatch(globaz.globall.db.BSession session) {
        super(session);
    }

    private void _doLogJournauxExistant(TreeMap<String, ?> hJournalExisteDeja) {
        Set<String> journalSet = hJournalExisteDeja.keySet();
        Iterator<String> it = journalSet.iterator();
        while (it.hasNext()) {
            String str = it.next();
            getMemoryLog().logMessage((String) hJournalExisteDeja.get(str), FWMessage.ERREUR, "Déclaration");
        }
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // start
        // TreeMap pour statistique (par affilié/année) :
        // JadeJdbcProfiler.setVerbose(true);
        // JadeJdbcProfiler.startProfiler();
        if (!isBatch.booleanValue()) {
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFilename(), Jade
                    .getInstance().getHomeDir() + "work/" + getFilename());
        }

        if (CIDeclarationBatch.CS_AC.equals(getType()) || CIDeclarationBatch.CS_AMI.equals(getType())) {
            CIDeclarationCentrale process = null;

            try {

                process = new CIDeclarationCentrale(getSession());
                process.setType(getType());
                process.setEMailAddress(getEMailAddress());
                process.setEchoToConsole(false);
                process.setSimulation(simulation);
                process.setAccepteEcrituresNegatives(getAccepteEcrituresNegatives());
                process.setTotalControle(getTotalControle());
                process.setNombreInscriptions(getNombreInscriptions());
                process.setParent(this);
                if (!JadeStringUtil.isBlank(getFilename()) && !isBatch.booleanValue()) {
                    process.setFilename(Jade.getInstance().getHomeDir() + "work/" + getFilename());
                } else {
                    process.setFilename(getFilename());
                }
                process.executeProcess();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                abort();
                return false;
            }
        } else {
            TreeMap<String, Object> hNbrInscriptionsTraites = new TreeMap<String, Object>();
            TreeMap<String, Object> hMontantInscritionsTraites = new TreeMap<String, Object>();
            TreeMap<String, Object> hNbrInscriptionsErreur = new TreeMap<String, Object>();
            TreeMap<String, Object> hMontantInscriptionsErreur = new TreeMap<String, Object>();
            TreeMap<String, Object> hNbrInscriptionsSuspens = new TreeMap<String, Object>();
            TreeMap<String, Object> hMontantInscriptionsSuspens = new TreeMap<String, Object>();
            TreeMap<String, Object> hNbrInscriptionsCI = new TreeMap<String, Object>();
            TreeMap<String, Object> hMontantInscriptionsCI = new TreeMap<String, Object>();
            TreeMap<String, Object> totauxJournaux = null;
            TreeMap<String, Object> nbInsc = null;
            TreeMap<String, Object> hNbrInscriptionsNegatives = new TreeMap<String, Object>();
            TreeMap<String, Object> hMontantInscriptionsNegatives = new TreeMap<String, Object>();
            TreeMap<String, Object> hNbrInscriptionsTotalControle = new TreeMap<String, Object>();
            TreeMap<String, Object> hMontantTotalControle = new TreeMap<String, Object>();
            // Cette table contient les journaux déjà crée
            // si le journal existait déjà avant le traitement, la clé est quand
            // même mise dans cette table et la valeur sera null.
            TreeMap<String, Object> tableJournaux = new TreeMap<String, Object>();
            TreeMap<String, Object> hJournalExisteDeja = new TreeMap<String, Object>();
            // table pour stocké les erreurs/info au niveau des assuré (detail)
            TreeMap<String, CIDeclarationRecord> tableLogAssure = new TreeMap<String, CIDeclarationRecord>();
            boolean modeInscription = JadeStringUtil.isBlank(getSimulation());
            boolean result = true;
            int line = 0;
            if (!modeInscription) {
                getMemoryLog().logMessage(getSession().getLabel("DT_MODE_SIMULATION"), FWMessage.INFORMATION,
                        "Déclaration");
            }
            try {
                String forNumeroAffilieSansPoint = CIUtil.unFormatAVS(getForNumeroAffilie());
                ICIDeclarationIterator itDec = null;
                if (CIDeclarationBatch.CS_PUCS.equals(Type)) {
                    itDec = new CIDeclarationPUCSIterator();
                    itDec.setTypeImport(Type);
                } else if (CIDeclarationBatch.CS_CLIENTS_GLOBAZ.equals(Type)) {
                    itDec = new CIDeclarationsAnciensClientsIterator();
                } else if (CIDeclarationBatch.CS_DATEN_TRAGER.equals(Type)) {
                    itDec = new CIDeclarationTextIterator();
                } else if (CIDeclarationBatch.CS_PUCS_II.equals(Type)) {
                    itDec = new CIDeclarationPUCSIterator();
                    itDec.setTypeImport(Type);
                } else {
                    itDec = new CIDeclarationParametreIterator();
                    itDec.setTypeImport(Type);
                }
                if (!isBatch.booleanValue()) {
                    itDec.setFilename(Jade.getInstance().getHomeDir() + "work/" + getFilename());
                } else {
                    itDec.setFilename(getFilename());
                }
                itDec.setTransaction(getTransaction());
                int nbRecords = itDec.size();
                setState("");
                setProgressScaleValue(nbRecords);
                while (itDec.hasNext()) {
                    line++;
                    setProgressCounter(line);
                    // System.out.println(line+"/"+nbRecords+" "+(line*100)/nbRecords+"%");
                    CIDeclarationRecord rec = itDec.next();
                    // si l'année de cotisation n'est pas précisé, ou quelle
                    // correspond a celle de ce record
                    if ((JadeStringUtil.isBlank(getAnneeCotisation())) || (rec.getAnnee().equals(getAnneeCotisation()))) {
                        // si le n°d'affilie n'est pas précisé, ou qu'il
                        // correspond a celui de ce record
                        if ((JadeStringUtil.isBlank(forNumeroAffilieSansPoint))
                                || (rec.getNumeroAffilie().equals(forNumeroAffilieSansPoint))) {
                            // initialisations
                            CIEcriture ecriture = new CIEcriture();
                            ecriture.setSession(getSession());
                            ArrayList<String> errors = new ArrayList<String>();
                            ArrayList<String> info = new ArrayList<String>();
                            ArrayList<String> ciAdd = new ArrayList<String>();
                            String numeroAffilie = rec.getNumeroAffilie();
                            String key = _getKey(rec);
                            // pour stockage intermédiaire
                            long nbrInscriptionsTraites = 0;
                            FWCurrency montantInscritionsTraites = new FWCurrency();
                            long nbrInscriptionsErreur = 0;
                            FWCurrency montantInscriptionsErreur = new FWCurrency();
                            long nbrInscriptionsSuspens = 0;
                            FWCurrency montantInscriptionsSuspens = new FWCurrency();
                            long nbrInscriptionsCI = 0;
                            FWCurrency montantInscriptionsCI = new FWCurrency();
                            long nbrInscriptionsNegatives = 0;
                            FWCurrency montantInscriptionsNegatives = new FWCurrency();
                            long nbrInscriptionsTotalControle = 0;
                            FWCurrency montantTotalControle = new FWCurrency();
                            // trouve le journal à utiliser pour ce record.
                            // il y a un journal par année/affilié.
                            // si le journal n'existe pas on le crée et on le
                            // garde dans une table car
                            // il peut être utilisé par plusieur ligne du
                            // fichier.
                            // si le journal existe préalablement au taitement
                            // du fichier, on génère une erreur.
                            // findJournal retourne le journal à utiliser, ou
                            // null si le jounal à utilisé existait déjà avant
                            // ce traitement,
                            // ce qui n'est pas authorisé.
                            CIJournal journal = _findJournal(modeInscription, rec, tableJournaux);
                            if (journal == null) {
                                // Erreur, ce journal existe déjà avant ce
                                // traitement
                                hJournalExisteDeja.put(key,
                                        getSession().getLabel("DT_JOURNAL_EXISTANT") + " " + rec.getNumeroAffilie()
                                                + "/" + rec.getAnnee());
                            } else {
                                // journal trouvé
                                ecriture.setIdJournal(journal.getIdJournal());
                                ecriture.setAnnee(rec.getAnnee());
                                boolean breakTests = false;
                                // boolean supended = false;
                                // Plausi période
                                // if(!modeInscription) {
                                try {

                                    int jourDebut = rec.getJourDebut();
                                    if (jourDebut < 0 || jourDebut > 31) {
                                        errors.add(getSession().getLabel("ERREUR_DATE_DEBUT"));
                                    } else {
                                        ecriture.setJourDebut("" + rec.getJourDebut());
                                    }

                                    int jourFin = rec.getJourFin();
                                    if (jourFin < 0 || jourFin > 31) {
                                        errors.add(getSession().getLabel("ERREUR_DATE_FIN"));
                                    } else {
                                        ecriture.setJourFin("" + rec.getJourFin());
                                    }

                                    int moisDebut = rec.getMoisDebut();
                                    if (((moisDebut < 1) || (moisDebut > 12)) && (99 != moisDebut) && (66 != moisDebut)) {
                                        errors.add(getSession().getLabel("DT_MOIS_DEBUT_INVALIDE"));
                                    } else {
                                        ecriture.setMoisDebut("" + rec.getMoisDebut());
                                    }
                                    int moisFin = rec.getMoisFin();
                                    if ((moisFin < 1) || ((moisFin > 12) && (99 != moisFin) && (66 != moisFin))) {
                                        errors.add(getSession().getLabel("DT_MOIS_FIN_INVALIDE"));
                                        // breakTests = true;
                                    } else {

                                        ecriture.setMoisFin("" + rec.getMoisFin());
                                    }
                                    if (moisDebut > moisFin) {
                                        errors.add(getSession().getLabel("DT_MOIS_DEBUT_PLUS_GRAND"));
                                        // breakTests = true;
                                    }
                                    if ((99 == moisDebut) && (99 == moisFin)) {
                                        if (!ecriture.getWrapperUtil().rechercheEcritureSemblablesDt(getTransaction(),
                                                CIUtil.formatNumeroAffilie(getSession(), numeroAffilie),
                                                rec.getNumeroAvs())) {
                                            errors.add(getSession().getLabel("MSG_ECRITURE_99"));
                                        }
                                    }

                                    // année en cours et futre sont interdites
                                    int annee = JACalendar.today().getYear();
                                    if (!"True".equals(accepteAnneeEnCours)) {
                                        if (Integer.parseInt(rec.getAnnee()) >= annee) {
                                            errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));
                                        }
                                    } else {
                                        if (Integer.parseInt(rec.getAnnee()) > annee) {
                                            errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));
                                        }
                                    }
                                } catch (Exception ex) {
                                    errors.add(getSession().getLabel("DT_MOIS_INVALIDE"));
                                }
                                // }
                                // Plausi montant
                                boolean montantPositif = rec.isMontantPositif();
                                String montantEcr = rec.getMontantEcr();
                                if (montantPositif) {
                                    try {
                                        FWCurrency cur = new FWCurrency(montantEcr);
                                        if (cur.compareTo(new FWCurrency("1")) == -1) {
                                            errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                                            ecriture.setMontant(montantEcr);
                                        } else {
                                            ecriture.setGre("01");
                                            ecriture.setMontant(cur.toStringFormat());
                                        }
                                    } catch (Exception inex) {
                                        errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
                                        montantEcr = "        0.00";
                                    }
                                } else {
                                    nbrInscriptionsNegatives++;
                                    montantInscriptionsNegatives.sub(montantEcr);
                                    if (!"True".equals(getAccepteEcrituresNegatives())) {
                                        FWCurrency cur = new FWCurrency(montantEcr);
                                        errors.add(getSession().getLabel("DT_ECRITURE_NEGATIVE"));
                                        ecriture.setGre("11");
                                        ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                                        ecriture.setMontant(cur.toStringFormat());

                                    } else {
                                        try {
                                            FWCurrency cur = new FWCurrency(montantEcr);
                                            if (cur.compareTo(new FWCurrency("1")) == -1) {
                                                errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                                            } else {
                                                ecriture.setGre("11");
                                                ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                                                ecriture.setMontant(cur.toStringFormat());
                                            }
                                        } catch (Exception inex) {
                                            errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
                                            montantEcr = "        0.00";
                                        }
                                    }
                                }
                                // Période affiliation
                                if (!_isInPeriodeAffiliation(rec)) {
                                    // Les dates ne correspondent pas avec la
                                    // période d'affiliation
                                    errors.add(getSession().getLabel("DT_ERR_DATE_AFFILIATION"));
                                }
                                // Plausi no avs
                                String noAvs = rec.getNumeroAvs().trim();
                                // Modif v4.12 => dans pucs, le no peut être
                                // vide, pour avoir un identiant, on set le no
                                if (JadeStringUtil.isBlank(noAvs)) {
                                    noAvs = "00000000000";
                                }
                                if (noAvs.endsWith("000")) {
                                    noAvs = rec.getNumeroAvs().substring(0, rec.getNumeroAvs().lastIndexOf("000")); // A
                                    // controler
                                }
                                // pour les cas ersam catherine
                                if (noAvs.trim().startsWith("000")) {
                                    if (!JadeStringUtil.isBlank(rec.getNomPrenom().trim())) {
                                        ecriture.setAvs("");
                                    } else {
                                        ecriture.setAvs(noAvs);
                                    }
                                } else {
                                    ecriture.setAvs(noAvs);
                                }
                                if (CIUtil.isNNSSlengthOrNegate(noAvs)) {
                                    ecriture.setNumeroavsNNSS("true");
                                    ecriture.setAvsNNSS("true");
                                }
                                ecriture.setNomPrenom(CIDeclarationBatch.getNomFormatCI(rec.getNomPrenom()));

                                int anneeNaissance = 60;
                                if (rec.getNumeroAvs().trim().length() < 13) {
                                    anneeNaissance = Integer.parseInt(rec.getNumeroAvs().substring(3, 5));
                                } else {
                                    CICompteIndividuel ci = ecriture.getForcedCi(getTransaction());
                                    if (ci != null) {
                                        JADate dateNaiss = new JADate(ci.getDateNaissance());
                                        anneeNaissance = dateNaiss.getYear();
                                        String anneeString = String.valueOf(anneeNaissance);
                                        if (anneeString.length() == 4) {
                                            anneeString = anneeString.substring(0, 2);
                                            anneeNaissance = Integer.parseInt(anneeString);
                                        }

                                    } else {
                                        anneeNaissance = 70;
                                    }

                                }
                                //
                                // !!! Attention test basé sur le numéro AVS
                                // pour calculé l'age
                                //
                                if ((anneeNaissance + 1918) > Integer.parseInt(rec.getAnnee())) {
                                    // test de l'age à partir du n°AVS
                                    errors.add(getSession().getLabel("DT_ERR_AGE_MIN"));
                                    breakTests = true;
                                } else {
                                    if (CIDeclarationBatch.isAvs0(noAvs)) {
                                        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                                        info.add(getSession().getLabel("DT_AVS_0"));
                                    } else {
                                        if (noAvs.length() < 11) {
                                            // avs trop court
                                            ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                                            info.add(getSession().getLabel("DT_ERR_AVS_11"));
                                        }
                                    }
                                    if (!ecriture.rechercheCI(getTransaction(), null, false, false)
                                            || getTransaction().hasErrors()) {
                                        info.add(getSession().getLabel("DT_NUM_AVS_INVALIDE"));
                                        breakTests = true;
                                    }
                                }
                                // test sur le total des inscriptions pour
                                // l'affiliation et l'année en cours
                                if ("True".equals(accepteEcrituresNegatives) && !montantPositif) {
                                    if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(
                                            getTransaction(), false).getRegistre())) {
                                        errors.add(getSession().getLabel("MSG_DT_INCONNU_ET_NEG"));
                                    } else {

                                        BigDecimal totalPourAff = new BigDecimal("0");
                                        try {
                                            totalPourAff = ecriture.getWrapperUtil().rechercheEcritureEmpResult(
                                                    getTransaction(), CIUtil.unFormatAVS(numeroAffilie));
                                            totalPourAff = totalPourAff.subtract(new BigDecimal(montantEcr.trim()));
                                        } catch (Exception e) {
                                            totalPourAff = new BigDecimal("0");

                                        }

                                        int res = totalPourAff.compareTo(new BigDecimal("0"));
                                        if (res < 0) {
                                            errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                        }
                                    }
                                }
                                if (!breakTests) {
                                    if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(
                                            getTransaction(), false).getRegistre())) {
                                        // ci PROVISOIRE
                                        // assuré inconnu
                                        info.add(getSession().getLabel("DT_ASSURE_INCONNU"));
                                        CICompteIndividuel ci = new CICompteIndividuel();
                                        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
                                        if ((!JadeStringUtil.isBlank(ecriture.getAvs()) || !JadeStringUtil
                                                .isBlank(ecriture.getNomPrenom()))
                                                && !"00000000".equals(ecriture.getAvs())) {

                                            if (!JadeStringUtil.isBlank(ecriture.getAvs())) {
                                                ciMgr.setForNumeroAvs(ecriture.getAvs());
                                            } else {
                                                ciMgr.setForNomPrenom(ecriture.getNomPrenom());
                                            }
                                        } else if ("00000000".equals(ecriture.getAvs())) {
                                            ciMgr.setForNumeroAvs(noAvs);
                                        }
                                        if (!JadeStringUtil.isBlank(ciMgr.getForNumeroAvs())
                                                || !JadeStringUtil.isBlank(ciMgr.getForNomPrenom())) {
                                            ciMgr.setSession(getSession());
                                            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                                            ciMgr.find(getTransaction());
                                            if (ciMgr.size() != 0) {
                                                ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                                                CIEcritureManager ecrMgr = new CIEcritureManager();
                                                ecrMgr.setSession(getSession());
                                                ecrMgr.setForAnnee(rec.getAnnee());
                                                ecrMgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                                                ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(),
                                                        numeroAffilie));
                                                ecrMgr.find(getTransaction());
                                                for (int i = 0; i < ecrMgr.size(); i++) {
                                                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                                                    if ((ecr.getMoisDebut().equals(rec.getMoisDebut() + ""))
                                                            && (ecr.getMoisFin().equals(rec.getMoisFin() + ""))
                                                            && ("01".equals(ecr.getGreFormat())
                                                                    || "11".equals(ecr.getGreFormat())
                                                                    || "07".equals(ecr.getGreFormat()) || "17"
                                                                        .equals(ecr.getGreFormat()))

                                                            // &&
                                                            // (ecr.getMontant().equals(ecriture.getMontant())))
                                                            // {
                                                            && ((ecr.getMontant().substring(0, ecr.getMontant()
                                                                    .length() - 3)).equals(ecriture.getMontant()
                                                                    .substring(0, ecriture.getMontant().length() - 3)))) {

                                                        if ((JadeStringUtil.isBlank(ecriture.getExtourne()) && "0"
                                                                .equals(ecr.getExtourne()))
                                                                || ecr.getExtourne().equals(ecriture.getExtourne())) {
                                                            // erreur: écriture
                                                            // identique
                                                            errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));
                                                            // breakTests =
                                                            // true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        // ci ok, recherche des écritures
                                        // identiques

                                        CIEcritureManager ecrMgr = new CIEcritureManager();
                                        ecrMgr.setSession(getSession());
                                        ecrMgr.setForAnnee(rec.getAnnee());
                                        ecrMgr.setForCompteIndividuelId(ecriture.getCI(getTransaction(), false)
                                                .getCompteIndividuelId());

                                        ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(), numeroAffilie));
                                        ecrMgr.find(getTransaction());
                                        // System.out.println(">>>>>"+rec.getNumeroAvs()+"-"+ecrMgr.size()+numeroAffilie.substring(0,3)+"."+numeroAffilie.substring(3));
                                        for (int i = 0; i < ecrMgr.size(); i++) {
                                            CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                                            // System.out.println(rec.getNumeroAvs()+"-"+ecr.getCode()+"-"+CIEcriture.CS_CODE_PROVISOIRE);
                                            if (CIEcriture.CS_CODE_PROVISOIRE.equals(ecr.getCode())) {
                                                // erreur: écriture provisoire
                                                // déjà présente
                                                errors.add(getSession().getLabel("DT_INSCR_PROV"));
                                                // breakTests = true;
                                                break;
                                            }
                                            if ((ecr.getMoisDebut().equals(rec.getMoisDebut() + ""))
                                                    && (ecr.getMoisFin().equals(rec.getMoisFin() + ""))
                                                    && ("01".equals(ecr.getGreFormat())
                                                            || "11".equals(ecr.getGreFormat())
                                                            || "07".equals(ecr.getGreFormat()) || "17".equals(ecr
                                                            .getGreFormat()))
                                                    && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3))
                                                            .equals(ecriture.getMontant().substring(0,
                                                                    ecriture.getMontant().length() - 3)))) {

                                                if ((JadeStringUtil.isBlank(ecriture.getExtourne()) && "0".equals(ecr
                                                        .getExtourne()))
                                                        || ecr.getExtourne().equals(ecriture.getExtourne())) {

                                                    // erreur: écriture
                                                    // identique
                                                    errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));
                                                    // breakTests = true;
                                                    break;
                                                }

                                            }
                                        }

                                        // au CI, tester clôture
                                        String clo = ecriture.getCI(getTransaction(), false).getDerniereCloture(true);
                                        if (ecriture.aCloturer(new JADate(clo))) {
                                            // écriture avant clôture
                                            ciAdd.add(getSession().getLabel("DT_CI_ADDITIONEL"));
                                            if ((errors.size() == 0) && (info.size() == 0)) {
                                                nbrInscriptionsCI++;

                                                if (montantPositif) {
                                                    montantInscriptionsCI.add(montantEcr);
                                                } else {
                                                    montantInscriptionsCI.sub(montantEcr);
                                                }
                                            }
                                        } else {
                                            if (!ecriture.getCI(getTransaction(), false).isCiOuvert().booleanValue()) {
                                                if (ecriture.isPeriodeDeCotisationACheval(getTransaction(), new JADate(
                                                        clo))) {
                                                    errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                                                } else {
                                                    info.add(getSession().getLabel("DT_ECR_APRES_CLOTURE"));

                                                }
                                            } else {
                                                if (ecriture.isPeriodeDeCotisationACheval(getTransaction(), new JADate(
                                                        clo))) {
                                                    errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                                                }

                                                if ((errors.size() == 0) && (info.size() == 0)) {
                                                    nbrInscriptionsCI++;
                                                    if (montantPositif) {
                                                        montantInscriptionsCI.add(montantEcr);
                                                    } else {
                                                        montantInscriptionsCI.sub(montantEcr);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (errors.size() != 0) {
                                    // si il y a eu des erreurs
                                    nbrInscriptionsErreur++;
                                    if (montantPositif) {
                                        montantInscriptionsErreur.add(montantEcr);
                                    } else {
                                        montantInscriptionsErreur.sub(montantEcr);
                                    }
                                    result = false;
                                } else {
                                    // pas d'erreur
                                    if (info.size() != 0) {
                                        nbrInscriptionsSuspens++;
                                        if (montantPositif) {
                                            montantInscriptionsSuspens.add(montantEcr);
                                        } else {
                                            montantInscriptionsSuspens.sub(montantEcr);
                                        }
                                    }
                                    // -------------------------------------------------------------------------------
                                    // Ajout écriture et mis a jour du journal
                                    // -------------------------------------------------------------------------------
                                    if (modeInscription) {
                                        // Si on est en mode linkDraco, on ne
                                        // passe pas par l'écriture, mais par
                                        // l'inscription DRACO
                                        if ("True".equals(accepteLienDraco)) {
                                            DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
                                            // recherche de la déclaration en
                                            // question
                                            DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                                            decMgr.setForIdJournal(journal.getIdJournal());
                                            decMgr.setSession((BSession) getSessionDS(getSession()));
                                            decMgr.find(getTransaction());
                                            DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                                    .getFirstEntity();
                                            insc.setDeclaration(declarationDraco);
                                            insc.setSession((BSession) getSessionDS(getSession()));
                                            insc.setIdDeclaration(declarationDraco.getIdDeclaration());
                                            if (!JadeStringUtil.isIntegerEmpty(String.valueOf(rec.getJourDebut()))) {
                                                insc.setPeriodeDebut(rec.getJourDebut() + "." + rec.getMoisDebut());
                                            } else {
                                                insc.setPeriodeDebut(ecriture.getMoisDebutPad());
                                            }
                                            if (!JadeStringUtil.isIntegerEmpty(String.valueOf(rec.getJourFin()))) {
                                                insc.setPeriodeFin(rec.getJourFin() + "." + ecriture.getMoisFin());
                                            } else {
                                                insc.setPeriodeFin(ecriture.getMoisFinPad());
                                            }
                                            if (!JadeStringUtil.isIntegerEmpty(rec.getCodeCanton())) {
                                                try {
                                                    String codeCanton = CIUtil.codeUtilisateurToCodeSysteme(
                                                            getTransaction(), rec.getCodeCanton(), "PYCANTON",
                                                            getSession());
                                                    insc.setCodeCanton(codeCanton);
                                                } catch (Exception e) {
                                                }
                                            }
                                            insc.setMontant(JANumberFormatter.deQuote(ecriture.getMontant()));
                                            insc.setNumeroAvs(CIUtil.unFormatAVS(ecriture.getAvs()));
                                            insc.setNomPrenom(ecriture.getNomPrenom());
                                            insc.setAnneeInsc(ecriture.getAnnee());
                                            insc.add(getTransaction());
                                            if (rec.getMontantAc() != null) {
                                                BigDecimal montantCommunique = new BigDecimal(rec.getMontantAc());
                                                BigDecimal montantEcriture = new BigDecimal(insc.getACI());
                                                if (montantCommunique.compareTo(montantEcriture) != 0) {
                                                    info.add(getSession().getLabel("MSG_MONTANT_AC"));
                                                }
                                            }

                                        } else {
                                            ecriture.setNoSumNeeded(true);
                                            // Modif. mettre le kbbatt à 2 pour
                                            // concordance nnss
                                            ecriture.setWantForDeclaration(new Boolean(false));
                                            ecriture.add(getTransaction());
                                        }
                                        getTransaction().disableSpy();
                                        if (!getTransaction().hasErrors()) {
                                            getTransaction().commit();
                                        }
                                    }
                                }
                                // -------------------------------------------------------------------------------
                                // log assuré
                                // -------------------------------------------------------------------------------
                                rec.setCiAdd(ciAdd);
                                rec.setInfo(info);
                                rec.setErrors(errors);
                                tableLogAssure.put(_getFullKey(rec, line), rec);
                                if (getTransaction().hasErrors()) {
                                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), "Déclaration");
                                    getMemoryLog().logMessage(ecriture.getNomPrenom(), FWMessage.ERREUR, "Déclaration");
                                    getTransaction().clearErrorBuffer();
                                }
                                nbrInscriptionsTraites++;
                                if (montantPositif) {
                                    montantInscritionsTraites.add(montantEcr);
                                } else {
                                    montantInscritionsTraites.sub(montantEcr);
                                }
                                if (!modeInscription) {
                                    // simulation
                                    // faire un rollback permettant d'effacer le
                                    // CI temporaire éventuellement créés
                                    getTransaction().rollback();
                                }
                                // -------------------------------------------------------------------------------
                                // met a jour les TreeMaps pour les summaries
                                // -------------------------------------------------------------------------------
                                nbrInscriptionsTotalControle = nbrInscriptionsCI + nbrInscriptionsSuspens;
                                montantTotalControle.add(montantInscriptionsCI);
                                montantTotalControle.add(montantInscriptionsSuspens);

                                _updateSummary(hNbrInscriptionsTraites, hMontantInscritionsTraites,
                                        hNbrInscriptionsErreur, hMontantInscriptionsErreur, hNbrInscriptionsSuspens,
                                        hMontantInscriptionsSuspens, hNbrInscriptionsCI, hMontantInscriptionsCI,
                                        hNbrInscriptionsNegatives, hMontantInscriptionsNegatives,
                                        nbrInscriptionsTraites, montantInscritionsTraites, nbrInscriptionsErreur,
                                        montantInscriptionsErreur, nbrInscriptionsSuspens, montantInscriptionsSuspens,
                                        nbrInscriptionsCI, montantInscriptionsCI, nbrInscriptionsNegatives,
                                        montantInscriptionsNegatives, hNbrInscriptionsTotalControle,
                                        nbrInscriptionsTotalControle, hMontantTotalControle, montantTotalControle, key);

                            } // fin du else 'journal trouvé'
                        } // fin du if 'numéro affilie'
                    } // fin du if 'annee'
                      // System.gc();
                } // fin du while principle
                if (modeInscription) {
                    // maj des totaux des journaux

                    Set<String> keys = tableJournaux.keySet();
                    Iterator<String> iter = keys.iterator();

                    getTransaction().disableSpy();
                    while (iter.hasNext()) {

                        String key = iter.next();
                        CIJournal journal = (CIJournal) tableJournaux.get(key);

                        // Mettre à jour les inscriptions
                        if (!JadeStringUtil.isIntegerEmpty(journal.getIdJournal())) {
                            journal.updateInscription(getTransaction());
                        }
                        if ("True".equals(accepteLienDraco)) {
                            DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                            decMgr.setForIdJournal(journal.getIdJournal());
                            decMgr.setSession((BSession) getSessionDS(getSession()));
                            decMgr.find(getTransaction());
                            if (decMgr.size() > 0) {
                                DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                        .getFirstEntity();
                                declarationDraco.calculeTotauxAcAf();
                            }
                        }

                        hMontantTotalControle.keySet();

                        // if (iter1.hasNext()) {
                        FWCurrency montantTotal = (FWCurrency) hMontantTotalControle.get(key);
                        // }

                        // Mettre à jour le journal
                        journal.setIdJournal(journal.getIdJournal());
                        journal.setSession(getSession());
                        journal.retrieve();
                        if (!JadeStringUtil.isDecimalEmpty(montantTotal.toString())) {
                            journal.setTotalControle(montantTotal.toString());
                        }
                        journal.update();

                    }

                    getTransaction().enableSpy();
                }
                // ------------------------------------------------------------------------------
                // affichage / génération des rapports
                // ------------------------------------------------------------------------------
                _doLogJournauxExistant(hJournalExisteDeja); // des Journaux qui
                // existait dejà
                // CIDeclarationTextOutput doc = new CIDeclarationTextOutput();
                ICIDeclarationOutput doc = new CIDeclarationHTMLOutput();
                doc.setSession(getSession());
                doc.setSimulation(!modeInscription);
                doc.setData(tableLogAssure);
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setDocumentType("0161CCI");
                docInfo.setDocumentTypeNumber("");
                this.registerAttachedDocument(docInfo, doc.getOutputFile());
                // CIDeclarationSummaryTextOutput docSummary = new
                // CIDeclarationSummaryTextOutput();
                ICIDeclarationOutput docSummary = new CIDeclarationSummaryHTMLOutput();
                docSummary.setSession(getSession());
                docSummary.setSimulation(!modeInscription);
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(hNbrInscriptionsTraites);
                params.add(hMontantInscritionsTraites);
                params.add(hNbrInscriptionsErreur);
                params.add(hMontantInscriptionsErreur);
                params.add(hNbrInscriptionsSuspens);
                params.add(hMontantInscriptionsSuspens);
                params.add(hNbrInscriptionsCI);
                params.add(hMontantInscriptionsCI);
                params.add(tableJournaux);
                // ajout totaux journaux pour PUCS
                if (JadeStringUtil.isBlank(totalControle)) {
                    params.add(new FWCurrency(0L));
                } else {
                    params.add(new FWCurrency(totalControle));
                }
                if (JadeStringUtil.isBlank(nombreInscriptions)) {
                    params.add(new Long(0L));
                } else {
                    params.add(new Long(nombreInscriptions));
                }
                try {
                    totauxJournaux = itDec.getTotauxJournaux();
                } catch (Exception err) {
                    err.printStackTrace();
                }
                try {
                    nbInsc = itDec.getNbSalaires();
                } catch (Exception err) {
                    err.printStackTrace();
                }

                params.add(totauxJournaux);
                params.add(nbInsc);
                params.add(hNbrInscriptionsNegatives);
                params.add(hMontantInscriptionsNegatives);
                params.add(hMontantTotalControle);
                params.add(hNbrInscriptionsTotalControle);
                docSummary.setData(params);
                this.registerAttachedDocument(docInfo, docSummary.getOutputFile());
                // ------------------------------------------------------------------------------
                // Comparaison avec somme de control (si renseigné )
                // ------------------------------------------------------------------------------
                if (!JadeStringUtil.isBlank(totalControle)) {
                    FWCurrency totalCalcule = new FWCurrency(0);
                    Collection<Object> values = hMontantInscritionsTraites.values();
                    Iterator<Object> it = values.iterator();
                    while (it.hasNext()) {
                        FWCurrency cur = (FWCurrency) it.next();
                        totalCalcule.add(cur);
                    }
                    FWCurrency totalControleFormate = new FWCurrency(totalControle);
                    if (!totalCalcule.toStringFormat().equals(totalControleFormate.toStringFormat())) {
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_TOTALE_COR_PAS"), FWMessage.ERREUR,
                                "Déclaration");
                        getMemoryLog().logMessage(
                                getSession().getLabel("DT_LOG_TOTALE_CTRL") + " : "
                                        + totalControleFormate.toStringFormat(), FWMessage.INFORMATION, "Déclaration");
                        getMemoryLog().logMessage(
                                getSession().getLabel("DT_LOG_TOTALE_CAL") + " : " + totalCalcule.toStringFormat(),
                                FWMessage.INFORMATION, "Déclaration");
                        isErrorMontant = true;
                        result = false;
                    }
                } // fin total contrôle
                  // ------------------------------------------------------------------------------
                  // Comparaison avec nombre d'inscriptions (si renseigné )
                  // ------------------------------------------------------------------------------
                if (!JadeStringUtil.isBlank(nombreInscriptions)) {
                    long nombreInscritionsCalcule = 0;
                    Collection<Object> values = hNbrInscriptionsTraites.values();
                    Iterator<Object> it = values.iterator();
                    while (it.hasNext()) {
                        long tmp = ((Long) it.next()).longValue();
                        nombreInscritionsCalcule += tmp;
                    }
                    new FWCurrency(totalControle);
                    if (!(nombreInscritionsCalcule + "").equals(nombreInscriptions)) {
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_NB_INS_COR_PAS"), FWMessage.ERREUR,
                                "Déclaration");
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_NB_INS") + " : " + nombreInscriptions,
                                FWMessage.INFORMATION, "Déclaration");
                        getMemoryLog().logMessage(
                                getSession().getLabel("DT_LOG_NB_INS_FICH") + " : " + nombreInscritionsCalcule + "",
                                FWMessage.INFORMATION, "Déclaration");
                        isErrorNbInscriptions = true;
                        result = false;
                    }
                } // fin nombre d'inscriptions

            } catch (Exception ioe) {
                ioe.printStackTrace();
                result = false;
                getMemoryLog().logMessage(getSession().getLabel("MSG_FORMAT_FICHIER_MAUVAIS"), FWMessage.FATAL,
                        "Déclaration");
                getMemoryLog().logMessage(ioe.toString(), FWMessage.FATAL, "Déclaration");

            }

            return result;
        }

    }

    /*
     * Cherche le journal à utiliser pour ce record Si le journal a déjà été ouvert dans ce process, on le réutilise
     * sinon on le crée (si modeInscription)
     */
    private CIJournal _findJournal(boolean modeInscription, CIDeclarationRecord rec,
            TreeMap<String, Object> tableJournaux) throws Exception {
        // cle pour pouvoir stock un journal par affilié/année
        String key = _getKey(rec);
        CIJournal journal = null;
        String numAffFormate = CIUtil.formatNumeroAffilie(getSession(), rec.getNumeroAffilie());
        if (tableJournaux.containsKey(key)) {
            journal = (CIJournal) tableJournaux.get(key);
        } else {
            // on a pas encore eu à traité ce journal.
            // si il existe dejà dans la DB, on génère une erreur
            CIJournalManager jrnMgr = new CIJournalManager();
            jrnMgr.setSession(getSession());
            jrnMgr.setForAnneeCotisation(rec.getAnnee());
            jrnMgr.setForIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            jrnMgr.setForIdAffiliation(numAffFormate);
            int size = jrnMgr.getCount(getTransaction());
            journal = new CIJournal();
            journal.setSession(getSession());
            journal.setAnneeCotisation(rec.getAnnee());
            journal.setIdAffiliation(numAffFormate, true, false);
            journal.setTotalControle(getTotalControle().equals("") ? "200.00" : getTotalControle());
            journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
            journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
            boolean wantCreatePrincipale = true;
            if (size == 0) {
                // si il n'existe pas encore dans la DB, on le crée (sauf en
                // mode simulation)
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            } else {
                wantCreatePrincipale = false;
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_COMPLEMENTAIRE);
            }
            try {
                if (modeInscription) {

                    // mode inscription
                    journal.add(getTransaction());
                    if (!getTransaction().hasErrors()) {
                        if ("True".equals(accepteLienDraco)) {
                            DSDeclarationViewBean declaration = null;
                            DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
                            dsMgr.setSession((BSession) getSessionDS(getSession()));
                            dsMgr.setForAffiliationId(journal.getIdAffiliation());
                            dsMgr.setForAnnee(journal.getAnneeCotisation());
                            dsMgr.setForEtat(DSDeclarationViewBean.CS_OUVERT);
                            if (wantCreatePrincipale) {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                            } else {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                            }
                            dsMgr.find(getTransaction());
                            if (dsMgr.size() > 0) {
                                declaration = (DSDeclarationViewBean) dsMgr.getFirstEntity();
                                declaration.setIdJournal(journal.getIdJournal());
                                if (!JadeStringUtil.isIntegerEmpty(totalControle)) {
                                    declaration.setTotalControleDS(totalControle);
                                }
                                declaration.update(getTransaction());
                            } else {
                                declaration = new DSDeclarationViewBean();
                                declaration.setAffiliationId(journal.getIdAffiliation());
                                declaration.setSession((BSession) getSessionDS(getSession()));
                                declaration.setAnnee(journal.getAnneeCotisation());
                                if (!JadeStringUtil.isIntegerEmpty(totalControle)) {
                                    declaration.setTotalControleDS(totalControle);
                                }
                                if (wantCreatePrincipale) {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                                } else {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                                }
                                declaration.setEtat(DSDeclarationViewBean.CS_OUVERT);
                                declaration.setIdJournal(journal.getIdJournal());
                                declaration.add(getTransaction());
                            }
                        }
                    }
                    if (getTransaction().hasErrors()) {
                        System.out.println("Erreur transaction :" + getTransaction().getErrors().toString());
                        // Pour éviter de logger dans le mail et d'ajouter une
                        // erreur dans la trans alors que le cas est géré
                        getTransaction().clearErrorBuffer();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                journal = null;
            }
            tableJournaux.put(key, journal);
        }
        return journal;
    }

    /*
     * Retourne un clé qui sera utilisée par pour trier les treemaps du process
     * 
     * cette clé est composée comme suit : du n° affilié de l'année du n°AVS du mois de deb du mois de fin
     */
    private String _getFullKey(CIDeclarationRecord rec, int num) {
        String numAvs = rec.getNumeroAvs();
        String moisDebut = (rec.getMoisDebut() < 10) ? "0" + rec.getMoisDebut() : "" + rec.getMoisDebut();
        String moisFin = (rec.getMoisFin() < 10) ? "0" + rec.getMoisFin() : "" + rec.getMoisFin();
        return CIUtil.unFormatAVS(rec.getNumeroAffilie()) + rec.getAnnee() + numAvs + moisDebut + moisFin + num;
    }

    /*
     * Rretourne un clé qui sera utilisée pour trier les treemaps du process cette clé est composée du n° affilié et de
     * l'année
     */
    private String _getKey(CIDeclarationRecord rec) {
        return CIUtil.unFormatAVS(rec.getNumeroAffilie()) + rec.getAnnee() + rec.getNomAffilie();
    }

    private boolean _isInPeriodeAffiliation(CIDeclarationRecord rec) throws Exception {
        if ((rec.getDebutAffiliation() == null) || (rec.getFinAffiliation() == null)) {
            return false;
        }
        JADate debAff = new JADate(rec.getDebutAffiliation());
        JADate finAff = new JADate(("".equals(rec.getFinAffiliation())) ? "01.01.9999" : rec.getFinAffiliation());
        // Si c'est 99-99, on compare juste l'année
        if ((99 == rec.getMoisDebut()) && (99 == rec.getMoisFin())) {
            int anneeInt = Integer.parseInt(rec.getAnnee());
            int anneeDebutAff = Integer.parseInt(rec.getDebutAffiliation().substring(6));
            if (anneeInt < anneeDebutAff) {
                return false;
            }
            if (!JAUtil.isDateEmpty(rec.getFinAffiliation())) {
                int anneeFinAff = Integer.parseInt(rec.getFinAffiliation().substring(6));
                if (anneeInt > anneeFinAff) {
                    return false;
                }
            }
        } else {
            JADate deb = new JADate(1, rec.getMoisDebut(), Integer.parseInt(rec.getAnnee()));
            JADate fin = new JADate(1, rec.getMoisFin(), Integer.parseInt(rec.getAnnee()));
            if (!BSessionUtil.compareDateBetweenOrEqual(getTransaction().getSession(), debAff.toStr("."),
                    finAff.toStr("."), deb.toStr("."))) {
                return false;
            }
            if (!BSessionUtil.compareDateBetweenOrEqual(getTransaction().getSession(), debAff.toStr("."),
                    finAff.toStr("."), fin.toStr("."))) {
                return false;
            }
        }
        return true;
    };

    /*
     * Met à jour les TreeMap utilisés pour les summary par affilié/Année
     */
    private void _updateSummary(TreeMap<String, Object> hNbrInscriptionsTraites,
            TreeMap<String, Object> hMontantInscritionsTraites, TreeMap<String, Object> hNbrInscriptionsErreur,
            TreeMap<String, Object> hMontantInscriptionsErreur, TreeMap<String, Object> hNbrInscriptionsSuspens,
            TreeMap<String, Object> hMontantInscriptionsSuspens, TreeMap<String, Object> hNbrInscriptionsCI,
            TreeMap<String, Object> hMontantInscriptionsCI, TreeMap<String, Object> hNbrInscriptionsNegatives,
            TreeMap<String, Object> hMontantInscriptionsNegatives, long nbrInscriptionsTraites,
            FWCurrency montantInscritionsTraites, long nbrInscriptionsErreur, FWCurrency montantInscriptionsErreur,
            long nbrInscriptionsSuspens, FWCurrency montantInscriptionsSuspens, long nbrInscriptionsCI,
            FWCurrency montantInscriptionsCI, long nbrInscritptionsNegatives, FWCurrency montantInscriptionsNegatives,
            TreeMap<String, Object> hNbrInscriptionsTotalControle, long nbrInscriptionsTotalControle,
            TreeMap<String, Object> hMontantTotalControle, FWCurrency montantTotalControle, String key) {
        // compteur
        Long value = new Long(0L);
        if (hNbrInscriptionsTraites.get(key) == null) {
            hNbrInscriptionsTraites.put(key, new Long(0L));
        }
        if (hNbrInscriptionsErreur.get(key) == null) {
            hNbrInscriptionsErreur.put(key, new Long(0L));
        }
        if (hNbrInscriptionsSuspens.get(key) == null) {
            hNbrInscriptionsSuspens.put(key, new Long(0L));
        }
        if (hNbrInscriptionsCI.get(key) == null) {
            hNbrInscriptionsCI.put(key, new Long(0L));
        }
        if (hNbrInscriptionsNegatives.get(key) == null) {
            hNbrInscriptionsNegatives.put(key, new Long(0L));
        }
        if (hNbrInscriptionsTotalControle.get(key) == null) {
            hNbrInscriptionsTotalControle.put(key, new Long(0L));
        }
        value = (Long) hNbrInscriptionsTraites.get(key);
        hNbrInscriptionsTraites.put(key, new Long(value.longValue() + nbrInscriptionsTraites));
        value = (Long) hNbrInscriptionsErreur.get(key);
        hNbrInscriptionsErreur.put(key, new Long(value.longValue() + nbrInscriptionsErreur));
        value = (Long) hNbrInscriptionsSuspens.get(key);
        hNbrInscriptionsSuspens.put(key, new Long(value.longValue() + nbrInscriptionsSuspens));
        value = (Long) hNbrInscriptionsCI.get(key);
        hNbrInscriptionsCI.put(key, new Long(value.longValue() + nbrInscriptionsCI));
        value = (Long) hNbrInscriptionsNegatives.get(key);
        hNbrInscriptionsNegatives.put(key, new Long(value.longValue() + +nbrInscritptionsNegatives));
        value = (Long) hNbrInscriptionsTotalControle.get(key);
        hNbrInscriptionsTotalControle.put(key, new Long(value.longValue() + nbrInscriptionsTotalControle));
        // montants
        FWCurrency montant = new FWCurrency(0L);
        if (hMontantInscritionsTraites.get(key) == null) {
            hMontantInscritionsTraites.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsErreur.get(key) == null) {
            hMontantInscriptionsErreur.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsSuspens.get(key) == null) {
            hMontantInscriptionsSuspens.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsCI.get(key) == null) {
            hMontantInscriptionsCI.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsNegatives.get(key) == null) {
            hMontantInscriptionsNegatives.put(key, new FWCurrency("0"));
        }
        if (hMontantTotalControle.get(key) == null) {
            hMontantTotalControle.put(key, new FWCurrency("0"));
        }
        montant = (FWCurrency) hMontantInscritionsTraites.get(key);
        montant.add(montantInscritionsTraites);
        hMontantInscritionsTraites.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsErreur.get(key);
        montant.add(montantInscriptionsErreur);
        hMontantInscriptionsErreur.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsSuspens.get(key);
        montant.add(montantInscriptionsSuspens);
        hMontantInscriptionsSuspens.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsCI.get(key);
        montant.add(montantInscriptionsCI);
        hMontantInscriptionsCI.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsNegatives.get(key);
        montant.add(montantInscriptionsNegatives);
        hMontantInscriptionsNegatives.put(key, montant);
        montant = (FWCurrency) hMontantTotalControle.get(key);
        montant.add(montantTotalControle);
        hMontantTotalControle.put(key, montant);
    }

    @Override
    protected void _validate() throws Exception {

        // Nom de fichier : obligatoire
        if (JadeStringUtil.isBlank(filename)) {
            this._addError(getSession().getLabel("FICHIER_DEC_ETRE_RENSEIGNE"));
        }
        // adresse email obligatoire
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_ETRE_RENSEIGNE"));
        }
        // divers :

        setControleTransaction(true);
        if (!CIDeclarationBatch.CS_AC.equals(Type) && !CIDeclarationBatch.CS_AMI.equals(Type)) {
            setSendCompletionMail(true);
            setSendMailOnError(true);
        } else {
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }

    }

    /**
     * @return
     */
    public String getAccepteAnneeEnCours() {
        return accepteAnneeEnCours;

    }

    /**
     * Returns the accepteEcrituresNegatives.
     * 
     * @return String
     */
    public String getAccepteEcrituresNegatives() {
        return accepteEcrituresNegatives;
    }

    /**
     * @return
     */
    public String getAccepteLienDraco() {
        return accepteLienDraco;
    }

    /**
     * Returns the fromAnnee.
     * 
     * @return String
     */
    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError() || isErrorMontant || isErrorNbInscriptions) {
            return getSession().getLabel("EMAIL_IMPORTATION_ECHEC");
        } else {
            return getSession().getLabel("EMAIL_IMPORTATION_SUCCES");
        }
    }

    /**
     * Returns the fileName.
     * 
     * @return String
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the forNumeroAffilie.
     * 
     * @return String
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    /**
     * Returns the nombreInscriptions.
     * 
     * @return String
     */
    public String getNombreInscriptions() {
        return nombreInscriptions;
    }

    public BISession getSessionDS(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionDraco");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("DRACO").newSession(local);
            local.setAttribute("sessionDraco", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Returns the simulation.
     * 
     * @return String
     */
    public String getSimulation() {
        return simulation;
    }

    /**
     * Returns the totalControle.
     * 
     * @return String
     */
    public String getTotalControle() {
        return totalControle;
    }

    /**
     * Returns the type.
     * 
     * @return String
     */
    public String getType() {
        return Type;
    }

    /**
     * Returns the hasChild.
     * 
     * @return boolean
     */
    public boolean isHasChild() {
        return hasChild;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAccepteAnneeEnCours(String accepteAnneeEnCours) {
        this.accepteAnneeEnCours = accepteAnneeEnCours;
    }

    /**
     * Sets the accepteEcrituresNegatives.
     * 
     * @param accepteEcrituresNegatives
     *            The accepteEcrituresNegatives to set
     */
    public void setAccepteEcrituresNegatives(String accepteEcrituresNegatives) {
        this.accepteEcrituresNegatives = accepteEcrituresNegatives;
    }

    /**
     * @param string
     */
    public void setAccepteLienDraco(String string) {
        accepteLienDraco = string;
    }

    /**
     * Sets the fromAnnee.
     * 
     * @param fromAnnee
     *            The fromAnnee to set
     */
    public void setAnneeCotisation(String fromAnnee) {
        anneeCotisation = fromAnnee;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
    }

    /**
     * Sets the fileName.
     * 
     * @param fileName
     *            The fileName to set
     */
    public void setFilename(String fileName) {
        filename = fileName;
    }

    /**
     * Sets the forNumeroAffilie.
     * 
     * @param forNumeroAffilie
     *            The forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * Sets the hasChild.
     * 
     * @param hasChild
     *            The hasChild to set
     */
    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    /**
     * Sets the nombreInscriptions.
     * 
     * @param nombreInscriptions
     *            The nombreInscriptions to set
     */
    public void setNombreInscriptions(String nombreInscriptions) {
        this.nombreInscriptions = nombreInscriptions;
    }

    /**
     * Sets the simulation.
     * 
     * @param simulation
     *            The simulation to set
     */
    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    /**
     * Sets the totalControle.
     * 
     * @param totalControle
     *            The totalControle to set
     */
    public void setTotalControle(String totalControle) {
        this.totalControle = JANumberFormatter.deQuote(totalControle);
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            The type to set
     */
    public void setType(String type) {
        Type = type;
    }

}
