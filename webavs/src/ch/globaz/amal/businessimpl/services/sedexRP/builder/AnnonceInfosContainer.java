package ch.globaz.amal.businessimpl.services.sedexRP.builder;

/**
 * Container d'informations relatifs à une annonce
 * 
 * @author cbu
 * 
 */
public class AnnonceInfosContainer {
    private String periodeFrom = null;
    private String periodeTo = null;
    private String recipientId = null;
    private String referenceDay = null;

    public String getPeriodeFrom() {
        return periodeFrom;
    }

    public String getPeriodeTo() {
        return periodeTo;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getReferenceDay() {
        return referenceDay;
    }

    public void setPeriodeFrom(String periodeFrom) {
        this.periodeFrom = periodeFrom;
    }

    public void setPeriodeTo(String periodeTo) {
        this.periodeTo = periodeTo;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setReferenceDay(String referenceDay) {
        this.referenceDay = referenceDay;
    }
}
