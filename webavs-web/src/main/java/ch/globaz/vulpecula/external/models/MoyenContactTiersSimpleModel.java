package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @since WebBMS 2.3
 */
public class MoyenContactTiersSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -2578496446603446720L;

    private String idContact;
    private String typeContact;
    private String valeur;
    private String application;

    @Override
    public String getId() {
        return getIdContact();
    }

    @Override
    public void setId(String id) {
        setIdContact(id);
    }

    public String getTypeContact() {
        return typeContact;
    }

    public void setTypeContact(String typeContact) {
        this.typeContact = typeContact;
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

    /**
     * @return the idContact
     */
    public String getIdContact() {
        return idContact;
    }

    /**
     * @param idContact the idContact to set
     */
    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }
}
