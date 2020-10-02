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
}
