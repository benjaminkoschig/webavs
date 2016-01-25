package globaz.phenix.toolbox;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

public class CPDataDecision {
    private BigDecimal cotisationAvs;

    private String dateDebutDecision;
    private String dateFacturation;
    private String dateFinDecision;
    private String genre;
    private boolean isComplementaire;

    private BigDecimal revenuCi;

    public CPDataDecision(BigDecimal cotisationAvs, String dateFacturation, String genre, boolean isComplementaire,
            BigDecimal revenuCi, String dateDebutDecision, String dateFinDecision) {
        super();
        this.cotisationAvs = cotisationAvs;
        this.dateFacturation = dateFacturation;
        this.genre = genre;
        this.isComplementaire = isComplementaire;
        this.revenuCi = revenuCi;
        this.dateDebutDecision = dateDebutDecision;
        this.dateFinDecision = dateFinDecision;
    }

    /**
     * Cette méthode permet de contrôler si deux décisions de cotisations personnelles se chevauchent Dans le cas ou une
     * des décisions est une complémentaire, elle sera considérée comme non chevauchante
     * 
     * @param session
     * @param dataDecisionToCompare
     * @return true si chevauchement de date
     * @throws Exception
     */
    public boolean checkChevauchementDateDecision(BSession session, CPDataDecision dataDecisionToCompare)
            throws Exception {
        boolean chevauchementDecision = false;
        // Contrôle que les dates soient valables
        if (isDateDebutEtFinValides() && dataDecisionToCompare.isDateDebutEtFinValides()) {
            // Contrôle s'il y a des chevauchement entre les dates des décisions
            if (BSessionUtil.compareDateFirstLower(session, getDateDebutDecision(),
                    dataDecisionToCompare.getDateDebutDecision())) {
                if (BSessionUtil.compareDateBetweenOrEqual(session, getDateDebutDecision(), getDateFinDecision(),
                        dataDecisionToCompare.getDateDebutDecision())) {
                    chevauchementDecision = true;
                } else {
                    if (BSessionUtil.compareDateBetweenOrEqual(session, getDateDebutDecision(), getDateFinDecision(),
                            dataDecisionToCompare.getDateFinDecision())) {
                        chevauchementDecision = true;
                    }
                }
            } else {
                if (BSessionUtil.compareDateBetweenOrEqual(session, dataDecisionToCompare.getDateDebutDecision(),
                        dataDecisionToCompare.getDateFinDecision(), getDateDebutDecision())) {
                    chevauchementDecision = true;
                } else {
                    if (BSessionUtil.compareDateBetweenOrEqual(session, dataDecisionToCompare.getDateDebutDecision(),
                            dataDecisionToCompare.getDateFinDecision(), getDateFinDecision())) {
                        chevauchementDecision = true;
                    }
                }

            }
        } else {
            throw new Exception("Les dates des décisions ne sont pas correctes");
        }

        return chevauchementDecision;
    }

    public BigDecimal getCotisationAvs() {
        return cotisationAvs;
    }

    /**
     * @return the dateDebutDecision
     */
    public String getDateDebutDecision() {
        return dateDebutDecision;
    }

    public String getDateFacturation() {
        return dateFacturation;
    }

    /**
     * @return the dateFinDecision
     */
    public String getDateFinDecision() {
        return dateFinDecision;
    }

    public String getGenre() {
        return genre;
    }

    public BigDecimal getRevenuCi() {
        return revenuCi;
    }

    public boolean isComplementaire() {
        return isComplementaire;
    }

    private boolean isDateDebutEtFinValides() {
        if (JadeDateUtil.isGlobazDate(getDateDebutDecision()) && !JadeStringUtil.isBlankOrZero(getDateDebutDecision())
                && JadeDateUtil.isGlobazDate(getDateFinDecision())
                && !JadeStringUtil.isBlankOrZero(getDateFinDecision())) {
            return true;
        } else {
            return false;
        }
    }

    public void setComplementaire(boolean isComplementaire) {
        this.isComplementaire = isComplementaire;
    }

    public void setCotisationAvs(BigDecimal cotisationAvs) {
        this.cotisationAvs = cotisationAvs;
    }

    /**
     * @param dateDebutDecision
     *            the dateDebutDecision to set
     */
    public void setDateDebutDecision(String dateDebutDecision) {
        this.dateDebutDecision = dateDebutDecision;
    }

    public void setDateFacturation(String dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    /**
     * @param dateFinDecision
     *            the dateFinDecision to set
     */
    public void setDateFinDecision(String dateFinDecision) {
        this.dateFinDecision = dateFinDecision;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRevenuCi(BigDecimal revenuCi) {
        this.revenuCi = revenuCi;
    }

    @Override
    public String toString() {
        String result = "************ dataDecision ************\n";
        result += "revenuCi      	 : " + revenuCi + "\n";
        result += "cotisationAvs 	 : " + cotisationAvs + "\n";
        result += "genre         	 : " + genre + "\n";
        result += "isComplementaire  : " + isComplementaire + "\n";
        result += "dateFacturation   : " + dateFacturation + "\n";
        result += "dateDebutDecision : " + dateDebutDecision + "\n";
        result += "dateFinDecision : " + dateFinDecision + "\n\n";

        return result;
    }
}
