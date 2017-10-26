package ch.globaz.common.process.byitem;

public class ProcessItemsJobInfos {
    private final String idJob;
    private final Long timeEntity;
    private final Long timeBefore;
    private final Integer nbEntityTotal;
    private final Integer nbEntityInError;

    public ProcessItemsJobInfos(String idJob) {
        this.idJob = idJob;
        timeEntity = null;
        timeBefore = null;
        nbEntityTotal = null;
        nbEntityInError = null;
    }

    public ProcessItemsJobInfos(String idJob, Long timeEntity, Long timeBefore, Integer nbEntityTotal,
            Integer nbEntityInError) {
        this.idJob = idJob;
        this.timeEntity = timeEntity;
        this.timeBefore = timeBefore;
        this.nbEntityTotal = nbEntityTotal;
        this.nbEntityInError = nbEntityInError;
    }

    public String getIdJob() {
        return idJob;
    }

    public Long getTimeEntity() {
        return timeEntity;
    }

    public Long getTimeBefore() {
        return timeBefore;
    }

    public Integer getNbEntityTotal() {
        return nbEntityTotal;
    }

    public Integer getNbEntityInError() {
        return nbEntityInError;
    }

    public Long computeTotal() {
        if (timeBefore != null) {
            return timeBefore + timeEntity;
        }
        return 0L;
    }

    public Long computeItemAvrageTreatment() {
        if (timeEntity != null) {
            return timeEntity / nbEntityTotal;
        }
        return 0L;
    }

    public Long computeItemAvrage() {
        if (timeEntity != null) {
            return (timeEntity + timeBefore) / nbEntityTotal;
        }
        return 0L;
    }

}
