package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class CPListeDecisionsDefinitivesViewBean implements FWViewBeanInterface, BIPersistentObject {

    private String annee;
    private String dateDebut;
    private String dateFin;
    private Boolean decisionActive = new Boolean(false);
    private String email;
    private String fromAffilie;
    private String genreDecision;
    private String id;
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
    private BISession session;
    private String toAffilie;
    private String typeDecision;

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public Boolean getDecisionActive() {
        return decisionActive;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getFromAffilie() {
        return fromAffilie;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    public String getToAffilie() {
        return toAffilie;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    @Override
    public void retrieve() throws Exception {
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDecisionActive(Boolean decisionActive) {
        this.decisionActive = decisionActive;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSession(BISession session) {
        this.session = session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    @Override
    public void update() throws Exception {
    }
}
