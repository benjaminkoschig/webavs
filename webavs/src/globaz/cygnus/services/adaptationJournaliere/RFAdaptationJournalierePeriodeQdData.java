package globaz.cygnus.services.adaptationJournaliere;

import java.util.HashSet;
import java.util.Set;

public class RFAdaptationJournalierePeriodeQdData {

    private String csDegreApi = "";
    private String genrePc = "";
    private String id_periode = "";
    private String id_qd = "";
    private Set<String> idsTiersQd = new HashSet<String>();
    private Boolean isRi = false;
    private String montantExcedentDeRevenu = "";
    private String typeBeneficiaire = "";
    private String typePc = "";
    private String typeRemboursementConjoint = "";
    private String typeRemboursementRequerant = "";

    public RFAdaptationJournalierePeriodeQdData(String genrePc, String id_qd, String id_periode,
            String typeBeneficiaire, String typePc, boolean isRi, String montantExcedentDeRevenu,
            String typeRemboursementRequerant, String typeRemboursementConjoint, String csDegreApi) {
        super();
        this.genrePc = genrePc;
        this.id_qd = id_qd;
        this.id_periode = id_periode;
        this.typeBeneficiaire = typeBeneficiaire;
        this.typePc = typePc;
        this.isRi = isRi;
        this.montantExcedentDeRevenu = montantExcedentDeRevenu;
        this.typeRemboursementRequerant = typeRemboursementRequerant;
        this.typeRemboursementConjoint = typeRemboursementConjoint;
        this.csDegreApi = csDegreApi;
    }

    public String getCsDegreApi() {
        return csDegreApi;
    }

    public String getGenrePc() {
        return genrePc;
    }

    public String getId_periode() {
        return id_periode;
    }

    public String getId_qd() {
        return id_qd;
    }

    public Set<String> getIdsTiersQd() {
        return idsTiersQd;
    }

    public Boolean getIsRi() {
        return isRi;
    }

    public String getMontantExcedentDeRevenu() {
        return montantExcedentDeRevenu;
    }

    public String getTypeBeneficiaire() {
        return typeBeneficiaire;
    }

    public String getTypePc() {
        return typePc;
    }

    public String getTypeRemboursementConjoint() {
        return typeRemboursementConjoint;
    }

    public String getTypeRemboursementRequerant() {
        return typeRemboursementRequerant;
    }

    public void setCsDegreApi(String csDegreApi) {
        this.csDegreApi = csDegreApi;
    }

    public void setGenrePc(String genrePc) {
        this.genrePc = genrePc;
    }

    public void setId_periode(String id_periode) {
        this.id_periode = id_periode;
    }

    public void setId_qd(String id_qd) {
        this.id_qd = id_qd;
    }

    public void setIdsTiersQd(Set<String> idsTiersQd) {
        this.idsTiersQd = idsTiersQd;
    }

    public void setIsRi(Boolean isRi) {
        this.isRi = isRi;
    }

    public void setMontantExcedentDeRevenu(String montantExcedentDeRevenu) {
        this.montantExcedentDeRevenu = montantExcedentDeRevenu;
    }

    public void setTypeBeneficiaire(String typeBeneficiaire) {
        this.typeBeneficiaire = typeBeneficiaire;
    }

    public void setTypePc(String typePc) {
        this.typePc = typePc;
    }

    public void setTypeRemboursementConjoint(String typeRemboursementConjoint) {
        this.typeRemboursementConjoint = typeRemboursementConjoint;
    }

    public void setTypeRemboursementRequerant(String typeRemboursementRequerant) {
        this.typeRemboursementRequerant = typeRemboursementRequerant;
    }

}
