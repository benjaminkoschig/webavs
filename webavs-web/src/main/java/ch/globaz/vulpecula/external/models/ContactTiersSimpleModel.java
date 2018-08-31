package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @since WebBMS 2.3
 */
public class ContactTiersSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -2578496446603446720L;

    private String idTiers;
    private String idContact;

    @Override
    public String getId() {
        return getIdContact();
    }

    @Override
    public void setId(String id) {
        setIdContact(id);
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the idContact
     */
    public String getIdContact() {
        return idContact;
    }

    /**
     * @param idTiers the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param idContact the idContact to set
     */
    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

}
