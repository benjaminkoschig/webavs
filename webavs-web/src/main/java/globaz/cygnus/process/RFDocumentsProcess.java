package globaz.cygnus.process;

import globaz.cygnus.topaz.RFDocumentFactory;
import globaz.globall.util.JADate;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

/**
 * author fha
 */
public class RFDocumentsProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDocument;
    private String detailRequerant = "";
    private String email = "";
    private String idDocument = "";
    private String idTiers = "";
    private Boolean isRegimeNouveauMontantMensuel = Boolean.FALSE;
    private Boolean isRegimeRecenteRevision = Boolean.FALSE;
    // private String isSendToGed = "";
    private Boolean miseEnGed = null;
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
    private String regimeLibelleRegime = "";
    private String regimeMontantAllocationMensuelle = "";
    private String regimeMontantAllocationMensuelleApresRevision = "";
    private String regimeMontantAllocationMensuelleRappel = "";
    private String regimeMontantAllocationMensuelleSuppression = "";
    private String regimeMontantOctroi = "";

    /**
     * Crée une nouvelle instance de la classe RFDocumentsProcess.
     */
    public RFDocumentsProcess() {
        super();
    }

    public String getDateDocument() {
        return dateDocument;
    }

    // private BSession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDetailRequerant() {
        return detailRequerant;
    }

    public String getEmail() {
        return email;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdTiers() {
        return idTiers;
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

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    public void run() {

        // JadePublishDocumentInfo pubInfosInter = JadePublishDocumentInfoProvider.newInstance(this);
        // pubInfosInter.setOwnerEmail(this.getEmail());
        // pubInfosInter.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, this.getEmail());
        // pubInfosInter.setDocumentTitle(this.getSession().getCodeLibelle(this.idDocument));
        // pubInfosInter.setArchiveDocument(false);
        // pubInfosInter.setPublishDocument(false);
        // pubInfosInter.setDocumentSubject(this.getSession().getCodeLibelle(this.idDocument));

        JadePublishDocumentInfo pubInfos = JadePublishDocumentInfoProvider.newInstance(this);
        pubInfos.setOwnerEmail(getEmail());
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmail());
        pubInfos.setDocumentTitle(getSession().getCodeLibelle(idDocument));
        pubInfos.setPublishDocument(true);
        pubInfos.setDocumentSubject(getSession().getCodeLibelle(idDocument));
        pubInfos.setArchiveDocument(miseEnGed);

        try {

            // Cette property doit être contenue dans un try
            pubInfos.setDocumentProperty("annee", JADate.getYear(getDateDocument()).toString());

            // utilisation de la factory pour generer le bon document qu'on
            // passe en param.
            RFDocumentFactory factory = new RFDocumentFactory();
            JadePrintDocumentContainer docContainer = new JadePrintDocumentContainer();
            if (miseEnGed) {
                docContainer = factory.remplir(this, true);
            } else {
                docContainer = factory.remplir(this, false);
            }

            docContainer.setMergedDocDestination(pubInfos);
            this.createDocuments(docContainer);

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
        }
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDetailRequerant(String detailRequerant) {
        this.detailRequerant = detailRequerant;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
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

}
