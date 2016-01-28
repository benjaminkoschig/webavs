package globaz.draco.print.list;

import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.printing.FWDocumentTable;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Cette classe permet d'imprimer la pr�impression de la d�claration de salaires.
 * 
 * @author: S�bastien Chappatte
 */
public class DSListDeclarationSalaire extends FWIAbstractManagerDocumentList {

    private static final long serialVersionUID = -5869950875318772546L;

    private class SummaryDeclaration {
        String _dateEngagement = new String();
        String _noAVS = new String();
        String _nomPrenom = new String();
        String idDeclaration = new String();

        public SummaryDeclaration() {
        }
    }

    private String annee = new String();

    private String dateRetourDes = new String();

    private TreeMap tableSummaryDeclaration = new TreeMap();

    /**
     * Commentaire relatif au constructeur DSListDeclarationSalaire.
     */
    public DSListDeclarationSalaire() throws Exception {
        super(new globaz.globall.db.BSession(DSApplication.DEFAULT_APPLICATION_DRACO), "ListPreImp", "[company]",
                "Pr�impression d�clarations salaires", null, "/draco.properties");
    }

    /**
     * Commentaire relatif au constructeur DSListDeclarationSalaire.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public DSListDeclarationSalaire(BProcess parent) throws Exception {
        this();
        setParentWithCopy(parent);
    }

    /**
     * Commentaire relatif au constructeur DSListDeclarationSalaire.
     * 
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentSubject
     *            java.lang.String
     * @param manager
     *            globaz.globall.db.BManager
     */
    public DSListDeclarationSalaire(String filenameRoot, String companyName, String documentSubject,
            globaz.globall.db.BManager manager) {
        super(manager.getSession(), filenameRoot, companyName, documentSubject, manager);
    }

    /**
     * Commentaire relatif au constructeur DSListDeclarationSalaire.
     * 
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentSubject
     *            java.lang.String
     * @param manager
     *            globaz.globall.db.BManager
     * @param propertyFile
     *            java.lang.String
     */
    public DSListDeclarationSalaire(String filenameRoot, String companyName, String documentSubject,
            globaz.globall.db.BManager manager, String propertyFile) {
        super(manager.getSession(), filenameRoot, companyName, documentSubject, manager, propertyFile);
    }

    /**
     * Cette m�thode permet d'effectuer de pr�paratifs avant de lancer l'ex�cution du report.
     */
    @Override
    public final void _beforeExecuteReport() {
        // Sous contr�le d'exceptions
        try {
            // Titre de la liste
            String sTitre = getSession().getLabel("6010");
            // Charger les valeurs du raport
            _setDocumentTitle(sTitre);
            // V�rifier condition de sortie
            if (isAborted()) {
                return;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
            return;
        }
        return;
    }

    /**
     * Ajoute une ligne dans la table de donn�es (utiliser <code>_addCell(Object)</code>)
     * 
     * @param entity
     *            l'entit� contenant les donn�es
     */
    @Override
    protected void addRow(globaz.globall.db.BEntity entity) {
    }

    /**
     * Cette m�thode permet de param�trer le bas de page. Dans le cas de cette liste, toute la pr�paration du document
     * sera effectu�e dans cette partie
     */
    @Override
    protected void bindReportFooter() throws FWIException {
        // Parcourir les d�clarations
        Iterator iter = tableSummaryDeclaration.values().iterator();
        Iterator iterEnt = tableSummaryDeclaration.values().iterator();
        FWIDocumentTable table = null;
        String curIdDeclaration = null;
        // Lecture du premier enregistrement pour r�cup�rer la description de la
        // d�claration
        if (iterEnt.hasNext()) {
            SummaryDeclaration sd1 = (SummaryDeclaration) iterEnt.next();
            try {
                // Instancier la d�claration de salaires
                DSDeclarationViewBean decl = new DSDeclarationViewBean();
                decl.setSession(getSession());
                decl.setIdDeclaration(sd1.idDeclaration);
                decl.setAnnee(getAnnee());
                decl.setAlternateKey(1);
                decl.retrieve(getTransaction());
                table = new FWIDocumentTable();
                table.addColumn("", FWDocumentTable.LEFT, 1);
                table.addColumn("", FWDocumentTable.LEFT, 3);
                table.addColumn("", FWDocumentTable.LEFT, 6);
                table.hideColumns();
                table.hideGrid();
                table.endTableDefinition();
                table.addCell("Original � retourner � la caisse le " + getDateRetourDes() + " au plus tard");
                table.addRow();
                if (decl != null && !JadeStringUtil.isBlank(decl.getAffiliation().getAffilieNumero())) {
                    table.addCell(decl.getAffiliation().getAffilieNumero());
                    table.addRow();
                    table.addCell(decl.getAnnee());
                    table.addRow();
                } else {
                    table.addCell("");
                    table.addRow();
                    table.addCell("");
                    table.addRow();
                }
                _addTable(table);
            } catch (Exception e) {
            }
        }
        table = new FWIDocumentTable();
        table.addColumn("Num�ro AVS", FWDocumentTable.LEFT, 4);
        table.addColumn("Nom et pr�nom", FWDocumentTable.LEFT, 1);
        table.addColumn("Date d'engagement", FWDocumentTable.RIGHT, 1);
        table.addColumn("Date de licenciement", FWDocumentTable.RIGHT, 1);
        table.addColumn("AVS/AI/APG", FWDocumentTable.RIGHT, 1);
        table.addColumn("Ass. Ch�mage", FWDocumentTable.RIGHT, 1);
        table.endTableDefinition();
        while (iter.hasNext()) {
            SummaryDeclaration sc = (SummaryDeclaration) iter.next();
            // Cr�ation d'une table
            if (curIdDeclaration != null && !sc.idDeclaration.equals(curIdDeclaration)) {
            }
        }
    }

    /**
     * Cette m�thode permet de param�trer l'ent�te du document.
     */
    @Override
    protected void bindReportHeader() {
    }

    /**
     * Gets the annee
     * 
     * @return Returns a String
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Gets the dateRetourDes
     * 
     * @return Returns a String
     */
    public String getDateRetourDes() {
        return dateRetourDes;
    }

    /**
     * Initialise la table des donn�es
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de d�clarer les colonnes
     * <li><code>_group...(..)</code> permet de d�clarer les groupages
     * </ul>
     */
    @Override
    protected void initializeTable() {
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     * @param etape
     *            globaz.osiris.db.contentieux.CAParametreEtape
     * @param date
     *            java.lang.String
     * @param taxe
     *            java.lang.String
     * @param montant
     *            java.lang.String
     * @param remarque
     *            java.lang.String
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Sets the annee
     * 
     * @param annee
     *            The annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * Sets the dateRetourDes
     * 
     * @param dateRetourDes
     *            The dateRetourDes to set
     */
    public void setDateRetourDes(String dateRetourDes) {
        this.dateRetourDes = dateRetourDes;
    }
}