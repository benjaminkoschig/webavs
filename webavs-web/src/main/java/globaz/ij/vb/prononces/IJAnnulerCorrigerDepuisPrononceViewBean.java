package globaz.ij.vb.prononces;

import globaz.ij.db.prononces.IJPrononce;

/**
 * 
 * @author rco Crée le 09.07.2013 Modifié le 03.10.2013
 * 
 */
public class IJAnnulerCorrigerDepuisPrononceViewBean extends IJAbstractPrononceProxyViewBean {

    private String dateDebutPrononcerAAnnuler;
    private String dateDebutPrononcerAAnnulerAvant;

    private String dateFinPrononcerAAnnuler;
    private String dateFinPrononcerAAnnulerAvant;

    // private String idPrononce;
    private boolean isAnnulerPossible;

    public IJAnnulerCorrigerDepuisPrononceViewBean() {
        super(new IJPrononce());
    }

    /**
     * 
     * @param prononce
     */
    public IJAnnulerCorrigerDepuisPrononceViewBean(IJPrononce prononce) {
        super(prononce);
    }

    public String getDateDebutPrononcerAAnnuler() {
        if (dateDebutPrononcerAAnnuler == null) {
            return "";
        } else {
            return dateDebutPrononcerAAnnuler;
        }
    }

    public String getDateDebutPrononcerAAnnulerAvant() {
        return dateDebutPrononcerAAnnulerAvant;
    }

    public String getDateFinPrononcerAAnnuler() {
        if (dateFinPrononcerAAnnuler == null) {
            return "";
        } else {
            return dateFinPrononcerAAnnuler;
        }
    }

    public String getDateFinPrononcerAAnnulerAvant() {
        if (dateFinPrononcerAAnnulerAvant == null) {
            return "";
        } else {
            return dateFinPrononcerAAnnulerAvant;
        }
    }

    public boolean getIsAnnulerPossible() {
        return isAnnulerPossible;
    }

    public String getMessageAnnulerInterdit() {
        String message = getSession().getLabel("JSP_ANNULER_CORRIGER_INTERDIT_EXPLICATION");
        message = message.replace("{1}", getDateDebutPrononcerAAnnuler() + " - " + getDateFinPrononcerAAnnuler());
        message = message.replace("{2}", getDateDebutPrononcerAAnnulerAvant() + " - "
                + getDateFinPrononcerAAnnulerAvant());
        return message;
    }

    /**
     * 
     * @param dateDebutBIAnnuler
     */
    public void setDateDebutPrononcerAAnnuler(String dateDebutBIAnnuler) {
        dateDebutPrononcerAAnnuler = dateDebutBIAnnuler;
    }

    /**
     * 
     * @param dateFinBIAnnuler
     */
    public void setDateDebutPrononcerAAnnulerAvant(String dateFinBIAnnuler) {
        dateDebutPrononcerAAnnulerAvant = dateFinBIAnnuler;
    }

    /**
     * 
     * @param dateFinPrononcerAAnnuler
     */
    public void setDateFinPrononcerAAnnuler(String dateFinPrononcerAAnnuler) {
        this.dateFinPrononcerAAnnuler = dateFinPrononcerAAnnuler;
    }

    /**
     * 
     * @param dateFinPrononcerAAnnulerAvant
     */
    public void setDateFinPrononcerAAnnulerAvant(String dateFinPrononcerAAnnulerAvant) {
        this.dateFinPrononcerAAnnulerAvant = dateFinPrononcerAAnnulerAvant;
    }

    /**
     * 
     * @param isAnnulerPossible
     */
    public void setIsAnnulerPossible(boolean isAnnulerPossible) {
        this.isAnnulerPossible = isAnnulerPossible;
    }

}
