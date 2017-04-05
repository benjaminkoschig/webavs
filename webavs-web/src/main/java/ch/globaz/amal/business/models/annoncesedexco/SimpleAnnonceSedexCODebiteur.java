package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceSedexCODebiteur extends JadeSimpleModel {
    private String idAnnonceSedexCODebiteur = null;
    private String idAnnonceSedexCO = null;
    private String idFamille = null;
    private String idContribuable = null;
    private String nssDebiteur = null;
    private String nomPrenomDebiteur = null;
    private String npaLocaliteDebiteur = null;
    private String rueNumeroDebiteur = null;
    private String acte = null;
    private String interets = null;
    private String frais = null;
    private String total = null;
    private String message = null;

    @Override
    public String getId() {
        return idAnnonceSedexCODebiteur;
    }

    @Override
    public void setId(String id) {
        idAnnonceSedexCODebiteur = id;
    }

    public String getIdAnnonceSedexCODebiteur() {
        return idAnnonceSedexCODebiteur;
    }

    public void setIdAnnonceSedexCODebiteur(String idAnnonceSedexCODebiteur) {
        this.idAnnonceSedexCODebiteur = idAnnonceSedexCODebiteur;
    }

    public String getIdAnnonceSedexCO() {
        return idAnnonceSedexCO;
    }

    public void setIdAnnonceSedexCO(String idAnnonceSedexCO) {
        this.idAnnonceSedexCO = idAnnonceSedexCO;
    }

    public String getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    public String getIdContribuable() {
        return idContribuable;
    }

    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public String getNssDebiteur() {
        return nssDebiteur;
    }

    public void setNssDebiteur(String nssDebiteur) {
        this.nssDebiteur = nssDebiteur;
    }

    public String getNomPrenomDebiteur() {
        return nomPrenomDebiteur;
    }

    public void setNomPrenomDebiteur(String nomPrenomDebiteur) {
        this.nomPrenomDebiteur = nomPrenomDebiteur;
    }

    public String getNpaLocaliteDebiteur() {
        return npaLocaliteDebiteur;
    }

    public void setNpaLocaliteDebiteur(String npaLocaliteDebiteur) {
        this.npaLocaliteDebiteur = npaLocaliteDebiteur;
    }

    public String getRueNumeroDebiteur() {
        return rueNumeroDebiteur;
    }

    public void setRueNumeroDebiteur(String rueNumeroDebiteur) {
        this.rueNumeroDebiteur = rueNumeroDebiteur;
    }

    public String getActe() {
        return acte;
    }

    public void setActe(String acte) {
        this.acte = acte;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getInterets() {
        return interets;
    }

    public void setInterets(String interets) {
        this.interets = interets;
    }

    public String getFrais() {
        return frais;
    }

    public void setFrais(String frais) {
        this.frais = frais;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
