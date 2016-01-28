package globaz.naos.db.attestation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * Viewbean de l'écran attestationPersonnelle_de.jsp
 * 
 * @author SCO
 * @since 05 juil. 2011
 */
public class AFAttestationPersonnelleViewBean extends AFAbstractViewBean {

    private String annee;
    private String dateEnvoiMasse;
    private String email;
    private String fromAffilie;
    private String toAffilie;

    public String getAnnee() {
        return annee;
    }

    public String getDateEnvoiMasse() {
        return dateEnvoiMasse;
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

    public String getToAffilie() {
        return toAffilie;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateEnvoiMasse(String dateEnvoiMasse) {
        this.dateEnvoiMasse = dateEnvoiMasse;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }
}
