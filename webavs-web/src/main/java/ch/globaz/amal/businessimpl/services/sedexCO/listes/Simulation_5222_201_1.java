package ch.globaz.amal.businessimpl.services.sedexCO.listes;

import ch.globaz.simpleoutputlist.annotation.Column;

public class Simulation_5222_201_1 {
    private String nssPersonne = null;
    private String nomPrenomPersonne = null;
    private String nomCaisse = null;

    @Column(name = "NSS", order = 0)
    public String getNssPersonne() {
        return nssPersonne;
    }

    public void setNssPersonne(String nssPersonne) {
        this.nssPersonne = nssPersonne;
    }

    @Column(name = "Nom prénom", order = 1)
    public String getNomPrenomPersonne() {
        return nomPrenomPersonne;
    }

    public void setNomPrenomPersonne(String nomPrenomPersonne) {
        this.nomPrenomPersonne = nomPrenomPersonne;
    }

    @Column(name = "Caisse maladie", order = 2)
    public String getNomCaisse() {
        return nomCaisse;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }
}
