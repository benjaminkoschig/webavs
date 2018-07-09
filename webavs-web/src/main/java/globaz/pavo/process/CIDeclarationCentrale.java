package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeInitProperties;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationCentraleHTMLOutput;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationCentraleIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationCentraleSummaryHtmlOutput;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationCentraleXMLIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationMilitaireIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationRecord;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationOutput;
import globaz.pavo.util.CIUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CIDeclarationCentrale extends BProcess {

    private static final long serialVersionUID = -7216170972130657323L;

    public static String getNomFormatCI(String nomPrenom) {
        if (JadeStringUtil.isBlank(nomPrenom)) {
            // tel quel
            return nomPrenom;
        }
        nomPrenom = nomPrenom.trim().toUpperCase();
        int sep = nomPrenom.indexOf(',');
        if (sep != -1) {
            // test de l'espace après et avant la virgule
            int before;
            if (' ' == nomPrenom.charAt(sep - 1)) {
                before = sep - 1;
                while (' ' == nomPrenom.charAt(before)) {
                    before--;
                }
            } else {
                before = sep - 1;
            }
            int next;
            if ((nomPrenom.length() > sep + 1) && (' ' == nomPrenom.charAt(sep + 1))) {
                next = sep + 1;
                while (' ' == nomPrenom.charAt(next)) {
                    next++;
                }
            } else {
                next = sep + 1;
            }
            return nomPrenom.substring(0, before + 1) + "," + nomPrenom.substring(next);
        } else {
            // remplacement du premier espace en virgule
            sep = nomPrenom.indexOf(' ');
            if (sep != -1) {
                int next = sep + 1;
                while (' ' == nomPrenom.charAt(next)) {
                    next++;
                }
                return nomPrenom.substring(0, sep) + "," + nomPrenom.substring(next);
            } else {
                return nomPrenom + ",";
            }
        }

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
        String logg = "false";
        if (args.length > 3) {
            logg = args[3];
        }

        CIDeclarationCentrale process = null;
        try {

            if (logg.equalsIgnoreCase("true")) {
                JadeInitProperties.setLogDirectory("d:\\mylogs");
                Jade.getInstance().beginProfiling(CIDeclarationCentrale.class, args);

            }
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(uid, pwd);
            process = new CIDeclarationCentrale((BSession) session);
            process.setEMailAddress(email);
            process.setEchoToConsole(true);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("File: ");
            String file = input.readLine();
            if (!JadeStringUtil.isBlank(file)) {
                process.setFilename(file);
            } else {
                process.setFilename("inscriptions.txt");
            }

            System.out.print("Simulation [y/n]: ");
            process.setSimulation("N".equals(input.readLine().toUpperCase()) ? "" : "simulation");
            process.executeProcess();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Jade.getInstance().endProfiling();
            }
        }
        System.exit(0);
    }

    private String accepteEcrituresNegatives = "";
    private String anneeCotisation = "";
    private String filename = "";
    private String forNumeroAffilie = "";
    private boolean isErrorMontant = false;
    private boolean isErrorNbInscriptions = false;
    private CIJournal journal = null;
    private String nombreInscriptions = "";

    private String simulation = "";

    private String totalControle = "";

    private String type = "";

    /**
     * Commentaire relatif au constructeur CIDeclaration.
     */
    public CIDeclarationCentrale() {
        super();
    }

    public CIDeclarationCentrale(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CIDeclaration.
     */
    public CIDeclarationCentrale(globaz.globall.db.BSession session) {
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
    protected boolean _executeProcess() {
        // TreeMap pour statistique (par affilié/année) :
        TreeMap<String, Object> hNbrInscriptionsTraites = new TreeMap<String, Object>();
        TreeMap<String, Object> hMontantInscritionsTraites = new TreeMap<String, Object>();
        TreeMap<String, Object> hNbrInscriptionsErreur = new TreeMap<String, Object>();
        TreeMap<String, Object> hMontantInscriptionsErreur = new TreeMap<String, Object>();
        TreeMap<String, Object> hNbrInscriptionsSuspens = new TreeMap<String, Object>();
        TreeMap<String, Object> hMontantInscriptionsSuspens = new TreeMap<String, Object>();
        TreeMap<String, Object> hNbrInscriptionsCI = new TreeMap<String, Object>();
        TreeMap<String, Object> hMontantInscriptionsCI = new TreeMap<String, Object>();
        // Cette table contient les journaux déjà crée
        // si le journal existait déjà avant le traitement, la clé est quand
        // même mise dans cette table et la valeur sera null.
        TreeMap<String, CIJournal> tableJournaux = new TreeMap<String, CIJournal>();
        TreeMap<String, Object> hJournalExisteDeja = new TreeMap<String, Object>();
        // table pour stocké les erreurs/info au niveau des assuré (detail)
        TreeMap<String, CIDeclarationRecord> tableLogAssure = new TreeMap<String, CIDeclarationRecord>();
        boolean modeInscription = JadeStringUtil.isBlank(getSimulation());
        boolean result = true;
        int line = 0;
        if (!modeInscription) {
            getMemoryLog()
                    .logMessage(getSession().getLabel("DT_MODE_SIMULATION"), FWMessage.INFORMATION, "Déclaration");
        }
        try {
            String forNumeroAffilieSansPoint = CIUtil.unFormatAVS(getForNumeroAffilie());

            ICIDeclarationIterator itDec = null;
            if ("militaire".equals(getType())) {
                itDec = new CIDeclarationMilitaireIterator();
            } else if (CIDeclaration.CS_AC_XML.equals(getType())) {
                itDec = new CIDeclarationCentraleXMLIterator();
            } else {
                itDec = new CIDeclarationCentraleIterator();
            }

            itDec.setFilename(getFilename());
            itDec.setTransaction(getTransaction());
            int nbRecords = itDec.size();
            getParent().setState("");
            getParent().setProgressScaleValue(nbRecords);
            while (itDec.hasNext()) {
                line++;
                getParent().setProgressCounter(line);
                // System.out.println(line+"/"+nbRecords+" "+(line*100)/nbRecords+"%");
                CIDeclarationRecord rec = itDec.next();
                if ("militaire".equals(getType()) && !"6666666666".equals(rec.getNumeroAffilie())) {
                    this._addError(getTransaction(), getSession().getLabel("MSG_FORMAT_FICHIER_MAUVAIS"));
                    this._addError(getTransaction(),
                            "abr-nr " + rec.getNumeroAffilie() + "Vers.-Nr." + rec.getNumeroAvs());
                    abort();
                    return false;
                }

                // si l'année de cotisation n'est pas précisé, ou quelle
                // correspond a celle de ce record
                if ((JadeStringUtil.isBlank(getAnneeCotisation())) || (rec.getAnnee().equals(getAnneeCotisation()))) {
                    // si le n°d'affilie n'est pas précisé, ou qu'il correspond
                    // a celui de ce record
                    if ((JadeStringUtil.isBlank(forNumeroAffilieSansPoint))
                            || (rec.getNumeroAffilie().equals(forNumeroAffilieSansPoint))) {
                        // initialisations
                        CIEcriture ecriture = new CIEcriture();
                        ecriture.setSession(getSession());
                        ArrayList<String> errors = new ArrayList<String>();
                        ArrayList<String> info = new ArrayList<String>();
                        ArrayList<String> ciAdd = new ArrayList<String>();
                        rec.getNumeroAffilie();
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
                        // trouve le journal à utiliser pour ce record.
                        // il y a un journal par année/affilié.
                        // si le journal n'existe pas on le crée et on le garde
                        // dans une table car
                        // il peut être utilisé par plusieur ligne du fichier.
                        // si le journal existe préalablement au taitement du
                        // fichier, on génère une erreur.
                        // findJournal retourne le journal à utiliser, ou null
                        // si le jounal à utilisé existait déjà avant ce
                        // traitement,
                        // ce qui n'est pas authorisé.
                        CIJournal journal = _findJournal(modeInscription, rec, tableJournaux);
                        setProgressDescription(rec.getNumeroAvs() + "<br>" + line + "/" + nbRecords + "<br>");
                        if (getParent().isAborted() || isAborted()) {
                            journal.delete(getTransaction());
                            if (getTransaction().hasErrors()) {
                                getTransaction().commit();
                            }
                            return false;
                        }

                        if (journal == null) {
                            // Erreur, ce journal existe déjà avant ce
                            // traitement
                            hJournalExisteDeja.put(key,
                                    getSession().getLabel("DT_JOURNAL_EXISTANT") + " " + rec.getNumeroAffilie() + "/"
                                            + rec.getAnnee());
                        } else {
                            // journal trouvé
                            ecriture.setIdJournal(journal.getIdJournal());
                            ecriture.setAnnee(rec.getAnnee());
                            boolean breakTests = false;
                            // boolean supended = false;
                            // Plausi période
                            // if(!modeInscription) {
                            try {
                                int moisDebut = rec.getMoisDebut();
                                if ((moisDebut < 1) || (moisDebut > 12)) {
                                    errors.add(getSession().getLabel("DT_MOIS_DEBUT_INVALIDE"));
                                    // breakTests = true;
                                } else {
                                    ecriture.setMoisDebut("" + rec.getMoisDebut());
                                }
                                int moisFin = rec.getMoisFin();
                                if ((moisFin < 1) || (moisFin > 12)) {
                                    errors.add(getSession().getLabel("DT_MOIS_FIN_INVALIDE"));
                                    // breakTests = true;
                                } else {
                                    ecriture.setMoisFin("" + rec.getMoisFin());
                                }
                                if (moisDebut > moisFin) {
                                    errors.add(getSession().getLabel("DT_MOIS_DEBUT_PLUS_GRAND"));
                                    // breakTests = true;
                                }
                                // année en cours et futre sont interdites
                                int annee = JACalendar.today().getYear();
                                if (Integer.parseInt(rec.getAnnee()) >= annee) {
                                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));
                                    // breakTests = true;
                                }
                            } catch (Exception ex) {
                                errors.add(getSession().getLabel("DT_MOIS_INVALIDE"));
                                // breakTests = true;
                            }
                            // }
                            boolean montantPositif = rec.isMontantPositif();
                            String montantEcr = rec.getMontantEcr();
                            if ("999999".equals(rec.getNumeroAffilie().substring(0, 6))
                                    || "77777777777".equals(rec.getNumeroAffilie())
                                    || "88888888888".equals(rec.getNumeroAffilie())) {
                                ecriture.setGre(rec.getGenreEcriture());
                                ecriture.setMontant(rec.getMontantEcr());
                                if ("999999".equals(rec.getNumeroAffilie().substring(0, 6))) {
                                    ecriture.setCaisseChomage(rec.getNumeroAffilie().substring(6, 11));
                                }
                                if (Integer.valueOf(rec.getGenreEcriture()).intValue() > 9) {
                                    montantPositif = false;
                                }

                            } else {
                                // Plausi montant

                                ecriture.setGre(rec.getGenreEcriture());
                                ecriture.setMontant(rec.getMontantEcr());
                                if (Integer.valueOf(rec.getGenreEcriture()).intValue() > 9) {
                                    montantPositif = false;
                                }

                            }

                            if (montantPositif) {
                                try {
                                    FWCurrency cur = new FWCurrency(montantEcr);
                                    if (cur.compareTo(new FWCurrency("1")) == -1) {
                                        errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                                        // breakTests = true;
                                    } else {
                                        // ecriture.setGre("01");
                                        ecriture.setMontant(cur.toStringFormat());
                                    }
                                } catch (Exception inex) {
                                    errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
                                    montantEcr = "        0.00";
                                    // breakTests = true;
                                }
                            } else {
                                if (!"True".equals(getAccepteEcrituresNegatives())) {
                                    errors.add(getSession().getLabel("DT_ECRITURE_NEGATIVE"));
                                } else {

                                    FWCurrency cur = new FWCurrency(montantEcr);
                                    if (cur.compareTo(new FWCurrency("1")) == -1) {
                                        errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                                        // breakTests = true;
                                    } else {
                                        // ecriture.setGre("01");
                                        ecriture.setMontant(cur.toStringFormat());
                                    }
                                }

                                // écriture négative
                                // errors.add(getSession().getLabel("DT_ECRITURE_NEGATIVE"));
                                // breakTests = true;
                            }

                            // fin else
                            // Période affiliation
                            // Plausi no avs
                            String noAvs = rec.getNumeroAvs().trim();
                            if (noAvs.endsWith("000") && (noAvs.trim().length() != 13)) {
                                noAvs = rec.getNumeroAvs().substring(0, rec.getNumeroAvs().lastIndexOf("000")); // A
                                // controler
                            }
                            ecriture.setAvs(noAvs);
                            if (CIUtil.isNNSSlengthOrNegate(noAvs)) {
                                ecriture.setNumeroavsNNSS("true");
                                ecriture.setAvsNNSS("true");
                            }
                            if ("true".equals(ecriture.getNumeroavsNNSS()) && !NSUtil.nssCheckDigit(ecriture.getAvs())) {
                                errors.add(getSession().getLabel("MSG_CI_VAL_AVS"));

                            }
                            ecriture.setNomPrenom(CIDeclarationCentrale.getNomFormatCI(rec.getNomPrenom()));

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
                                        anneeString = anneeString.substring(2, 4);
                                        anneeNaissance = Integer.parseInt(anneeString);
                                    }

                                } else {
                                    anneeNaissance = 70;
                                }

                            }
                            //
                            // !!! Attention test basé sur le numéro AVS pour
                            // calculé l'age
                            //
                            if ((anneeNaissance + 1918) > Integer.parseInt(rec.getAnnee())) {
                                // test de l'age à partir du n°AVS
                                errors.add(getSession().getLabel("DT_ERR_AGE_MIN"));
                                breakTests = true;
                            } else {
                                if (CIDeclarationCentrale.isAvs0(noAvs)) {
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
                                    // erreur de création de CI
                                    info.add(getSession().getLabel("DT_NUM_AVS_INVALIDE"));
                                    breakTests = true;
                                }
                            }
                            if ("True".equals(accepteEcrituresNegatives) && !montantPositif) {
                                if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(getTransaction(),
                                        false).getRegistre())) {
                                    errors.add(getSession().getLabel("MSG_DT_INCONNU_ET_NEG"));
                                } else {

                                    try {
                                        if ("999999".equals(rec.getNumeroAffilie().substring(0, 6))) {

                                            if (!ecriture.getWrapperUtil().rechercheEcritureEmpApg(getTransaction(),
                                                    CIJournal.CS_ASSURANCE_CHOMAGE, true, true)) {
                                                errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                            }
                                        } else if ("6666666666".equals(rec.getNumeroAffilie())) {
                                            if (!ecriture.getWrapperUtil().rechercheEcritureEmpApg(getTransaction(),
                                                    CIJournal.CS_ASSURANCE_MILITAIRE, true, true)) {
                                                errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                            }
                                        } else if ("77777777777".equals(rec.getNumeroAffilie())) {
                                            if (!ecriture.getWrapperUtil().rechercheEcritureEmpApg(getTransaction(),
                                                    CIJournal.CS_APG, false, true)) {
                                                errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                            }
                                        } else if ("88888888888".equals(rec.getNumeroAffilie())) {
                                            if (!ecriture.getWrapperUtil().rechercheEcritureEmpApg(getTransaction(),
                                                    CIJournal.CS_IJAI, false, true)) {
                                                errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                            }
                                        }
                                    } catch (Exception e) {
                                        errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                    }
                                }
                            }

                            if (!breakTests) {
                                if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ecriture.getCI(getTransaction(),
                                        false).getRegistre())) {

                                    info.add(getSession().getLabel("DT_ASSURE_INCONNU"));

                                } else {

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
                                            if (ecriture
                                                    .isPeriodeDeCotisationACheval(getTransaction(), new JADate(clo))) {
                                                errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                                            } else {
                                                info.add(getSession().getLabel("DT_ECR_APRES_CLOTURE"));
                                            }

                                        } else {
                                            if (ecriture
                                                    .isPeriodeDeCotisationACheval(getTransaction(), new JADate(clo))) {
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
                                    ecriture.setNoSumNeeded(false);
                                    ecriture.add(getTransaction());
                                    getTransaction().disableSpy();
                                    // journal.updateInscription(getTransaction());
                                    // getTransaction().enableSpy();
                                    if (!getTransaction().hasErrors() && !isAborted()) {
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
                                // faire un rollback permettant d'effacer le CI
                                // temporaire éventuellement créés
                                getTransaction().rollback();
                            }
                            // -------------------------------------------------------------------------------
                            // met a jour les TreeMaps pour les summaries
                            // -------------------------------------------------------------------------------
                            _updateSummary(hNbrInscriptionsTraites, hMontantInscritionsTraites, hNbrInscriptionsErreur,
                                    hMontantInscriptionsErreur, hNbrInscriptionsSuspens, hMontantInscriptionsSuspens,
                                    hNbrInscriptionsCI, hMontantInscriptionsCI, nbrInscriptionsTraites,
                                    montantInscritionsTraites, nbrInscriptionsErreur, montantInscriptionsErreur,
                                    nbrInscriptionsSuspens, montantInscriptionsSuspens, nbrInscriptionsCI,
                                    montantInscriptionsCI, key);
                        } // fin du else 'journal trouvé'
                    } // fin du if 'numéro affilie'
                } // fin du if 'annee'
            } // fin du while principle
            if (modeInscription) {
                // maj des totaux des journaux
                Iterator<CIJournal> jourIt = tableJournaux.values().iterator();
                getTransaction().disableSpy();
                while (jourIt.hasNext()) {
                    CIJournal jnr = jourIt.next();
                    if (!JadeStringUtil.isIntegerEmpty(jnr.getIdJournal())) {
                        jnr.updateInscription(getTransaction());
                    }
                }
                getTransaction().enableSpy();
            }
            // ------------------------------------------------------------------------------
            // affichage / génération des rapports
            // ------------------------------------------------------------------------------
            _doLogJournauxExistant(hJournalExisteDeja); // des Journaux qui
            // existait dejà
            // CIDeclarationTextOutput doc = new CIDeclarationTextOutput();
            ICIDeclarationOutput doc = new CIDeclarationCentraleHTMLOutput();
            doc.setSession(getSession());
            doc.setSimulation(!modeInscription);
            doc.setData(tableLogAssure);

            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0161CCI");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, doc.getOutputFile());

            // CIDeclarationSummaryTextOutput docSummary = new
            // CIDeclarationSummaryTextOutput();
            ICIDeclarationOutput docSummary = new CIDeclarationCentraleSummaryHtmlOutput();
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
            docSummary.setData(params);
            JadePublishDocumentInfo docInfoSummary = createDocumentInfo();
            docInfoSummary.setDocumentType("0161CCI");
            docInfoSummary.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfoSummary, docSummary.getOutputFile());
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
                    getMemoryLog()
                            .logMessage(
                                    getSession().getLabel("DT_LOG_TOTALE_CTRL") + " : "
                                            + totalControleFormate.toStringFormat(), FWMessage.INFORMATION,
                                    "Déclaration");
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
        }

        return result;
    }

    /*
     * Cherche le journal à utiliser pour ce record Si le journal a déjà été ouvert dans ce process, on le réutilise
     * sinon on le crée (si modeInscription)
     */
    private CIJournal _findJournal(boolean modeInscription, CIDeclarationRecord rec,
            TreeMap<String, CIJournal> tableJournaux) throws Exception {
        // cle pour pouvoir stock un journal par affilié/année
        String key = _getKey(rec);

        if (tableJournaux.containsKey(key)) {
            // journal = (CIJournal) tableJournaux.get(key);
        } else {
            if (journal == null) {
                if ("999999".equals(rec.getNumeroAffilie().substring(0, 6))) {
                    journal = new CIJournal();
                    journal.setSession(getSession());
                    journal.setIdTypeInscription(CIJournal.CS_ASSURANCE_CHOMAGE);
                    journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
                    journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
                    journal.setTotalControle(getTotalControle());
                } else {
                    if ("6666666666".equals(rec.getNumeroAffilie())) {
                        journal = new CIJournal();
                        journal.setSession(getSession());
                        journal.setIdTypeInscription(CIJournal.CS_ASSURANCE_MILITAIRE);
                        journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
                        journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
                        journal.setTotalControle(getTotalControle());

                    }
                    if ("77777777777".equals(rec.getNumeroAffilie())) {
                        journal = new CIJournal();
                        journal.setSession(getSession());
                        journal.setIdTypeInscription(CIJournal.CS_APG);
                        journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
                        journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
                        journal.setTotalControle(getTotalControle());
                    }
                    if ("88888888888".equals(rec.getNumeroAffilie())) {
                        journal = new CIJournal();
                        journal.setSession(getSession());
                        journal.setIdTypeInscription(CIJournal.CS_IJAI);
                        journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
                        journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
                        journal.setTotalControle(getTotalControle());
                    }
                }

                try {
                    if (modeInscription) {
                        // mode inscription
                        journal.add(getTransaction());
                        if (getTransaction().hasErrors()) {
                            System.out.println("Erreur transaction :" + getTransaction().getErrors().toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    journal = null;
                }

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
        return CIUtil.unFormatAVS(rec.getNumeroAffilie() + "#" + rec.getAnnee());

    }

    /*
     * Met à jour les TreeMap utilisés pour les summary par affilié/Année
     */
    private void _updateSummary(TreeMap<String, Object> hNbrInscriptionsTraites,
            TreeMap<String, Object> hMontantInscritionsTraites, TreeMap<String, Object> hNbrInscriptionsErreur,
            TreeMap<String, Object> hMontantInscriptionsErreur, TreeMap<String, Object> hNbrInscriptionsSuspens,
            TreeMap<String, Object> hMontantInscriptionsSuspens, TreeMap<String, Object> hNbrInscriptionsCI,
            TreeMap<String, Object> hMontantInscriptionsCI, long nbrInscriptionsTraites,
            FWCurrency montantInscritionsTraites, long nbrInscriptionsErreur, FWCurrency montantInscriptionsErreur,
            long nbrInscriptionsSuspens, FWCurrency montantInscriptionsSuspens, long nbrInscriptionsCI,
            FWCurrency montantInscriptionsCI, String key) {
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
        value = (Long) hNbrInscriptionsTraites.get(key);
        hNbrInscriptionsTraites.put(key, new Long(value.longValue() + nbrInscriptionsTraites));
        value = (Long) hNbrInscriptionsErreur.get(key);
        hNbrInscriptionsErreur.put(key, new Long(value.longValue() + nbrInscriptionsErreur));
        value = (Long) hNbrInscriptionsSuspens.get(key);
        hNbrInscriptionsSuspens.put(key, new Long(value.longValue() + nbrInscriptionsSuspens));
        value = (Long) hNbrInscriptionsCI.get(key);
        hNbrInscriptionsCI.put(key, new Long(value.longValue() + nbrInscriptionsCI));
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
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @return
     */
    public String getAccepteEcrituresNegatives() {
        return accepteEcrituresNegatives;
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

    /**
     * Returns the nombreInscriptions.
     * 
     * @return String
     */
    public String getNombreInscriptions() {
        return nombreInscriptions;
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
        return type;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setAccepteEcrituresNegatives(String string) {
        accepteEcrituresNegatives = string;
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
        this.type = type;
    }

}
