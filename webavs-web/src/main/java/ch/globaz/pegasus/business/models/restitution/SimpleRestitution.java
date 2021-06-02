package ch.globaz.pegasus.business.models.restitution;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRestitution extends JadeSimpleModel {

    private String idResitution;
    private String idDossier;
    private String montantRestitutionPCAvsFederal;
    private String montantRestitutionPCAIFederal;
    private String montantRestitutionPCAvsSubside;
    private String montantRestitutionPCAISubside;
    private String montantRestitutionPCAvsCantonal;
    private String montantRestitutionPCAICantonal;
    private String montantRestitutionPCRfmAvs;
    private String montantRestitutionPCRfmAI;
    private String typeRestPCAVSFed;
    private String typeRestPCAIFed;
    private String typeRestPCAvsSubside;
    private String typeRestPCAISubside;
    private String typeRestPCAvsCantonal;
    private String typeRestPCAICantonal;
    private String typeRestPCRfmAvs;
    private String typeRestPCRfmAI;
    private String idJournal;


    @Override
    public String getId() {
        return idResitution;
    }

    @Override
    public void setId(String id) {
        idResitution= id;
    }

    public String getIdResitution() {
        return idResitution;
    }

    public void setIdResitution(String idResitution) {
        this.idResitution = idResitution;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public String getMontantRestitutionPCAvsFederal() {
        return montantRestitutionPCAvsFederal;
    }

    public void setMontantRestitutionPCAvsFederal(String montantRestitutionPCAvsFederal) {
        this.montantRestitutionPCAvsFederal = montantRestitutionPCAvsFederal;
    }

    public String getMontantRestitutionPCAIFederal() {
        return montantRestitutionPCAIFederal;
    }

    public void setMontantRestitutionPCAIFederal(String montantRestitutionPCAIFederal) {
        this.montantRestitutionPCAIFederal = montantRestitutionPCAIFederal;
    }

    public String getMontantRestitutionPCAvsSubside() {
        return montantRestitutionPCAvsSubside;
    }

    public void setMontantRestitutionPCAvsSubside(String montantRestitutionPCAvsSubside) {
        this.montantRestitutionPCAvsSubside = montantRestitutionPCAvsSubside;
    }

    public String getMontantRestitutionPCAISubside() {
        return montantRestitutionPCAISubside;
    }

    public void setMontantRestitutionPCAISubside(String montantRestitutionPCAISubside) {
        this.montantRestitutionPCAISubside = montantRestitutionPCAISubside;
    }

    public String getMontantRestitutionPCAvsCantonal() {
        return montantRestitutionPCAvsCantonal;
    }

    public void setMontantRestitutionPCAvsCantonal(String montantRestitutionPCAvsCantonal) {
        this.montantRestitutionPCAvsCantonal = montantRestitutionPCAvsCantonal;
    }

    public String getMontantRestitutionPCAICantonal() {
        return montantRestitutionPCAICantonal;
    }

    public void setMontantRestitutionPCAICantonal(String montantRestitutionPCAICantonal) {
        this.montantRestitutionPCAICantonal = montantRestitutionPCAICantonal;
    }

    public String getMontantRestitutionPCRfmAvs() {
        return montantRestitutionPCRfmAvs;
    }

    public void setMontantRestitutionPCRfmAvs(String montantRestitutionPCRfmAvs) {
        this.montantRestitutionPCRfmAvs = montantRestitutionPCRfmAvs;
    }

    public String getMontantRestitutionPCRfmAI() {
        return montantRestitutionPCRfmAI;
    }

    public void setMontantRestitutionPCRfmAI(String montantRestitutionPCRfmAI) {
        this.montantRestitutionPCRfmAI = montantRestitutionPCRfmAI;
    }
    public String getTypeRestPCAVSFed() {
        return typeRestPCAVSFed;
    }

    public void setTypeRestPCAVSFed(String typeRestPCAVSFed) {
        this.typeRestPCAVSFed = typeRestPCAVSFed;
    }

    public String getTypeRestPCAIFed() {
        return typeRestPCAIFed;
    }

    public void setTypeRestPCAIFed(String typeRestPCAIFed) {
        this.typeRestPCAIFed = typeRestPCAIFed;
    }

    public String getTypeRestPCAvsSubside() {
        return typeRestPCAvsSubside;
    }

    public void setTypeRestPCAvsSubside(String typeRestPCAvsSubside) {
        this.typeRestPCAvsSubside = typeRestPCAvsSubside;
    }

    public String getTypeRestPCAISubside() {
        return typeRestPCAISubside;
    }

    public void setTypeRestPCAISubside(String typeRestPCAISubside) {
        this.typeRestPCAISubside = typeRestPCAISubside;
    }

    public String getTypeRestPCAvsCantonal() {
        return typeRestPCAvsCantonal;
    }

    public void setTypeRestPCAvsCantonal(String typeRestPCAvsCantonal) {
        this.typeRestPCAvsCantonal = typeRestPCAvsCantonal;
    }

    public String getTypeRestPCAICantonal() {
        return typeRestPCAICantonal;
    }

    public void setTypeRestPCAICantonal(String typeRestPCAICantonal) {
        this.typeRestPCAICantonal = typeRestPCAICantonal;
    }

    public String getTypeRestPCRfmAvs() {
        return typeRestPCRfmAvs;
    }

    public void setTypeRestPCRfmAvs(String typeRestPCRfmAvs) {
        this.typeRestPCRfmAvs = typeRestPCRfmAvs;
    }

    public String getTypeRestPCRfmAI() {
        return typeRestPCRfmAI;
    }

    public void setTypeRestPCRfmAI(String typeRestPCRfmAI) {
        this.typeRestPCRfmAI = typeRestPCRfmAI;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }
}
