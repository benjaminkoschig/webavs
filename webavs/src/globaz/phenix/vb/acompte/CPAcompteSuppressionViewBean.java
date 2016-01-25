package globaz.phenix.vb.acompte;

import globaz.phenix.vb.CPAbstractPersistentObjectViewBean;

public class CPAcompteSuppressionViewBean extends CPAbstractPersistentObjectViewBean {
    private String forGenreAffilie = "";
    private String forTypeDecision = "";
    private String idPassage = "";
    private java.lang.String libellePassage = "";

    @Override
    public void add() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    @Override
    public void delete() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public String getEMailAddress() {
        return getSession().getUserEMail();
    }

    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForTypeDecision() {
        return forTypeDecision;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    @Override
    public void retrieve() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public void setEMailAddress(String mailAddress) {
    }

    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForTypeDecision(String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setLibellePassage(java.lang.String libellePassage) {
        this.libellePassage = libellePassage;
    }

    @Override
    public void update() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

}
