package globaz.cygnus.mappingXmlml;

public class RFXmlmlAdaptationJournaliereBean implements Comparable<RFXmlmlAdaptationJournaliereBean> {

    private String typeDeMessage;
    private String idAdaptationJournaliere;
    private String idTiersBeneficiaire;
    private String nss;
    private String msgErreur;
    private String idDecisionPc;
    private String numDecisionPc;
    private String communePolitique;

    public RFXmlmlAdaptationJournaliereBean(String typeDeMessage, String idAdaptationJournaliere,
            String idTiersBeneficiaire, String nss, String msgErreur, String idDecisionPc, String numDecisionPc) {
        super();
        this.typeDeMessage = typeDeMessage;
        this.idAdaptationJournaliere = idAdaptationJournaliere;
        this.idTiersBeneficiaire = idTiersBeneficiaire;
        this.nss = nss;
        this.msgErreur = msgErreur;
        this.idDecisionPc = idDecisionPc;
        this.numDecisionPc = numDecisionPc;
        communePolitique = "";
    }

    public String getTypeDeMessage() {
        return typeDeMessage;
    }

    public void setTypeDeMessage(String typeDeMessage) {
        this.typeDeMessage = typeDeMessage;
    }

    public String getIdAdaptationJournaliere() {
        return idAdaptationJournaliere;
    }

    public void setIdAdaptationJournaliere(String idAdaptationJournaliere) {
        this.idAdaptationJournaliere = idAdaptationJournaliere;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getMsgErreur() {
        return msgErreur;
    }

    public void setMsgErreur(String msgErreur) {
        this.msgErreur = msgErreur;
    }

    public String getIdDecisionPc() {
        return idDecisionPc;
    }

    public void setIdDecisionPc(String idDecisionPc) {
        this.idDecisionPc = idDecisionPc;
    }

    public String getNumDecisionPc() {
        return numDecisionPc;
    }

    public void setNumDecisionPc(String numDecisionPc) {
        this.numDecisionPc = numDecisionPc;
    }

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    @Override
    public int compareTo(RFXmlmlAdaptationJournaliereBean o) {
        return getCommunePolitique().compareTo(o.getCommunePolitique());
    }

}
