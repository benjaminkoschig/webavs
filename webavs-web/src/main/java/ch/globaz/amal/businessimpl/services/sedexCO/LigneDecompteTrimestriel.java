package ch.globaz.amal.businessimpl.services.sedexCO;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.simpleoutputlist.annotation.Column;

public class LigneDecompteTrimestriel {
    private String nssDebiteur = null;
    private String nomPrenomDebiteur = null;
    private String typeActe = null;
    private List<PersonneAssuree> personnesAssurees = null;
    private Montant interets = null;
    private Montant frais = null;
    private Montant total = null;
    private String message = null;

    public List<PersonneAssuree> addPersonneAssuree(PersonneAssuree personneAssuree) {
        if (personnesAssurees == null) {
            personnesAssurees = new ArrayList<PersonneAssuree>();
        }
        personnesAssurees.add(personneAssuree);
        return personnesAssurees;
    }

    public String getNssDebiteur() {
        return nssDebiteur;
    }

    public void setNssDebiteur(String nssDebiteur) {
        this.nssDebiteur = nssDebiteur;
    }

    @Column(name = "Débiteur", order = 1)
    public String getNomPrenomDebiteur() {
        return nomPrenomDebiteur;
    }

    public void setNomPrenomDebiteur(String nomPrenomDebiteur) {
        this.nomPrenomDebiteur = nomPrenomDebiteur;
    }

    @Column(name = "Acte", order = 2)
    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public Montant getInterets() {
        return interets;
    }

    public void setInterets(Montant interets) {
        this.interets = interets;
    }

    public Montant getFrais() {
        return frais;
    }

    public void setFrais(Montant frais) {
        this.frais = frais;
    }

    public Montant getTotal() {
        return total;
    }

    public void setTotal(Montant total) {
        this.total = total;
    }

    public List<PersonneAssuree> getPersonnesAssurees() {
        return personnesAssurees;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
