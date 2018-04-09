package globaz.pavo.print.itext;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * @author MMO
 * @since 14.09.2011
 */
public class CISplittingApercuAndLettreAccompagnementMergeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String MSG_ENTETE = "L'impression du document 'Aperçu de splitting' s'est terminée avec échec : \r\n";
    private String adresseAssure = "";
    private String adresseExConjoint = "";
    private boolean check = false;
    private Boolean chkLettreAccompagnementAssure = new Boolean(false);
    private Boolean chkLettreAccompagnementExConjoint = new Boolean(false);
    private String formulePolitesseAssure = "";
    private String formulePolitesseExConjoint = "";
    private String idAnnonceAssure = "";
    private String idAnnonceExConjoint = "";
    private String langueISOAssure = "";
    private String langueISOExConjoint = "";
    private String numeroAVSAssure = "";
    private String numeroAVSExConjoint = "";
    private Boolean cache = false;

    @Override
    protected void _executeCleanUp() {
        // NE FAIT RIEN

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        if (!cache) {

            JadePublishDocumentInfo theMergedDocInfo = createDocumentInfo();
            try {

                if (getChkLettreAccompagnementAssure()) {

                    CISplittingLettreAccompagnement_Doc lettreAccompagnementAssureDoc = new CISplittingLettreAccompagnement_Doc();
                    lettreAccompagnementAssureDoc.setParentWithCopy(this);
                    lettreAccompagnementAssureDoc.setNumeroAVS(getNumeroAVSAssure());
                    lettreAccompagnementAssureDoc.setFormulePolitesse(getFormulePolitesseAssure());
                    lettreAccompagnementAssureDoc.setAdresse(getAdresseAssure());
                    lettreAccompagnementAssureDoc.setLangueISO(getLangueISOAssure());

                    lettreAccompagnementAssureDoc.executeProcess();
                    registerDocuments(lettreAccompagnementAssureDoc.getAttachedDocuments());
                }

                if (getChkLettreAccompagnementExConjoint()) {

                    CISplittingLettreAccompagnement_Doc lettreAccompagnementExConjointDoc = new CISplittingLettreAccompagnement_Doc();
                    lettreAccompagnementExConjointDoc.setParentWithCopy(this);
                    lettreAccompagnementExConjointDoc.setNumeroAVS(getNumeroAVSExConjoint());
                    lettreAccompagnementExConjointDoc.setFormulePolitesse(getFormulePolitesseExConjoint());
                    lettreAccompagnementExConjointDoc.setAdresse(getAdresseExConjoint());
                    lettreAccompagnementExConjointDoc.setLangueISO(getLangueISOExConjoint());

                    lettreAccompagnementExConjointDoc.executeProcess();
                    registerDocuments(lettreAccompagnementExConjointDoc.getAttachedDocuments());
                }

                CI_Spliting apercuSplittingDoc = new CI_Spliting();
                apercuSplittingDoc.setParentWithCopy(this);

                if (!JadeStringUtil.isIntegerEmpty(getIdAnnonceAssure())) {
                    apercuSplittingDoc.setIdAnnonceAssure(getIdAnnonceAssure());
                }

                if (!JadeStringUtil.isIntegerEmpty(getIdAnnonceExConjoint())) {
                    apercuSplittingDoc.setIdAnnonceConjoint(getIdAnnonceExConjoint());
                }

                apercuSplittingDoc.setNumeroAVSAssure(getNumeroAVSAssure());
                apercuSplittingDoc.setNumeroAVSExConjoint(getNumeroAVSExConjoint());
                apercuSplittingDoc.setAdresseAssure(getAdresseAssure());
                apercuSplittingDoc.setAdresseExConjoint(getAdresseExConjoint());
                apercuSplittingDoc.setCheck(isCheck());
                apercuSplittingDoc.setLangueISO(getLangueISOAssure());
                apercuSplittingDoc.setLangueISOConjoint(getLangueISOExConjoint());

                apercuSplittingDoc.executeProcess();
                registerDocuments(apercuSplittingDoc.getAttachedDocuments());

                theMergedDocInfo.setPublishDocument(true);
                this.mergePDF(theMergedDocInfo, true, 0, false, null);

            } catch (Exception e) {

                getSession().addError(
                        getSession().getLabel("DOCUMENT_IMPRESSION_PROBLEME") + " : " + this.getClass().getName()
                                + " : " + e.toString());

                getMemoryLog().logMessage(
                        getSession().getLabel("DOCUMENT_IMPRESSION_PROBLEME") + " : " + this.getClass().getName()
                                + " : " + e.toString(), FWMessage.ERREUR, this.getClass().getName());

                if (theMergedDocInfo != null) {
                    theMergedDocInfo.setDocumentNotes(getMemoryLog().getMessagesInString());
                }

                return false;
            }

            return !(isAborted() || isOnError() || getSession().hasErrors());
        }
        return false;

    }

    public String getAdresseAssure() {
        return adresseAssure;
    }

    public String getAdresseExConjoint() {
        return adresseExConjoint;
    }

    public Boolean getChkLettreAccompagnementAssure() {
        return chkLettreAccompagnementAssure;
    }

    public Boolean getChkLettreAccompagnementExConjoint() {
        return chkLettreAccompagnementExConjoint;
    }

    @Override
    protected String getEMailObject() {

        if (cache) {
            return MSG_ENTETE + getSession().getLabel("APERCU_SPLITTING_EMAIL_SUBJECT_DROIT_INSUFFISANT");
        }

        if (!isAborted() && !isOnError() && !getSession().hasErrors()) {
            return getSession().getLabel("APERCU_SPLITTING_EMAIL_SUBJECT_SUCCES");
        }

        return getSession().getLabel("APERCU_SPLITTING_EMAIL_SUBJECT_ERROR");
    }

    public String getFormulePolitesseAssure() {
        return formulePolitesseAssure;
    }

    public String getFormulePolitesseExConjoint() {
        return formulePolitesseExConjoint;
    }

    public String getIdAnnonceAssure() {
        return idAnnonceAssure;
    }

    public String getIdAnnonceExConjoint() {
        return idAnnonceExConjoint;
    }

    public String getLangueISOAssure() {
        return langueISOAssure;
    }

    public String getLangueISOExConjoint() {
        return langueISOExConjoint;
    }

    public String getNumeroAVSAssure() {
        return numeroAVSAssure;
    }

    public String getNumeroAVSExConjoint() {
        return numeroAVSExConjoint;
    }

    public boolean isCheck() {
        return check;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setAdresseAssure(String adresseAssure) {
        this.adresseAssure = adresseAssure;
    }

    public void setAdresseExConjoint(String adresseExConjoint) {
        this.adresseExConjoint = adresseExConjoint;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setChkLettreAccompagnementAssure(Boolean chkLettreAccompagnementAssure) {
        this.chkLettreAccompagnementAssure = chkLettreAccompagnementAssure;
    }

    public void setChkLettreAccompagnementExConjoint(Boolean chkLettreAccompagnementExConjoint) {
        this.chkLettreAccompagnementExConjoint = chkLettreAccompagnementExConjoint;
    }

    public void setFormulePolitesseAssure(String formulePolitesseAssure) {
        this.formulePolitesseAssure = formulePolitesseAssure;
    }

    public void setFormulePolitesseExConjoint(String formulePolitesseExConjoint) {
        this.formulePolitesseExConjoint = formulePolitesseExConjoint;
    }

    public void setIdAnnonceAssure(String idAnnonceAssure) {
        this.idAnnonceAssure = idAnnonceAssure;
    }

    public void setIdAnnonceExConjoint(String idAnnonceExConjoint) {
        this.idAnnonceExConjoint = idAnnonceExConjoint;
    }

    public void setLangueISOAssure(String langueISOAssure) {
        this.langueISOAssure = langueISOAssure;
    }

    public void setLangueISOExConjoint(String langueISOExConjoint) {
        this.langueISOExConjoint = langueISOExConjoint;
    }

    public void setNumeroAVSAssure(String numeroAVSAssure) {
        this.numeroAVSAssure = numeroAVSAssure;
    }

    public void setNumeroAVSExConjoint(String numeroAVSExConjoint) {
        this.numeroAVSExConjoint = numeroAVSExConjoint;
    }

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

}
