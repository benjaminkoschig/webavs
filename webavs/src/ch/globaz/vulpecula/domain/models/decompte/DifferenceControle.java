package ch.globaz.vulpecula.domain.models.decompte;

public class DifferenceControle {
    public static final DifferenceControle VALIDE = new DifferenceControle();

    static {
        VALIDE.isValid = true;
    }

    String montantControle = "";
    String totalContributions = "";
    String difference = "";
    String remarqueRectification = "";
    boolean isValid = false;

    public String getDifference() {
        return difference;
    }

    public String getMontantControle() {
        return montantControle;
    }

    public String getTotalContributions() {
        return totalContributions;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setDifference(final String difference) {
        this.difference = difference;
    }

    public void setMontantControle(final String montantControle) {
        this.montantControle = montantControle;
    }

    public void setTotalContributions(final String totalContributions) {
        this.totalContributions = totalContributions;
    }

    public void setValid(final boolean isValid) {
        this.isValid = isValid;
    }

    public String getRemarqueRectification() {
        return remarqueRectification;
    }

    public void setRemarqueRectification(String remarqueRectification) {
        this.remarqueRectification = remarqueRectification;
    }
}
