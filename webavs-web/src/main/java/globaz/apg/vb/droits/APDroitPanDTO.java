package globaz.apg.vb.droits;

import globaz.apg.db.droits.APDroitPandemie;

public class APDroitPanDTO extends APDroitDTO {

    private String idGestionnaire;
    private String noControlePers = "";
    private String noCompte = "";
    private String etat = "";
    private String pays = "";
    private String npa = "";

    public APDroitPanDTO(APDroitPandemie droit) {
        super(droit);
        noControlePers = droit.getNoControlePers();
        noCompte = droit.getNoCompte();
        etat = droit.getEtat();
        pays = droit.getPays();
        npa = droit.getNpa();
        idGestionnaire = droit.getIdGestionnaire();
    }

    public APDroitPanDTO() {
    }

    @Override
    public void setNoAVS(String string) {
        super.setNoAVS(string);
    }

    @Override
    public void setIdTiers(String idTiers) {
        super.setIdTiers(idTiers);
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }


    public void setNoControlePers(String noControlePers) {
        this.noControlePers = noControlePers;
    }

    public String getNoControlePers() {
        return noControlePers;
    }

    public void setNoCompte(String noCompte) {
        this.noCompte = noCompte;
    }

    public String getNoCompte() {
        return noCompte;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getEtat() {
        return etat;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getPays() {
        return pays;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getNpa() {
        return npa;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

}
