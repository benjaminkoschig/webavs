package ch.globaz.common.process.byitem;

import java.util.Date;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

class ProcessEntity extends JadeEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    private Date startDate;
    private Date endDate;
    private ProcessState etat;
    private Long timeEntity;
    private Long timeBefore;
    private Long timeAfter;
    private Integer nbEntityTotal;
    private Integer nbEntityInError;
    private String user;
    private String key;

    @Override
    protected void writeProperties() {
        writeDateTime(ProcessEntityTableDef.START_DATE, startDate);
        writeDateTime(ProcessEntityTableDef.END_DATE, endDate);
        this.write(ProcessEntityTableDef.ETAT, etat.toString());
        this.write(ProcessEntityTableDef.TIME_ITEM, timeEntity);
        this.write(ProcessEntityTableDef.TIME_BEFORE, timeBefore);
        this.write(ProcessEntityTableDef.TIME_AFTER, timeAfter);
        this.write(ProcessEntityTableDef.NB_ITEM_TOTAL, nbEntityTotal);
        this.write(ProcessEntityTableDef.NB_ITEM_IN_ERROR, nbEntityInError);
        this.write(ProcessEntityTableDef.USER, user);
        this.write(ProcessEntityTableDef.KEY_PROCES, key);
    }

    @Override
    protected void readProperties() {
        startDate = this.readDateTime(ProcessEntityTableDef.START_DATE);
        endDate = this.readDateTime(ProcessEntityTableDef.END_DATE);
        String setat = this.read(ProcessEntityTableDef.ETAT);
        etat = ProcessState.valueOf(setat);
        timeEntity = this.read(ProcessEntityTableDef.TIME_ITEM);
        timeBefore = this.read(ProcessEntityTableDef.TIME_BEFORE);
        timeAfter = this.read(ProcessEntityTableDef.TIME_AFTER);
        nbEntityInError = this.read(ProcessEntityTableDef.NB_ITEM_IN_ERROR);
        nbEntityTotal = this.read(ProcessEntityTableDef.NB_ITEM_TOTAL);
        user = this.read(ProcessEntityTableDef.USER);
        key = this.read(ProcessEntityTableDef.KEY_PROCES);
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return ProcessEntityTableDef.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ProcessState getEtat() {
        return etat;
    }

    public void setEtat(ProcessState etat) {
        this.etat = etat;
    }

    public Long getTimeEntity() {
        return timeEntity;
    }

    public void setTimeEntity(Long timeEntity) {
        this.timeEntity = timeEntity;
    }

    public Long getTimeBefore() {
        return timeBefore;
    }

    public void setTimeBefore(Long timeBefore) {
        this.timeBefore = timeBefore;
    }

    public Long getTimeAfter() {
        return timeAfter;
    }

    public void setTimeAfter(Long timeAfter) {
        this.timeAfter = timeAfter;
    }

    public Integer getNbEntityTotal() {
        return nbEntityTotal;
    }

    public void setNbEntityTotal(Integer nbEntityTotal) {
        this.nbEntityTotal = nbEntityTotal;
    }

    public Integer getNbEntityInError() {
        return nbEntityInError;
    }

    public void setNbEntityInError(Integer nbEntityInError) {
        this.nbEntityInError = nbEntityInError;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
