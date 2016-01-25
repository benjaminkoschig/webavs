package globaz.pavo.print.list;

import globaz.framework.printing.FWAbstractManagerDocumentList;
import globaz.framework.printing.FWDocumentTable;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIUtil;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Imprime le journal des inscriptions au comptes individuels. Date de création : (07.01.2003 12:56:29)
 * 
 * @author: Administrator
 */
public class CIListJournal extends FWAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean detail = new Boolean(true);
    private String forIdTypeCompte = new String();
    private String fromAnnee = new String();
    private String fromDateInscription = new String();
    private String fromIdJournal = new String();
    // tri
    private String fromNumeroAvs = new String();
    private Boolean recapitulation = new Boolean(true);
    // totaux
    private HashMap sums = new HashMap();
    private CISommesEcritures total = new CISommesEcritures();
    private String tri;
    private String untilAnnee = new String();
    private String untilDateInscription = new String();
    private String untilIdJournal = new String();
    private String untilNumeroAvs = new String();

    public CIListJournal() throws Exception {
        super(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO), "filename", "", "title", new CIEcritureManager(),
                "/PAVO.properties");
    }

    public CIListJournal(FWProcess parent) throws Exception {
        this();
        setParentWithCopy(parent);
    }

    public CIListJournal(String filenameRoot, String companyName, String documentSubject, BManager manager) {
        super(manager.getSession(), filenameRoot, companyName, documentSubject, manager, "/PAVO.properties");
    }

    @Override
    public final void _beforeExecuteReport() {
        // nom de la caisse
        try {
            CIApplication application = (CIApplication) getSession().getApplication();
            _setCompanyName(application.getAdministrationLocale(getSession()).getNom());
        } catch (Exception ex) {
            // pas de nom trouvé
        }

        try {
            // Préparer le manager
            CIEcritureManager mgr = (CIEcritureManager) _getManager();
            mgr.setSession(getSession());
            // clauses from
            mgr.setFromAnnee(getFromAnnee());
            mgr.setFromDateInscription(getFromDateInscription());
            mgr.setFromIdJournal(getFromIdJournal());
            mgr.setFromNumeroAvs(getFromNumeroAvs());
            // clauses until
            mgr.setUntilAnnee(getUntilAnnee());
            mgr.setUntilDateInscription(getUntilDateInscription());
            mgr.setUntilIdJournal(getUntilIdJournal());
            mgr.setUntilNumeroAvs(getUntilNumeroAvs());
            // clauses for
            if (!JAUtil.isStringEmpty(getForIdTypeCompte())) {
                mgr.setForIdTypeCompte(getForIdTypeCompte());
            }
            // tri
            mgr.setTri(getTri());
            // Vérifier condition de sortie
            if (isAborted()) {
                return;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    /**
     * Crée une nouvelle instance du manager à utiliser, peut retourner null.
     */
    @Override
    protected final BManager _createManager() {
        return new CIEcritureManager();
    }

    @Override
    protected void _validate() {
        if (JAUtil.isStringEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("MSG_COMPTA_JOURNAL_EMAIL"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("MSG_COMPTA_JOURNAL_EMAIL_INV"));
            }
        }
        if (JAUtil.isStringEmpty(getFromIdJournal())) {
            this._addError(getSession().getLabel("MSG_COMPTA_JOURNAL_ID"));
        } else {
            if (JAUtil.isStringEmpty(getUntilIdJournal())) {
                untilIdJournal = fromIdJournal;
            }
        }
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
        }
    }

    /**
     * Ajoute une ligne dans la table de données (utiliser <code>_addCell(Object)</code>)
     * 
     * @param entity
     *            l'entité contenant les données
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    @Override
    protected void addRow(globaz.globall.db.BEntity entity) throws Exception {
        CIEcriture ecriture = (CIEcriture) entity;
        if (isDetail().booleanValue()) {
            _addCell(ecriture.getNssFormate() + " " + ecriture.getNomPrenom());
            if (!JAUtil.isIntegerEmpty(ecriture.getMoisDebut())) {
                _addCell((ecriture.getMoisDebut().length() == 1 ? "0" : "") + ecriture.getMoisDebut() + "-"
                        + (ecriture.getMoisFin().length() == 1 ? "0" : "") + ecriture.getMoisFin());
            } else {
                _addCell("");
            }
            _addCell(ecriture.getAnnee());
            _addCell(ecriture.getGreFormat());
            _addCell(JANumberFormatter
                    .format(JANumberFormatter.formatZeroValues(ecriture.getMontantSigne(), true, true)));
            _addCell(ecriture.getNoNomEmployeur());
            _addCell(globaz.pavo.translation.CodeSystem.getLibelle(ecriture.getCodeSpecial(), getSession()));
            _addCell(globaz.pavo.translation.CodeSystem.getLibelle(ecriture.getBrancheEconomique(), getSession()));
            _addCell(ecriture.getPartBta());
            _addCell(globaz.pavo.translation.CodeSystem.getLibelle(ecriture.getCode(), getSession()));
            if (JAUtil.isIntegerEmpty(ecriture.getCaisseChomage())) {
                _addCell("");
            } else {
                _addCell(ecriture.getCaisseChomage());
            }
            String date = ecriture.getJournal(null, false).getDateInscription();
            if (JAUtil.isIntegerEmpty(date)) {
                date = ecriture.getJournal(getTransaction(), false).getDateInscription();
            }
            if (JAUtil.isStringEmpty(date)) {
                BigDecimal decimal = new BigDecimal(Long.parseLong(ecriture.getEspionSaisie().substring(0, 8)));
                _addCell(new JADate(decimal).toStr("."));
            } else {
                _addCell(date);
            }
            _addCell(globaz.pavo.translation.CodeSystem.getLibelle(ecriture.getIdTypeCompte(), getSession()));
        }
        // calcul des totaux
        // avec centimes
        CISommesEcritures sum;
        if (sums.containsKey(ecriture.getGenreEcriture())) {
            sum = (CISommesEcritures) sums.get(ecriture.getGenreEcriture());
        } else {
            sum = new CISommesEcritures();
            sums.put(ecriture.getGenreEcriture(), sum);
        }
        sum.additionne(ecriture.getMontantSigne());
        total.additionne(ecriture.getMontantSigne());
    }

    @Override
    protected void bindReportFooter() {
        // Insérer un saut de page
        if (isRecapitulation().booleanValue()) {
            _addPageBreak();
            // Création d'une table
            FWDocumentTable table = new FWDocumentTable();
            table.addColumn("Récapitulation par genre", FWDocumentTable.LEFT, 3);
            table.addColumn("Nombre", FWDocumentTable.LEFT, 1);
            table.addColumn("Total revenus", FWDocumentTable.RIGHT, 1);
            table.addColumn("Total revenus inscrits", FWDocumentTable.RIGHT, 1);
            table.hideGrid();
            table.endTableDefinition();
            // Parcourir les sommes avec et sans centimes
            Iterator iter = sums.keySet().iterator();
            while (iter.hasNext()) {
                String cs = (String) iter.next();
                table.addCell(CodeSystem.getCodeUtilisateur(cs, getSession()) + " "
                        + CodeSystem.getLibelle(cs, getSession()));
                CISommesEcritures sommeEcritures = (CISommesEcritures) sums.get(cs);
                table.addCell(String.valueOf(sommeEcritures.getCompteur()));
                table.addCell(sommeEcritures.getMontantCts());
                table.addCell(sommeEcritures.getMontantSansCts());
                table.addRow();
            }
            table.addCell("Totaux");
            table.addCell(String.valueOf(total.getCompteur()));
            table.addCell(total.getMontantCts());
            table.addCell(total.getMontantSansCts());
            table.addRow();
            // Insérer la table
            _addTable(table);
        }
    }

    @Override
    protected void bindReportHeader() {
        // Création d'une table
        FWDocumentTable table = new FWDocumentTable();
        table.addColumn("", FWDocumentTable.LEFT, 1);
        table.addColumn("", FWDocumentTable.LEFT, 3);
        table.hideColumns();
        table.hideGrid();
        table.endTableDefinition();
        // Imprimer les paramètres
        table.addCell("Journal");
        // un seul journal
        CIJournal journal = new CIJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getFromIdJournal());
        String desc;
        try {
            journal.retrieve();
            desc = journal.getIdJournal() + " - " + journal.getDescription();
        } catch (Exception ex) {
            desc = "n/a";
        }
        table.addCell(getTextEntete(getFromIdJournal(), getUntilIdJournal(), desc, true));
        table.addRow();
        table.addCell("Compte Individuel");
        CICompteIndividuelManager ci = new CICompteIndividuelManager();
        ci.setSession(getSession());
        ci.setForNumeroAvs(getFromNumeroAvs());
        desc = "n/a";
        try {
            ci.find();
            if (ci.size() > 0) {
                desc = ((CICompteIndividuel) ci.getEntity(0)).getNomPrenom();
            }
        } catch (Exception ex) {
            // lassier à n/a
        }
        table.addCell(getTextEntete(getFromNumeroAvs(), getUntilNumeroAvs(), desc, false));
        table.addRow();
        table.addCell("Date d'inscription");
        table.addCell(getTextEntete(getFromDateInscription(), getUntilDateInscription(), "", false));
        table.addRow();
        table.addCell("Année de cotisation");
        table.addCell(getTextEntete(getFromAnnee(), getUntilAnnee(), "", false));
        table.addRow();
        table.addCell("Compte");
        if (JAUtil.isStringEmpty(getForIdTypeCompte())) {
            table.addCell("*");
        } else {
            table.addCell(CodeSystem.getLibelle(getForIdTypeCompte(), getSession()));
        }
        table.addRow();
        // Imprimer
        _addTable(table);
        this._addLine("", "", "");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeCompte() {
        return forIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateInscription() {
        return fromDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    /**
     * Retourne le texte pour l'en-tête de la liste.<br>
     * Date de création : (09.01.2003 09:53:17)
     * 
     * @return le texte
     * @param de
     *            la valeur de
     * @param a
     *            la valeur a
     * @param description
     *            la description si de = a
     * @param onlyDescription
     *            si de = a, seule la description doit être retournée.
     */
    private String getTextEntete(String de, String a, String description, boolean onlyDescription) {
        String result = "";
        if (JAUtil.isStringEmpty(de)) {
            if (JAUtil.isStringEmpty(a)) {
                return "*";
            }
            // result = getSession().getLabel("MSG_LIST_JOURNAL_JUSQUE");
        } else {
            result = de;
        }
        if (JAUtil.isStringEmpty(a)) {
            result = getSession().getLabel("MSG_LIST_JOURNAL_DES") + " " + result;
        } else {
            if (de.equals(a)) {
                if (onlyDescription) {
                    result = description;
                } else {
                    result += " " + description;
                }
            } else {
                if (JAUtil.isStringEmpty(de)) {
                    result = getSession().getLabel("MSG_LIST_JOURNAL_JUSQUE") + " " + a;
                } else {
                    result += " - " + a;
                }
            }
        }
        return result;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 11:45:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTri() {
        return tri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilDateInscription() {
        return untilDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilIdJournal() {
        return untilIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilNumeroAvs() {
        return untilNumeroAvs;
    }

    /**
     * Initialise la table des données
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de déclarer les colonnes
     * <li><code>_group...(..)</code> permet de déclarer les groupages
     * </ul>
     */
    @Override
    protected void initializeTable() {
        if (isDetail().booleanValue()) {
            this._addColumnLeft("Compte individuel");
            this._addColumnLeft("Période");
            this._addColumnLeft("Année");
            this._addColumnLeft("Genre");
            this._addColumnRight("Revenu");
            this._addColumnLeft("Employeur/conjoint");
            this._addColumnLeft("CSp");
            this._addColumnLeft("BrEc");
            this._addColumnLeft("Bta");
            this._addColumnLeft("CIr");
            this._addColumnLeft("CCh");
            this._addColumnLeft("Date d'inscription");
            this._addColumnLeft("Compte");
            _groupManual();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 11:11:17)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isDetail() {
        return detail;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 11:11:18)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isRecapitulation() {
        return recapitulation;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 11:11:17)
     * 
     * @param newDetail
     *            java.lang.Boolean
     */
    public void setDetail(java.lang.Boolean newDetail) {
        detail = newDetail;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newForIdTypeCompte
     *            java.lang.String
     */
    public void setForIdTypeCompte(java.lang.String newForIdTypeCompte) {
        forIdTypeCompte = newForIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newFromAnnee
     *            java.lang.String
     */
    public void setFromAnnee(java.lang.String newFromAnnee) {
        fromAnnee = newFromAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newFromDateInscription
     *            java.lang.String
     */
    public void setFromDateInscription(java.lang.String newFromDateInscription) {
        fromDateInscription = CIUtil.padDate(newFromDateInscription);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newFromIdJournal
     *            java.lang.String
     */
    public void setFromIdJournal(java.lang.String newFromIdJournal) {
        fromIdJournal = newFromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newFromNumeroAvs
     *            java.lang.String
     */
    public void setFromNumeroAvs(java.lang.String newFromNumeroAvs) {
        fromNumeroAvs = unFormatAVS(newFromNumeroAvs);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 11:11:18)
     * 
     * @param newRecapitulation
     *            java.lang.Boolean
     */
    public void setRecapitulation(java.lang.Boolean newRecapitulation) {
        recapitulation = newRecapitulation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 11:45:17)
     * 
     * @param newTri
     *            java.lang.String
     */
    public void setTri(java.lang.String newTri) {
        tri = newTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newUntilAnnee
     *            java.lang.String
     */
    public void setUntilAnnee(java.lang.String newUntilAnnee) {
        untilAnnee = newUntilAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newUntilDateInscription
     *            java.lang.String
     */
    public void setUntilDateInscription(java.lang.String newUntilDateInscription) {
        untilDateInscription = CIUtil.padDate(newUntilDateInscription);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newUntilIdJournal
     *            java.lang.String
     */
    public void setUntilIdJournal(java.lang.String newUntilIdJournal) {
        untilIdJournal = newUntilIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 09:07:28)
     * 
     * @param newUntilNumeroAvs
     *            java.lang.String
     */
    public void setUntilNumeroAvs(java.lang.String newUntilNumeroAvs) {
        untilNumeroAvs = unFormatAVS(newUntilNumeroAvs);
    }

    /**
     * Enlève les . de séparation du numéro avs. Date de création : (09.01.2003 10:07:14)
     * 
     * @return le numéro avs formatté
     * @param avs
     *            le numéro avs à fomatter
     */
    private String unFormatAVS(String avs) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < avs.length(); i++) {
            if (avs.charAt(i) != '.') {
                buf.append(avs.charAt(i));
            }
        }
        return buf.toString();
    }
}
