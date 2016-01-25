package ch.globaz.pegasus.business.models.process.statistiquesofas;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;

public class RenteApiIj extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String allocationImpotantMontant = null;
    private String donnefinanciereHeaderCsType = null;
    private String ijaiMontant = null;
    private String renteAvsAiMontant = null;
    private String renteAvsAiTypePC = null;
    private String renteAvsAiTypeRente = null;

    // private SimpleAllocationImpotent simpleAllocationImpotent = null;
    // private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader;
    private SimpleDroitMembreFamille simpleDroitMembreFamille = null;

    // private SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi = null;
    // private SimpleRenteAvsAi simpleRenteAvsAi = null;

    public String getAllocationImpotantMontant() {
        return allocationImpotantMontant;
    }

    public String getDonnefinanciereHeaderCsType() {
        return donnefinanciereHeaderCsType;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIjaiMontant() {
        return ijaiMontant;
    }

    public String getRenteAvsAiMontant() {
        return renteAvsAiMontant;
    }

    public String getRenteAvsAiTypePC() {
        return renteAvsAiTypePC;
    }

    public String getRenteAvsAiTypeRente() {
        return renteAvsAiTypeRente;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAllocationImpotantMontant(String allocationImpotantMontant) {
        this.allocationImpotantMontant = allocationImpotantMontant;
    }

    public void setDonnefinanciereHeaderCsType(String donnefinanciereHeaderCsType) {
        this.donnefinanciereHeaderCsType = donnefinanciereHeaderCsType;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIjaiMontant(String ijaiMontant) {
        this.ijaiMontant = ijaiMontant;
    }

    public void setRenteAvsAiMontant(String renteAvsAiMontant) {
        this.renteAvsAiMontant = renteAvsAiMontant;
    }

    public void setRenteAvsAiTypePC(String renteAvsAiTypePC) {
        this.renteAvsAiTypePC = renteAvsAiTypePC;
    }

    public void setRenteAvsAiTypeRente(String renteAvsAiTypeRente) {
        this.renteAvsAiTypeRente = renteAvsAiTypeRente;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
