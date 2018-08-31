package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 *
 * @since WebBMS 2.3
 */
public class ContactSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -8460565180847339925L;

    private String forIdContact;
    private String forIdTiers;
    private String forNom;
    private String forPrenom;

    @Override
    public Class<ContactComplexModel> whichModelClass() {
        return ContactComplexModel.class;
    }

    /**
     * @return the forIdContact
     */
    public String getForIdContact() {
        return forIdContact;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forNom
     */
    public String getForNom() {
        return forNom;
    }

    /**
     * @return the forPrenom
     */
    public String getForPrenom() {
        return forPrenom;
    }

    /**
     * @param forIdContact the forIdContact to set
     */
    public void setForIdContact(String forIdContact) {
        this.forIdContact = forIdContact;
    }

    /**
     * @param forIdTiers the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forNom the forNom to set
     */
    public void setForNom(String forNom) {
        this.forNom = forNom;
    }

    /**
     * @param forPrenom the forPrenom to set
     */
    public void setForPrenom(String forPrenom) {
        this.forPrenom = forPrenom;
    }

}
