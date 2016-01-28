package globaz.pavo.db.process;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIImprimerListJournauxPartielsProcessViewBean extends CIAbstractPersistentViewBean {

    private String forAnnee = "";
    private String forDate = "";
    private String fordateInscription = "";
    private String forIdType = "";
    private String fromUser = "";
    private String likeIdAffiliation = "";

    public CIImprimerListJournauxPartielsProcessViewBean() throws Exception {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDate() {
        return forDate;
    }

    public String getFordateInscription() {
        return fordateInscription;
    }

    public String getForIdType() {
        return forIdType;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getLikeIdAffiliation() {
        return likeIdAffiliation;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setFordateInscription(String fordateInscription) {
        this.fordateInscription = fordateInscription;
    }

    public void setForIdType(String forIdType) {
        this.forIdType = forIdType;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setLikeIdAffiliation(String likeIdAffiliation) {
        this.likeIdAffiliation = likeIdAffiliation;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
