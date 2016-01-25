package globaz.osiris.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListSoldeSection;

/**
 * Cette classe permet de lancer l'impression de la liste des soldes par section
 * 
 * @author: Administrator
 */
public class CAIProcessListSoldeSection extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CAIListSoldeSection documentIt = null;
    private globaz.framework.printing.FWDocumentListener documentListener = new globaz.framework.printing.FWDocumentListener();
    private boolean erreur = false;
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private java.lang.String forIdTypeSection = new String();
    private java.lang.String forSelectionRole = new String();
    private java.lang.String forSelectionSigne = new String();
    private java.lang.String forSelectionTriCA = new String();
    private java.lang.String forSelectionTriSection = new String();
    private java.lang.String idSection = new String();

    /**
     * Commentaire relatif au constructeur CAIProcessListSoldeSection.
     */
    public CAIProcessListSoldeSection() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CAIProcessListSoldeSection.
     * 
     * @param parent
     *            BProcess
     */
    public CAIProcessListSoldeSection(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CAIProcessListSoldeSection.
     */
    public CAIProcessListSoldeSection(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution
     */
    @Override
    protected void _executeCleanUp() {
        // Terminer le listener de document en cas d'erreur
        if (isOnError() && isErreur()) {
            getDocumentListener().abort();
        }
    }

    /**
     * Execution du processus
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // Variable pour le document Itext
            documentIt = new CAIListSoldeSection(this);
            documentIt.setForIdTypeSection(getForIdTypeSection());
            documentIt.setForSelectionRole(getForSelectionRole());
            documentIt.setForSelectionTriCA(getForSelectionTriCA());
            documentIt.setForSelectionTriSection(getForSelectionTriSection());
            documentIt.setForSelectionSigne(getForSelectionSigne());
            documentIt.setForIdGenreCompte(getForIdGenreCompte());
            documentIt.setForIdCategorie(getForIdCategorie());
            documentIt.bindData(getIdSection());
            documentIt.executeProcess();
        } catch (Exception e) {
            setErreur(true);
        } finally {
            // Fin de registration
            getDocumentListener().endRegistering();
        }
        return isErreur();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.08.2002 14:04:44)
     * 
     * @return globaz.framework.printing.FWDocumentListener
     */
    public globaz.framework.printing.FWDocumentListener getDocumentListener() {
        return documentListener;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("6012");
        } else {
            return getSession().getLabel("6011");
        }
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
     * Returns the forIdTypeSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * Returns the forSelectionRole.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Returns the forSelectionSigne.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Returns the forSelectionTriCA.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * Returns the forSelectionTriSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * Returns the idSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdSection() {
        return idSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.08.2002 14:16:13)
     * 
     * @return boolean
     */
    public boolean isErreur() {
        return erreur;
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
     * Insérez la description de la méthode ici. Date de création : (07.08.2002 14:04:44)
     * 
     * @param newDocumentListener
     *            globaz.framework.printing.FWDocumentListener
     */
    public void setDocumentListener(globaz.framework.printing.FWDocumentListener newDocumentListener) {
        documentListener = newDocumentListener;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.08.2002 14:16:13)
     * 
     * @param newErreur
     *            boolean
     */
    public void setErreur(boolean newErreur) {
        erreur = newErreur;
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
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * Sets the forIdTypeSection.
     * 
     * @param forIdTypeSection
     *            The forIdTypeSection to set
     */
    public void setForIdTypeSection(java.lang.String forIdTypeSection) {
        this.forIdTypeSection = forIdTypeSection;
    }

    /**
     * Sets the forSelectionRole.
     * 
     * @param forSelectionRole
     *            The forSelectionRole to set
     */
    public void setForSelectionRole(java.lang.String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    /**
     * Sets the forSelectionSigne.
     * 
     * @param forSelectionSigne
     *            The forSelectionSigne to set
     */
    public void setForSelectionSigne(java.lang.String forSelectionSigne) {
        this.forSelectionSigne = forSelectionSigne;
    }

    /**
     * Sets the forSelectionTriCA.
     * 
     * @param forSelectionTriCA
     *            The forSelectionTriCA to set
     */
    public void setForSelectionTriCA(java.lang.String forSelectionTriCA) {
        this.forSelectionTriCA = forSelectionTriCA;
    }

    /**
     * Sets the forSelectionTriSection.
     * 
     * @param forSelectionTriSection
     *            The forSelectionTriSection to set
     */
    public void setForSelectionTriSection(java.lang.String forSelectionTriSection) {
        this.forSelectionTriSection = forSelectionTriSection;
    }

    /**
     * Sets the idSection.
     * 
     * @param idSection
     *            The idSection to set
     */
    public void setIdSection(java.lang.String idSection) {
        this.idSection = idSection;
    }

}
