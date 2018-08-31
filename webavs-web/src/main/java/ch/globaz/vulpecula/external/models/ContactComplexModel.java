package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @since WebBMS 2.1
 */
public class ContactComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -1273608888881937457L;

    private ContactSimpleModel contactSimpleModel;
    private ContactTiersSimpleModel contactTiersSimpleModel;

    public ContactComplexModel() {
        contactSimpleModel = new ContactSimpleModel();
        contactTiersSimpleModel = new ContactTiersSimpleModel();
    }

    @Override
    public String getId() {
        return contactSimpleModel.getIdContact();
    }

    @Override
    public String getSpy() {
        return contactSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        contactSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        contactSimpleModel.setSpy(spy);
    }

    /**
     * @return the contact
     */
    public ContactSimpleModel getContactSimpleModel() {
        return contactSimpleModel;
    }

    /**
     * @return the contactTiers
     */
    public ContactTiersSimpleModel getContactTiersSimpleModel() {
        return contactTiersSimpleModel;
    }

    /**
     * @param contact the contact to set
     */
    public void setContactSimpleModel(ContactSimpleModel contact) {
        contactSimpleModel = contact;
    }

    /**
     * @param contactTiers the contactTiers to set
     */
    public void setContactTiersSimpleModel(ContactTiersSimpleModel contactTiers) {
        contactTiersSimpleModel = contactTiers;
    }

}
