package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class OrganeExecutionSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String genre = null;
    private String idAdrdebtax = null;
    private String idAdressePaiement = null;
    private String idEntifiantDTA = null;
    private String idOrganeExecution = null;
    private String idRubrique = null;
    private String idTypeTaitementBV = null;
    private String idTypeTaitementLS = null;
    private String modeTransfert = null;
    private String noAdherent = null;
    private String noAdherentBVR = null;
    private String nom = null;
    private String nomClasseParseBVR = null;
    private String nomClasseParserLSV = null;
    private String numInterneLSV = null;

    public String getGenre() {
        return genre;
    }

    @Override
    public String getId() {
        return idOrganeExecution;
    }

    public String getIdAdrdebtax() {
        return idAdrdebtax;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdEntifiantDTA() {
        return idEntifiantDTA;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdTypeTaitementBV() {
        return idTypeTaitementBV;
    }

    public String getIdTypeTaitementLS() {
        return idTypeTaitementLS;
    }

    public String getModeTransfert() {
        return modeTransfert;
    }

    public String getNoAdherent() {
        return noAdherent;
    }

    public String getNoAdherentBVR() {
        return noAdherentBVR;
    }

    public String getNom() {
        return nom;
    }

    public String getNomClasseParseBVR() {
        return nomClasseParseBVR;
    }

    public String getNomClasseParserLSV() {
        return nomClasseParserLSV;
    }

    public String getNumInterneLSV() {
        return numInterneLSV;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public void setId(String id) {
        idOrganeExecution = id;
    }

    public void setIdAdrdebtax(String idAdrdebtax) {
        this.idAdrdebtax = idAdrdebtax;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdEntifiantDTA(String idEntifiantDTA) {
        this.idEntifiantDTA = idEntifiantDTA;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdTypeTaitementBV(String idTypeTaitementBV) {
        this.idTypeTaitementBV = idTypeTaitementBV;
    }

    public void setIdTypeTaitementLS(String idTypeTaitementLS) {
        this.idTypeTaitementLS = idTypeTaitementLS;
    }

    public void setModeTransfert(String modeTransfert) {
        this.modeTransfert = modeTransfert;
    }

    public void setNoAdherent(String noAdherent) {
        this.noAdherent = noAdherent;
    }

    public void setNoAdherentBVR(String noAdherentBVR) {
        this.noAdherentBVR = noAdherentBVR;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomClasseParseBVR(String nomClasseParseBVR) {
        this.nomClasseParseBVR = nomClasseParseBVR;
    }

    public void setNomClasseParserLSV(String nomClasseParserLSV) {
        this.nomClasseParserLSV = nomClasseParserLSV;
    }

    public void setNumInterneLSV(String numInterneLSV) {
        this.numInterneLSV = numInterneLSV;
    }

}
