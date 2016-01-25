/*
 * Créé le 2 février 2011
 */
package globaz.cygnus.services;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

/**
 * 
 * @author JJE
 * 
 */
public class RFRetrieveInfoDroitPCServiceData {

    private String csDegreApi = "";
    private String dateDebutPcaccordee = "";
    private String dateFinPcaccordee = "";
    private String genrePc = "";
    private String idPcAccordee = "";
    private String idTiers = "";
    private String idVersionDroit = "";
    private boolean isRi = false;
    private List<PCAMembreFamilleVO> personneDansPlanCalculList = new ArrayList<PCAMembreFamilleVO>();
    private String soldeExcedent = "";
    private String typeBeneficiaire = "";
    private String typePc = "";
    private String typeRemboursement = "";

    public String getCsDegreApi() {
        return csDegreApi;
    }

    public String getDateDebutPcaccordee() {
        return dateDebutPcaccordee;
    }

    public String getDateFinPcaccordee() {
        return dateFinPcaccordee;
    }

    public String getGenrePc() {
        return genrePc;
    }

    public String getIdPcAccordee() {
        return idPcAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public List<PCAMembreFamilleVO> getPersonneDansPlanCalculList() {
        return personneDansPlanCalculList;
    }

    public String getSoldeExcedent() {
        return soldeExcedent;
    }

    public String getTypeBeneficiaire() {
        return typeBeneficiaire;
    }

    public String getTypePc() {
        return typePc;
    }

    public String getTypeRemboursement() {
        return typeRemboursement;
    }

    public boolean isRi() {
        return isRi;
    }

    public void setCsDegreApi(String csDegreApi) {
        this.csDegreApi = csDegreApi;
    }

    public void setDateDebutPcaccordee(String dateDebutPcaccordee) {
        this.dateDebutPcaccordee = dateDebutPcaccordee;
    }

    public void setDateFinPcaccordee(String dateFinPcaccordee) {
        this.dateFinPcaccordee = dateFinPcaccordee;
    }

    public void setGenrePc(String genrePc) {
        this.genrePc = genrePc;
    }

    public void setIdPcAccordee(String idPcAccordee) {
        this.idPcAccordee = idPcAccordee;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setPersonneDansPlanCalculList(List<PCAMembreFamilleVO> personneDansPlanCalculList) {
        this.personneDansPlanCalculList = personneDansPlanCalculList;
    }

    public void setRi(boolean isRi) {
        this.isRi = isRi;
    }

    public void setSoldeExcedent(String soldeExcedent) {
        this.soldeExcedent = soldeExcedent;
    }

    public void setTypeBeneficiaire(String typeBeneficiaire) {
        this.typeBeneficiaire = typeBeneficiaire;
    }

    public void setTypePc(String typePc) {
        this.typePc = typePc;
    }

    public void setTypeRemboursement(String typeRemboursement) {
        this.typeRemboursement = typeRemboursement;
    }

}