package globaz.osiris.process;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 2 juin 04
 * 
 * @author scr
 * 
 */
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.api.process.APIProcessUpload;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.file.paiement.CAFileParserFactory;
import globaz.osiris.file.paiement.ICAFileParser;
import globaz.osiris.file.paiement.exception.LabelNameException;
import globaz.osiris.helpers.paiement.CAProcessPaiementHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Traitement d'un fichier BVR Date de création : (14.02.2002 16:09:43)
 * 
 * @author: scr
 */
public class CAProcessPaiementEtranger extends BProcess implements APIProcessUpload {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class FranceSpecialCase {

        public final static String FRANCE_CODE_EXTOURNE = "90";
        public final static String FRANCE_READ_REC = "0607";

        public String recordType = null;

        /**
         * Constructor for MadridSpecialCase.
         */
        public FranceSpecialCase() {
            super();
        }

        public boolean hasNull() {
            return (recordType == null);
        }
    }

    class MadridSpecialCase {

        public final static String MADRID_READ_REC = "6070";
        public final static String MADRID_STOP_REC1 = "8070";
        public final static String MADRID_STOP_REC2 = "9070";

        public BigDecimal montantTotRec1 = null;
        public BigDecimal montantTotRec2 = null;
        public String recordType = null;
        public BigDecimal sumTotal = null;

        /**
         * Constructor for MadridSpecialCase.
         */
        public MadridSpecialCase() {
            super();
        }

        public boolean hasNull() {
            return ((montantTotRec1 == null) || (montantTotRec2 == null) || (sumTotal == null) || (recordType == null));
        }
    }

    class SondrioSpecialCase {

        public final static String SONDRIO_READ_REC = "20";
        public final static String SONDRIO_STOP_REC = "90";

        public BigDecimal montantTotRec = null;
        public String recordType = null;
        public BigDecimal sumTotal = null;

        /**
         * Constructor for MadridSpecialCase.
         */
        public SondrioSpecialCase() {
            super();
        }

        public boolean hasNull() {
            return ((montantTotRec == null) || (sumTotal == null) || (recordType == null));
        }
    }

    public final static String CS_MADRID = "231002";
    public final static String CS_POSTE_FRANCAISE = "231003";
    public final static String CS_SONDRIO = "231001";

    private static final String FILE_NAME = "paiementEtranger.xml";

    private String csTypeFichier = null;
    private String dateValeur = new String();
    private boolean echoToConsole = false;
    private java.lang.String fileName = new String();
    private FranceSpecialCase franceSpecialCase = new FranceSpecialCase();
    private java.lang.String idOrganeExecution = new String();
    private String libelle = new String();

    private MadridSpecialCase madridSpecialCase = new MadridSpecialCase();

    private Boolean simulation = new Boolean(false);
    private SondrioSpecialCase sondrioSpecialCase = new SondrioSpecialCase();
    private String taux = new String();

    /**
     * Commentaire relatif au constructeur CAProcessBVR.
     */
    public CAProcessPaiementEtranger() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 11:12:27)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessPaiementEtranger(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // Vérifier l'ordre groupé
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            getMemoryLog().logMessage("5320", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Vérifier le nom du fichier
        if (JadeStringUtil.isBlank(getFileName())) {
            getMemoryLog().logMessage("5324", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        if (isFileNameBlank()) {
            this._addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
            return false;
        }

        // Date valeur = date du jour si vide
        if (JadeStringUtil.isBlank(getDateValeur()) || JadeStringUtil.isIntegerEmpty(getDateValeur())) {
            getMemoryLog().logMessage("5327", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        } else {
            // Vérifier la date de valeur
            try {
                globaz.globall.db.BSessionUtil.checkDateGregorian(
                        ((globaz.globall.db.BApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                .getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS)).getAnonymousSession(),
                        getDateValeur());
            } catch (Exception e) {

            }
        }

        // Sous controle d'exceptions
        try {

            processSaisieAutomatique();
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
        return getMemoryLog().hasErrors();
    }

    @Override
    protected void _validate() throws Exception {
        if (isFileNameBlank()) {
            this._addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
        }
    }

    /**
     * Method convertDate. converti une date sous forme : YYMMDD en : DD.MM.YYYY
     * 
     * @param date
     * @return String
     */
    private String convertDate(String date) {

        try {
            String dd = date.substring(0, 2);
            String mm = date.substring(2, 4);
            String yy = date.substring(4, 6);

            if (CAProcessPaiementEtranger.CS_SONDRIO.equals(csTypeFichier)) {
                yy = date.substring(0, 2);
                dd = date.substring(4, 6);
            }

            String yyyy = null;
            if ((new Integer(yy)).intValue() > 80) {
                yyyy = "19" + yy;
            } else {
                yyyy = "20" + yy;
            }

            return dd + "." + mm + "." + yyyy;
        } catch (Exception e) {
            return null;
        }
    }

    // Traitement des cas spéciaux
    private boolean doTreatementForFileValidationData(String fileType, String montant, ICAFileParser fileParser)
            throws Exception {

        // Traitement du cas spécial pour MADRID
        if (CAProcessPaiementEtranger.CS_MADRID.equals(fileType)) {
            madridSpecialCase.recordType = fileParser.getField("RECORD_TYPE");
            if (MadridSpecialCase.MADRID_STOP_REC1.equals(madridSpecialCase.recordType)) {
                madridSpecialCase.montantTotRec1 = new BigDecimal(montant);
                return true;
            } else if (MadridSpecialCase.MADRID_STOP_REC2.equals(madridSpecialCase.recordType)) {
                madridSpecialCase.montantTotRec2 = new BigDecimal(montant);
                return true;
            } else if (!MadridSpecialCase.MADRID_READ_REC.equals(madridSpecialCase.recordType)) {
                return true;
            }

            // sumMontant
            try {
                if (madridSpecialCase.sumTotal == null) {
                    madridSpecialCase.sumTotal = new BigDecimal(0);
                }

                madridSpecialCase.sumTotal = madridSpecialCase.sumTotal.add(new BigDecimal(montant));
            } catch (Exception e) {
            }

            return false;

        }

        // Traitement du cas spécial pour SONDRIO
        else if (CAProcessPaiementEtranger.CS_SONDRIO.equals(fileType)) {
            sondrioSpecialCase.recordType = fileParser.getField("RECORD_TYPE");
            if (SondrioSpecialCase.SONDRIO_STOP_REC.equals(sondrioSpecialCase.recordType)) {
                String montantTot = fileParser.getField("MONTANT_TOTAL");
                montantTot = montantTot.replace(',', '.');
                sondrioSpecialCase.montantTotRec = new BigDecimal(montantTot);
                return true;
            }
            // sumMontant
            try {
                if (sondrioSpecialCase.sumTotal == null) {
                    sondrioSpecialCase.sumTotal = new BigDecimal(0);
                }

                sondrioSpecialCase.sumTotal = sondrioSpecialCase.sumTotal.add(new BigDecimal(montant));
            } catch (Exception e) {
            }

            return false;
        }

        // //Traitement du cas spécial pour SONDRIO
        // else if (CS_SONDRIO.equals(fileType)) {
        // sondrioSpecialCase.recordType = fileParser.getField("RECORD_TYPE");
        //
        // if
        // (SondrioSpecialCase.SONDRIO_STOP_REC.equals(sondrioSpecialCase.recordType))
        // {
        // String montantTot = fileParser.getField("MONTANT_TOTAL");
        // montantTot = montantTot.substring(0,
        // montantTot.length()-2)+"."+montantTot.substring(montantTot.length()-2,
        // montantTot.length());
        // sondrioSpecialCase.montantTotRec = new BigDecimal(montantTot);
        // return true;
        // }
        // else if
        // (!sondrioSpecialCase.SONDRIO_READ_REC.equals(sondrioSpecialCase.recordType))
        // {
        // return true;
        // }
        //
        // //sumMontant
        // try {
        // if (sondrioSpecialCase.sumTotal==null)
        // sondrioSpecialCase.sumTotal = new BigDecimal(0);
        //
        // sondrioSpecialCase.sumTotal = sondrioSpecialCase.sumTotal.add(new
        // BigDecimal(montant));
        // } catch (Exception e) {}
        // finally {return false;}
        // }

        // Traitement du cas spécial pour Poste francaise
        else if (CAProcessPaiementEtranger.CS_POSTE_FRANCAISE.equals(fileType)) {
            franceSpecialCase.recordType = fileParser.getField("RECORD_TYPE");
            if (!FranceSpecialCase.FRANCE_READ_REC.equals(franceSpecialCase.recordType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the csTypeFichier.
     * 
     * @return String
     */
    public String getCsTypeFichier() {
        return csTypeFichier;
    }

    /**
     * Returns the dateValeur.
     * 
     * @return String
     */
    @Override
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:57:24)
     * 
     * @return boolean
     */
    @Override
    public boolean getEchoToConsole() {
        return echoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            if (getSimulation().booleanValue()) {
                obj = getSession().getLabel("PMT_ETRANGER_MAIL_SIMUL_ERROR");
            } else {
                obj = getSession().getLabel("PMT_ETRANGER_MAIL_ERROR");
            }
        } else if (getSimulation().booleanValue()) {
            obj = getSession().getLabel("PMT_ETRANGER_MAIL_SIMUL_OK");
        } else {
            obj = getSession().getLabel("PMT_ETRANGER_MAIL_OK");
        }

        // Restituer l'objet
        return obj;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:59:41)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getFileName() {
        return fileName;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 16:10:31)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Returns the libelle.
     * 
     * @return String
     */
    @Override
    public String getLibelle() {
        return libelle;
    }

    /**
     * Returns the simulation.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getSimulation() {
        return simulation;
    }

    /**
     * Returns the taux.
     * 
     * @return String
     */
    public String getTaux() {
        return taux;
    }

    /**
     * Le nom de fichier est-il vide ?
     * 
     * @return
     */
    private boolean isFileNameBlank() {
        return JadeStringUtil.isBlank(getFileName());
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour ou de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void processSaisieAutomatique() throws Exception {

        // Do traitement here
        ICAFileParser fileParser = null;
        if (CAProcessPaiementEtranger.CS_SONDRIO.equals(csTypeFichier)) {
            fileParser = CAFileParserFactory.newInstance(CAProcessPaiementEtranger.FILE_NAME, "SONDRIO");
        } else if (CAProcessPaiementEtranger.CS_MADRID.equals(csTypeFichier)) {
            fileParser = CAFileParserFactory.newInstance(CAProcessPaiementEtranger.FILE_NAME, "MADRID");
        } else if (CAProcessPaiementEtranger.CS_POSTE_FRANCAISE.equals(csTypeFichier)) {
            fileParser = CAFileParserFactory.newInstance(CAProcessPaiementEtranger.FILE_NAME, "POSTE_FRANCAISE");
        } else {
            throw new Exception("Type de fichier inconnu");
        }

        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFileName(), Jade
                .getInstance().getHomeDir() + "work/" + getFileName());

        fileParser.setSource(Jade.getInstance().getHomeDir() + "work/" + getFileName());
        List errorMsg = new ArrayList();

        CAJournal journal = null;
        boolean isJournalCreated = false;

        int counter = 0;
        int nbErrors = 0;
        String noAvs = "";
        boolean isFileOnError = false;

        String codeIsoMonnaie = fileParser.getCodeIsoMonnaie();

        getMemoryLog().logMessage(getSession().getLabel("PMT_ETRANGER_FILE_NAME") + " " + getFileName(),
                FWMessage.INFORMATION, this.getClass().getName());
        while (fileParser.hasNext()) {
            String codeDebitCredit = APIEcriture.CREDIT;
            fileParser.goToNextRecord();

            String montant = null;

            montant = fileParser.getField("MONTANT");

            // Le fichier italie contient un montant au format : NNNNNNN,CC
            // Les autres fichiers sont aux format : NNNNNNCC
            if (CAProcessPaiementEtranger.CS_SONDRIO.equals(csTypeFichier)) {
                try {
                    montant = montant.replace(',', '.');
                } catch (Exception e) {
                    montant = null;
                }
            } else {
                try {
                    montant = montant.substring(0, montant.length() - 2) + "."
                            + montant.substring(montant.length() - 2, montant.length());
                } catch (Exception e) {
                    montant = null;
                }
            }
            if (CAProcessPaiementEtranger.CS_POSTE_FRANCAISE.equals(csTypeFichier)) {
                if (FranceSpecialCase.FRANCE_CODE_EXTOURNE.equals(fileParser.getField("CODE_DEBIT_CREDIT"))) {
                    codeDebitCredit = APIEcriture.DEBIT;
                }
            }

            // Traitement des cas spéciaux
            if (doTreatementForFileValidationData(csTypeFichier, montant, fileParser)) {
                continue;
            }

            counter++;
            // La date est sous forme : YYMMDD
            // La transformer en DD.MM.YYYY

            String date = convertDate(fileParser.getField("DATE"));
            noAvs = fileParser.getField("NO_AVS");

            boolean isError = false;

            CAProcessPaiementHelper helper = new CAProcessPaiementHelper(getSession(), getTransaction());

            // Mode SIMULATION ON
            if (getSimulation().booleanValue()) {
                try {
                    helper.validateDate(date);
                } catch (LabelNameException lne) {
                    isError = true;
                    errorMsg.add(getSession().getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
                } catch (Exception e) {
                    isError = true;
                    errorMsg.add(e.getMessage());
                }

                try {
                    helper.validateMontant(montant);
                    helper.validateMontant(taux);
                } catch (LabelNameException lne) {
                    isError = true;
                    errorMsg.add(getSession().getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
                }

                try {
                    helper.validateNoAvs(noAvs, true, false);
                } catch (LabelNameException lne) {
                    isError = true;
                    String msg = getSession().getLabel(lne.getLabel());
                    if (CAProcessPaiementEtranger.CS_SONDRIO.equals(csTypeFichier)) {
                        try {
                            msg += " " + fileParser.getField("NOM_ASSURE");
                        } catch (Exception e) {
                        }
                        ;
                    }
                    errorMsg.add(msg + " " + lne.getExtraMessage());
                } catch (Exception e) {
                    isError = true;
                    errorMsg.add(e.getMessage());
                }
                if (isError) {
                    errorMsg.add("File record = " + fileParser.getDumpFileRecord());
                }
            }

            // Mode SIMULATION OFF
            else {

                FWCurrency montantME = null;
                FWCurrency montantCHF = null;
                CACompteAnnexe compteAnnexe = null;

                try {
                    helper.validateDate(date);
                } catch (LabelNameException lne) {
                    isError = true;
                    date = null;
                    errorMsg.add(getSession().getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
                } catch (Exception e) {
                    isError = true;
                    date = null;
                    errorMsg.add(e.getMessage());
                }

                try {
                    montantME = helper.validateMontant(montant);
                    montantME = new FWCurrency(JANumberFormatter.format(montantME.toString(), 0.01, 2,
                            JANumberFormatter.NEAR));

                    helper.validateMontant(getTaux());
                    BigDecimal tauxConversion = new BigDecimal(getTaux());

                    BigDecimal montantTmp = montantME.getBigDecimalValue();
                    montantTmp = montantTmp.multiply(tauxConversion);
                    montantCHF = new FWCurrency(JANumberFormatter.format(montantTmp.toString(), 0.01, 2,
                            JANumberFormatter.NEAR));
                } catch (LabelNameException lne) {
                    isError = true;
                    montantME = null;
                    montantCHF = null;
                    errorMsg.add(getSession().getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
                }

                try {
                    compteAnnexe = helper.validateNoAvs(noAvs, true, false);
                } catch (Exception e) {
                    compteAnnexe = null;
                    isError = true;
                    if (e instanceof LabelNameException) {
                        String msg = getSession().getLabel(((LabelNameException) e).getLabel());
                        if (CAProcessPaiementEtranger.CS_SONDRIO.equals(csTypeFichier)) {
                            try {
                                msg += " " + fileParser.getField("NOM_ASSURE");
                            } catch (Exception exc) {
                            }
                            ;
                        }
                        errorMsg.add(msg + " " + ((LabelNameException) e).getExtraMessage());
                    } else {
                        errorMsg.add(e.getMessage());
                    }
                }

                try {
                    helper.validateCompteAnnexeIsASurveillerEstVerrouille(compteAnnexe);
                } catch (Exception e) {
                    errorMsg.add(e.getMessage());
                }

                if (!isJournalCreated) {
                    journal = new CAJournal();
                    journal.setSession(getSession());
                    journal.setDateValeurCG(dateValeur);
                    if (JadeStringUtil.isBlank(libelle)) {
                        journal.setLibelle(getSession().getLabel("PMT_ETRANGER_LIBELLE_JOURNAL_AUTO"));
                    } else {
                        journal.setLibelle(libelle);
                    }
                    journal.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);
                    journal.add(getTransaction());

                    // Création du journal
                    isJournalCreated = true;
                }
                helper.addPaiementEtranger(journal.getIdJournal(), compteAnnexe, noAvs, montantCHF == null ? null
                        : montantCHF.toString(), montantME == null ? null : montantME.toString(), taux, codeIsoMonnaie,
                        codeDebitCredit, date, idOrganeExecution, fileParser.getDumpFileRecord());
            }

            if (isError) {
                nbErrors++;
            }

            if (errorMsg.size() > 0) {
                getMemoryLog().logMessage(getSession().getLabel("PMT_ETRANGER_ERROR_TITLE") + " " + noAvs,
                        FWMessage.INFORMATION, this.getClass().getName());
                for (Iterator iter = errorMsg.iterator(); iter.hasNext();) {
                    String msg = (String) iter.next();
                    getMemoryLog().logMessage(msg, FWMessage.FATAL, this.getClass().getName());
                }
                getMemoryLog().logMessage("File record = " + fileParser.getDumpFileRecord(),
                        FWViewBeanInterface.WARNING, this.getClass().getName());
                getMemoryLog().logMessage(" ", FWMessage.INFORMATION, this.getClass().getName());
                errorMsg.clear();
            }
        }
        // end while
        // Validate file data
        try {
            validateFileData(csTypeFichier);
        } catch (LabelNameException lne) {
            // Le contenu des fichiers n'est pas valide -> on ne comptabilise
            // pas le journal.
            isFileOnError = true;
            getMemoryLog().logMessage(getSession().getLabel(lne.getLabel()) + " " + lne.getExtraMessage(),
                    FWMessage.FATAL, this.getClass().getName());
        }

        if (isJournalCreated) {
            // comptabilisation du journal; condition : le journal doit avoir
            // été préalablement créé
            // et aucune erreur détectée. Si des erreurs ont été identifiée,
            // l'utilisateur devra le comptabiliser lui-même
            if ((nbErrors == 0) && !isFileOnError) {
                // journal.comptabiliser(this);
                journal.setEtat(CAJournal.OUVERT);
                journal.update(getTransaction());
            } else {
                journal.setEtat(CAJournal.ERREUR);
                journal.update(getTransaction());
            }

            if (journal.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), journal.getClass().getName());
                getMemoryLog().logMessage("5600", null, FWMessage.FATAL, this.getClass().getName());
            }
        }
        getMemoryLog().logMessage(getSession().getLabel("PMT_ETRANGER_NBR_TRAITE") + String.valueOf(counter),
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("PMT_ETRANGER_NBR_ERROR") + String.valueOf(nbErrors),
                FWMessage.INFORMATION, this.getClass().getName());
        if ((nbErrors != 0) && isJournalCreated) {
            getMemoryLog().logMessage(getSession().getLabel("PMT_ETRANGER_JRN_NON_COMPTABILISER"),
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }

    }

    /**
     * Sets the csTypeFichier.
     * 
     * @param csTypeFichier
     *            The csTypeFichier to set
     */
    public void setCsTypeFichier(String csTypeFichier) {
        this.csTypeFichier = csTypeFichier;
    }

    /**
     * Sets the dateValeur.
     * 
     * @param dateValeur
     *            The dateValeur to set
     */
    @Override
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:57:24)
     * 
     * @param newEchoToConsole
     *            boolean
     */
    @Override
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 10:59:41)
     * 
     * @param newFileName
     *            java.lang.String
     */
    @Override
    public void setFileName(java.lang.String newFileName) {
        fileName = newFileName;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 16:10:31)
     * 
     * @param newOrganeExecution
     *            java.lang.String
     */
    @Override
    public void setIdOrganeExecution(java.lang.String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    /**
     * Sets the libelle.
     * 
     * @param libelle
     *            The libelle to set
     */
    @Override
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Sets the simulation.
     * 
     * @param simulation
     *            The simulation to set
     */
    @Override
    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    /**
     * Sets the taux.
     * 
     * @param taux
     *            The taux to set
     */
    public void setTaux(String taux) {
        this.taux = taux;
    }

    public JADate validateDate(String date) throws Exception, LabelNameException {

        // Contrôle validation de la date
        JACalendar cal = new JACalendarGregorian();
        JADate jdate = new JADate(date);
        // La date n'est pas valide
        if (!cal.isValid(jdate)) {
            throw new LabelNameException("PMT_ETRANGER_DATE_ERROR", date);
        }

        return jdate;
    }

    private void validateFileData(String csTypeFichier) throws LabelNameException {
        if (CAProcessPaiementEtranger.CS_MADRID.equals(csTypeFichier)) {
            // File validation
            if (madridSpecialCase.hasNull()) {
                throw new LabelNameException("PMT_ETRANGER_FILE_VALIDATION_ERROR", "");
            }
            if (madridSpecialCase.montantTotRec1.compareTo(madridSpecialCase.montantTotRec2) != 0) {
                throw new LabelNameException("PMT_ETRANGER_FILE_VALIDATION_TOT_AMOUNT_ERROR", "");
            }
            if (madridSpecialCase.montantTotRec1.compareTo(madridSpecialCase.sumTotal) != 0) {
                throw new LabelNameException("PMT_ETRANGER_FILE_VALIDATION_SUM_AMOUNT_ERROR", "");
            }
        } else if (CAProcessPaiementEtranger.CS_SONDRIO.equals(csTypeFichier)) {
            // File validation
            if (sondrioSpecialCase.hasNull()) {
                throw new LabelNameException("PMT_ETRANGER_FILE_VALIDATION_ERROR", "");
            }
            if (sondrioSpecialCase.montantTotRec.compareTo(sondrioSpecialCase.sumTotal) != 0) {
                throw new LabelNameException("PMT_ETRANGER_FILE_VALIDATION_SUM_AMOUNT_ERROR", "");
            }
        }
        return;
    }

    @Override
    public String getIdYellowReportFile() {
        return "";
    }

    @Override
    public void setIdYellowReportFile(String id) {
        // Rien a faire
    }

}
