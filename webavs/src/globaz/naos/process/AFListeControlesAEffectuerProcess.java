/*
 * Créé le 14 févr. 07
 */
package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.db.controleEmployeur.AFControlesAEffectuerManager;
import globaz.naos.itext.controleEmployeur.AFListeControlesAEffectuerExcel;

/**
 * @author hpe
 * 
 */

public class AFListeControlesAEffectuerProcess extends BProcess implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = new String();
    private String annee1 = new String();
    private String annee2 = new String();
    private String annee3 = new String();
    private String annee4 = new String();
    private String annee5 = new String();
    private String genreControle = new String();
    private Boolean isAvecReviseur = null;
    private Boolean isGenerationControleEmployeur = null;
    private Boolean isTenirComptePerioAffilie = null;

    private Boolean isTenirComptePerioCaisse = null;
    private Boolean listeExcel = null;
    private Boolean listePdf = null;
    private String masseSalA = new String();
    private String masseSalDe = new String();
    private String typeAdresse = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFListeControlesAEffectuerProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {

            // Création du manager
            AFControlesAEffectuerManager manager = new AFControlesAEffectuerManager();
            manager.setSession(getSession());
            manager.setForAnnee(getAnnee());
            manager.setForGenreControle(getGenreControle());
            manager.setForMasseSalA(getMasseSalA());
            manager.setForMasseSalDe(getMasseSalDe());
            manager.setIsAvecReviseur(isAvecReviseur);

            // manager.setSession(getSession());
            // manager.setForMasseSalA(getMasseSalA());
            // manager.setForMasseSalDe(getMasseSalDe());
            // manager.setForAnnee1(getAnnee1());
            // manager.setForAnnee2(getAnnee2());
            // manager.setForAnnee3(getAnnee3());
            // manager.setForAnnee4(getAnnee4());
            // manager.setForAnnee5(getAnnee5());
            // manager.setForGenreControle(getGenreControle());
            // manager.setIsTenirComptePerioAffilie(getIsTenirComptePerioAffilie());
            // manager.setIsTenirComptePerioCaisse(getIsTenirComptePerioCaisse());

            manager.find(BManager.SIZE_NOLIMIT);

            // S'il faut Générer le contrôle employeur
            // if (getIsGenerationControleEmployeur().booleanValue()){
            //
            // }

            // Création du document
            AFListeControlesAEffectuerExcel excelDoc = new AFListeControlesAEffectuerExcel(getSession(), this);

            // Setter autres propriétés ?
            excelDoc.setGenreControle(getGenreControle());
            excelDoc.setIsAvecReviseur(getIsAvecReviseur());
            // excelDoc.setIsGenerationControleEmployeur(getIsGenerationControleEmployeur());
            // excelDoc.setIsTenirComptePerioAffilie(getIsTenirComptePerioAffilie());
            // excelDoc.setIsTenirComptePerioCaisse(getIsTenirComptePerioCaisse());
            excelDoc.setAnnee(getAnnee());
            // excelDoc.setAnnee2(getAnnee2());
            // excelDoc.setAnnee3(getAnnee3());
            // excelDoc.setAnnee4(getAnnee4());
            // excelDoc.setAnnee5(getAnnee5());
            excelDoc.setMasseSalA(getMasseSalA());
            excelDoc.setMasseSalDe(getMasseSalDe());
            excelDoc.setTypeAdresse(getTypeAdresse());
            // excelDoc.setProcessAppelant(this);

            // incProgressCounter();
            if (isAborted()) {
                return false;
            }

            excelDoc.populateSheetListe(manager, getTransaction());

            // incProgressCounter();

            if (!isAborted()) {
                this.registerAttachedDocument(excelDoc.getOutputFile());
            }

            // incProgressCounter();
            return true;
        } catch (Exception e) {
            abort();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (!listeExcel.booleanValue() && !listePdf.booleanValue()) {
            this._addError(getTransaction(), getSession().getLabel("MENU_CONTROLES_A_EFFECTUER_CHOIX"));
        }
    }

    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getAnnee1() {
        return annee1;
    }

    /**
     * @return
     */
    public String getAnnee2() {
        return annee2;
    }

    /**
     * @return
     */
    public String getAnnee3() {
        return annee3;
    }

    /**
     * @return
     */
    public String getAnnee4() {
        return annee4;
    }

    /**
     * @return
     */
    public String getAnnee5() {
        return annee5;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_AEFFECTUER_ERROR");
        } else {
            return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_AEFFECTUER");
        }
    }

    /**
     * @return
     */
    public String getGenreControle() {
        return genreControle;
    }

    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    /**
     * @return
     */
    public Boolean getIsGenerationControleEmployeur() {
        return isGenerationControleEmployeur;
    }

    /**
     * @return
     */
    public Boolean getIsTenirComptePerioAffilie() {
        return isTenirComptePerioAffilie;
    }

    /**
     * @return
     */
    public Boolean getIsTenirComptePerioCaisse() {
        return isTenirComptePerioCaisse;
    }

    public Boolean getListeExcel() {
        return listeExcel;
    }

    public Boolean getListePdf() {
        return listePdf;
    }

    /**
     * @return
     */
    public String getMasseSalA() {
        return masseSalA;
    }

    /**
     * @return
     */
    public String getMasseSalDe() {
        return masseSalDe;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param string
     */
    public void setAnnee1(String string) {
        annee1 = string;
    }

    /**
     * @param string
     */
    public void setAnnee2(String string) {
        annee2 = string;
    }

    /**
     * @param string
     */
    public void setAnnee3(String string) {
        annee3 = string;
    }

    /**
     * @param string
     */
    public void setAnnee4(String string) {
        annee4 = string;
    }

    /**
     * @param string
     */
    public void setAnnee5(String string) {
        annee5 = string;
    }

    /**
     * @param string
     */
    public void setGenreControle(String string) {
        genreControle = string;
    }

    public void setIsAvecReviseur(Boolean isAvecReviseur) {
        this.isAvecReviseur = isAvecReviseur;
    }

    /**
     * @param boolean1
     */
    public void setIsGenerationControleEmployeur(Boolean boolean1) {
        isGenerationControleEmployeur = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setIsTenirComptePerioAffilie(Boolean boolean1) {
        isTenirComptePerioAffilie = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setIsTenirComptePerioCaisse(Boolean boolean1) {
        isTenirComptePerioCaisse = boolean1;
    }

    public void setListeExcel(Boolean listeExcel) {
        this.listeExcel = listeExcel;
    }

    public void setListePdf(Boolean listePdf) {
        this.listePdf = listePdf;
    }

    /**
     * @param string
     */
    public void setMasseSalA(String string) {
        masseSalA = string;
    }

    /**
     * @param string
     */
    public void setMasseSalDe(String string) {
        masseSalDe = string;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

}
