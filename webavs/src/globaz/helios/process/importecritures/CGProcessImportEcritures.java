package globaz.helios.process.importecritures;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.db.utils.CGUtils;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ch.horizon.jaspe.util.JADate;

public class CGProcessImportEcritures extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_GLOBAL_EXERCICE_INEXISTANT = "GLOBAL_EXERCICE_INEXISTANT";
    private static final String LABEL_JOURNAL_CHAMP_LIB_ERROR = "JOURNAL_CHAMP_LIB_ERROR";
    private StringBuilder bodyMail;
    private String dateTraitement;
    private String directoryFile;
    private String fileName;
    private String idExerciceComptable;
    private String libelleJournal;
    Map<String, CGJournal> mandatJournal = new HashMap<String, CGJournal>();

    private StringBuffer messageTransaction = new StringBuffer();

    Map<String, FWCurrency> totalJournal = new HashMap<String, FWCurrency>();

    private Boolean traitementSchedule = null;

    /**
     * Constructor for CGProcessImportEcritures.
     */
    public CGProcessImportEcritures() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param parent
     */
    public CGProcessImportEcritures(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param session
     */
    public CGProcessImportEcritures(BSession session) throws Exception {
        super(session);
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
    protected boolean _executeProcess() throws Exception {

        try {
            return executeImportations(getFileName());
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        // Test des champs si mode de chargement via écran
        // En mode automatique ces champs son repris selon la balise <importEcriture>
        if (getTraitementSchedule().equals(Boolean.FALSE)) {
            if (JadeStringUtil.isBlank(getLibelleJournal())) {
                this._addError(getTransaction(),
                        getSession().getLabel(CGProcessImportEcritures.LABEL_JOURNAL_CHAMP_LIB_ERROR));
            }
            if (JadeStringUtil.isIntegerEmpty(getIdExerciceComptable())) {
                this._addError(getTransaction(),
                        getSession().getLabel(CGProcessImportEcritures.LABEL_GLOBAL_EXERCICE_INEXISTANT));
            }
            if (isFileNameBlank()) {
                this._addError(getTransaction(), getSession().getLabel("FILE_NOT_FOUND"));
            }
        }
    }

    /**
     * Ajout des écritures collective.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addEcrituresCollective(Document doc) throws Exception {
        NodeList elements = CGImportEcrituresUtils.getEcrituresCollective(doc);
        CGJournal journal = null;
        for (int i = 0; i < elements.getLength(); i++) {
            Element ecritureCollective = (Element) elements.item(i);

            try {
                String idExercice = "";
                // Recherche si journal déjà créé
                String numMandat = CGImportEcrituresUtils.getMandat(getSession(), ecritureCollective,
                        getIdExerciceComptable());
                CGMandat mandat = retrieveMandat(numMandat);
                if (mandat.isNew()) {
                    this._addError(getSession().getLabel("GLOBAL_MANDAT_INEXISTANT"));
                } else {
                    if (mandatJournal.containsKey(numMandat)) {
                        journal = mandatJournal.get(numMandat);
                    } else {
                        setDateTraitement(findDateValeur(ecritureCollective));
                        // IdExercice = id exercice sélectionné si process lancé depuis l'écran
                        // sinon dépend des valeurs du fichier xml (mandat + période)
                        idExercice = fillIdExercice(mandat);
                        // Création du journal
                        journal = createJournal(doc, numMandat, idExercice);
                        // Sauvegarde pour optimisation
                        mandatJournal.put(numMandat, journal);
                    }
                    if ((journal != null) && (getTransaction().hasErrors() == false)) {
                        createEcritureCollective(ecritureCollective, journal, mandat);
                        // Sauvegarde du total pour le journal
                        saveTotalJournal(ecritureCollective, mandat.getLibelle());
                    }
                }
                if (getTransaction().hasErrors()) {
                    messageTransaction.append("Error line id=" + CGImportEcrituresUtils.getId(ecritureCollective)
                            + " - " + getTransaction().getErrors() + "\n");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                }
            } catch (Exception e) {
                messageTransaction.append("Error line id=" + CGImportEcrituresUtils.getId(ecritureCollective) + " - "
                        + e.getMessage() + "\n");
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            } finally {
                for (Iterator<?> iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                    messageTransaction.append(iter.next() + "\n");
                }
                getMemoryLog().clear();
            }
        }
    }

    /**
     * Ajout des nouvelles écritures double.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @param journal
     * @throws Exception
     */
    private void addEcrituresDouble(Document doc) throws Exception {
        NodeList elements = CGImportEcrituresUtils.getEcrituresDouble(doc);
        CGJournal journal = null;
        for (int i = 0; i < elements.getLength(); i++) {
            Element ecritureDouble = (Element) elements.item(i);
            try {
                String idExercice = "";
                // Recherche si journal déjà créé
                String numMandat = CGImportEcrituresUtils.getMandat(getSession(), ecritureDouble,
                        getIdExerciceComptable());
                // Recherche du mandat
                CGMandat mandat = retrieveMandat(numMandat);
                if (mandat.isNew()) {
                    this._addError(getTransaction(), getSession().getLabel("GLOBAL_MANDAT_INEXISTANT"));
                } else {
                    if (mandatJournal.containsKey(numMandat)) {
                        journal = mandatJournal.get(numMandat);
                    } else {
                        setDateTraitement(findDateValeur(ecritureDouble));
                        // IdExercice = id exercice sélectionné si process lancé depuis l'écran
                        // sinon dépend des valeurs du fichier xml (mandat + période)
                        idExercice = fillIdExercice(mandat);
                        // Création du journal
                        journal = createJournal(doc, numMandat, idExercice);
                        // Sauvegarde pour optimisation
                        mandatJournal.put(numMandat, journal);
                    }
                    if ((journal != null) && (getTransaction().hasErrors() == false)) {
                        CGGestionEcritureViewBean ecritures = createEcritureDouble(ecritureDouble, journal, mandat);
                        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);
                        // Sauvegarde du total pour le journal
                        saveTotalJournal(ecritureDouble, mandat.getLibelle());
                    }
                }
                if (getTransaction().hasErrors()) {
                    messageTransaction.append("Error line id=" + CGImportEcrituresUtils.getId(ecritureDouble) + " - "
                            + getTransaction().getErrors() + "\n");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                }
            } catch (Exception e) {
                messageTransaction.append("Error line id=" + CGImportEcrituresUtils.getId(ecritureDouble) + " - "
                        + e.getMessage() + "\n");
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            } finally {
                for (Iterator<?> iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                    messageTransaction.append(iter.next() + "\n");
                }
                getMemoryLog().clear();
            }
        }
    }

    /**
     * Ajout de l'entête et des différentes écritures de l'écriture collective spécifiée dans le doc XML.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @throws Exception
     */
    private void createEcritureCollective(Element e, CGJournal journal, CGMandat mandat) throws Exception {
        NodeList elements = CGImportEcrituresUtils.getEcritures(e);

        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());

        ecritures.setDateValeur(CGImportEcrituresUtils.getDateEntete(e, journal));
        ecritures.setIdJournal(journal.getIdJournal());
        ecritures.setPiece(CGImportEcrituresUtils.getPieceEntete(e));

        ArrayList<CGEcritureViewBean> ecrituresList = new ArrayList<CGEcritureViewBean>();

        for (int i = 0; i < elements.getLength(); i++) {
            Element elementEcriture = (Element) elements.item(i);

            CGEcritureViewBean ecriture = new CGEcritureViewBean();
            setChampEcritureCollective(journal, elementEcriture, ecriture, mandat);

            ecrituresList.add(ecriture);
        }

        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);
    }

    /**
     * Créer l'écriture double en fonction de l'élément xml.
     * 
     * @param session
     * @param transaction
     * @param e
     * @param journal
     * @return
     * @throws Exception
     */
    private CGGestionEcritureViewBean createEcritureDouble(Element e, CGJournal journal, CGMandat mandat)
            throws Exception {

        ArrayList<CGEcritureViewBean> ecrituresList = new ArrayList<CGEcritureViewBean>();
        CGEcritureViewBean ecritureCrebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();
        // Set écriture de crédit
        setChampEcritureDouble(e, ecritureCrebit, true, mandat, journal);
        fillChampCommunEcriture(journal, e, ecritureCrebit, journal.getIdExerciceComptable());
        // Set écriture de débit
        setChampEcritureDouble(e, ecritureDebit, false, mandat, journal);
        fillChampCommunEcriture(journal, e, ecritureDebit, journal.getIdExerciceComptable());

        ecrituresList.add(ecritureCrebit);
        ecrituresList.add(ecritureDebit);
        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());
        ecritures.setDateValeur(CGImportEcrituresUtils.getDate(e, journal));
        ecritures.setIdJournal(journal.getIdJournal());
        ecritures.setPiece(CGImportEcrituresUtils.getPiece(e));
        ecritures.setEcritures(ecrituresList);
        ecritures.setJournal(journal);
        return ecritures;
    }

    /**
     * Créer le journal qui contiendra les nouvelles écritures.
     * 
     * @param session
     * @param transaction
     * @param doc
     * @return
     * @throws Exception
     */
    private CGJournal createJournal(Document doc, String mandat, String idExercice) throws Exception {
        CGJournal journal = new CGJournal();
        if ((idExercice == null) || (JadeStringUtil.isBlankOrZero(idExercice) == true)) {
            journal = null;
        } else {
            journal.setSession(getSession());
            if (getTraitementSchedule().equals(Boolean.TRUE)) {
                String libelle = doc.getDocumentElement().getAttribute(CGImportEcrituresUtils.TAG_LIBELLE_JOURNAL);
                if (JadeStringUtil.isEmpty(libelle) == false) {
                    journal.setLibelle(libelle);
                }
            } else {
                journal.setLibelle(getLibelleJournal());
            }
            journal.setIdExerciceComptable(idExercice);
            journal.setDateValeur(getDateTraitement());
            if (JadeStringUtil.isBlank(getDateTraitement())) {
                journal.setDateValeur(JACalendar.todayJJsMMsAAAA());
            }
            journal.setDate(JACalendar.todayJJsMMsAAAA());
            journal.setIdPeriodeComptable(retrieveIdPeriodeComptable(journal.getIdExerciceComptable()));
            journal.setEstPublic(new Boolean(false));
            journal.setEstConfidentiel(new Boolean(false));
            journal.setIdTypeJournal(CGJournal.CS_TYPE_MANUEL); // ????
            journal.setProprietaire(getSession().getUserId());

            journal.add(getTransaction());
        }
        return journal;
    }

    /**
     * Exécution de l'importation des écritures en fonction du fichier XML.
     * 
     * @param session
     * @param transaction
     * @param sourceFile
     * @return
     * @throws Exception
     */
    private boolean executeImportations(String sourceFile) throws Exception {
        boolean success = false;
        Document doc = retrieveDocument(sourceFile);
        if (doc != null) {
            addEcrituresDouble(doc);
            addEcrituresCollective(doc);
        }
        // Affichage du protocole
        // Set<String> key = ;
        // On envoi le mail avec les problemes si il y en a.
        if (messageTransaction.length() != 0) {
            CGUtils.addMailInformationsError(getMemoryLog(), messageTransaction.toString(), "",
                    getSession().getLabel("PB_FILE").trim() + " " + sourceFile);
            success = false;
        } else {
            success = true;
            // Impression du protocole: Nombre total par mandat.
            imprimerProtocoleDansMail();
        }
        // Si traitement schedule => déplacement du fichier traité
        if (getTraitementSchedule()) {
            return moveFileProcessed(success);
        }

        return success;
    }

    /**
     * Initialisation des champs communs aux différentes types d'écriture
     * 
     * @param journal
     * @param elementEcriture
     * @param ecriture
     * @param idExercice
     * @throws Exception
     */
    protected void fillChampCommunEcriture(CGJournal journal, Element elementEcriture, CGEcritureViewBean ecriture,
            String idExercice) throws Exception {
        // Initialisation des champs communs aux différentes types d'écriture
        ecriture.setSession(getSession());
        ecriture.setLibelle(CGImportEcrituresUtils.getLibelle(elementEcriture));
        ecriture.setMontant(CGImportEcrituresUtils.getMontant(elementEcriture));
        ecriture.setMontantMonnaie(CGImportEcrituresUtils.getMontantEtranger(elementEcriture));
        ecriture.setCoursMonnaie(CGImportEcrituresUtils.getCours(elementEcriture));
        ecriture.setIdExerciceComptable(idExercice);
        ecriture.setIdMandat(journal.getExerciceComptable().getIdMandat());
        ecriture.setIdJournal(journal.getIdJournal());
        ecriture.setDate(CGImportEcrituresUtils.getDate(elementEcriture, journal));
        ecriture.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(getSession(), ecriture));
    }

    protected String fillIdExercice(CGMandat mandat) throws Exception {
        // String idExercice="";;
        // if (this.getTraitementSchedule().equals(Boolean.TRUE)) {
        // Dépend du mandat et de la période
        // idExercice = this.retrieveIdExerciceComptable(mandat.getIdMandat());
        // } else {
        // Id sélectionné depuis l'écran (maode manuel)
        // idExercice = this.getIdExerciceComptable();
        // }
        // return idExercice;
        return retrieveIdExerciceComptable(mandat.getIdMandat());
    }

    protected String findDateValeur(Element element) {
        String dateValeur = "";
        try {
            dateValeur = CGImportEcrituresUtils.getDateValeur(element);
            if (JadeStringUtil.isBlankOrZero(dateValeur) == true) {
                dateValeur = getDateTraitement();
            }
        } catch (Exception e) {
            dateValeur = "";
        }
        // Si date de valeur non renseigné dans le fichier XML => prendre la date défini dans le nom de fichier
        try {
            if (JadeStringUtil.isEmpty(dateValeur)) {
                // la date se trouve après le 2ème _ dans le nom de fichier
                if (JadeStringUtil.isEmpty(getFileName()) == false) {
                    int beginIndexDate = getFileName().indexOf("_", getFileName().indexOf("_") + 1) + 1;
                    int endIndexDate = beginIndexDate + 10;
                    dateValeur = getFileName().substring(beginIndexDate, endIndexDate);
                }
                dateValeur = new JADate(dateValeur).toStr(".");
            } else {
                dateValeur = new JADate(dateValeur).toStr(".");
            }
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("JOURNAL_DATE_VALEUR_INVALID") + dateValeur);
        }
        return dateValeur;
    }

    /**
     * @return Returns the dateTraitement.
     */
    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getDirectoryFile() {
        return directoryFile;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if ((messageTransaction.length() > 0) || isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("IMPORT_ECRITURES_ERROR") + " " + fileName;
        } else {
            return getSession().getLabel("IMPORT_ECRITURES_OK") + " " + fileName;
        }
    }

    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return Returns the idExerciceComptable.
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * @return Returns the libelleJournal.
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    @Override
    public String getSubjectDetail() {
        if (bodyMail == null) {
            return super.getSubjectDetail();
        } else {
            return bodyMail.toString();
        }
    }

    public Boolean getTraitementSchedule() {
        return traitementSchedule;
    }

    /**
     * Imprime le total par mandat dans l'email
     */
    protected void imprimerProtocoleDansMail() {
        try {
            for (Map.Entry<String, FWCurrency> totalJournalEntry : totalJournal.entrySet()) {
                if (bodyMail == null) {
                    bodyMail = new StringBuilder();
                    bodyMail.append(getSession().getLabel("TOTAL_MANDAT") + fileName + "\n\n");
                }
                FWCurrency total = totalJournalEntry.getValue();
                String infoAAfficher = totalJournalEntry.getKey() + "&nbsp;\t\tTotal: " + total.toString() + "\n";
                bodyMail.append(infoAAfficher);

            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "");
        }
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
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Déplacement du fichier traité dans le répertoire adéquate (soit OK ou Traite)
     * 
     * @param success
     * @return
     */
    protected boolean moveFileProcessed(boolean success) {
        String directoryFileToSend = "";
        if (success) {
            directoryFileToSend = getDirectoryFile() + "Traite/";
        } else {
            directoryFileToSend = getDirectoryFile() + "KO/";
        }
        File file = new File(directoryFileToSend);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            // Copie du fichier traité dans le répertoire adéquate (répertoire différent en fonction de
            // successfulExecution)
            JadeFsFacade.copyFile(getDirectoryFile() + fileName, directoryFileToSend + fileName);
            JadeFsFacade.delete(getDirectoryFile() + fileName);
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), "Error export file: " + e.toString());
            return false;
        }
    }

    /**
     * Extrait et Parse le fichier passé en paramètre
     * 
     * @param sourceFileName
     * @return
     * @throws Exception
     */
    private Document retrieveDocument(String sourceFileName) {
        try {
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + sourceFileName, Jade
                    .getInstance().getHomeDir() + "work/" + sourceFileName);

            return JadeXmlReader.parseFile(new FileInputStream(Jade.getInstance().getHomeDir() + "work/"
                    + sourceFileName));
        } catch (Exception e) {
            messageTransaction.append(e.getMessage() + "\n");
            return null;
        }
    }

    protected String retrieveIdExerciceComptable(String idMandat) throws Exception {
        CGExerciceComptableManager exMng = new CGExerciceComptableManager();
        exMng.setSession(getSession());
        exMng.setForIdMandat(idMandat);
        exMng.setBetweenDateDebutDateFin(getDateTraitement());
        exMng.find();
        if (exMng.getSize() != 1) {
            // Si exercice non trouvé selon fichier, prendre l'idexerice de l'écran
            if (!JadeStringUtil.isBlankOrZero(getIdExerciceComptable())) {
                return getIdExerciceComptable();
            } else {
                this._addError(getTransaction(), getSession().getLabel("EXER_COMPTABLE_AUCUN_EXERCICE_COMPTABLE")
                        .trim()
                        + ": "
                        + getSession().getLabel("MANDAT")
                        + " "
                        + idMandat
                        + getSession().getLabel("AU")
                        + getDateTraitement());
                return null;
            }
        } else {
            return ((CGExerciceComptable) exMng.getFirstEntity()).getIdExerciceComptable();
        }
    }

    /**
     * Retourne la période en cours de l'exercice comptable à une date donnée.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private String retrieveIdPeriodeComptable(String idExercice) throws Exception {
        if (JadeStringUtil.isEmpty(idExercice)) {
            return null;
        }
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(idExercice);
        manager.setForPeriodeOuverte(true);

        String date = getDateTraitement();
        if (JadeStringUtil.isBlank(date)) {
            date = JACalendar.todayJJsMMsAAAA();
        }
        manager.setForDateInPeriode(date);
        manager.find(getTransaction());
        if (manager.isEmpty()) {
            this._addError(getTransaction(), getSession().getLabel("PERIODE_COMPTABLE_NO_PERIODE_MATCH") + " - "
                    + idExercice + " - " + date);
            return null;
        }
        return ((CGPeriodeComptable) manager.getFirstEntity()).getIdPeriodeComptable();
    }

    protected CGMandat retrieveMandat(String numMandat) throws Exception {
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(numMandat);
        mandat.retrieve();
        return mandat;
    }

    /**
     * Sauvegarde du total par journal - Utile pour reprendre dans le protocole
     * 
     * @param ecritureCollective
     * @param numMandat
     * @throws Exception
     */
    protected void saveTotalJournal(Element ecritureCollective, String numMandat) throws Exception {
        FWCurrency currency = new FWCurrency();
        if (totalJournal.containsKey(numMandat)) {
            currency = totalJournal.get(numMandat);
        }
        String varMontant = CGImportEcrituresUtils.getMontant(ecritureCollective);
        if (JadeStringUtil.isBlankOrZero(varMontant) == false) {
            currency.add(varMontant);
        }
        totalJournal.put(numMandat, currency);
    }

    /**
     * Initialisation des champs pour les écritures de type collective
     * 
     * @param journal
     * @param elementEcriture
     * @param ecriture
     * @param mandat
     * @throws Exception
     */
    protected void setChampEcritureCollective(CGJournal journal, Element elementEcriture, CGEcritureViewBean ecriture,
            CGMandat mandat) throws Exception {

        ecriture.setPiece(CGImportEcrituresUtils.getPiece(elementEcriture));
        ecriture.setCodeDebitCredit(CGImportEcrituresUtils.getCodeDebitCredit(getSession(), elementEcriture));
        ecriture.setIdCompte(CGImportEcrituresUtils.getIdCompteEcritureCollective(getSession(), getTransaction(),
                elementEcriture, journal.getIdExerciceComptable(), mandat));
        // Initialisation des champs communs aux différentes types d'écriture
        fillChampCommunEcriture(journal, elementEcriture, ecriture, journal.getIdExerciceComptable());
    }

    /**
     * Initialisation des champs pour les écritures de type double
     * 
     * @param e
     * @param ecriture
     * @param credit
     * @param mandat
     * @param journal
     * @throws Exception
     */
    protected void setChampEcritureDouble(Element e, CGEcritureViewBean ecriture, boolean credit, CGMandat mandat,
            CGJournal journal) throws Exception {
        if (credit) {
            ecriture.setCodeDebitCredit(CodeSystem.CS_CREDIT);
            ecriture.setIdCompte(CGImportEcrituresUtils.getIdCompteCredite(getSession(), getTransaction(), e, journal
                    .getExerciceComptable().getIdExerciceComptable(), mandat));
        } else {
            ecriture.setCodeDebitCredit(CodeSystem.CS_DEBIT);
            ecriture.setIdCompte(CGImportEcrituresUtils.getIdCompteDebite(getSession(), getTransaction(), e, journal
                    .getExerciceComptable().getIdExerciceComptable(), mandat));
        }

        ecriture.setIdCentreCharge(CGImportEcrituresUtils.getIdCentreChargeCredite(e));
    }

    /**
     * @param dateTraitement
     *            The dateTraitement to set.
     */
    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setDirectoryFile(String fileDirectory) {
        directoryFile = fileDirectory;
    }

    /**
     * @param fileName
     *            The fileName to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param idExerciceComptable
     *            The idExerciceComptable to set.
     */
    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * @param libelleJournal
     *            The libelleJournal to set.
     */
    public void setLibelleJournal(String libelleJournal) {
        this.libelleJournal = libelleJournal;
    }

    public void setTraitementSchedule(Boolean traitementSchedule) {
        this.traitementSchedule = traitementSchedule;
    }
}
