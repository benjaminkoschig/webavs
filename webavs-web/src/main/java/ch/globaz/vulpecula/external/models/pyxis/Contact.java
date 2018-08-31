package ch.globaz.vulpecula.external.models.pyxis;

import java.util.HashMap;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

public class Contact implements DomainEntity {
    private String idContact = "";
    private String nom = "";
    private String prenom = "";
    protected String spy;

    private HashMap<TypeContact, MoyenContact> moyenContact;

    @Override
    public String getId() {
        return idContact;
    }

    @Override
    public void setId(String id) {
        idContact = id;
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

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * @return the moyen
     */
    public HashMap<TypeContact, MoyenContact> getMoyenContact() {
        return moyenContact;
    }

    /**
     * @param moyen the moyen to set
     */
    public void setMoyenContact(HashMap<TypeContact, MoyenContact> moyen) {
        moyenContact = moyen;
    }

}
