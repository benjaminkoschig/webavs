package globaz.osiris.db.print;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListSoldeCompteAnnexe;

public class CAListSoldeCompteAnnexeViewBean extends CAAbstractListProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDate = new String();
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private String forSelectionRole = new String();
    private String forSelectionSigne = new String();
    private String forSelectionTri = new String();
    private String idCompteAnnexe = new String();

    public CAListSoldeCompteAnnexeViewBean() throws Exception {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.print.CAAbstractListProcess#getDocument()
     */
    @Override
    FWIDocumentInterface getDocument() {
        try {
            CAIListSoldeCompteAnnexe doc = new CAIListSoldeCompteAnnexe(this);
            doc.setForSelectionRole(getForSelectionRole());
            doc.setForSelectionSigne(getForSelectionSigne());
            doc.setForSelectionTri(getForSelectionTri());
            doc.setIdCompteAnnexe(getIdCompteAnnexe());
            doc.setForDate(getForDate());
            doc.setEMailAddress(getEMailAddress());
            doc.setForIdGenreCompte(getForIdGenreCompte());
            doc.setForIdCategorie(getForIdCategorie());
            return doc;
        } catch (FWIException e) {
        }
        return null;
    }

    /**
     * Returns the forDate.
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
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
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 13:22:43)
     * 
     * @return String
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:58:41)
     * 
     * @return String
     */
    public String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 13:44:09)
     * 
     * @return String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:11:32)
     * 
     * @return String
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Sets the forDate.
     * 
     * @param forDate
     *            The forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
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

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 13:22:43)
     * 
     * @param newForSelectionRole
     *            String
     */
    public void setForSelectionRole(String newForSelectionRole) {
        forSelectionRole = newForSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:58:41)
     * 
     * @param newForSelectionSigne
     *            String
     */
    public void setForSelectionSigne(String newForSelectionSigne) {
        forSelectionSigne = newForSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 13:44:09)
     * 
     * @param newFromNumNom
     *            String
     */
    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

}
