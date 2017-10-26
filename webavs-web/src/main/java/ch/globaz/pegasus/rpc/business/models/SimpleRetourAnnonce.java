package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRetourAnnonce extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;
    private String id;
    private String idLien;
    private String idLienAnnonceDecision;
    private String csCategoriePlausi;
    private String csTypePlausi;
    private String csTypeViolationPlausi;
    private String codePlausi;
    private String officePC;
    private String officePCConflit;
    private String nssAnnonce;
    private String nssPersonne;
    private String caseIdConflit;
    private String decisionIdConflit;
    private String validFromConflit;
    private String validToConflit;
    private String csStatusRetour;
    private String remarqueRetour;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdLienAnnonceDecision() {
        return idLienAnnonceDecision;
    }

    public void setIdLienAnnonceDecision(String idLienAnnonceDecision) {
        this.idLienAnnonceDecision = idLienAnnonceDecision;
    }

    public String getIdLien() {
        return idLien;
    }

    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    public String getCsCategoriePlausi() {
        return csCategoriePlausi;
    }

    public void setCsCategoriePlausi(String csCategoriePlausi) {
        this.csCategoriePlausi = csCategoriePlausi;
    }

    public String getCsTypePlausi() {
        return csTypePlausi;
    }

    public void setCsTypePlausi(String csTypePlausi) {
        this.csTypePlausi = csTypePlausi;
    }

    public String getCsTypeViolationPlausi() {
        return csTypeViolationPlausi;
    }

    public void setCsTypeViolationPlausi(String csTypeViolationPlausi) {
        this.csTypeViolationPlausi = csTypeViolationPlausi;
    }

    public String getCodePlausi() {
        return codePlausi;
    }

    public void setCodePlausi(String codePlausi) {
        this.codePlausi = codePlausi;
    }

    public String getOfficePC() {
        return officePC;
    }

    public void setOfficePC(String officePC) {
        this.officePC = officePC;
    }

    public String getOfficePCConflit() {
        return officePCConflit;
    }

    public void setOfficePCConflit(String officePCConflit) {
        this.officePCConflit = officePCConflit;
    }

    public String getNssAnnonce() {
        return nssAnnonce;
    }

    public void setNssAnnonce(String nssAnnonce) {
        this.nssAnnonce = nssAnnonce;
    }

    public String getNssPersonne() {
        return nssPersonne;
    }

    public void setNssPersonne(String nssPersonne) {
        this.nssPersonne = nssPersonne;
    }

    public String getCaseIdConflit() {
        return caseIdConflit;
    }

    public void setCaseIdConflit(String caseIdConflit) {
        this.caseIdConflit = caseIdConflit;
    }

    public String getDecisionIdConflit() {
        return decisionIdConflit;
    }

    public void setDecisionIdConflit(String decisionIdConflit) {
        this.decisionIdConflit = decisionIdConflit;
    }

    public String getValidFromConflit() {
        return validFromConflit;
    }

    public void setValidFromConflit(String validFromConflit) {
        this.validFromConflit = validFromConflit;
    }

    public String getValidToConflit() {
        return validToConflit;
    }

    public void setValidToConflit(String validToConflit) {
        this.validToConflit = validToConflit;
    }

    public String getCsStatusRetour() {
        return csStatusRetour;
    }

    public void setCsStatusRetour(String csStatusRetour) {
        this.csStatusRetour = csStatusRetour;
    }

    public String getRemarqueRetour() {
        return remarqueRetour;
    }

    public void setRemarqueRetour(String remarqueRetour) {
        this.remarqueRetour = remarqueRetour;
    }

}
