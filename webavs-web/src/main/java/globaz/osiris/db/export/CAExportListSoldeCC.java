package globaz.osiris.db.export;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexeAtDate_DS;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexeCC_DS;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexe_DS;

/**
 * Date de création : (17.06.2003 08:34:14)
 * 
 * @author: ado
 */
public class CAExportListSoldeCC extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CALIST_SOLDE_C_C = "CAListSoldeCC";
    private static final String TOUS_1000 = "1000";
    protected String forDate = new String();
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    protected String forSelectionCC = new String();
    protected java.lang.String forSelectionRole = new String();
    protected java.lang.String forSelectionSigne = new String();

    //
    protected java.lang.String forSelectionTri = new String();
    protected java.lang.String idCompteAnnexe = new String();

    /**
     * Commentaire relatif au constructeur CAExportListSoldeCC.
     */
    public CAExportListSoldeCC() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CAExportListSoldeCC.
     * 
     * @param parent
     *            BProcess
     */
    public CAExportListSoldeCC(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CAExportListSoldeCC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAExportListSoldeCC(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // Sélection des comptes annexes sans date et sans CC
            BManager manager = null;
            if ((forDate == null)
                    || ((forDate.trim().length() == 0) && forSelectionCC.equals(CAExportListSoldeCC.TOUS_1000))) {
                manager = new CAIListSoldeCompteAnnexe_DS();
                ((CAIListSoldeCompteAnnexe_DS) manager).setSession(getSession());
                ((CAIListSoldeCompteAnnexe_DS) manager).setForSelectionRole(forSelectionRole);
                ((CAIListSoldeCompteAnnexe_DS) manager).setForSelectionSigne(forSelectionSigne);
                ((CAIListSoldeCompteAnnexe_DS) manager).setForSelectionTri(forSelectionTri);
                ((CAIListSoldeCompteAnnexe_DS) manager).setForIdGenreCompte(getForIdGenreCompte());
                ((CAIListSoldeCompteAnnexe_DS) manager).setForIdCategorie(getForIdCategorie());
                // Sélection des comptes annexes sans date avec choix du CC
            } else if ((forDate == null)
                    || ((forDate.trim().length() == 0) && !forSelectionCC.equals(CAExportListSoldeCC.TOUS_1000))) {
                manager = new CAIListSoldeCompteAnnexeCC_DS();
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setSession(getSession());
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setForSelectionRole(forSelectionRole);
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setForSelectionSigne(forSelectionSigne);
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setForSelectionTri(forSelectionTri);
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setForSelectionCC(forSelectionCC);
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setForIdGenreCompte(getForIdGenreCompte());
                ((CAIListSoldeCompteAnnexeCC_DS) manager).setForIdCategorie(getForIdCategorie());
            } else {
                // Sélection des comptes annexes avec date
                manager = new CAIListSoldeCompteAnnexeAtDate_DS();
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setSession(getSession());
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForSelectionRole(forSelectionRole);
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForSelectionSigne(forSelectionSigne);
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForSelectionTri(forSelectionTri);
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForDate(forDate);
                if (!forSelectionCC.equals(CAExportListSoldeCC.TOUS_1000)) {
                    ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForSelectionCC(forSelectionCC);
                }
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForIdGenreCompte(getForIdGenreCompte());
                ((CAIListSoldeCompteAnnexeAtDate_DS) manager).setForIdCategorie(getForIdCategorie());
            }
            if (!isAborted()) {
                manager.find(BManager.SIZE_NOLIMIT);
                if (!isAborted()) {
                    CAListSoldeCompteAnnexe excelDoc = new CAListSoldeCompteAnnexe(
                            CAExportListSoldeCC.CALIST_SOLDE_C_C, getSession(), getTransaction(), this);
                    excelDoc.setDocumentInfo(createDocumentInfo());
                    excelDoc.populateSheet(manager);
                    if (!isAborted()) {
                        this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        if (isAborted()) {
            return false;
        }
        return true;
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
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
            obj = getSession().getLabel("IMPRESSION_LISTE_SOLDES_PAS_OK");
        } else {
            obj = getSession().getLabel("IMPRESSION_LISTE_SOLDES_OK");
        }
        // Restituer l'objet
        return obj;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

}
