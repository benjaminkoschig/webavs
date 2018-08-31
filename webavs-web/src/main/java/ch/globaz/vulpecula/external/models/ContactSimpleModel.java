package ch.globaz.vulpecula.external.models;

/**
 * @since WebBMS 2.3
 */
public class ContactSimpleModel extends globaz.jade.persistence.model.JadeSimpleModel {
    private static final long serialVersionUID = -5638047731515208811L;

    private String idContact = "";
    private String nom = "";
    private String prenom = "";

    @Override
    public String getId() {
        return getIdContact();
    }

    @Override
    public void setId(String id) {
        setIdContact(id);
    }

    /**
     * @return the idContact
     */
    public String getIdContact() {
        return idContact;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param idContact the idContact to set
     */
    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
