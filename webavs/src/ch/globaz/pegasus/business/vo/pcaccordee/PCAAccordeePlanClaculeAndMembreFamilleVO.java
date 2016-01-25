package ch.globaz.pegasus.business.vo.pcaccordee;

import java.util.ArrayList;
import java.util.List;

public class PCAAccordeePlanClaculeAndMembreFamilleVO {
    private String csEtatPC = null;
    private String csGenrePC = null;
    private String csTypePC = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String excedentPCAnnuel = null;
    private String idDossier = null;
    private String idPcAccordee = null;
    private String idTiersBeneficiair = null;
    private String idVersionDroitPC = null;

    private List<PCAMembreFamilleVO> listMembreFamilleVO = null;

    private String montantPCMensuelle = null;

    private String montantPrimeMoyenAssMaladie = null;

    public PCAAccordeePlanClaculeAndMembreFamilleVO() {
        listMembreFamilleVO = new ArrayList<PCAMembreFamilleVO>();
    }

    public String getCsEtatPC() {
        return csEtatPC;
    }

    public String getCsGenrePC() {
        return csGenrePC;
    }

    public String getCsTypePC() {
        return csTypePC;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getExcedentPCAnnuel() {
        return excedentPCAnnuel;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdPcAccordee() {
        return idPcAccordee;
    }

    public String getIdTiersBeneficiair() {
        return idTiersBeneficiair;
    }

    public String getIdVersionDroitPC() {
        return idVersionDroitPC;
    }

    public List<PCAMembreFamilleVO> getListMembreFamilleVO() {
        return listMembreFamilleVO;
    }

    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    public String getMontantPrimeMoyenAssMaladie() {
        return montantPrimeMoyenAssMaladie;
    }

    public void setCsEtatPC(String csEtatPC) {
        this.csEtatPC = csEtatPC;
    }

    public void setCsGenrePC(String csGenrePC) {
        this.csGenrePC = csGenrePC;
    }

    public void setCsTypePC(String csTypePC) {
        this.csTypePC = csTypePC;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setExcedentPCAnnuel(String excedentPCAnnuel) {
        this.excedentPCAnnuel = excedentPCAnnuel;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdPcAccordee(String idPcAccordee) {
        this.idPcAccordee = idPcAccordee;
    }

    public void setIdTiersBeneficiair(String idTiersBeneficiair) {
        this.idTiersBeneficiair = idTiersBeneficiair;
    }

    public void setIdVersionDroitPC(String idVersionDroitPC) {
        this.idVersionDroitPC = idVersionDroitPC;
    }

    public void setListMembreFamilleVO(List<PCAMembreFamilleVO> listMembreFamilleVO) {
        this.listMembreFamilleVO = listMembreFamilleVO;
    }

    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }

    public void setMontantPrimeMoyenAssMaladie(String montantPrimeMoyenAssMaladie) {
        this.montantPrimeMoyenAssMaladie = montantPrimeMoyenAssMaladie;
    }

}
