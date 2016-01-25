package globaz.osiris.vb.lettrage;

import globaz.globall.db.BSpy;
import globaz.osiris.application.CAApplication;
import globaz.pyxis.util.TIViewBeanSupport;
import java.util.ArrayList;
import java.util.List;

public class CALettrageViewBean extends TIViewBeanSupport {

    private String bsParams = ""; // utilisé par doLettrage
    private String category = ""; // utilisé par versementSection

    private String comment = ""; // utilisé par report

    private String contextPath = "";

    private String filter = ""; // pour filtrage recherche des comptes annexe
    // inputs
    private String fromCompteAnnexe = "";
    private String idCompteAnnexe = "";
    private String idExclusion = ""; // utilisé pour inclureSection
    private String idModeCompensation = ""; // utilisé par report

    private String idOrdreEnAttente = ""; // utilisé par annulerVersementSection
    private String idSection = "";
    private String montantVersement = ""; // utilisé par versementSection

    private String params = ""; // utilisé par doLettrage

    // outputs
    private StringBuffer response = new StringBuffer();
    private String role = "";
    private String toCompteAnnexe = "";
    private List<String> roleToExcludeList = new ArrayList<String>();

    public List<String> getRoleToExcludeList() {
        return roleToExcludeList;
    }

    public void addRoleToExcludeList(String roleToExcludeToAddToList) throws Exception {
        if (roleToExcludeToAddToList == null || roleToExcludeToAddToList.isEmpty()) {
            throw new Exception("No role setted in roleToExcludeList");
        }
        roleToExcludeList.add(roleToExcludeToAddToList);
    }

    public String getBsParams() {
        return bsParams;
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getFilter() {
        return filter;
    }

    public String getFromCompteAnnexe() {
        return fromCompteAnnexe;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExclusion() {
        return idExclusion;
    }

    public String getIdModeCompensation() {
        return idModeCompensation;
    }

    public String getIdOrdreEnAttente() {
        return idOrdreEnAttente;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getMontantVersement() {
        return montantVersement;
    }

    public String getParams() {
        return params;
    }

    /*
     * Getter et Setter
     */
    public StringBuffer getResponse() {
        return response;
    }

    public String getRole() {
        return role;
    }

    /*
     * Other
     */
    public BSpy getSpy() {
        return null;
    }

    public String getToCompteAnnexe() {
        return toCompteAnnexe;
    }

    public String selectedWhenSingleTypeAffilie() {
        String plusieursType = CAApplication.getApplicationOsiris().getProperty("plusieursTypeAffilie");
        if ("TRUE".equalsIgnoreCase(plusieursType)) {
            return "";
        } else {
            return "selected";
        }

    }

    public void setBsParams(String bsParams) {
        this.bsParams = bsParams;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setFromCompteAnnexe(String fromCompteAnnexe) {
        this.fromCompteAnnexe = fromCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExclusion(String idExclusion) {
        this.idExclusion = idExclusion;
    }

    public void setIdModeCompensation(String idModeCompensation) {
        this.idModeCompensation = idModeCompensation;
    }

    public void setIdOrdreEnAttente(String idOrdreEnAttente) {
        this.idOrdreEnAttente = idOrdreEnAttente;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setMontantVersement(String montantVersement) {
        this.montantVersement = montantVersement;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setResponse(StringBuffer response) {
        this.response = response;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToCompteAnnexe(String toCompteAnnexe) {
        this.toCompteAnnexe = toCompteAnnexe;
    }

}
