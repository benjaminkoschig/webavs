package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.contentieux.CASectionsBloqueesContentieuxManager;
import globaz.osiris.print.list.CAListSectionsBloqueesContentieux;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CAProcessListSectionsBloqueesContentieux extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean blocageInactif = Boolean.FALSE;
    private String forIdCategorie;
    private String forIdGenreCompte;
    private String forIdTypeSection;
    private String forSelectionCompte; // Solde
    private String forSelectionRole;
    private String forSelectionTriCA;
    private String forSelectionTriSection;
    private String idContMotifBloque; // Nouveau contencieux : Aquila
    private String idMotConSus; // Ancien contencieux

    public CAProcessListSectionsBloqueesContentieux() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CASectionsBloqueesContentieuxManager manager = new CASectionsBloqueesContentieuxManager();
            manager.setSession(getSession());
            manager.setForIdTypeSection(getForIdTypeSection());
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForSelectionTriCA(getForSelectionTriCA());
            manager.setForSelectionTriSection(getForSelectionTriSection());
            manager.setForIdGenreCompte(getForIdGenreCompte());
            manager.setForIdCategorie(getForIdCategorie());
            manager.setForSelectionCompte("1"); // Solde : ouvert (<>0)

            if (!getBlocageInactif().booleanValue()) {
                manager.setForContentieuxEstSuspendu(true);
                if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                    manager.setForIdMotConSus(getIdMotConSus());
                    manager.setForDateReferenceBlocage(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD));
                    // } else { // Contentieux Aquila
                    // Date de référence blocage géré dans
                    // populateSheetListeAquila() dans
                    // CAListSectionsBloqueesContentieux
                }
            }

            manager.find();

            // Création du document
            CAListSectionsBloqueesContentieux excelDoc = new CAListSectionsBloqueesContentieux(getSession());

            excelDoc.setForIdTypeSection(getForIdTypeSection());
            excelDoc.setForSelectionRole(getForSelectionRole());
            excelDoc.setForSelectionTriCA(getForSelectionTriCA());
            excelDoc.setForSelectionTriSection(getForSelectionTriSection());
            excelDoc.setForIdGenreCompte(getForIdGenreCompte());
            excelDoc.setForIdCategorie(getForIdCategorie());
            excelDoc.setForSelectionCompte(getForSelectionCompte()); // Solde
            excelDoc.setBlocageInactif(getBlocageInactif());
            excelDoc.setDocumentInfo(createDocumentInfo());
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                excelDoc.setIdMotConSus(getIdMotConSus()); // Ancien contentieux
                excelDoc.setProcessAppelant(this);
                excelDoc.populateSheetListe(manager, getTransaction());
            } else {
                excelDoc.setIdContMotifBloque(getIdMotConSus()); // Aquila
                excelDoc.setProcessAppelant(this);
                excelDoc.populateSheetListeAquila(manager, getTransaction());
            }

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;

        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @return
     */
    public Boolean getBlocageInactif() {
        return blocageInactif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SECBC_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("SECBC_SUJETMAIL_OK");
        }
    }

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
     * @author: sel Créé le : 21 sept. 06
     * @return
     */
    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * @return
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @return
     */
    public String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @return
     */
    public String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * @author: sel Créé le : 6 oct. 06
     * @return
     */
    public String getIdContMotifBloque() {
        return idContMotifBloque;
    }

    /**
     * @return
     */
    public String getIdMotConSus() {
        return idMotConSus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param string
     */
    public void setBlocageInactif(Boolean b) {
        blocageInactif = b;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @author: sel Créé le : 21 sept. 06
     * @param string
     */
    public void setForIdTypeSection(String string) {
        forIdTypeSection = string;
    }

    /**
     * @param string
     */
    public void setForSelectionCompte(String string) {
        forSelectionCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @param string
     */
    public void setForSelectionTriCA(String string) {
        forSelectionTriCA = string;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @param string
     */
    public void setForSelectionTriSection(String string) {
        forSelectionTriSection = string;
    }

    /**
     * @author: sel Créé le : 6 oct. 06
     * @param string
     */
    public void setIdContMotifBloque(String string) {
        idContMotifBloque = string;
    }

    /**
     * @param string
     */
    public void setIdMotConSus(String string) {
        idMotConSus = string;
    }

}
