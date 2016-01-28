package ch.globaz.vulpecula.external.models.osiris;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

public class Journal implements DomainEntity {
    private String id;
    private Date dateComptabilisation;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Date getDateComptabilisation() {
        return dateComptabilisation;
    }

    public void setDateComptabilisation(Date dateComptabilisation) {
        this.dateComptabilisation = dateComptabilisation;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
