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
 * Cette classe permet d'imprimer la préimpression de la déclaration de salaires.
 * 
 * @author: Sébastien Chappatte
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
                "Préimpression déclarations salaires", null, "/draco.properties");
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
     * Cette méthode permet d'effectuer de préparatifs avant de lancer l'exécution du report.
     */
    @Override
    public final void _beforeExecuteReport() {
        // Sous contrôle d'exceptions
        try {
            // Titre de la liste
            String sTitre = getSession().getLabel("6010");
            // Charger les valeurs du raport
            _setDocumentTitle(sTitre);
            // Vérifier condition de sortie
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
     * Ajoute une ligne dans la table de données (utiliser <code>_addCell(Object)</code>)
     * 
     * @param entity
     *            l'entité contenant les données
     */
    @Override
    protected void addRow(globaz.globall.db.BEntity entity) {
    }

    /**
     * Cette méthode permet de paramétrer le bas de page. Dans le cas de cette liste, toute la préparation du document
     * sera effectuée dans cette partie
     */
    @Override
    protected void bindReportFooter() throws FWIException {
        // Parcourir les déclarations
        Iterator iter = tableSummaryDeclaration.values().iterator();
        Iterator iterEnt = tableSummaryDeclaration.values().iterator();
        FWIDocumentTable table = null;
        String curIdDeclaration = null;
        // Lecture du premier enregistrement pour récupérer la description de la
        // déclaration
        if (iterEnt.hasNext()) {
            SummaryDeclaration sd1 = (SummaryDeclaration) iterEnt.next();
            try {
                // Instancier la déclaration de salaires
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
                table.addCell("Original à retourner à la caisse le " + getDateRetourDes() + " au plus tard");
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
        table.addColumn("Numéro AVS", FWDocumentTable.LEFT, 4);
        table.addColumn("Nom et prénom", FWDocumentTable.LEFT, 1);
        table.addColumn("Date d'engagement", FWDocumentTable.RIGHT, 1);
        table.addColumn("Date de licenciement", FWDocumentTable.RIGHT, 1);
        table.addColumn("AVS/AI/APG", FWDocumentTable.RIGHT, 1);
        table.addColumn("Ass. Chômage", FWDocumentTable.RIGHT, 1);
        table.endTableDefinition();
        while (iter.hasNext()) {
            SummaryDeclaration sc = (SummaryDeclaration) iter.next();
            // Création d'une table
            if (curIdDeclaration != null && !sc.idDeclaration.equals(curIdDeclaration)) {
            }
        }
    }

    /**
     * Cette méthode permet de paramétrer l'entête du document.
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
    }

    /**
     * Insérez la description de la méthode ici.
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