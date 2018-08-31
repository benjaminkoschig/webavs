package ch.globaz.vulpecula.external.models.pyxis;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

public class MoyenContact implements DomainEntity {
    private String idContact = "";
    private String valeur = "";
    private String application = "";
    private TypeContact type;
    protected String spy;

    @Override
    public String getId() {
        return idContact;
    }

    @Override
    public void setId(String id) {
        idContact = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public TypeContact getType() {
        return type;
    }

    public void setType(TypeContact type) {
        this.type = type;
    }

}
