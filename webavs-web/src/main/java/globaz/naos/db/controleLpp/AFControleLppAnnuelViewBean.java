package globaz.naos.db.controleLpp;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFControleLppAnnuelViewBean extends AFAbstractViewBean {

    private String annee;
    private String dateImpression;
    private String email;
    private boolean modeControle = true; // true = simulation
    private String typeAdresse;

    public String getAnnee() {
        return annee;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public boolean isModeControleSimulation() {
        return modeControle;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModeControle(boolean modeControle) {
        this.modeControle = modeControle;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
