package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BManager;
import globaz.hercule.db.CEAbstractViewBean;
import globaz.hercule.db.reviseur.CEReviseurManager;
import globaz.jade.log.JadeLogger;

/**
 * @author SCO
 * @since 2 déc. 2010
 */
public class CEControlesEffectuesViewBean extends CEAbstractViewBean {

    private String annee;
    private String fromDateImpression;
    private String toDateImpression;
    private String typeAdresse;
    private String visaReviseur;

    /**
     * Constructeur de CEControlesEffectuesViewBean
     */
    public CEControlesEffectuesViewBean() {
        super();
    }

    /**
     * Charge la liste des reviseurs
     * 
     * @return
     */
    public CEReviseurManager _getReviseursList() {

        CEReviseurManager reviseurMan = new CEReviseurManager();
        reviseurMan.setSession(getSession());

        try {

            reviseurMan.find(BManager.SIZE_NOLIMIT);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return reviseurMan;
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getAnnee() {
        return annee;
    }

    public String getFromDateImpression() {
        return fromDateImpression;
    }

    public String getToDateImpression() {
        return toDateImpression;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public String getVisaReviseur() {
        return visaReviseur;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFromDateImpression(String fromDateImpression) {
        this.fromDateImpression = fromDateImpression;
    }

    public void setToDateImpression(String toDateImpression) {
        this.toDateImpression = toDateImpression;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    public void setVisaReviseur(String visaReviseur) {
        this.visaReviseur = visaReviseur;
    }

}
