package globaz.cygnus.vb.documents;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.documents.RFDocuments;
import globaz.cygnus.utils.RFDocumentsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * author fha
 */
public class RFDocumentsViewBean extends RFDocuments implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeDocumentList = "";
    // gestion des types de soin
    private String codeTypeDocumentList = "";
    private String csEtatDossier = "";
    private String dateDebut = "";
    private String dateDocument = "";
    private String dateFin = "";

    private String detailRequerant = "";

    private Boolean displaySendToGed = false;
    private transient String documentsInnerJavascript = "";
    /*------------------------------instance fields------------------------------------*/
    private String email = "";
    private String idDossier = "";

    private String idGestionnaire = "";

    private String idTiers = "";
    private String idTypeDocument = "";
    private Boolean isRegimeNouveauMontantMensuel = Boolean.FALSE;
    private Boolean isRegimeRecenteRevision = Boolean.TRUE;
    private Boolean miseEnGed = null;

    private String nbJours = "";
    private String regimeDateAllocationMensuelleApresRevision = "";
    private String regimeDateAllocationMensuelleSuppression = "";
    private String regimeDateCourrierPrecedent = "";

    private String regimeDateCourrierPrecedent11 = "";

    private String regimeDateCourrierPrecedent12 = "";
    private String regimeDateCourrierPrecedent13 = "";
    private String regimeDateCourrierPrecedent15 = "";

    private String regimeDateDemandeIndemnisation = "";
    private String regimeDateDemandeIndemnisationRefus2 = "";
    private String regimeDateEnvoiLettre1_7 = "";
    private String regimeDateEnvoiLettre1_8 = "";
    private String regimeDateEnvoiQuestionnaire = "";

    private String regimeDateEvaluationOMSV = "";

    private String regimeDateLettre1_11 = "";
    private String regimeDateLettre1_3 = "";
    private String regimeDatePremierVersement = "";

    // champs pour le document decision de refus 1
    private String regimeLibelleRegime = "";
    private String regimeMontantAllocationMensuelle = "";
    private String regimeMontantAllocationMensuelleApresRevision = "";
    private String regimeMontantAllocationMensuelleRappel = "";
    private String regimeMontantAllocationMensuelleSuppression = "";

    private String regimeMontantOctroi = "";
    private Vector typeDocuments = null;

    @Override
    protected void _init() {
        // TODO Auto-generated method stub
        super._init();
    }

    public String getCodeDocumentList() {
        return codeDocumentList;
    }

    public String getCodeTypeDocumentList() {
        return codeTypeDocumentList;
    }

    public String getCsEtatDossier() {
        return csEtatDossier;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = ((BSession) getISession()).getApplication()._getSecurityManager()
                    .getUserForVisa(((BSession) getISession()), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    /*-----------------------------methods---------------------------------------------*/

    public String getDetailRequerant() {
        return detailRequerant;
    }

    public Boolean getDisplaySendToGed() {
        return displaySendToGed;
    }

    public String getDocumentsInnerJavascript() {

        if (JadeStringUtil.isBlank(documentsInnerJavascript)) {
            try {
                documentsInnerJavascript = RFDocumentsListsBuilder.getInstance(((BSession) getISession()))
                        .getDocumentsParInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }
        return documentsInnerJavascript;
    }

    public String getEmail() {
        return email;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = ((BSession) getISession()).getApplication()._getSecurityManager()
                    .getUserForVisa(((BSession) getISession()), getIdGestionnaire());
            return userName.getIdUser();
        }
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTypeDocument() {
        return idTypeDocument;
    }

    public Boolean getIsRegimeNouveauMontantMensuel() {
        return isRegimeNouveauMontantMensuel;
    }

    public Boolean getIsRegimeRecenteRevision() {
        return isRegimeRecenteRevision;
    }

    public Boolean getMiseEnGed() {
        return miseEnGed;
    }

    public String getNbJours() throws RemoteException, Exception {

        return PRAbstractApplication.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS).getProperty(
                RFApplication.PROPERTY_GROUPE_CYGNUS_NBJOURS_DOCUMENT);
    }

    public String getRegimeDateAllocationMensuelleApresRevision() {
        return regimeDateAllocationMensuelleApresRevision;
    }

    public String getRegimeDateAllocationMensuelleSuppression() {
        return regimeDateAllocationMensuelleSuppression;
    }

    public String getRegimeDateCourrierPrecedent() {
        return regimeDateCourrierPrecedent;
    }

    public String getRegimeDateCourrierPrecedent11() {
        return regimeDateCourrierPrecedent11;
    }

    public String getRegimeDateCourrierPrecedent12() {
        return regimeDateCourrierPrecedent12;
    }

    public String getRegimeDateCourrierPrecedent13() {
        return regimeDateCourrierPrecedent13;
    }

    public String getRegimeDateCourrierPrecedent15() {
        return regimeDateCourrierPrecedent15;
    }

    public String getRegimeDateDemandeIndemnisation() {
        return regimeDateDemandeIndemnisation;
    }

    public String getRegimeDateDemandeIndemnisationRefus2() {
        return regimeDateDemandeIndemnisationRefus2;
    }

    public String getRegimeDateEnvoiLettre1_7() {
        return regimeDateEnvoiLettre1_7;
    }

    public String getRegimeDateEnvoiLettre1_8() {
        return regimeDateEnvoiLettre1_8;
    }

    public String getRegimeDateEnvoiQuestionnaire() {
        return regimeDateEnvoiQuestionnaire;
    }

    public String getRegimeDateEvaluationOMSV() {
        return regimeDateEvaluationOMSV;
    }

    public String getRegimeDateLettre1_11() {
        return regimeDateLettre1_11;
    }

    public String getRegimeDateLettre1_3() {
        return regimeDateLettre1_3;
    }

    public String getRegimeDatePremierVersement() {
        return regimeDatePremierVersement;
    }

    public String getRegimeLibelleRegime() {
        return regimeLibelleRegime;
    }

    public String getRegimeMontantAllocationMensuelle() {
        return regimeMontantAllocationMensuelle;
    }

    public String getRegimeMontantAllocationMensuelleApresRevision() {
        return regimeMontantAllocationMensuelleApresRevision;
    }

    public String getRegimeMontantAllocationMensuelleRappel() {
        return regimeMontantAllocationMensuelleRappel;
    }

    public String getRegimeMontantAllocationMensuelleSuppression() {
        return regimeMontantAllocationMensuelleSuppression;
    }

    public String getRegimeMontantOctroi() {
        return regimeMontantOctroi;
    }

    public Vector getTypeDocuments() {
        try {
            if (typeDocuments == null) {
                typeDocuments = RFDocumentsListsBuilder.getInstance(((BSession) getISession())).getTypeDocuments();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDocuments;
    }

    public void setCodeDocumentList(String codeDocumentList) {
        this.codeDocumentList = codeDocumentList;
    }

    public void setCodeTypeDocumentList(String codeTypeDocumentList) {
        this.codeTypeDocumentList = codeTypeDocumentList;
    }

    public void setCsEtatDossier(String csEtatDossier) {
        this.csEtatDossier = csEtatDossier;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDetailRequerant(String detailRequerant) {
        this.detailRequerant = detailRequerant;
    }

    public void setDisplaySendToGed(Boolean displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTypeDocument(String idTypeDocument) {
        this.idTypeDocument = idTypeDocument;
    }

    public void setIsRegimeNouveauMontantMensuel(Boolean isRegimeNouveauMontantMensuel) {
        this.isRegimeNouveauMontantMensuel = isRegimeNouveauMontantMensuel;
    }

    public void setIsRegimeRecenteRevision(Boolean isRegimeRecenteRevision) {
        this.isRegimeRecenteRevision = isRegimeRecenteRevision;
    }

    public void setMiseEnGed(Boolean miseEnGed) {
        this.miseEnGed = miseEnGed;
    }

    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    public void setRegimeDateAllocationMensuelleApresRevision(String regimeDateAllocationMensuelleApresRevision) {
        this.regimeDateAllocationMensuelleApresRevision = regimeDateAllocationMensuelleApresRevision;
    }

    public void setRegimeDateAllocationMensuelleSuppression(String regimeDateAllocationMensuelleSuppression) {
        this.regimeDateAllocationMensuelleSuppression = regimeDateAllocationMensuelleSuppression;
    }

    public void setRegimeDateCourrierPrecedent(String regimeDateCourrierPrecedent) {
        this.regimeDateCourrierPrecedent = regimeDateCourrierPrecedent;
    }

    public void setRegimeDateCourrierPrecedent11(String regimeDateCourrierPrecedent11) {
        this.regimeDateCourrierPrecedent11 = regimeDateCourrierPrecedent11;
    }

    public void setRegimeDateCourrierPrecedent12(String regimeDateCourrierPrecedent12) {
        this.regimeDateCourrierPrecedent12 = regimeDateCourrierPrecedent12;
    }

    public void setRegimeDateCourrierPrecedent13(String regimeDateCourrierPrecedent13) {
        this.regimeDateCourrierPrecedent13 = regimeDateCourrierPrecedent13;
    }

    public void setRegimeDateCourrierPrecedent15(String regimeDateCourrierPrecedent15) {
        this.regimeDateCourrierPrecedent15 = regimeDateCourrierPrecedent15;
    }

    public void setRegimeDateDemandeIndemnisation(String regimeDateDemandeIndemnisation) {
        this.regimeDateDemandeIndemnisation = regimeDateDemandeIndemnisation;
    }

    public void setRegimeDateDemandeIndemnisationRefus2(String regimeDateDemandeIndemnisationRefus2) {
        this.regimeDateDemandeIndemnisationRefus2 = regimeDateDemandeIndemnisationRefus2;
    }

    public void setRegimeDateEnvoiLettre1_7(String regimeDateEnvoiLettre1_7) {
        this.regimeDateEnvoiLettre1_7 = regimeDateEnvoiLettre1_7;
    }

    public void setRegimeDateEnvoiLettre1_8(String regimeDateEnvoiLettre1_8) {
        this.regimeDateEnvoiLettre1_8 = regimeDateEnvoiLettre1_8;
    }

    public void setRegimeDateEnvoiQuestionnaire(String regimeDateEnvoiQuestionnaire) {
        this.regimeDateEnvoiQuestionnaire = regimeDateEnvoiQuestionnaire;
    }

    public void setRegimeDateEvaluationOMSV(String regimeDateEvaluationOMSV) {
        this.regimeDateEvaluationOMSV = regimeDateEvaluationOMSV;
    }

    public void setRegimeDateLettre1_11(String regimeDateLettre1_11) {
        this.regimeDateLettre1_11 = regimeDateLettre1_11;
    }

    public void setRegimeDateLettre1_3(String regimeDateLettre1_3) {
        this.regimeDateLettre1_3 = regimeDateLettre1_3;
    }

    public void setRegimeDatePremierVersement(String regimeDatePremierVersement) {
        this.regimeDatePremierVersement = regimeDatePremierVersement;
    }

    public void setRegimeLibelleRegime(String regimeLibelleRegime) {
        this.regimeLibelleRegime = regimeLibelleRegime;
    }

    public void setRegimeMontantAllocationMensuelle(String regimeMontantAllocationMensuelle) {
        this.regimeMontantAllocationMensuelle = regimeMontantAllocationMensuelle;
    }

    public void setRegimeMontantAllocationMensuelleApresRevision(String regimeMontantAllocationMensuelleApresRevision) {
        this.regimeMontantAllocationMensuelleApresRevision = regimeMontantAllocationMensuelleApresRevision;
    }

    public void setRegimeMontantAllocationMensuelleRappel(String regimeMontantAllocationMensuelleRappel) {
        this.regimeMontantAllocationMensuelleRappel = regimeMontantAllocationMensuelleRappel;
    }

    public void setRegimeMontantAllocationMensuelleSuppression(String regimeMontantAllocationMensuelleSuppression) {
        this.regimeMontantAllocationMensuelleSuppression = regimeMontantAllocationMensuelleSuppression;
    }

    public void setRegimeMontantOctroi(String regimeMontantOctroi) {
        this.regimeMontantOctroi = regimeMontantOctroi;
    }

    public void setTypeDocuments(Vector typeDocuments) {
        this.typeDocuments = typeDocuments;
    }

}
