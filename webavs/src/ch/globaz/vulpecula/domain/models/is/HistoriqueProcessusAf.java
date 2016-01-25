package ch.globaz.vulpecula.domain.models.is;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

public class HistoriqueProcessusAf implements DomainEntity {
    private String id;
    private String idProcessus;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdProcessus() {
        return idProcessus;
    }

    public void setIdProcessus(String idProcessus) {
        this.idProcessus = idProcessus;
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
