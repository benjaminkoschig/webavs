package globaz.auriga.vb.renouvellementdecisionmasse;

import globaz.auriga.vb.AUAbstractDefaultViewBean;
import globaz.globall.util.JACalendar;
import globaz.jade.context.JadeThread;
import globaz.naos.translation.CodeSystem;

public class AURenouvellementDecisionMasseViewBean extends AUAbstractDefaultViewBean {

    private String annee = null;
    private String email = null;
    private String libellePassage = null;
    private String numeroAffilieDebut = null;
    private String numeroAffilieFin = null;
    private String numeroPassage = null;
    private String typesAffForWidgetString = null;

    public AURenouvellementDecisionMasseViewBean() {
        super();
        email = JadeThread.currentUserEmail();
        annee = Integer.toString(JACalendar.today().getYear());

        // ATTENTION, les types doivent être séparés par un "_"
        setTypesAffForWidgetString(new String(CodeSystem.TYPE_AFFILI_CAP_EMPLOYEUR + "_"
                + CodeSystem.TYPE_AFFILI_CAP_INDEPENDANT));
    }

    public String getAnnee() {
        return annee;
    }

    public String getEmail() {
        return email;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    public String getNumeroAffilieDebut() {
        return numeroAffilieDebut;
    }

    public String getNumeroAffilieFin() {
        return numeroAffilieFin;
    }

    public String getNumeroPassage() {
        return numeroPassage;
    }

    public String getTypesAffForWidgetString() {
        return typesAffForWidgetString;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    public void setNumeroAffilieDebut(String numeroAffilieDebut) {
        this.numeroAffilieDebut = numeroAffilieDebut;
    }

    public void setNumeroAffilieFin(String numeroAffilieFin) {
        this.numeroAffilieFin = numeroAffilieFin;
    }

    public void setNumeroPassage(String numeroPassage) {
        this.numeroPassage = numeroPassage;
    }

    public void setTypesAffForWidgetString(String typesAffForWidgetString) {
        this.typesAffForWidgetString = typesAffForWidgetString;
    }

}
