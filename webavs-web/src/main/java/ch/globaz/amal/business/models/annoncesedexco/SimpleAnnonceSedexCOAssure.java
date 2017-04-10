package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceSedexCOAssure extends JadeSimpleModel {
    private static final long serialVersionUID = -2360050957298529938L;
    private String idAnnonceSedexCOPersonneAssuree = null;
    private String idAnnonceSedexCODebiteur = null;
    private String idFamille = null;
    private String idContribuable = null;
    private String idDetailFamille = null;
    private String nssAssure = null;
    private String nomPrenomAssure = null;
    private String npaLocaliteAssure = null;
    private String rueNumeroAssure = null;
    private String primePeriodeDebut = null;
    private String primePeriodeFin = null;
    private String primeMontant = null;
    private String costSharingPeriodeDebut = null;
    private String costSharingPeriodeFin = null;
    private String costSharingMontant = null;
    private String message = null;
    private String typeSubside = null;

    @Override
    public String getId() {
        return idAnnonceSedexCOPersonneAssuree;
    }

    @Override
    public void setId(String id) {
        idAnnonceSedexCOPersonneAssuree = id;
    }

    public String getIdAnnonceSedexCOPersonneAssuree() {
        return idAnnonceSedexCOPersonneAssuree;
    }

    public void setIdAnnonceSedexCOPersonneAssuree(String idAnnonceSedexCOPersonneAssuree) {
        this.idAnnonceSedexCOPersonneAssuree = idAnnonceSedexCOPersonneAssuree;
    }

    public String getIdAnnonceSedexCODebiteur() {
        return idAnnonceSedexCODebiteur;
    }

    public void setIdAnnonceSedexCODebiteur(String idAnnonceSedexCODebiteur) {
        this.idAnnonceSedexCODebiteur = idAnnonceSedexCODebiteur;
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

    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

    public String getNssAssure() {
        return nssAssure;
    }

    public void setNssAssure(String nssAssure) {
        this.nssAssure = nssAssure;
    }

    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    public void setNomPrenomAssure(String nomPrenomAssure) {
        this.nomPrenomAssure = nomPrenomAssure;
    }

    public String getNpaLocaliteAssure() {
        return npaLocaliteAssure;
    }

    public void setNpaLocaliteAssure(String npaLocaliteAssure) {
        this.npaLocaliteAssure = npaLocaliteAssure;
    }

    public String getRueNumeroAssure() {
        return rueNumeroAssure;
    }

    public void setRueNumeroAssure(String rueNumeroAssure) {
        this.rueNumeroAssure = rueNumeroAssure;
    }

    public String getPrimePeriodeDebut() {
        return primePeriodeDebut;
    }

    public void setPrimePeriodeDebut(String primePeriodeDebut) {
        this.primePeriodeDebut = primePeriodeDebut;
    }

    public String getPrimePeriodeFin() {
        return primePeriodeFin;
    }

    public void setPrimePeriodeFin(String primePeriodeFin) {
        this.primePeriodeFin = primePeriodeFin;
    }

    public String getPrimeMontant() {
        return primeMontant;
    }

    public void setPrimeMontant(String primeMontant) {
        this.primeMontant = primeMontant;
    }

    public String getCostSharingPeriodeDebut() {
        return costSharingPeriodeDebut;
    }

    public void setCostSharingPeriodeDebut(String costSharingPeriodeDebut) {
        this.costSharingPeriodeDebut = costSharingPeriodeDebut;
    }

    public String getCostSharingPeriodeFin() {
        return costSharingPeriodeFin;
    }

    public void setCostSharingPeriodeFin(String costSharingPeriodeFin) {
        this.costSharingPeriodeFin = costSharingPeriodeFin;
    }

    public String getCostSharingMontant() {
        return costSharingMontant;
    }

    public void setCostSharingMontant(String costSharingMontant) {
        this.costSharingMontant = costSharingMontant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTypeSubside() {
        return typeSubside;
    }

    public void setTypeSubside(String typeSubside) {
        this.typeSubside = typeSubside;
    }

}
