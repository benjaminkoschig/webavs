package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleRevenuHypothetique extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreMotif = null;
    private String csMotif = null;
    private String deductionLPP = null;
    private String deductionsSociales = null;
    private String fraisDeGarde = null;
    private String idDonneeFinanciereHeader = null;
    private String idRevenuHypothetique = null;
    private String montantRevenuHypothetiqueBrut = null;
    private String montantRevenuHypothetiqueNet = null;

    public String getAutreMotif() {
        return autreMotif;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getDeductionLPP() {
        return deductionLPP;
    }

    public String getDeductionsSociales() {
        return deductionsSociales;
    }

    public String getFraisDeGarde() {
        return fraisDeGarde;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idRevenuHypothetique;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdRevenuHypothetique() {
        return idRevenuHypothetique;
    }

    public String getMontantRevenuHypothetiqueBrut() {
        return montantRevenuHypothetiqueBrut;
    }

    public String getMontantRevenuHypothetiqueNet() {
        return montantRevenuHypothetiqueNet;
    }

    public void setAutreMotif(String autreMotif) {
        this.autreMotif = autreMotif;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setDeductionLPP(String deductionLPP) {
        this.deductionLPP = deductionLPP;
    }

    public void setDeductionsSociales(String deductionsSociales) {
        this.deductionsSociales = deductionsSociales;
    }

    public void setFraisDeGarde(String fraisDeGarde) {
        this.fraisDeGarde = fraisDeGarde;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idRevenuHypothetique = id;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdRevenuHypothetique(String idRevenuHypothetique) {
        this.idRevenuHypothetique = idRevenuHypothetique;
    }

    public void setMontantRevenuHypothetiqueBrut(String montantRevenuHypothetiqueBrut) {
        this.montantRevenuHypothetiqueBrut = montantRevenuHypothetiqueBrut;
    }

    public void setMontantRevenuHypothetiqueNet(String montantRevenuHypothetiqueNet) {
        this.montantRevenuHypothetiqueNet = montantRevenuHypothetiqueNet;
    }

}
